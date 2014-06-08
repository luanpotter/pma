package parser;

import java.util.*;

public class ConfigController {

  public static final String[] MESSAGES = {
    "Alias added succesfully.",
    "Alias already set to something else."
  };
  
  //todo: methods for configuring the actions, tasks, etc

  public Output addAlias(HashMap<String, String> args, Scope scope) {
    final String[] REQUIRED = new String[] { "alias", "connector" };
    Caller.permit(REQUIRED, args);

    Parser parser = scope.getParser();
    boolean results = parser.addAlias(args.get("alias"), args.get("connector"));

    return new Output(MESSAGES[results ? 0 : 1]);
  }
}