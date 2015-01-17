package br.com.dextra.pma.controllers;

import java.util.List;

import xyz.luan.console.fn.FnController;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.actions.Optional;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import br.com.dextra.pma.main.FileParser;
import br.com.dextra.pma.main.FileParser.InvalidFormatException;
import br.com.dextra.pma.main.Options.Option;
import br.com.dextra.pma.models.Day;
import br.com.dextra.pma.utils.MapBuilder;
import br.com.dextra.pma.utils.SimpleObjectAccess;

@FnController
public class ParserController extends BaseController {

    @Action("save")
    public CallResult save() throws InvalidFormatException {
        String fileName = context.o().get(Option.LOG_FILE);

        List<Day> days = FileParser.parseLogs(fileName, false);
        try {
            for (Day day : days) {
                day.save(console);
            }
        } catch (RuntimeException e) {
            console.error("Some error occured while sending data to server. Your data is safe at your backup and at tmp.dayslist.dat");
            SimpleObjectAccess.saveTo("tmp.dayslist.dat", days);
            throw e;
        }

        return CallResult.SUCCESS;
    }

    @Action("log")
    public CallResult log(@Optional String backup) throws InvalidFormatException {
        boolean bkp = "true".equals(backup);

        String fileName = context.o().get(bkp ? Option.BACKUP_FILE : Option.LOG_FILE);
        List<Day> days = FileParser.parseLogs(fileName, true);

        console.result(days.size() + " days parsed;");
        for (Day day : days) {
            console.result(day.toString()); // TODO output properly
        }
        return CallResult.SUCCESS;
    }

    @Action("today")
    public CallResult today() {
        console.error("TODO WIP");
        return CallResult.ERROR;
    }

    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":save", ":save", "Save current day on web service"));
        callables.add(new ActionCall(name + ":log", ":log", "Show current log"));
        callables.add(new ActionCall(name + ":log", ":log :backup", MapBuilder.<String, String> from("backup", "true"),
                "Show current log backup"));
        callables.add(new ActionCall(name + ":today", ":today", "Show what has been done today already"));
    }
}