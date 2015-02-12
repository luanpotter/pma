package br.com.dextra.pma.main;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.models.Appointment;
import br.com.dextra.pma.models.Day;

public class FileParser {

    public static final class InvalidFormatException extends Exception {
        private static final long serialVersionUID = 376344169681487559L;

        public InvalidFormatException(String error, int line) {
            super("Invalid log file! " + error + " [at line " + line + "]");
        }
    }

    private static Date parseDate(String[] parts, int lineNumber) throws InvalidFormatException {
        try {
            return new Date(parts[0]);
        } catch (NumberFormatException ex) {
            throw new InvalidFormatException("Invalid date, must be in the format yyyy-mm-dd", lineNumber);
        }
    }

    private static Time parseTime(String[] parts, int lineNumber) throws InvalidFormatException {
        try {
            return new Time(parts[1]);
        } catch (NumberFormatException ex) {
            throw new InvalidFormatException("Invalid time, must be in the format hh:mm", lineNumber);
        }
    }

    private static long parseTask(String[] parts, int lineNumber) throws InvalidFormatException {
        try {
            return parts.length >= 3 ? Long.parseLong(parts[2]) : Appointment.INTERVAL_TASK;
        } catch (NumberFormatException ex) {
            throw new InvalidFormatException("Invalid task id, must be a number", lineNumber);
        }
    }

    private static String parseDesc(String[] parts, int lineNumber) {
        String desc = parts.length >= 4 ? parts[3] : ".";
        for (int i = 4; i < parts.length; i++) {
            desc += "+" + parts[i];
        }
        return desc;
    }

    public static List<Day> parseLogs(String fileName, boolean keepFile) throws InvalidFormatException {
        List<Day> results = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) {
            return results;
        }

        Date currentDay = null;
        Map<Long, Appointment> taskTimes = emptyMap();
        Appointment lastAppointment = null;
        Time start = null, lastTime = null;

        List<String> linesCache = new ArrayList<>();
        int lineNumber = 0, lastSavedLineNumber = 0;
        try (BufferedReader p = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = p.readLine()) != null) {
                linesCache.add(line);
                lineNumber++;

                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\+");
                if (parts.length < 2) {
                    throw new InvalidFormatException("Each line must have at least 1 '+' signs: date+time[+task[+desc]]; found " + (parts.length - 1),
                            lineNumber);
                }
                Date date = parseDate(parts, lineNumber);
                Time time = parseTime(parts, lineNumber);
                long task = parseTask(parts, lineNumber);
                String desc = parseDesc(parts, lineNumber);

                if (taskTimes.get(task) == null) {
                    taskTimes.put(task, new Appointment(task));
                }
                Appointment currentAppointment = taskTimes.get(task);
                currentAppointment.addDescription(desc);

                if (currentDay == null) {
                    currentDay = date;
                    start = time;
                } else if (currentDay.equals(date)) {
                    lastAppointment.addTime(time.getDifference(lastTime));
                } else {
                    if (lastAppointment.getTask() != -1) {
                        throw new InvalidFormatException("Started a new day without ending previous one", lineNumber);
                    }
                    results.add(new Day(currentDay, start, lastTime, taskTimes));
                    currentDay = date;
                    start = time;
                    taskTimes = emptyMap();
                    lastSavedLineNumber = lineNumber - 1;
                }
                lastAppointment = currentAppointment;
                lastTime = time;
            }
        } catch (EOFException ex) {
        } catch (IOException ex) {
            throw new RuntimeException("Unable to read from file: " + fileName, ex);
        }

        if (lastAppointment.getTask() == -1 && currentDay != null) {
            results.add(new Day(currentDay, start, lastTime, taskTimes));
        }

        if (!keepFile) {
            file.delete();
            try (PrintWriter p = new PrintWriter(fileName)) {
                for (int i = lastSavedLineNumber; i < linesCache.size(); i++) {
                    p.println(linesCache.get(i));
                }
            } catch (IOException ex) {
                throw new RuntimeException("Unable to re-write file: " + fileName, ex);
            }
        }

        return results;
    }

    private static Map<Long, Appointment> emptyMap() {
        Map<Long, Appointment> taskTimes;
        taskTimes = new HashMap<>();
        taskTimes.put(Appointment.INTERVAL_TASK, new Appointment(Appointment.INTERVAL_TASK));
        return taskTimes;
    }
}
