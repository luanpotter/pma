package br.com.dextra.pma.model;

import java.io.Serializable;
import java.util.Calendar;

import lombok.Getter;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.service.FeriadosService;

import com.google.common.base.Predicate;

@Getter
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

	private static final int MOUNTH_START = -1, MOUNTH_COUNT = 4;

	public static Period findQuadrismestre(Date date) {
		int quadrimestre = getQuadrismestre(date);
		int startingYear = date.getYear(), endingYear = startingYear;
		int startingMonth = MOUNTH_COUNT * quadrimestre + MOUNTH_START;
		int endingMonth = startingMonth + MOUNTH_COUNT - 1;
		if (startingMonth < 0) {
			startingMonth += 12;
			startingYear--;
		}
		if (endingMonth >= 12) {
			endingMonth -= 12;
			endingYear++;
		}
		Date firstDate = new Date(startingYear, startingMonth + 1, 1).firstDay();
		Date lastDate = new Date(endingYear, endingMonth + 1, 1).lastDay();
		return new Period(firstDate, lastDate);
	}

	public static int getQuadrismestre(Date date) {
		return (date.getMonth() - MOUNTH_START - 1) / MOUNTH_COUNT;
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

	public Period[] splitOn(Date date) {
		if (date.before(start) || date.equals(start)) {
			return new Period[] { null, this };
		}
		if (date.after(start) && date.before(end)) {
			return new Period[] { new Period(start, date.addDays(-1)), new Period(date, end) };
		}

		assert date.after(end) || date.equals(end);
		return new Period[] { this, null };
	}

	public int expectedMinutes() {
		final Date turningPoint = new Date(2015, 05, 11);
		final int[] hoursPerDay = new int[] { 8, 6 };
		final int minutesPerHour = 60;

		Period[] periods = splitOn(turningPoint);
		int expectedMinutes = 0;
		for (int i = 0; i < periods.length; i++) {
			int days = periods[i] == null ? 0 : periods[i].countWorkingDays();
			expectedMinutes += days * hoursPerDay[i] * minutesPerHour;
		}
		return expectedMinutes;
	}

	@Override
	public String toString() {
		return start + " -> " + end;
	}
}
