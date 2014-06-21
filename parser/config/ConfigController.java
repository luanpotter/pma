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
  
  public Output addAlias(Map<String, String> args) {
    final String[] REQUIRED = new String[] { "alias", "connector" };
    Caller.permit(REQUIRED, args);

    boolean results = parser.addAlias(args.get("alias"), args.get("connector"));

    return new Output(MESSAGES[results ? 0 : 1]);
  }

  public Output listAliases(Map<String, String> args) {
    return parser.listAliasesFor(args.get("keyword") == null ? null : ':' + args.get("keyword"));
  }

  public static List<Callable> getDefaultActions() {
    List<Callable> callables = new ArrayList<>(1);

    callables.add(new Action(ConfigKeyword.ADD_ALIAS, ":config :aliases :add alias :to connector"));
    callables.add(new Action(ConfigKeyword.LIST_ALIASES, ":config :aliases :list"));
    callables.add(new Action(ConfigKeyword.LIST_ALIASES, ":config :aliases :list keyword"));

    return callables;
  }
}