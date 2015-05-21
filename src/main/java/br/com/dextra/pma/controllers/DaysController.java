package br.com.dextra.pma.controllers;

import java.util.List;

import xyz.luan.console.fn.FnController;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.main.Wrapper;
import br.com.dextra.pma.models.Day;

@FnController
public class DaysController extends BaseController {

    @Action("show")
    public CallResult show(Date date) {
        Day day = Wrapper.fetchDay(context, date);
        if (day == null) {
            console.error("Nothing found on the specified day.");
            return CallResult.ERROR;
        }
        day.print(console);
        return CallResult.SUCCESS;
    }

    @Action("minutes")
    public CallResult minutes(Date start, Date end) {
        int minutes = Wrapper.fetchMinutesWorked(start, end);
        console.result("Total time in period (inclusive): " + minutes + " min");
        return CallResult.SUCCESS;
    }

    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":show", ":show date", "Show the past date (how it is in PMA)"));
        callables.add(new ActionCall(name + ":minutes", ":minutes start end",
                "Sums all time worked in between the dates provided (inclusive)(in minutes)(fetches from PMA)"));
    }
}
