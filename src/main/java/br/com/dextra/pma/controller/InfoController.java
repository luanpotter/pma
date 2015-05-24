package br.com.dextra.pma.controller;

import java.util.List;
import java.util.Map;

import xyz.luan.console.fn.FnController;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.actions.ActionRef;
import xyz.luan.console.parser.actions.Optional;
import xyz.luan.console.parser.call.CallResult;
import xyz.luan.console.parser.callable.ActionCall;
import xyz.luan.console.parser.callable.Callable;
import xyz.luan.console.parser.callable.Pattern;
import br.com.dextra.pma.model.Project;
import br.com.dextra.pma.model.Task;
import br.com.dextra.pma.utils.MapBuilder;

@FnController
public class InfoController extends BaseController {

    @Action("list")
    public CallResult list(String type, @Optional String projectNameOrId) {
        if ("projects".equals(type)) {
            assert projectNameOrId == null;
            return listProjects();
        } else if ("tasks".equals(type)) {
            return listTasks(projectNameOrId);
        } else {
            throw new RuntimeException("Invalid parameters. type parameter is required and must be either 'projects' or 'tasks'.");
        }
    }

    private CallResult listProjects() {
        for (Project p : context.p().getProjects()) {
            console.result(p.toString());
        }
        return CallResult.SUCCESS;
    }

    private CallResult listTasks(String projectNameOrId) {
        List<Task> tasks;
        if (projectNameOrId == null) {
            tasks = context.p().getTasks();
        } else {
            Project project = context.p().getProject(projectNameOrId);
            if (project == null) {
                console.error("Invalid project name or id.");
                return CallResult.ERROR;
            }
            tasks = project.getTasks();
        }

        for (Task t : tasks) {
            console.result(t.toString());
        }
        return CallResult.SUCCESS;
    }

    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":list", ":list :projects", MapBuilder.<String, String> from("type", "projects"), "List all projects"));
        callables.add(new ActionCall(name + ":list", ":list :tasks", MapBuilder.<String, String> from("type", "tasks"), "List all tasks"));

        {
            Map<String, String> defaultArgs = MapBuilder.<String, String> from("type", "tasks");
            Pattern pattern = new Pattern(":list :tasks :from projectNameOrId", true);
            String description = "List all tasks from projectNameOrId project";
            callables.add(new ActionCall(new ActionRef(name, "list"), pattern, defaultArgs, description));
        }
    }
}