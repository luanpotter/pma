package br.com.dextra.pma.model;

import org.junit.Assert;
import org.junit.Test;

import br.com.dextra.pma.date.Date;

public class PeriodTest {

	@Test
	public void countWeekDays() {
		Period period = new Period(new Date("2015-03-03"), new Date("2015-04-08"));
		Assert.assertEquals(27, period.countWeekDays());

		period = new Period(new Date("2015-05-02"), new Date("2015-05-03"));
		Assert.assertEquals(0, period.countWeekDays());

		period = new Period(new Date("2015-05-05"), new Date("2015-05-05"));
		Assert.assertEquals(1, period.countWeekDays());
	}

	@Test
	public void countWorkingDays() {
		Period period = new Period(new Date("2015-03-03"), new Date("2015-04-08"));
		Assert.assertEquals(26, period.countWorkingDays());
	}

}
