package main;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.function.BiConsumer;

import utils.SimpleObjectAccess;

public class Aliases implements Serializable {

  private static final String FILE_NAME = "aliases.dat";

  private Map<String, Long> aliases;

  public static Aliases readOrCreate() {
    Aliases aliases = SimpleObjectAccess.<Aliases>readFrom(FILE_NAME);
    if (aliases != null) {
      return aliases;
    } else {
      return new Aliases();
    }
  }

  public Aliases() {
    this.aliases = new HashMap<>();
  }

  public void addAlias(String alias, Long task) {
    this.aliases.put(alias, task);
  }

  public Long getTaskByAlias(String alias) {
    return this.aliases.get(alias);
  }

  public void forEach(BiConsumer<String, Long> c) {
    this.aliases.forEach(c);
  }
}