package br.com.dextra.pma.controllers;

import java.util.List;

import xyz.luan.console.parser.ActionCall;
import xyz.luan.console.parser.Callable;
import xyz.luan.console.parser.Controller;
import xyz.luan.console.parser.Output;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.actions.Optional;
import br.com.dextra.pma.main.Options.Option;
import br.com.dextra.pma.main.PMAContext;
import br.com.dextra.pma.main.PMAParser;
import br.com.dextra.pma.main.PMAParser.InvalidFormatException;
import br.com.dextra.pma.models.Day;
import br.com.dextra.pma.utils.MapBuilder;

public class ParserController extends Controller<PMAContext> {

    @Action("save")
    public Output save() throws InvalidFormatException {
        Output res = new Output();
        String fileName = context.o().get(Option.LOG_FILE);
        List<Day> days = PMAParser.parseLogs(fileName, false);
        for (Day day : days) {
            res.append(day.save());
        }

        return res;
    }

    @Action("log")
    public Output log(@Optional String backup) throws InvalidFormatException {
        boolean bkp = "true".equals(backup);

        String fileName = context.o().get(bkp ? Option.BACKUP_FILE : Option.LOG_FILE);
        List<Day> days = PMAParser.parseLogs(fileName, true);

        Output out = new Output(days.size() + " days parsed;");
        for (Day day : days) {
            out.add(day.toString()); // TODO output properly
        }
        return out;
    }

    @Action("today")
    public Output today() {
        return new Output("TODO WIP");
    }

    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":save", ":save", "Save current day on web service"));
        callables.add(new ActionCall(name + ":log", ":log", "Show current log"));
        callables.add(new ActionCall(name + ":log", ":log :backup", MapBuilder.<String, String> from("backup", "true"),
                "Show current log backup"));
        callables.add(new ActionCall(name + ":today", ":today", "Show what has been done today already"));
    }
}