package controllers;

import java.util.Map;
import java.util.List;

import main.*;
import models.*;
import parser.Controller;
import parser.Output;
import main.Options.Option;
import main.PMAParser.InvalidFormatException;

public class ParserController extends Controller<PMAContext> {

  public Output save(Map<String, String> params) throws InvalidFormatException {
    Controller.empty(params);

    Output res = new Output();
    String fileName = context.o().get(Option.LOG_FILE);
    List<Day> days = PMAParser.parseLogs(fileName, false);
    for (Day day : days) {
      res.append(day.save());
    }

    return res;
  }

  public Output log(Map<String, String> params) throws InvalidFormatException {
    Controller.optional(params, "backup");
    boolean bkp = "true".equals(params.get("backup"));

    String fileName = context.o().get(bkp ? Option.BACKUP_FILE : Option.LOG_FILE);
    List<Day> days = PMAParser.parseLogs(fileName, true);

    Output out = new Output(days.size() + " days parsed;");
    for (Day day : days) {
      out.add(day.toString()); //TODO output properly
    }
    return out;
  }

  public Output today(Map<String, String> params) {
    Controller.empty(params);
    return new Output("TODO WIP");
  }
}