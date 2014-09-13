package controllers;

import java.util.Map;
import java.util.List;

import main.*;
import models.*;
import parser.Controller;
import parser.Output;

public class InfoController extends Controller<PMAContext> {

  public Output login(Map<String, String> params) {
    Controller.optional(params, "user");
    boolean result = PMAWrapper.login(params.get("user"));
    return new Output(String.valueOf(result));
  }

  public Output list(Map<String, String> params) {
    Controller.optional(Controller.required(params, "type"), "projectNameOrId");
    if ("projects".equals(params.get("type"))) {
      assert params.get("projectNameOrId") == null;
      return listProjects();
    } else if ("tasks".equals(params.get("type"))) {
      return listTasks(params.get("projectNameOrId"));
    } else {
      throw new RuntimeException("Invalid parameters. type parameter is required and must be either 'projects' or 'tasks'.");
    }
  }

  public Output update(Map<String, String> params) {
    Controller.optional(params);
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
}