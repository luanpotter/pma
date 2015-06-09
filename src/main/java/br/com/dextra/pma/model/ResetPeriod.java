package br.com.dextra.pma.model;

import java.io.Serializable;
import java.util.Calendar;

import lombok.Getter;
import br.com.dextra.pma.date.Date;

@Getter
public class ResetPeriod implements Serializable {

    private static final long serialVersionUID = -39166215184122953L;

    private Date start;
    private Date end;

    public ResetPeriod(Date start, Date end) {
        this.start = start;
        this.end = end;
        if (start.after(end)) {
            throw new RuntimeException("Start date cannot be after end date!");
        }
    }

    private static final int MOUNTH_START = -1, MOUNTH_COUNT = 4;

    public static ResetPeriod findFor(Date date) {
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
        return new ResetPeriod(firstDate, lastDate);
    }

    public static int getQuadrismestre(Date date) {
        return (date.getMonth() - MOUNTH_START - 1) / MOUNTH_COUNT;
    }

    public ResetPeriod withStart(Date start) {
        return new ResetPeriod(start, this.end);
    }

    public ResetPeriod withEnd(Date end) {
        return new ResetPeriod(this.start, end);
    }

    public int countWeekDays() {
        Calendar startCal = start.toCalendar();
        Calendar endCal = end.toCalendar();

        int workDays = 0;

        do {
            if (isWeekDay(startCal)) {
                ++workDays;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());

        return workDays;
    }

    private boolean isWeekDay(Calendar cal) {
        return cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
    }

    public ResetPeriod[] splitOn(Date date) {
	if (date.before(start) || date.equals(start)) {
            return new ResetPeriod[] { null, this };
        }
        if (date.after(start) && date.before(end)) {
            return new ResetPeriod[] { new ResetPeriod(start, date.addDays(-1)), new ResetPeriod(date, end) };
        }

	assert date.after(end) || date.equals(end);
        return new ResetPeriod[] { this, null };
    }

    public int expectedMinutes() {
        final Date turningPoint = new Date(2015, 05, 11);
        final int[] hoursPerDay = new int[] { 8, 6 };
        final int minutesPerHour = 60;

        ResetPeriod[] periods = splitOn(turningPoint);
        //System.out.println("8h: " + periods[0].start + " -> " + periods[0].end + " | 6: " + periods[1].start + " -> " + periods[1].end);
        int expectedMinutes = 0;
        for (int i = 0; i < periods.length; i++) {
            int days = periods[i] == null ? 0 : periods[i].countWeekDays();
            System.out.println("p" + i + " : " + days + " days");
            expectedMinutes += days * hoursPerDay[i] * minutesPerHour;
        }
        return expectedMinutes;
    }
}
