package controllers;

import java.util.Map;
import java.util.List;

import main.*;
import models.*;
import parser.Controller;
import parser.Output;

public class InfoController extends Controller<PMAContext> {

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
      tasks = context.p().getProject(projectNameOrId).getTasks();
    }

    for (Task t : tasks) {
      output.add(t.toString());
    }
    return output;

  }
}