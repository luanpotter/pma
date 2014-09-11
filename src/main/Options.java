package main;

import parser.Parser;
import utils.SimpleObjectAccess;

import java.util.function.BiConsumer;
import java.io.*;
import java.util.*;

public class Options implements Serializable {

  private static final String FILE_NAME = "options.dat";

  public static class InvalidOptionExcpetion extends Exception { }

  public enum Option {
    LOG_FILE("log.dat"), BACKUP_FILE("log.bkp.dat"), DEFAULT_TASK, DEFAULT_DESCRIPTION;

    private String defaultValue;

    private Option() {
      this(null);
    }

    private Option(String defaultValue) {
      this.defaultValue = defaultValue;
    }

    public String getDefault() {
      return this.defaultValue;
    }

    @Override
    public String toString() {
      return this.name().replace("_", "-").toLowerCase();
    }

    public static Option toOption(String s) throws InvalidOptionExcpetion {
      for (Option option : Option.values()) {
        if (option.toString().equals(s)) {
          return option;
        }
      }
      throw new InvalidOptionExcpetion();
    }
  }

  private Map<Option, String> options;

  public Options defaults() {
    this.options = new EnumMap<>(Option.class);
    for (Option option : Option.values()) {
      this.options.put(option, option.getDefault());
    }
    return this;
  }

  public String get(Option o) {
    return this.options.get(o);
  }

  public void set(Option o, String value) {
    this.options.put(o, value);
  }

  public String get(String name) throws InvalidOptionExcpetion {
    return get(Option.toOption(name));
  }

  public void set(String name, String value) throws InvalidOptionExcpetion { 
    set(Option.toOption(name), value);
  }

  public void list(BiConsumer<Option, String> consumer) {
    for (Option key : this.options.keySet()) {
      consumer.accept(key, this.options.get(key));
    }
  }

  public void save() {
    SimpleObjectAccess.saveTo(FILE_NAME, this);
  }

  public static Options readOrCreate() {
    Options config = SimpleObjectAccess.<Options>readFrom(FILE_NAME);
    if (config != null) {
      return config;
    } else {
      return SimpleObjectAccess.saveTo(FILE_NAME, new Options().defaults());
    }
  }
}
