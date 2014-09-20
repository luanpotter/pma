package br.com.dextra.pma.date;

import br.com.dextra.pma.utils.NumberUtils;

public class Time {
    private int hours, minutes;

    public Time() {
    }

    public Time(Time t) {
        this.hours = t.hours;
        this.minutes = t.minutes;
    }

    public Time(String t) {
        String[] p = t.split(":");
        if (p.length != 2)
            throw new IllegalArgumentException("String must be in format hh:mm");
        this.hours = Integer.parseInt(p[0]);
        if (this.hours < 0 || this.hours >= 24)
            throw new IllegalArgumentException("Hours must be between 0 and 24, " + this.hours + " found.");
        this.minutes = Integer.parseInt(p[1]);
        if (this.minutes < 0 || this.minutes >= 60)
            throw new IllegalArgumentException("Minutes must be between 0 and 60, " + this.minutes + " found.");
    }

    public Time(int minutes) {
        this.hours = minutes / 60;
        if (this.hours < 0 || this.hours >= 24)
            throw new IllegalArgumentException("Hours must be between 0 and 24");
        this.minutes = minutes % 60;
    }

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getRoundedMinutes() {
        return Math.round((float) minutes / 5) * 5;
    }

    public int getTotalMinutes() {
        return hours * 60 + getRoundedMinutes();
    }

    public int getActualTotalMinutes() {
        return hours * 60 + minutes;
    }

    public int getDifference(Time h) {
        return this.getActualTotalMinutes() - h.getActualTotalMinutes();
    }

    public void addMinutes(int minutes) {
        this.minutes += minutes;
        this.hours += this.minutes / 60;
        this.minutes %= 60;
    }

    @Override
    public String toString() {
        int minutes = getRoundedMinutes();
        int hours = this.hours + minutes / 60;
        minutes %= 60;
        return NumberUtils.toString(hours) + ':' + NumberUtils.toString(minutes);
    }
}