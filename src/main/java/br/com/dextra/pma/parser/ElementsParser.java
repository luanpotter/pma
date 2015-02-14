package br.com.dextra.pma.parser;

import lombok.experimental.UtilityClass;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.models.Appointment;

@UtilityClass
class ElementsParser {

    static Date parseDate(String[] parts, int lineNumber) throws InvalidFormatException {
        try {
            return new Date(parts[0]);
        } catch (NumberFormatException ex) {
            throw new InvalidFormatException("Invalid date, must be in the format yyyy-mm-dd", lineNumber);
        }
    }

    static Time parseTime(String[] parts, int lineNumber) throws InvalidFormatException {
        try {
            return new Time(parts[1]);
        } catch (NumberFormatException ex) {
            throw new InvalidFormatException("Invalid time, must be in the format hh:mm", lineNumber);
        }
    }

    static long parseTask(String[] parts, int lineNumber) throws InvalidFormatException {
        try {
            return parts.length >= 3 ? Long.parseLong(parts[2]) : Appointment.INTERVAL_TASK;
        } catch (NumberFormatException ex) {
            throw new InvalidFormatException("Invalid task id, must be a number", lineNumber);
        }
    }

    static String parseDesc(String[] parts, int lineNumber) {
        String desc = parts.length >= 4 ? parts[3] : ".";
        for (int i = 4; i < parts.length; i++) {
            desc += "+" + parts[i];
        }
        return desc;
    }
}
