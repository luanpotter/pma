package br.com.dextra.pma.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import xyz.luan.console.fn.FnController;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.model.Day;
import br.com.dextra.pma.model.Record;
import br.com.dextra.pma.model.Period;
import br.com.dextra.pma.service.PmaService;
import br.com.dextra.pma.utils.MapBuilder;

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

    @Action("showRecord")
    public CallResult showRecord(Date date) {
        Day day = PmaService.fetchDay(context, date);
        console.result(day.toRecordList().stream().map(r -> r.toString()).collect(Collectors.joining("\n")));
        return CallResult.SUCCESS;
    }

    @Action("shownterval")
    public CallResult showRecords(Date start, Date end) {
        List<Record> records = new ArrayList<>();
        while (!end.after(start)) {
            Day day = PmaService.fetchDay(context, start);
            start = start.addDays(1);
            if (day == null) {
                continue;
            }
            records.addAll(day.toRecordList());
        }
        console.result(records.stream().map(r -> r.toString()).collect(Collectors.joining("\n")));
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
    public CallResult status(Date date) {
        Period period = Period.findQuadrismestre(date).withEnd(date);
        int minutes = PmaService.fetchMinutesWorked(period.getStart(), period.getEnd());
        console.result("status: " + (minutes - period.expectedMinutes()) + " min");
        return CallResult.SUCCESS;
    }

    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":show", ":show date", "Show the given date (how it is in PMA)"));
        callables.add(new ActionCall(name + ":showRecord", ":show :record date", "Show the given date (how it is in PMA) as record lines"));
        callables.add(new ActionCall(name + ":shownterval", ":show :record start end", "Show the records in the interval"));
        callables.add(new ActionCall(name + ":minutes", ":minutes start end",
                "Sums all time worked in between the dates provided (inclusive)(in minutes)(fetches from PMA)"));
        callables.add(new ActionCall(name + ":minutesForMonth", ":minutes month",
                "Sums all time worked in given month (as yyyy-mm) (in minutes)(fetches from PMA)"));
        callables.add(new ActionCall(name + ":status", ":status date", "Show the status for the given date."));
        callables.add(new ActionCall(name + ":status", ":status", MapBuilder.from("date", Date.today().toString()), "Show the status for today."));
    }
}
