package parser.config;

import parser.*;
import java.util.*;

public class ConfigController implements Controller {

  private Parser parser;

  public ConfigController(Parser parser) {
    this.parser = parser;
  }

  public static final String[] MESSAGES = {
    "Alias added succesfully.",
    "Alias already set to something else."
  };
  
  //todo: methods for configuring the actions, tasks, etc

  public Output aliases(Map<String, String> args) {
    final String[] REQUIRED = new String[] { "alias", "connector" };
    Caller.permit(REQUIRED, args);

    boolean results = parser.addAlias(args.get("alias"), args.get("connector"));

    return new Output(MESSAGES[results ? 0 : 1]);
  }

  public static List<Callable> getDefaultActions() {
    List<Callable> callables = new ArrayList<>(1);

    callables.add(new Action(ConfigKeyword.ALIASES, ":config :aliases :add alias :to connector"));

    return callables;
  }
}