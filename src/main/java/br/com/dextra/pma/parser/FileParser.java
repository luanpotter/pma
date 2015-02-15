package br.com.dextra.pma.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import lombok.experimental.UtilityClass;
import br.com.dextra.pma.models.Appointment;
import br.com.dextra.pma.models.Day;

@UtilityClass
public class FileParser {

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

    private Scanner getScanner(File file) {
        try {
            return new Scanner(file);
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

        if (lastRecord.getTask() == Appointment.INTERVAL_TASK && currentDay != null) {
            currentDay.end(lastRecord.getTime());
            results.add(currentDay);
            fileCache.savedLine(lineNumber);
        }

        return results;
    }

    public void replaceFiles(String fileName) {
        File original = new File(fileName);
        File current = new File(fileName + ".new");

        original.delete();
        current.renameTo(original);
    }
}
