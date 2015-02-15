package br.com.dextra.pma.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.models.Appointment;
import br.com.dextra.pma.models.Day;

public class FileParserTest {

    @Test
    public void testSimpleFile() throws InvalidFormatException {
        List<String> lines = Arrays.asList("2014-02-02+08:00+20", "2014-02-02+11:00", "2014-02-02+12:00+20", "2014-02-02+17:00");
        FileCache cache = new FileCache();
        List<Day> days = FileParser.parseFile(lines.iterator(), cache);

        assertCacheIsEmpty(cache);
        Day day = fetchOnlyDay(days, new Date(2014, 2, 2));

        List<Appointment> apps = getNAppointmentsSortedByTask(day, 1);
        Appointment onlyApp = apps.get(0);
        Assert.assertEquals(20, onlyApp.getTask());
        Assert.assertEquals(new Time(8 * 60), onlyApp.getDuration());
    }

    @Test
    public void testMultipleTasksFile() throws InvalidFormatException {
        List<String> lines = Arrays.asList(new String[] {
            "2014-02-02+07:00+1",
            "2014-02-02+08:20+2",
            "2014-02-02+11:00",
            "2014-02-02+12:00+2",
            "2014-02-02+14:30+3",
            "2014-02-02+16:00" });

        FileCache cache = new FileCache();
        List<Day> days = FileParser.parseFile(lines.iterator(), cache);

        assertCacheIsEmpty(cache);
        Day day = fetchOnlyDay(days, new Date(2014, 2, 2));

        List<Appointment> apps = getNAppointmentsSortedByTask(day, 3);

        Appointment firstApp = apps.get(0);
        Assert.assertEquals(1, firstApp.getTask());
        Assert.assertEquals(new Time(60 + 20), firstApp.getDuration());

        Appointment secondApp = apps.get(1);
        Assert.assertEquals(2, secondApp.getTask());
        Assert.assertEquals(new Time(5 * 60 + 10), secondApp.getDuration());

        Appointment thirdApp = apps.get(2);
        Assert.assertEquals(3, thirdApp.getTask());
        Assert.assertEquals(new Time(60 + 30), thirdApp.getDuration());
    }

    @Test
    public void testMultipleDays() throws InvalidFormatException {
        List<String> lines = Arrays.asList(new String[] {
            "2014-02-02+08:00+20",
            "2014-02-02+11:00",
            "2014-02-02+12:00+20",
            "2014-02-02+17:00",
            "2014-02-03+07:00+10",
            "2014-02-03+11:00",
            "2014-02-03+12:00+10",
            "2014-02-03+17:00" });
        FileCache cache = new FileCache();
        List<Day> days = FileParser.parseFile(lines.iterator(), cache);

        assertCacheIsEmpty(cache);
        Assert.assertEquals(2, days.size());
        Day day1 = days.get(0);

        Appointment onlyApp1 = getNAppointmentsSortedByTask(day1, 1).get(0);
        Assert.assertEquals(20, onlyApp1.getTask());
        Assert.assertEquals(new Time(8 * 60), onlyApp1.getDuration());

        Day day2 = days.get(1);
        Appointment onlyApp2 = getNAppointmentsSortedByTask(day2, 1).get(0);
        Assert.assertEquals(10, onlyApp2.getTask());
        Assert.assertEquals(new Time(9 * 60), onlyApp2.getDuration());
    }

    @Test
    public void testMultipleDaysNotEnded() throws InvalidFormatException {
        List<String> lines = Arrays.asList(new String[] {
            "2014-02-02+08:00+20",
            "2014-02-02+11:00",
            "2014-02-02+12:00+20",
            "2014-02-02+17:00",
            "2014-02-03+07:00+10",
            "2014-02-03+11:00",
            "2014-02-03+12:00+10" });
        FileCache cache = new FileCache();
        List<Day> days = FileParser.parseFile(lines.iterator(), cache);

        assertCacheHas(cache, 3);
        Day day = fetchOnlyDay(days, new Date(2014, 2, 2));

        Appointment onlyApp1 = getNAppointmentsSortedByTask(day, 1).get(0);
        Assert.assertEquals(20, onlyApp1.getTask());
        Assert.assertEquals(new Time(8 * 60), onlyApp1.getDuration());
    }

    @Test
    public void testSameTaskTwice() throws InvalidFormatException {
        List<String> lines = Arrays.asList("2014-02-02+07:00+1", "2014-02-02+08:20+2", "2014-02-02+09:00+1", "2014-02-02+11:00");
        FileCache cache = new FileCache();
        List<Day> days = FileParser.parseFile(lines.iterator(), cache);

        assertCacheIsEmpty(cache);
        Day day = fetchOnlyDay(days, new Date(2014, 2, 2));

        List<Appointment> apps = getNAppointmentsSortedByTask(day, 2);

        Appointment firstApp = apps.get(0);
        Assert.assertEquals(1, firstApp.getTask());
        Assert.assertEquals(new Time(3 * 60 + 20), firstApp.getDuration());

        Appointment secondApp = apps.get(1);
        Assert.assertEquals(2, secondApp.getTask());
        Assert.assertEquals(new Time(40), secondApp.getDuration());
    }

    @Test
    public void testWithComment() throws InvalidFormatException {
        List<String> lines = Arrays.asList("2014-02-02+07:00+1+Line 1", "2014-02-02+08:20+2", "2014-02-02+09:00+1+Line 2", "2014-02-02+11:00");
        FileCache cache = new FileCache();
        List<Day> days = FileParser.parseFile(lines.iterator(), cache);

        assertCacheIsEmpty(cache);
        Day day = fetchOnlyDay(days, new Date(2014, 2, 2));

        List<Appointment> apps = getNAppointmentsSortedByTask(day, 2);

        Appointment firstApp = apps.get(0);
        Assert.assertEquals(1, firstApp.getTask());
        Assert.assertEquals(new Time(3 * 60 + 20), firstApp.getDuration());
        Assert.assertEquals("Line 1\nLine 2", firstApp.getComment());

        Appointment secondApp = apps.get(1);
        Assert.assertEquals(2, secondApp.getTask());
        Assert.assertEquals(new Time(40), secondApp.getDuration());
        Assert.assertEquals(Appointment.DEFAULT_DESC, secondApp.getComment());
    }

    private List<Appointment> getNAppointmentsSortedByTask(Day day, int n) {
        List<Appointment> apps = day.getAppointments();
        Assert.assertEquals(n, apps.size());
        Collections.sort(apps, new Comparator<Appointment>() {
            @Override
            public int compare(Appointment o1, Appointment o2) {
                return Long.valueOf(o1.getTask()).compareTo(o2.getTask());
            }
        });
        return apps;
    }

    private Day fetchOnlyDay(List<Day> days, Date date) {
        Assert.assertEquals(1, days.size());
        Day firstDay = days.get(0);
        if (!firstDay.getDate().equals(date)) {
            Assert.assertTrue(false);
        }
        return firstDay;
    }

    private void assertCacheIsEmpty(FileCache cache) {
        cache.consumeRemainingFile((s) -> {
            Assert.assertTrue(false);
        });
    }

    private void assertCacheHas(FileCache cache, int rows) {
        final AtomicInteger count = new AtomicInteger();
        cache.consumeRemainingFile((s) -> {
            count.incrementAndGet();
        });
        Assert.assertEquals(rows, count.intValue());
    }
}
