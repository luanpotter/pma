package br.com.dextra.pma.main;

import java.io.Serializable;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.model.TimedPeriod;
import br.com.dextra.pma.utils.CollectionUtils;
import br.com.dextra.pma.utils.SimpleObjectAccess;

@Getter
public class TimedPeriods implements Serializable {

	private final class CompareByStartDate implements Comparator<TimedPeriod>, Serializable {

		private static final long serialVersionUID = 2437492556492244229L;

		@Override
		public int compare(TimedPeriod thiz, TimedPeriod that) {
			Date thizStart = thiz.getPeriod().getStart();
			Date thatStart = that.getPeriod().getStart();

			if (thizStart == null) {
				if (thatStart == null) {
					return 0;
				}
				return -1;
			}
			if (thatStart == null) {
				return +1;
			}
			return thizStart.compareTo(thatStart);
		}
	}

	private static final long serialVersionUID = 1306367033533807140L;

	private static final String FILE_NAME = "periods.dat";

	@Setter
	private int defaultHoursPerDay;

	private SortedSet<TimedPeriod> periods;

	public TimedPeriods() {
		this.defaultHoursPerDay = 8;
		this.periods = new TreeSet<>(new CompareByStartDate());
	}

	public static TimedPeriods readOrCreate() {
		TimedPeriods periods = SimpleObjectAccess.<TimedPeriods> readFrom(FILE_NAME);
		if (periods != null) {
			return periods;
		} else {
			return new TimedPeriods();
		}
	}

	public void save() {
		SimpleObjectAccess.saveTo(FILE_NAME, this);
	}

	public boolean add(TimedPeriod period) {
		boolean result = periods.add(period);
		if (result) {
			if (validatePeriods()) {
				return true;
			}
			periods.remove(period);
			return false;
		}
		return false;
	}

	private boolean validatePeriods() {
		TimedPeriod before = null;
		for (TimedPeriod period : periods) {
			if (before != null && before.getPeriod().getEnd() == null) {
				return false;
			}
			if (period.getPeriod().getStart() == null) {
				if (before != null) {
					return false;
				}
			} else if (before != null && !before.getPeriod().getEnd().before(period.getPeriod().getStart())) {
				return false;
			}
			before = period;
		}
		return true;
	}

	public boolean remove(Integer i) {
		try {
			CollectionUtils.remove(periods, i);
			return true;
		} catch (ArrayIndexOutOfBoundsException ex) {
			return false;
		}
	}
}
