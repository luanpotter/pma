package br.com.dextra.pma.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TimedPeriod implements Serializable {

	private static final long serialVersionUID = -7799340576068423872L;

	private Period period;
	private int hoursPerDay;

	@Override
	public String toString() {
		return period + ": " + hoursPerDay + " h/day";
	}
}
