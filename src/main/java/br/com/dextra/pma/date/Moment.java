package br.com.dextra.pma.date;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.io.Serializable;
import java.util.Calendar;

public class Moment implements Serializable {

	private static final long serialVersionUID = 989609738139366139L;

	private Date date;
	private Time time;

	public Moment() {
		this(Calendar.getInstance());
	}

	public Moment(Calendar currentTime) {
		this.date = new Date(currentTime.get(YEAR), currentTime.get(MONTH) + 1, currentTime.get(DAY_OF_MONTH));
		this.time = new Time(currentTime.get(HOUR_OF_DAY), currentTime.get(MINUTE));
	}

	public Date getDate() {
		return this.date;
	}

	public Time getTime() {
		return this.time;
	}

	@Override
	public String toString() {
		return date.toString() + '+' + time.toString();
	}
}