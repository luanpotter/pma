package br.com.dextra.pma.date;

import br.com.dextra.pma.utils.NumberUtils;

public class Date {
    private int year, month, day;

    public Date(String date) {
        String[] p = date.split("-");
        if (p.length != 3)
            throw new IllegalArgumentException("String must be in format yyyy-mm-dd");
        this.year = Integer.parseInt(p[0]);
        this.month = Integer.parseInt(p[1]);
        this.day = Integer.parseInt(p[2]);
    }

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Date))
            return false;
        Date d = (Date) o;
        return this.year == d.year && this.month == d.month && this.day == d.day;
    }

    @Override
    public String toString() {
        return NumberUtils.toString(year) + '-' + NumberUtils.toString(month) + '-' + NumberUtils.toString(day);
    }
}