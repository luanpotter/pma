package br.com.dextra.pma.model;

import org.junit.Assert;
import org.junit.Test;

import br.com.dextra.pma.date.Date;
import static br.com.dextra.pma.model.ResetPeriod.getQuadrismestre;

public class ResetPeriodTest {

    @Test
    public void countWeekDays() {
        ResetPeriod period = new ResetPeriod(new Date("2015-03-03"), new Date("2015-04-08"));
        Assert.assertEquals(27, period.countWeekDays());

        period = new ResetPeriod(new Date("2015-05-02"), new Date("2015-05-03"));
        Assert.assertEquals(0, period.countWeekDays());

        period = new ResetPeriod(new Date("2015-05-05"), new Date("2015-05-05"));
        Assert.assertEquals(1, period.countWeekDays());
    }

    @Test
    public void countWorkingDays() {
        ResetPeriod period = new ResetPeriod(new Date("2015-03-03"), new Date("2015-04-08"));
        Assert.assertEquals(26, period.countWorkingDays());
    }

    @Test
    public void findFor() {
        ResetPeriod period = ResetPeriod.findFor(new Date("2015-01-30"));
        Assert.assertEquals(new Date("2014-12-01"), period.getStart());
        Assert.assertEquals(new Date("2015-03-31"), period.getEnd());

        period = ResetPeriod.findFor(new Date("2012-04-13"));
        Assert.assertEquals(new Date("2012-04-01"), period.getStart());
        Assert.assertEquals(new Date("2012-07-31"), period.getEnd());

        period = ResetPeriod.findFor(new Date("1996-11-02"));
        Assert.assertEquals(new Date("1996-08-01"), period.getStart());
        Assert.assertEquals(new Date("1996-11-30"), period.getEnd());

        period = ResetPeriod.findFor(new Date("2000-12-01"));
        Assert.assertEquals(new Date("2000-12-01"), period.getStart());
        Assert.assertEquals(new Date("2001-03-31"), period.getEnd());
    }

    @Test
    public void getQuadrimestre() {
        Assert.assertEquals(0, getQuadrismestre(new Date("2010-01-01")));
        Assert.assertEquals(0, getQuadrismestre(new Date("2010-02-01")));
        Assert.assertEquals(0, getQuadrismestre(new Date("2010-03-01")));
        Assert.assertEquals(1, getQuadrismestre(new Date("2010-04-01")));
        Assert.assertEquals(1, getQuadrismestre(new Date("2010-05-01")));
        Assert.assertEquals(1, getQuadrismestre(new Date("2010-06-01")));
        Assert.assertEquals(1, getQuadrismestre(new Date("2010-07-01")));
        Assert.assertEquals(2, getQuadrismestre(new Date("2010-08-01")));
        Assert.assertEquals(2, getQuadrismestre(new Date("2010-09-01")));
        Assert.assertEquals(2, getQuadrismestre(new Date("2010-10-01")));
        Assert.assertEquals(2, getQuadrismestre(new Date("2010-11-01")));
        Assert.assertEquals(3, getQuadrismestre(new Date("2010-12-01")));
    }

}
