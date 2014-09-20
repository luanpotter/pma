package controllers;

import java.util.Map;
import java.io.*;

import main.Options.Option;
import main.*;
import date.*;
import models.*;
import parser.Controller;
import parser.Output;

public class LoggingController extends Controller<PMAContext> {

  public Output here(Map<String, String> params) {
    Controller.optional(params, "taskNameOrId");

    String nameOrId = params.get("taskNameOrId");
    if (nameOrId == null) {
      nameOrId = String.valueOf(context.o().get(Option.DEFAULT_TASK));
    }

    Task task = context.p().getTask(context.a(), nameOrId);
    if (task == null) {
      if (params.get("taskNameOrId") == null) {
        return new Output("No name or id is specified, and default task is invalid: '" + nameOrId + "'. To change the default task, run options set default-task taskNameOrId");
      }
      return new Output("Specified name or id is invalid.");
    }

    log(new Moment(), task.getId());
    return new Output("Logged successfully.");
  }

  public Output exit(Map<String, String> params) {
    Controller.empty(params);
    log(new Moment(), -1);
    return new Output("Logged successfully.");
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
}