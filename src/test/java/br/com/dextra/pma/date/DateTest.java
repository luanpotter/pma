package br.com.dextra.pma.date;

import org.junit.Assert;
import org.junit.Test;

public class DateTest {

    @Test
    public void firstDay() {
        Assert.assertEquals(new Date("2013-05-01"), new Date("2013-05-02").firstDay());
        Assert.assertEquals(new Date("2013-02-01"), new Date("2013-02-03").firstDay());
        Assert.assertEquals(new Date("2016-02-01"), new Date("2016-02-28").firstDay());
    }

    @Test
    public void lastDay() {
        Assert.assertEquals(new Date("2013-05-31"), new Date("2013-05-02").lastDay());
        Assert.assertEquals(new Date("2013-02-28"), new Date("2013-02-03").lastDay());
        Assert.assertEquals(new Date("2016-02-29"), new Date("2016-02-28").lastDay());
    }

    @Test
    public void daysInMonth() {
        Assert.assertEquals(30, new Date("2013-04-02").daysInMonth());
        Assert.assertEquals(28, new Date("2013-02-02").daysInMonth());
        Assert.assertEquals(29, new Date("2016-02-02").daysInMonth());
    }

    @Test
    public void dateFromString() {
        Date date = new Date("2013-04-12");
        Assert.assertEquals(12, date.getDay());
        Assert.assertEquals(4, date.getMonth());
        Assert.assertEquals(2013, date.getYear());
    }

    @Test
    public void dateToString() {
        Date date = new Date(1, 2, 1998);
        Assert.assertEquals("01-02-1998", date.toString());
    }

    @Test
    public void dateEquals() {
        Assert.assertEquals(new Date("2000-04-05"), new Date(2000, 4, 5));
        Assert.assertNotEquals(new Date("2000-05-04"), new Date(2000, 4, 5));
    }

    @Test
    public void before() {
        Assert.assertTrue(new Date("2000-04-02").before(new Date("2000-04-03")));
        Assert.assertTrue(new Date("2000-04-02").before(new Date("2000-05-01")));
        Assert.assertTrue(new Date("2000-04-02").before(new Date("2001-03-01")));
        Assert.assertFalse(new Date("2000-04-02").before(new Date("2000-04-01")));
        Assert.assertFalse(new Date("2000-04-02").before(new Date("2000-03-01")));
        Assert.assertFalse(new Date("2000-04-02").before(new Date("1999-03-01")));
    }

    @Test
    public void after() {
        Assert.assertFalse(new Date("2000-04-02").after(new Date("2000-04-03")));
        Assert.assertFalse(new Date("2000-04-02").after(new Date("2000-05-01")));
        Assert.assertFalse(new Date("2000-04-02").after(new Date("2001-03-01")));
        Assert.assertTrue(new Date("2000-04-02").after(new Date("2000-04-01")));
        Assert.assertTrue(new Date("2000-04-02").after(new Date("2000-03-01")));
        Assert.assertTrue(new Date("2000-04-02").after(new Date("1999-03-01")));
    }

}
