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
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public Date lastDay() {
        return new Date(year, month, daysInMonth());
    }

    @Override
    public String toString() {
        return NumberUtils.toString(year) + '-' + NumberUtils.toString(month) + '-' + NumberUtils.toString(day);
    }
}