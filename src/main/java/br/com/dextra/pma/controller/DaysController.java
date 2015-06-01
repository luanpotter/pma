package br.com.dextra.pma.controller;

import java.util.List;

import xyz.luan.console.fn.FnController;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.model.Day;
import br.com.dextra.pma.model.ResetPeriod;
import br.com.dextra.pma.service.PmaService;

@FnController
public class DaysController extends BaseController {

    @Action("show")
    public CallResult show(Date date) {
        Day day = PmaService.fetchDay(context, date);
        if (day == null) {
            console.error("Nothing found on the specified day.");
            return CallResult.ERROR;
        }
        day.print(console);
        return CallResult.SUCCESS;
    }

    @Action("minutes")
    public CallResult minutes(Date start, Date end) {
        int minutes = PmaService.fetchMinutesWorked(start, end);
        console.result("Total time in period (inclusive): " + minutes + " min");
        return CallResult.SUCCESS;
    }

    @Action("minutesForMonth")
    public CallResult minutesForMonth(String month) {
        Date firstDay = new Date(month + "-01");
        return minutes(firstDay, firstDay.lastDay());
    }

    @Action("status")
    public CallResult status(Date date, Integer feriados) {
        ResetPeriod period = ResetPeriod.findFor(date);
        int minutes = PmaService.fetchMinutesWorked(period.getStart(), period.getEnd());
        int expectedMinutes = (new ResetPeriod(period.getStart(), date).countWeekDays() - feriados) * 6 * 60;
        console.result("status: " + (minutes - expectedMinutes) + " min");
        return CallResult.SUCCESS;
    }

    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":show", ":show date", "Show the past date (how it is in PMA)"));
        callables.add(new ActionCall(name + ":minutes", ":minutes start end",
                "Sums all time worked in between the dates provided (inclusive)(in minutes)(fetches from PMA)"));
        callables.add(new ActionCall(name + ":minutesForMonth", ":minutes month",
                "Sums all time worked in given month (as yyyy-mm) (in minutes)(fetches from PMA)"));
        callables.add(new ActionCall(name + ":status", ":status date feriados", "Show the status for the given date."));
    }
}
