package parser;

import java.io.Serializable;
import java.util.*;

public class Parser implements Serializable {
  
  private Map<String, String> aliases;
  private List<Callable> callables;

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
}