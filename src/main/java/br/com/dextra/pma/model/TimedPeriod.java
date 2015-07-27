package br.com.dextra.pma.model;

import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import lombok.Getter;

@Getter
public class TimedPeriod extends Period {

	private static final long serialVersionUID = -7799340576068423872L;

	private int hoursPerDay;

	public TimedPeriod(Date start, Date end, int hoursPerDay) {
		super(start, end);
		this.hoursPerDay = hoursPerDay;
	}

	public int countWorkingMinutes() {
		return countWorkingHours() * Time.MINUTES_PER_HOUR;
	}

	private int countWorkingHours() {
		return countWorkingDays() * hoursPerDay;
	}

	@Override
	public String toString() {
		return super.toString() + ": " + hoursPerDay + " h/day";
	}
}
