package parser;

import java.util.*;

public class Task implements Callable {

  private String pattern;
  private List<Action> actions;

  public Task(String pattern, List<Action> actions) {
    this.pattern = pattern;
    this.actions = actions;
  }

  public Call[] parse(String[] args, Map<String, String> aliases) {
    Call[] calls = new Call[actions.size()];

    for (int i = 0; i < actions.size(); i++) {
      calls[i] = actions.get(i).parseAction(args, aliases);
    }

    return calls;
  }

}