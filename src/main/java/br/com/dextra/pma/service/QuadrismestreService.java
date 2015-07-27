package br.com.dextra.pma.service;

import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.model.Period;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QuadrismestreService {

	private final int MOUNTH_START = -1, MOUNTH_COUNT = 4;

	public Period findFor(Date date) {
		int quadrimestre = findIndexFor(date);
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

	public int findIndexFor(Date date) {
		return (date.getMonth() - MOUNTH_START - 1) / MOUNTH_COUNT;
	}
}
