package br.com.dextra.pma.controllers;

import java.util.List;

import xyz.luan.console.parser.ActionCall;
import xyz.luan.console.parser.ActionRef;
import xyz.luan.console.parser.Callable;
import xyz.luan.console.parser.Controller;
import xyz.luan.console.parser.Output;
import xyz.luan.console.parser.Pattern;
import xyz.luan.console.parser.actions.Action;
import br.com.dextra.pma.main.PMAContext;
import br.com.dextra.pma.models.Task;

public class AliasesController extends Controller<PMAContext> {

    @Action("list")
    public Output list() {
        final Output output = new Output("-- Alias : taskId --");
        context.a().forEach((String alias, Long taskId) -> {
            output.add("- " + alias + " : " + taskId);
        });
        return output;
    }

    @Action("get")
    public Output get(String alias) {
        Long task = context.a().getTaskByAlias(alias);
        if (task == null) {
            return new Output("Alias " + alias + " not associated with any task.");
        }
        return new Output("Alias " + alias + " maps to task id " + task + ".");
    }

    @Action("set")
    public Output set(String alias, String taskNameOrId) {
        Task task = context.p().getTaskWithoutAlias(taskNameOrId);
        if (task == null) {
            return new Output("Specified taskName " + taskNameOrId + " is not valid.");
        }

        context.a().addAlias(alias, task.getId());
        return new Output("Alias " + alias + " was successfully added to task id " + task.getId());
    }
    
    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":list", ":aliases", "List all aliases with their values"));
        callables.add(new ActionCall(name + ":get", ":aliases :get alias", "Return the current value of alias"));
        callables.add(new ActionCall(
            new ActionRef(name, "set"),
            new Pattern(":aliases :set alias taskNameOrId", true),
            "Set the value of alias to taskNameOrId")
        );
    }
}
