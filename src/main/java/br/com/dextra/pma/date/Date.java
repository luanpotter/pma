package br.com.dextra.pma.date;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.io.Serializable;
import java.util.Calendar;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import br.com.dextra.pma.utils.NumberUtils;

@EqualsAndHashCode
public class Date implements Serializable {

    private static final long serialVersionUID = -446196417235991965L;

    @Getter
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

    public Date(Calendar currentTime) {
        this(currentTime.get(YEAR), currentTime.get(MONTH) + 1, currentTime.get(DAY_OF_MONTH));
    }

    public int daysInMonth() {
        return toCalendar().getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public Calendar toCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        return cal;
    }

    public long toMillis() {
        return toCalendar().getTime().getTime();
    }

    public Date firstDay() {
        return new Date(year, month, 1);
    }

    public Date lastDay() {
        return new Date(year, month, daysInMonth());
    }

    public boolean before(Date date) {
        return this.toMillis() < date.toMillis();
    }

    public boolean after(Date date) {
        return this.toMillis() > date.toMillis();
    }

    public Date addDays(int days) {
        Calendar c = toCalendar();
        c.add(Calendar.DAY_OF_MONTH, days);
        return new Date(c);
    }

    public static Date today() {
        return new Date(Calendar.getInstance());
    }

    @Override
    public String toString() {
        return NumberUtils.toString(year) + '-' + NumberUtils.toString(month) + '-' + NumberUtils.toString(day);
    }
}
