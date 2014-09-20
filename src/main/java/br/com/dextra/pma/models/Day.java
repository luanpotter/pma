package br.com.dextra.pma.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.main.PMAWrapper;
import br.com.dextra.pma.parser.Output;

public class Day implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8717524564031422430L;
    private Date date;
    private Time startTime;
    private Time endTime;
    private Time interval;
    private List<Appointment> appointments;

    public Day(Date date, Time start, Time end, Time interval, Map<Long, Appointment> appointments) {
        this.date = date;
        this.startTime = start;
        this.endTime = end;
        this.interval = interval;

        this.appointments = new ArrayList<>();
        for (Long task : appointments.keySet()) {
            this.appointments.add(appointments.get(task));
        }
    }

    public Output save() {
        Output out = PMAWrapper.createDay(date, startTime, endTime, interval);
        for (Appointment a : appointments) {
            out.append(a.save(date));
        }
        return out;
    }

    @Override
    public String toString() {
        return "[" + date + "] from: " + startTime + " to " + endTime + " except for " + interval + "; tasks: "
                + Arrays.toString(appointments.toArray());
    }
}