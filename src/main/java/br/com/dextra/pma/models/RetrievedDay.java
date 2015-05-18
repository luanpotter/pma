package br.com.dextra.pma.models;

import java.util.Map;

import xyz.luan.console.parser.Console;
import br.com.dextra.pma.date.Date;

/**
 * This day is different because it is retrieved from the API. Therefore it has
 * not start, interval and end times (for some reason). If ever the API is to be
 * changed, to support retrieving this data, this Class shall too.
 */
public class RetrievedDay extends Day {

    private static final long serialVersionUID = -6537517208271975760L;

    private RetrievedDay(Date date) {
        super(date, null);
    }

    @Override
    protected void printInternal(Console console) {
        getAppointments().forEach(a -> console.result(a));
    }

    public static Day parseFromAPIResponse(Date date, Map<Long, Appointment> appointments) {
        Day day = new RetrievedDay(date);
        day.appointments = appointments;
        return day;
    }
}
