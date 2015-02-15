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

    public Moment(Calendar currentTime) {
        this.date = new Date(currentTime);
        this.time = new Time(currentTime);
    }

    @Override
    public String toString() {
        return date.toString() + '+' + time.toString();
    }
}