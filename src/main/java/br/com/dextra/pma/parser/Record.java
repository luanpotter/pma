package br.com.dextra.pma.parser;

import lombok.Getter;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.models.Appointment;

class Record {

    @Getter
    private Date date;

    @Getter
    private Time time;

    @Getter
    private long task;

    @Getter
    private String desc;

    public Record(String line, int lineNumber) throws InvalidFormatException {
        String[] parts = line.split("\\+");
        assertValidLength(lineNumber, parts);

        this.date = Record.parseDate(parts, lineNumber);
        this.time = Record.parseTime(parts, lineNumber);
        this.task = Record.parseTask(parts, lineNumber);
        this.desc = Record.parseDesc(parts, lineNumber);

    }

    private static void assertValidLength(int lineNumber, String[] parts) throws InvalidFormatException {
        if (parts.length <= 1) {
            throw new InvalidFormatException("Each line must have at least 1 '+' signs: date+time[+task[+desc]]; found " + (parts.length - 1), lineNumber);
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
}
