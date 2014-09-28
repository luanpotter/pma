package br.com.dextra.pma.controllers;

import java.util.List;

import xyz.luan.console.parser.ActionCall;
import xyz.luan.console.parser.Callable;
import xyz.luan.console.parser.Controller;
import xyz.luan.console.parser.Output;
import xyz.luan.console.parser.actions.Action;
import xyz.luan.console.parser.actions.Required;
import br.com.dextra.pma.main.PMAContext;
import br.com.dextra.pma.models.Project;
import br.com.dextra.pma.models.Task;
import br.com.dextra.pma.utils.MapBuilder;

public class InfoController extends Controller<PMAContext> {

    @Action("list")
    public Output list(@Required String type, String projectNameOrId) {
        if ("projects".equals(type)) {
            assert projectNameOrId == null;
            return listProjects();
        } else if ("tasks".equals(type)) {
            return listTasks(projectNameOrId);
        } else {
            throw new RuntimeException("Invalid parameters. type parameter is required and must be either 'projects' or 'tasks'.");
        }
    }

    @Action("update")
    public Output update() {
        context.p().update();
        return new Output("Projects and tasks successfully updated.");
    }

    private Output listProjects() {
        Output output = new Output();
        for (Project p : context.p().getProjects()) {
            output.add(p.toString());
        }
        return output;
    }

    private Output listTasks(String projectNameOrId) {
        Output output = new Output();

        List<Task> tasks;
        if (projectNameOrId == null) {
            tasks = context.p().getTasks();
        } else {
            Project project = context.p().getProject(projectNameOrId);
            if (project == null) {
                return new Output("Invalid project name or id.");
            }
            tasks = project.getTasks();
        }

        for (Task t : tasks) {
            output.add(t.toString());
        }
        return output;
    }

    public static void defaultCallables(String name, List<Callable> callables) {
        callables.add(new ActionCall(name + ":list", ":list :projects", MapBuilder.<String, String> from("type", "projects"), "List all projects"));
        callables.add(new ActionCall(name + ":list", ":list :tasks", MapBuilder.<String, String> from("type", "tasks"), "List all tasks"));
        callables.add(new ActionCall(name + ":list", ":list :tasks :from projectNameOrId", MapBuilder.<String, String> from("type", "tasks"), "List all tasks from projectNameOrId project"));
        callables.add(new ActionCall(name + ":update", ":update", "Update the list of projects and tasks"));
    }
}