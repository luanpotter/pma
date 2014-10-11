package br.com.dextra.pma.controllers;

import java.util.List;

import xyz.luan.console.fn.FnController;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.actions.ActionRef;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import xyz.luan.console.parser.callable.Pattern;
import br.com.dextra.pma.models.Task;

@FnController
public class AliasesController extends BaseController {

    @Action("list")
    public CallResult list() {
        console.result("-- Alias : taskId --");
        context.a().forEach((String alias, Long taskId) -> {
            console.result("- " + alias + " : " + taskId);
        });
        return CallResult.SUCCESS;
    }

    @Action("get")
    public CallResult get(String alias) {
        Long task = context.a().getTaskByAlias(alias);
        if (task == null) {
            console.error("Alias " + alias + " not associated with any task.");
            return CallResult.ERROR;
        }
        console.message("Alias " + alias + " maps to task id " + task + ".");
        return CallResult.SUCCESS;
    }

    @Action("set")
    public CallResult set(String alias, String taskNameOrId) {
        Task task = context.p().getTaskWithoutAlias(taskNameOrId);
        if (task == null) {
            console.error("Specified taskName " + taskNameOrId + " is not valid.");
            return CallResult.ERROR;
        }

        context.a().addAlias(alias, task.getId());
        console.message("Alias " + alias + " was successfully added to task id " + task.getId());
        return CallResult.SUCCESS;
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
