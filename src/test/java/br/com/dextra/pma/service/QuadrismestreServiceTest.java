package br.com.dextra.pma.service;

import static br.com.dextra.pma.service.QuadrismestreService.findFor;
import static br.com.dextra.pma.service.QuadrismestreService.findIndexFor;

import org.junit.Assert;
import org.junit.Test;

import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.model.Period;

public class QuadrismestreServiceTest {
	@Test
	public void findForTest() {
		Period period;

		period = findFor(new Date("2015-01-30"));
		Assert.assertEquals(new Date("2014-12-01"), period.getStart());
		Assert.assertEquals(new Date("2015-03-31"), period.getEnd());

		period = findFor(new Date("2012-04-13"));
		Assert.assertEquals(new Date("2012-04-01"), period.getStart());
		Assert.assertEquals(new Date("2012-07-31"), period.getEnd());

		period = findFor(new Date("1996-11-02"));
		Assert.assertEquals(new Date("1996-08-01"), period.getStart());
		Assert.assertEquals(new Date("1996-11-30"), period.getEnd());

		period = findFor(new Date("2000-12-01"));
		Assert.assertEquals(new Date("2000-12-01"), period.getStart());
		Assert.assertEquals(new Date("2001-03-31"), period.getEnd());
	}

	@Test
	public void getQuadrimestreTest() {
		Assert.assertEquals(0, findIndexFor(new Date("2010-01-01")));
		Assert.assertEquals(0, findIndexFor(new Date("2010-02-01")));
		Assert.assertEquals(0, findIndexFor(new Date("2010-03-01")));
		Assert.assertEquals(1, findIndexFor(new Date("2010-04-01")));
		Assert.assertEquals(1, findIndexFor(new Date("2010-05-01")));
		Assert.assertEquals(1, findIndexFor(new Date("2010-06-01")));
		Assert.assertEquals(1, findIndexFor(new Date("2010-07-01")));
		Assert.assertEquals(2, findIndexFor(new Date("2010-08-01")));
		Assert.assertEquals(2, findIndexFor(new Date("2010-09-01")));
		Assert.assertEquals(2, findIndexFor(new Date("2010-10-01")));
		Assert.assertEquals(2, findIndexFor(new Date("2010-11-01")));
		Assert.assertEquals(3, findIndexFor(new Date("2010-12-01")));
	}
}
