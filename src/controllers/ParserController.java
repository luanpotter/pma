package controllers;

import java.util.Map;
import java.util.List;

import main.*;
import models.*;
import parser.Controller;
import parser.Output;

public class ParserController extends Controller<PMAContext> {

  public Output save(Map<String, String> params) {
    Controller.optional(params, "backup");
    boolean bkp = "true".equals(params.get("backup"));

    if (bkp) {

    } else {

    }

    return new Output("TODO WIP");
  }

  public Output log(Map<String, String> params) {
    return new Output("TODO WIP");
  }

  public Output today(Map<String, String> params) {
    return new Output("TODO WIP");
  }
}