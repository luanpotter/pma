package parser.config;

import parser.*;
import java.util.*;

public class ConfigController implements Controller {

  private Parser parser;

  public ConfigController(Parser parser) {
    this.parser = parser;
  }

  public static final String[] MESSAGES = {
    "Alias added successfully.",
    "Alias already set to something else."
  };
  
  public Output addAlias(Map<String, String> args) {
    Controller.required(args, "alias", "keyword");

    boolean results = parser.addAlias(args.get("alias"), args.get("keyword"));

    return new Output(MESSAGES[results ? 0 : 1]);
  }

  public Output listAliases(Map<String, String> args) {
    Controller.empty(args);
    return parser.listAliasesFor(args.get("keyword") == null ? null : ':' + args.get("keyword"));
  }

  public Output listKeywords(Map<String, String> args) {
    Controller.empty(args);
    return parser.listKeywords();
  }

  public static List<Callable> getDefaultActions() {
    List<Callable> callables = new ArrayList<>(1);

    callables.add(new Action(ConfigKeyword.LIST_KEYWORDS, new Pattern(":config :keywords"), "List all keywords"));
    callables.add(new Action(ConfigKeyword.ADD_ALIAS, new Pattern(":config :aliases :add alias :to keyword"), "Add an alias to keyword"));
    callables.add(new Action(ConfigKeyword.LIST_ALIASES, new Pattern(":config :aliases :list"), "List all aliases"));
    callables.add(new Action(ConfigKeyword.LIST_ALIASES, new Pattern(":config :aliases :list keyword"), "List all aliases for this keyword"));

    return callables;
  }
}