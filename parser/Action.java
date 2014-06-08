package parser;

import java.util.Map;
import java.util.HashMap;

public class Action implements Callable {

  private String pattern;
  private Call predefCall;
  private Map<String, String> argsMapping;

  public Action(Keyword keyword, String pattern) {
    this(keyword, pattern, new HashMap<String, String>());
  }

  public Action(Keyword keyword, String pattern, Map<String, String> argsValues) {
    this(keyword, pattern, argsValues, null);
  }

  public Action(Keyword keyword, String pattern, Map<String, String> argsValues, Map<String, String> argsMapping) {
    this.pattern = pattern;
    this.predefCall = new Call(keyword, argsValues);
    this.argsMapping = argsMapping;
  }

  public Call parseAction(String[] args, Map<String, String> aliases) {
    String[] sections = pattern.split(" ");
    if (sections.length != args.length) {
      return null; //no match
    }

    Map<String, String> map = new HashMap<>();
    for (int i = 0; i < sections.length; i++) {
      String section = sections[i];
      if (section.charAt(0) == ':') {
        if (!aliases.get(args[i]).equals(section)) {
          return null; //no match
        }
      } else {
        if (this.argsMapping == null) {
          map.put(section, args[i]);
        } else {
          String realArgName = this.argsMapping.get(section);
          if (realArgName == null) {
            return null; //no match
          } else {
            map.put(realArgName, args[i]);
          }
        }
      }
    }

    return predefCall.newCall(map);
  }

  public Call[] parse(String[] args, Map<String, String> aliases) {
    Call call = parseAction(args, aliases);
    if (call == null) {
      return null;
    } else {
      return new Call[] { call };
    }
  }
}