package parser;

import java.io.Serializable;
import java.util.*;

public class Parser implements Serializable {
  
  private Map<String, String> aliases;
  private List<Callable> callables;

  public static final List<Class<? extends Enum>> KEYWORD_LIST = new ArrayList<>();
  static {
    KEYWORD_LIST.add(parser.config.HelpKeyword.class);
    KEYWORD_LIST.add(parser.config.ConfigKeyword.class);
  }

  public Parser() {
    this(new HashMap<String, String>(), new ArrayList<Callable>());
  }

  public Parser(Map<String, String> aliases, List<Callable> callables) {
    this.aliases = aliases;
    this.callables = callables;
  }

  public Call[] parse(String[] args) {
    if (args.length < 1) {
      return null;
    }

    for (Callable c : callables) {
      Call[] calls = c.parse(args, aliases);
      if (calls != null) {
        return calls;
      }
    }

    return null; //no match
  }

  public void addCallable(Callable c) {
    this.callables.add(c);
  }

  public boolean removeCallable(int index) {
    if (index >= 0 && index < this.callables.size()) {
      this.callables.remove(index);
      return true;
    } else {
      return false;
    }
  }

  public Output listKeywords() {
    Output out = new Output();
    for (Class<? extends Enum> keywordList : KEYWORD_LIST) {
      out.add(keywordList.getSimpleName());
      out.tabIn();
      for (Object keyword : keywordList.getEnumConstants()) {
        out.add(keyword.toString());
      }
      out.tabOut();
    }
    return out;
  }

  public Output listCallables() {
    return this.listCallables("");
  }

  public Output listCallables(String start) {
    Output out = new Output();

    int actualStart = start.lastIndexOf(":");
    if (actualStart < 0) {
      actualStart = 0;
    }

    out.add("Listing all callables" + (start.isEmpty() ? "" : " starting with '" + start + "'") + ":");
    for (Callable c : callables) {
      String pattern = c.getPattern().toString();
      if (pattern.startsWith(start)) {
        out.add(pattern.substring(actualStart) + " - " + c.getDescription());
      }
    }

    return out;
  }

  public boolean addAlias(String alias, String keyword) {
    if (this.aliases.get(alias) != null) {
      return false;
    }
    this.aliases.put(alias, keyword);
    return true;
  }

  public boolean deleteAlias(String alias) {
    if (this.aliases.get(alias) == null) {
      return false;
    }
    this.aliases.remove(alias);
    return true;
  }

  public String getAlias(String alias) {
    return this.aliases.get(alias);
  }

  public Output listAliases() {
    return listAliasesFor(null);
  }

  public Output listAliasesFor(String keyword) {
    Output output = new Output();

    for (Map.Entry<String, String> entry : aliases.entrySet()) {
      if (keyword == null) {
        output.add(entry.getKey() + ": " + entry.getValue().substring(1));
      } else {
        if (keyword.equals(entry.getValue())) {
          output.add(entry.getKey());
        }
      }
    }

    return output;
  }
}