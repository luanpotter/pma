package br.com.dextra.pma.date;

import java.io.Serializable;
import java.util.Calendar;

import lombok.Getter;

public class Moment implements Serializable {

    private static final long serialVersionUID = 989609738139366139L;

    @Getter
    private Date date;

    @Getter
    private Time time;

    public Moment() {
        this(Calendar.getInstance());
    }

    public Moment(String s) {
        this(getCalendarFor(s));
    }

    // TODO add all cases, make it simpler
    private static Calendar getCalendarFor(String s) {
        String[] ps = s.split(" ");
        int value = Integer.parseInt(ps[0]);
        String unit = ps[1].trim();
        Calendar cal = Calendar.getInstance();
        if (unit.equals("seconds")) {
            cal.add(Calendar.SECOND, value);
	} else {
            throw new RuntimeException("Invalid unit");
        }
        return cal;
    }

    public Moment(Calendar currentTime) {
        this.date = new Date(currentTime);
        this.time = new Time(currentTime);
    }

    @Override
    public String toString() {
        return date.toString() + '+' + time.toString();
    }
}
