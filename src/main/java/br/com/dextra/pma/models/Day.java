package br.com.dextra.pma.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import xyz.luan.console.parser.Console;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.main.Wrapper;

public class Day implements Serializable {

    private static final long serialVersionUID = -8717524564031422430L;

    private static final int MAX_DELTA = 5;
    private static final String MAX_DELTA_EXCEEDED_ERROR = "Something went wrong! The sum of the activities specified does not comply with the start/ending times. Did you edit the log.dat file by hand?";

    private Date date;
    private Time startTime;
    private Time endTime;
    private Time interval;
    private List<Appointment> appointments;

    public Day(Date date, Time start, Time end, Map<Long, Appointment> appointments) {
        this.date = date;
        this.startTime = start;
        this.endTime = end;
        this.interval = appointments.get(Appointment.INTERVAL_TASK).getDuration();
        parseAppointments(appointments);
    }

    private void parseAppointments(Map<Long, Appointment> appointments) {
        this.appointments = new ArrayList<>();
        appointments.keySet().stream().filter((t) -> t != -1).forEach((task) -> this.appointments.add(appointments.get(task)));
        fixPossibleImprecisionMistake(appointments);
    }

    private void fixPossibleImprecisionMistake(Map<Long, Appointment> appointments) {
        int actualTotalMinutes = this.appointments.stream().mapToInt((a) -> a.getDuration().getRoundedMinutes()).sum();
        int realTotalMinutes = endTime.getRoundedMinutes() - startTime.getRoundedMinutes() - interval.getRoundedMinutes();
        int delta = Math.abs(actualTotalMinutes - realTotalMinutes);
        if (delta > MAX_DELTA) {
            throw new RuntimeException(MAX_DELTA_EXCEEDED_ERROR);
        }
        if (delta > 0) {
            appointments.get(0).addTime(realTotalMinutes - actualTotalMinutes);
        }
    }

    public void save(Console c) {
        c.result(Wrapper.createDay(date, startTime, endTime, interval));
        for (Appointment a : appointments) {
            c.result(a.save(date));
        }
    }

    public Date getDate() {
        return date;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    @Override
    public String toString() {
        return "[" + date + "] from: " + startTime + " to " + endTime + " except for " + interval + "; tasks: " + Arrays.toString(appointments.toArray());
    }
}