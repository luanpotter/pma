package br.com.dextra.pma.controllers;

import java.util.List;

import xyz.luan.console.fn.FnController;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.main.Wrapper;

@FnController
public class DaysController extends BaseController {

    @Action("show")
    public CallResult show(Date date) {
        Wrapper.fetchDay(context, date).print(console);
        return CallResult.SUCCESS;
    }

    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":show", ":show date", "Show the past date (how it is in PMA)"));
    }
}
