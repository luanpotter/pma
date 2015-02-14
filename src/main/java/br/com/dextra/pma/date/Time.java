package br.com.dextra.pma.date;

import java.io.Serializable;

import br.com.dextra.pma.utils.NumberUtils;

public class Time implements Serializable {

    private static final long serialVersionUID = 9180454594812684317L;

    private int minutes;

    public Time() {
    }

    public Time(String t) {
        this(parseString(t));
    }

    private static int parseString(String t) {
        String[] p = t.split(":");
        if (p.length != 2)
            throw new IllegalArgumentException("String must be in format hh:mm");
        int hours = Integer.parseInt(p[0]);
        if (hours < 0 || hours >= 24)
            throw new IllegalArgumentException("Hours must be between 0 and 24, " + hours + " found.");
        int minutes = Integer.parseInt(p[1]);
        if (minutes < 0 || minutes >= 60)
            throw new IllegalArgumentException("Minutes must be between 0 and 60, " + minutes + " found.");
        return hours * 60 + minutes;
    }

    public Time(int minutes) {
        this.minutes = minutes;
    }

    public Time(int hours, int minutes) {
        this.minutes = hours * 60 + minutes;
    }

    public int getRoundedMinutes() {
        return Math.round((float) minutes / 5) * 5;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getDifference(Time h) {
        return this.getMinutes() - h.getMinutes();
    }

    @Override
    public String toString() {
        int totalMinutes = getRoundedMinutes();
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return NumberUtils.toString(hours) + ':' + NumberUtils.toString(minutes);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Time)) {
            return false;
        }
        Time other = (Time) obj;
        return this.minutes == other.minutes;
    }

    public void addMinutes(int minutes) {
        this.minutes += minutes;
    }
}