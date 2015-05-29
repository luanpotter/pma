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
}
