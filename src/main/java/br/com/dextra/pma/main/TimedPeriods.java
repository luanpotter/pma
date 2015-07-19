package br.com.dextra.pma.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import br.com.dextra.pma.model.TimedPeriod;
import br.com.dextra.pma.utils.SimpleObjectAccess;

@Getter
public class TimedPeriods implements Serializable {

	private static final long serialVersionUID = 1306367033533807140L;

	private static final String FILE_NAME = "periods.dat";

	@Setter
	private int defaultHoursPerDay;

	private List<TimedPeriod> periods;

	public TimedPeriods() {
		this.defaultHoursPerDay = 8;
		this.periods = new ArrayList<>();
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
}
