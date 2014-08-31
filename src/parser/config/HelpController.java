package parser.config;

import parser.*;
import java.util.*;

public class HelpController implements Controller {

  private Parser parser;

  public HelpController(Parser parser) {
    this.parser = parser;
  }

  public Output list(Map<String, String> args) {
    Controller.empty(args);
    return parser.listCallables();
  }

  public Output show(Map<String, String> args) {
    Controller.required(args, "command");
    return parser.listCallables(args.get("command"));
  }

  public static List<Callable> getDefaultActions() {
    List<Callable> callables = new ArrayList<>(1);

    callables.add(new Action(HelpKeyword.LIST, new Pattern(":help"), "List all callables"));
    callables.add(new Action(HelpKeyword.SHOW, new Pattern(":help command", true), "List all callables starting with command"));

    return callables;
  }
}