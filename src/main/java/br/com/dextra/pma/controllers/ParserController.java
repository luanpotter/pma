package br.com.dextra.pma.controllers;

import java.util.List;

import xyz.luan.console.fn.FnController;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.actions.Optional;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import br.com.dextra.pma.exceptions.NotLoggedIn;
import br.com.dextra.pma.main.Wrapper;
import br.com.dextra.pma.main.Options.Option;
import br.com.dextra.pma.models.Appointment;
import br.com.dextra.pma.models.Day;
import br.com.dextra.pma.parser.FileParser;
import br.com.dextra.pma.parser.InvalidFormatException;
import br.com.dextra.pma.utils.MapBuilder;
import br.com.dextra.pma.utils.SimpleObjectAccess;

@FnController
public class ParserController extends BaseController {

    @Action("save")
    public CallResult save() throws InvalidFormatException {
        assertLoggedIn();
        String fileName = context.o().get(Option.LOG_FILE);

        List<Day> days = FileParser.parseLogs(fileName, true);
        try {
            for (Day day : days) {
                day.save(console);
            }
        } catch (RuntimeException e) {
            console.error("Some error occured while sending data to server. Your data is safe at your backup and at tmp.dayslist.dat");
            SimpleObjectAccess.saveTo("tmp.dayslist.dat", days);
            throw e;
        }

        FileParser.replaceFiles(fileName);

        return CallResult.SUCCESS;
    }

    private void assertLoggedIn() {
        if (!Wrapper.isLoggedIn()) {
            throw new NotLoggedIn();
        }
    }

    @Action("log")
    public CallResult log(@Optional String backup) throws InvalidFormatException {
        boolean bkp = "true".equals(backup);

        String fileName = context.o().get(bkp ? Option.BACKUP_FILE : Option.LOG_FILE);
        List<Day> days = FileParser.parseLogs(fileName, false);

        console.result(days.size() + " days parsed;");
        for (Day day : days) {
            console.result(day.getDate());
            console.tabIn();
            console.result(String.format("Start: %s | Interval: %s | End: %s", day.getStartTime(), day.getInterval(), day.getEndTime()));
            for (Appointment a : day.getAppointments()) {
                console.result(a);
            }
            console.tabOut();
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
        callables.add(new ActionCall(name + ":log", ":log :backup", MapBuilder.<String, String> from("backup", "true"), "Show current log backup"));
        callables.add(new ActionCall(name + ":today", ":today", "Show what has been done today already"));
    }
}