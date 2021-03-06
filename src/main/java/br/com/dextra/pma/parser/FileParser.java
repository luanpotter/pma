package br.com.dextra.pma.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.model.Appointment;
import br.com.dextra.pma.model.CurrentDay;
import br.com.dextra.pma.model.Day;
import br.com.dextra.pma.model.Record;

@UtilityClass
public class FileParser {

    public Day parseDay(String fileName, Date target) throws InvalidFormatException {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        Record lastRecord = null;
        CurrentDay day = null;

        int lineNumber = 0;
        Iterator<String> lines = getScanner(file);

        while (lines.hasNext()) {
            String line = lines.next();

            if (line.isEmpty()) {
                continue;
            }
            Record record = new Record(line, lineNumber);
            if (!record.getDate().equals(target)) {
                if (day != null) {
                    break;
                } else {
                    continue;
                }
            }

            if (lastRecord == null) {
                day = new CurrentDay(record.getDate(), record.getTime());
            } else {
                int minutes = record.getTime().getDifference(lastRecord.getTime());
                day.addTask(lastRecord.getTask(), lastRecord.getDesc(), minutes);
            }
            lastRecord = record;
            lineNumber++;
        }

        boolean dayNotFound = day == null;
        if (dayNotFound) {
            return null;
        }

        day.setLastRecord(lastRecord);
        return day;
    }

    public List<Day> parseLogs(String fileName, boolean createResultFile) throws InvalidFormatException {
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

    private Iterator<String> getScanner(File file) {
        try {
            return new BufferedReader(new FileReader(file)).lines().iterator();
        } catch (IOException ex) {
            throw new RuntimeException("Unable to read from log file.", ex);
        }
    }

    List<Day> parseFile(Iterator<String> lines, FileCache fileCache) throws InvalidFormatException {
        List<Day> results = new ArrayList<>();

        Record lastRecord = null;
        Day currentDay = null;

        int lineNumber = 0;
        while (lines.hasNext()) {
            String line = lines.next();
            fileCache.add(line);

            if (line.isEmpty()) {
                continue;
            }
            Record record = new Record(line, lineNumber);

            if (lastRecord == null) {
                currentDay = new Day(record.getDate(), record.getTime());
            } else {
                if (record.getDate().equals(currentDay.getDate())) {
                    int minutes = record.getTime().getDifference(lastRecord.getTime());
                    currentDay.addTask(lastRecord.getTask(), lastRecord.getDesc(), minutes);
                } else {
                    if (lastRecord.getTask() != Appointment.INTERVAL_TASK) {
                        throw new InvalidFormatException("Last record of every day must not have a task.", lineNumber);
                    }
                    currentDay.end(lastRecord.getTime());
                    results.add(currentDay);
                    fileCache.savedLine(lineNumber);

                    currentDay = new Day(record.getDate(), record.getTime());
                }
            }
            lastRecord = record;
            lineNumber++;
        }

        if (currentDay != null && lastRecord.getTask() == Appointment.INTERVAL_TASK) {
            currentDay.end(lastRecord.getTime());
            results.add(currentDay);
            fileCache.savedLine(lineNumber);
        }

        return results;
    }

    @SneakyThrows
    public void replaceFiles(String fileName) {
        Files.move(toPath(fileName + ".new"), toPath(fileName), StandardCopyOption.REPLACE_EXISTING);
    }

    private Path toPath(String fileName) {
        return new File(fileName).toPath();
    }
}
