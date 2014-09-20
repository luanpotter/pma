package br.com.dextra.pma.controllers;

import java.util.Map;

import br.com.dextra.pma.main.PMAContext;
import br.com.dextra.pma.models.Task;
import br.com.dextra.pma.parser.Controller;
import br.com.dextra.pma.parser.Output;

public class AliasesController extends Controller<PMAContext> {

    public Output list(Map<String, String> params) {
        Controller.empty(params);

        final Output output = new Output("-- Alias : taskId --");
        context.a().forEach((String alias, Long taskId) -> {
            output.add("- " + alias + " : " + taskId);
        });
        return output;
    }

    public Output get(Map<String, String> params) {
        Controller.required(params, "alias");
        String alias = params.get("alias");
        Long task = context.a().getTaskByAlias(alias);
        if (task == null) {
            return new Output("Alias " + alias + " not associated with any task.");
        }
        return new Output("Alias " + alias + " maps to task id " + task + ".");
    }

    public Output set(Map<String, String> params) {
        Controller.required(params, "alias", "taskNameOrId");

        String nameOrId = params.get("taskNameOrId");
        Task task = context.p().getTaskWithoutAlias(nameOrId);
        if (task == null) {
            return new Output("Specified taskName " + nameOrId + " is not valid.");
        }

        String alias = params.get("alias");
        context.a().addAlias(alias, task.getId());
        return new Output("Alias " + alias + " was successfully added to task id " + task.getId());
    }

}
