package main;

import date.*;
import models.*;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class PMAParser {

  public static final class InvalidFormatException extends Exception {
    private static final long serialVersionUID = 376344169681487559L;

    public InvalidFormatException(String error, int line) {
      super("Invalid log file!" + error + " [at line " + line + "]");
    }   
  }

  public static List<Day> parseLogs(String fileName, boolean keepFile) throws InvalidFormatException {
    List<Day> results = new ArrayList<>();
    File file = new File(fileName);
    if (!file.exists()) {
      return results;
    }

    Date currentDay = null;
    Map<Long, Appointment> taskTimes = new HashMap<>();
    Appointment lastAppointment = null;
    Time start = null, lastTime = null;
    Time interval = new Time();

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
        if (parts.length < 2 || parts.length > 4) {
          throw new InvalidFormatException("Each line must have 1-3 '+' signs: date+time[+task[+desc]]; found " + (parts.length - 1), lineNumber);
        }
        Date date = new Date(parts[0]);
        Time time = new Time(parts[1]);
        long task;
        try {
          task = parts.length >= 3 ? Long.parseLong(parts[2]) : -1l;
        } catch (NumberFormatException ex) {
          throw new InvalidFormatException("Invalid task id, must be a number", lineNumber);
        }
        String desc = parts.length == 4 ? parts[3] : ".";
        Appointment currentAppointment = task == -1 ? null : new Appointment(task, desc);

        if (currentDay == null) {
          currentDay = date;
          start = time;
        } else if (currentDay.equals(date)) {
          if (lastAppointment == null) {
            interval.addMinutes(time.getDifference(lastTime));
          } else {
            lastAppointment.setTime(time.getDifference(lastTime));
            taskTimes.put(lastAppointment.getTask(), lastAppointment);
          }
        } else {
          if (lastAppointment != null) {
            throw new InvalidFormatException("Started a new day without ending previous one", lineNumber);
          }
          results.add(new Day(currentDay, start, lastTime, interval, taskTimes));
          currentDay = date;
          start = time;
          interval = new Time();
          taskTimes = new HashMap<>();
          lastSavedLineNumber = lineNumber - 1;
        }
        lastAppointment = currentAppointment;
        lastTime = time;
      }
    } catch (EOFException ex) {
    } catch (IOException ex) {
      throw new RuntimeException("Unable to read from file: " + fileName, ex);
    }

    if (lastAppointment == null && currentDay != null) {
      results.add(new Day(currentDay, start, lastTime, interval, taskTimes));
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
}