package br.com.dextra.pma.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(exclude = { "hoursPerDay" })
public class TimedPeriod implements Serializable {

	private static final long serialVersionUID = -7799340576068423872L;

	private Period period;
	private int hoursPerDay;

	@Override
	public String toString() {
		return period + ": " + hoursPerDay + " h/day";
	}
}
