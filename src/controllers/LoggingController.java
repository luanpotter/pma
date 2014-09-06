package controllers;

import java.util.Map;

import main.*;
import models.*;
import parser.*;

public class LoggingController extends Controller<PMAContext> {

  public Output here(Map<String, String> params) {
    Controller.optional(params, "taskNameOrId");

    String nameOrId = params.get("taskNameOrId");
    if (nameOrId == null) {
      nameOrId = String.valueOf(Config.c().getDefaultTaskId());
    }

    return null;
  }
}