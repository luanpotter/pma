package main;

import parser.Parser;
import utils.SimpleObjectAccess;

import java.io.*;
import java.util.*;

public class Options implements Serializable {

  private static final String FILE_NAME = "config.dat";

  public enum Option {
    LOG_FILE_NAME("log.dat"), BACKUP_FILE_NAME("log.bkp.dat"), DEFAULT_TASK_ID, DEFAULT_DESCRIPTION;

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

  public Options() {
    this.options = new EnumMap<>();
    for (Option option : Option.values()) {
      this.options.put(option, option.getDefault());
    }
  }

  public String get(Option o) {
    return this.options.get(o);
  }

  public void set(Option o, String value) {
    return this.options.put(o, value);
  }

  public String get(String name, String value) throws InvalidOptionExcpetion {
    return get(Option.toOption(name));
  }

  public void set(String name, String value) throws InvalidOptionExcpetion { 
    set(Option.toOption(name), value);
  }

  public static Options readOrCreate() {
    Options config = SimpleObjectAccess.<Options>readFrom(FILE_NAME);
    if (config != null) {
      return config;
    } else {
      return SimpleObjectAccess.saveTo(FILE_NAME, new Options());
    }
  }
}
