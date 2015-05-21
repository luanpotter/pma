package br.com.dextra.pma.date;

import org.junit.Assert;
import org.junit.Test;

public class DateTest {

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
}
