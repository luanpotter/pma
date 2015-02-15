package br.com.dextra.pma.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import xyz.luan.console.parser.Console;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.main.Wrapper;

public class Day implements Serializable {

    private static final long serialVersionUID = -8717524564031422430L;

    private static final int MAX_DELTA = 5;
    private static final String MAX_DELTA_EXCEEDED_ERROR = "Something went wrong! The sum of the activities specified does not comply with the start/ending times. Did you edit the log.dat file by hand?";

    @Getter
    private Date date;

    @Getter
    private Time startTime, interval, endTime;

    private Map<Long, Appointment> appointments;

    public Day(Date date, Time start) {
        this.date = date;
        this.startTime = start;
        this.interval = new Time();
        this.appointments = new HashMap<>();
    }

    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments.values());
    }

    public void save(Console c) {
        c.result(Wrapper.createDay(date, startTime, endTime, interval));
        for (Appointment a : appointments.values()) {
            c.result(a.save(date));
        }
    }

    public void addTask(long task, String desc, int minutes) {
        if (task == Appointment.INTERVAL_TASK) {
            interval.addMinutes(minutes);
        } else {
            if (!appointments.containsKey(task)) {
                appointments.put(task, new Appointment(task));
            }
            appointments.get(task).addTime(minutes);
            appointments.get(task).addDescription(desc);
        }
    }

    public void end(Time time) {
        endTime = time;
        fixPossibleImprecisionMistake();
    }

    private void fixPossibleImprecisionMistake() {
        int actualTotalMinutes = getTotalTimeInTasks();
        int realTotalMinutes = endTime.getRoundedMinutes() - startTime.getRoundedMinutes() - interval.getRoundedMinutes();
        int delta = Math.abs(actualTotalMinutes - realTotalMinutes);
        if (delta > MAX_DELTA) {
            throw new RuntimeException(MAX_DELTA_EXCEEDED_ERROR);
        }
        if (delta > 0) {
            appointments.get(0).addTime(realTotalMinutes - actualTotalMinutes);
        }
    }

    public final void print(Console console) {
        console.result(date);
        console.tabIn();
        printInternal(console);
        console.result("Total time in tasks: " + new Time(getTotalTimeInTasks()));
        console.tabOut();
    }

    protected int getTotalTimeInTasks() {
        return appointments.values().stream().mapToInt(a -> a.getDuration().getRoundedMinutes()).sum();
    }

    protected void printInternal(Console console) {
        if (endTime == null) {
            console.result(String.format("Start: %s | Interval: %s", startTime, interval));
        } else {
            console.result(String.format("Start: %s | Interval: %s | End: %s", startTime, interval, endTime));
        }
        getAppointments().forEach(a -> console.result(a));
    }
}