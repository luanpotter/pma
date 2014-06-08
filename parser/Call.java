package parser;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

public class Call implements Serializable {
  private Keyword keyword;
  private Map<String, String> args;

  public Call(Keyword keyword, Map<String, String> args) {
    this.keyword = keyword;
    this.args = args;
  }

  public Keyword getKeyword() {
    return keyword;
  }

  public Map<String, String> getArgs() {
    return args;
  }

  public Call newCall(Map<String, String> newArgs) {
    Map<String, String> args = new HashMap<>(this.args);
    args.putAll(newArgs);

    return new Call(this.keyword, args);
  }
}
