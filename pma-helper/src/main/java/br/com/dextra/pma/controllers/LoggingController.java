package br.com.dextra.pma.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import xyz.luan.console.parser.Controller;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.actions.ActionRef;
import xyz.luan.console.parser.actions.Optional;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import xyz.luan.console.parser.callable.Pattern;
import br.com.dextra.pma.date.Moment;
import br.com.dextra.pma.main.Options.Option;
import br.com.dextra.pma.main.PmaContext;
import br.com.dextra.pma.models.Task;

public class LoggingController extends Controller<PmaContext> {

    private static final String ERROR_MESSAGE = "No name or id is specified, and default task is invalid: '%s'. To change the default task, run options set default-task taskNameOrId";

    @Action("here")
    public CallResult here(@Optional String taskNameOrId) {
        if (taskNameOrId == null) {
            taskNameOrId = context.o().get(Option.DEFAULT_TASK);
        }

        Task task = context.p().getTask(context.a(), taskNameOrId);
        if (task == null) {
            if (taskNameOrId == null) {
                console.error(String.format(ERROR_MESSAGE, taskNameOrId));
                return CallResult.ERROR;
            }
            console.error("Specified name or id is invalid.");
            return CallResult.ERROR;
        }

        log(new Moment(), task.getId());
        console.result("Logged successfully.");
        return CallResult.SUCCESS;
    }

    @Action("exit")
    public CallResult exit() {
        log(new Moment(), -1);
        console.result("Logged successfully.");
        return CallResult.SUCCESS;
    }

    private void log(Moment m, long taskId) {
        log(context.o().get(Option.LOG_FILE), m, taskId);

        String backup = context.o().get(Option.BACKUP_FILE);
        if (backup != null) {
            log(backup, m, taskId);
        }
    }

    private void log(String fileName, Moment m, long taskId) {
        try (PrintWriter p = new PrintWriter(new FileWriter(new File(fileName), true))) {
            String log = m.toString() + (taskId != -1 ? "+" + taskId : "");
            p.println(log);
        } catch (IOException ex) {
            throw new RuntimeException("Could not log to file!", ex);
        }
    }

    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":here", ":here", "Start counting on default task"));
        callables.add(new ActionCall(new ActionRef(name + ":here"), new Pattern(":here taskNameOrId", true),
                "Start counting on taskNameOrId task"));
        callables.add(new ActionCall(name + ":exit", ":exit", "Exit to break"));
    }
}