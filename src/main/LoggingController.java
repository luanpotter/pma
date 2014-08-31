package main;

import java.util.Map;

import parser.*;

public class LoggingController extends Controller<PMAContext> {

  public Output here(Map<String, String> params) {
    Controller.optional(params, "taskNameOrId");

    int taskId;
    String nameOrId = params.get("taskNameOrId");
    if (nameOrId == null) {
      taskId = Config.c().getDefaultTaskId();
    } else {
      try {
        taskId = Integer.parseInt(nameOrId);
      } catch (NumberFormatException ex) {
        throw new RuntimeException("Not implemented yet!");
        //taskId = idFromName(nameOrId); // < TODO
      }
    }

    return null;
  }
}