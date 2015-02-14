package br.com.dextra.pma.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import lombok.experimental.UtilityClass;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.models.Appointment;
import br.com.dextra.pma.models.Day;

@UtilityClass
public class FileParser {

    private static void assertValidLength(int lineNumber, String[] parts) throws InvalidFormatException {
        if (parts.length <= 1) {
            throw new InvalidFormatException("Each line must have at least 1 '+' signs: date+time[+task[+desc]]; found " + (parts.length - 1), lineNumber);
        }
    }

    private static Map<Long, Appointment> emptyAppointmentsMap() {
        Map<Long, Appointment> taskTimes;
        taskTimes = new HashMap<>();
        taskTimes.put(Appointment.INTERVAL_TASK, new Appointment(Appointment.INTERVAL_TASK));
        return taskTimes;
    }

    public static List<Day> parseLogs(String fileName, boolean createResultFile) throws InvalidFormatException {
        File file = new File(fileName);
        if (!file.exists()) {
            return Collections.emptyList();
        }

        FileCache fileCache = new FileCache();
        List<Day> results = parseFile(getScanner(file), fileCache);

        if (createResultFile) {
            fileCache.printRemainingFile(fileName + ".new");
        }

        return results;
    }

    private static Scanner getScanner(File file) {
        try {
            return new Scanner(file);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to read from log file.", ex);
        }
    }

    static List<Day> parseFile(Iterator<String> lines, FileCache fileCache) throws InvalidFormatException {
        List<Day> results = new ArrayList<>();
        Date currentDay = null;
        Map<Long, Appointment> taskTimes = emptyAppointmentsMap();
        Appointment lastAppointment = null;
        Time start = null, lastTime = null;

        int lineNumber = 0;

        while (lines.hasNext()) {
            String line = lines.next();
            fileCache.add(line);

            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\+");
            assertValidLength(lineNumber, parts);

            Date date = ElementsParser.parseDate(parts, lineNumber);
            Time time = ElementsParser.parseTime(parts, lineNumber);
            long task = ElementsParser.parseTask(parts, lineNumber);
            String desc = ElementsParser.parseDesc(parts, lineNumber);

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
                taskTimes = emptyAppointmentsMap();
                fileCache.savedLine(lineNumber);
            }
            lastAppointment = currentAppointment;
            lastTime = time;

            lineNumber++;
        }

        if (lastAppointment.getTask() == -1 && currentDay != null) {
            results.add(new Day(currentDay, start, lastTime, taskTimes));
            fileCache.savedLine(lineNumber);
        }

        return results;
    }

    public static void replaceFiles(String fileName) {
        File original = new File(fileName);
        File current = new File(fileName + ".new");

        original.delete();
        current.renameTo(original);
    }
}
