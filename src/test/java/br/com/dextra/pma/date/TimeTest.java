package br.com.dextra.pma.date;

import org.junit.Assert;
import org.junit.Test;

public class TimeTest {

    @Test
    public void timeFromString() {
        Assert.assertEquals(20, new Time("00:20").getMinutes());
        Assert.assertEquals(4 * 60 + 20, new Time("04:20").getMinutes());
        Assert.assertEquals(60 + 21, new Time("01:21").getMinutes());
    }

    @Test
    public void incorrectTimeFromString() {
        assertErrorMessage("String must be in format hh:mm", "0020");
        assertErrorMessage("Hours must be between 0 and 24, 30 found.", "30:20");
        assertErrorMessage("Minutes must be between 0 and 60, 71 found.", "02:71");
    }

    private void assertErrorMessage(String message, String arg) {
        try {
            new Time(arg);
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals(message, ex.getMessage());
            return;
        }
        Assert.assertTrue(false);
    }

    @Test
    public void testRoundedMinutes() {
        Assert.assertEquals(30, new Time(31).getRoundedMinutes());
        Assert.assertEquals(3 * 60, new Time("02:59").getRoundedMinutes());
    }

    @Test
    public void testRoundedToString() {
        Assert.assertEquals("00:30", new Time(31).toString());
        Assert.assertEquals("03:00", new Time("02:59").toString());
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(new Time(31), new Time("00:31"));
        Assert.assertEquals(new Time(2 * 60 + 24), new Time("02:24"));
    }
}
