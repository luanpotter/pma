package parser;

import java.util.*;

public class Task implements Callable {

  private String description;
  private Pattern pattern;
  private List<Action> actions;

  public Task(Pattern pattern, List<Action> actions, String description) {
    this.pattern = pattern;
    this.actions = actions;
    this.description = description;
  }

  @Override
  public Call[] parse(String[] args, Map<String, String> aliases) {
    Map<String, String> map = pattern.parse(args, aliases);
    if (map == null) {
      return null;
    }

    Call[] calls = new Call[actions.size()];
    for (int i = 0; i < actions.size(); i++) {
      calls[i] = actions.get(i).parseAction(args, map);
    }

    return calls;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public Pattern getPattern() {
    return this.pattern;
  }
}