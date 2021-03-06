package br.com.dextra.pma.controller;

import java.util.Calendar;
import java.util.List;

import xyz.luan.console.fn.FnController;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.actions.Optional;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.exception.NotLoggedIn;
import br.com.dextra.pma.main.Options.Option;
import br.com.dextra.pma.model.Day;
import br.com.dextra.pma.model.Record;
import br.com.dextra.pma.parser.FileParser;
import br.com.dextra.pma.parser.InvalidFormatException;
import br.com.dextra.pma.service.PmaService;
import br.com.dextra.pma.utils.MapBuilder;
import br.com.dextra.pma.utils.SimpleObjectAccess;

@FnController
public class ParserController extends BaseController {

    @Action("save")
    public CallResult save() throws InvalidFormatException {
        assertLoggedIn();
        String fileName = context.o().get(Option.LOG_FILE);

        List<Day> days = FileParser.parseLogs(fileName, true);
        if (days.isEmpty()) {
            console.message("Nothing to be saved.");
            return CallResult.SUCCESS;
        }

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
        if (!PmaService.isLoggedIn()) {
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
            day.print(console);
        }
        return CallResult.SUCCESS;
    }

    @Action("today")
    public CallResult today() throws InvalidFormatException {
        Day day = FileParser.parseDay(context.o().get(Option.LOG_FILE), new Date(Calendar.getInstance()));
        if (day == null) {
            console.error("Today was not logged in the file yet.");
            return CallResult.ERROR;
        }
        day.print(console);
        return CallResult.SUCCESS;
    }

    @Action("current-task")
    public CallResult currentTask() throws InvalidFormatException {
        Day day = FileParser.parseDay(context.o().get(Option.LOG_FILE), new Date(Calendar.getInstance()));
        if (day == null) {
            console.error("Today was not logged in the file yet.");
            return CallResult.ERROR;
        }
	Record lastRecord = day.lastRecord();
	if (lastRecord == null) {
            console.error("Today was not logged in the file yet.");
            return CallResult.ERROR;
	}
	console.result(lastRecord.getTask());
        return CallResult.SUCCESS;
    }

    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":save", ":save", "Save current day on web service"));
        callables.add(new ActionCall(name + ":log", ":log", "Show current log"));
        callables.add(new ActionCall(name + ":log", ":log :backup", MapBuilder.<String, String> from("backup", "true"), "Show current log backup"));
        callables.add(new ActionCall(name + ":today", ":today", "Show what has been done today already"));
        callables.add(new ActionCall(name + ":current-task", ":current-task", "Show the last task id"));
    }
}
