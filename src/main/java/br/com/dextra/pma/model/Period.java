package br.com.dextra.pma.model;

import java.io.Serializable;
import java.util.Calendar;

import com.google.common.base.Predicate;

import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.service.FeriadosService;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Period implements Serializable {

	private static final long serialVersionUID = -39166215184122953L;

	private Date start;
	private Date end;

	public Period(Date start, Date end) {
		this.start = start;
		this.end = end;
		if (start != null && end != null && start.after(end)) {
			throw new RuntimeException("Start date cannot be after end date!");
		}
	}

	public Date theDayBefore() {
		return start.addDays(-1);
	}

	public Date theDayAfter() {
		return end.addDays(+1);
	}

	public Period withStart(Date start) {
		return new Period(start, this.end);
	}

	public Period withEnd(Date end) {
		return new Period(this.start, end);
	}

	public int countWeekDays() {
		return countDays(c -> isWeekDay(c));
	}

	public int countWorkingDays() {
		return countDays(c -> isWeekDay(c) && isNotFeriado(c));
	}

	private int countDays(Predicate<Calendar> where) {
		Calendar startCal = start.toCalendar();
		Calendar endCal = end.toCalendar();

		int workDays = 0;

		do {
			if (where.apply(startCal)) {
				++workDays;
			}
			startCal.add(Calendar.DAY_OF_MONTH, 1);
		} while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());

		return workDays;
	}

	private static boolean isNotFeriado(Calendar cal) {
		return !FeriadosService.isFeriado(new Date(cal));
	}

	private boolean isWeekDay(Calendar cal) {
		return cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
	}

	@Override
	public String toString() {
		return start + " -> " + end;
	}
}
