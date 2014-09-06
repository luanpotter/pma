package main;

import parser.Parser;
import utils.SimpleObjectAccess;

import java.io.*;
import java.util.*;

public class Config implements Serializable {

  private static final String FILE_NAME = "config.dat";
  private static final Config INSTANCE;
  static {
    Config obj = SimpleObjectAccess.<Config>readFrom(FILE_NAME);
    if (obj != null) {
      INSTANCE = obj;
    } else {
      INSTANCE = new Config();
      INSTANCE.initDefaults();
      saveData();
    }
  }

  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    if (Config.INSTANCE == null) {
      ois.defaultReadObject();
    } else {
      throw new IOException("Singleton class can only be instanciated once!");
    }
  }

  private Parser parser;
  private String logFileName, backupFileName;
  private long defaultTaskId;
  private String defaultDescription;

  private Config() { }

  public static void saveData() {
    try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
      stream.writeObject(INSTANCE);
    } catch (IOException ex) {
      System.err.println("Error found when saving new default config file! You can still use the program, but configs won't be saved. Error: " + ex.getMessage());
    }
  }

  public static Config c() {
    return INSTANCE;
  }

  public static PMAContext createContext() {
    return Setup.setupContext(INSTANCE.parser);
  }

  private void initDefaults() {
    this.logFileName = "log.dat";
    this.backupFileName = "log.bkp.dat";
    this.defaultTaskId = -1l;
    this.defaultDescription = null;
    this.parser = Setup.defaultParser();
  }

  public String getLogFileName() {
    return this.logFileName;
  }

  public String getBackupFileName() {
    return backupFileName;
  }

  public long getDefaultTaskId() {
    return defaultTaskId;
  }

  public String getDefaultDescription() {
    return defaultDescription;
  }
}
