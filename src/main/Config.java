package main;

import parser.Parser;
import models.Project;

import java.io.*;
import java.util.*;

public class Config implements Serializable {

  static {
    Parser.KEYWORD_LIST.add(main.PMAKeyword.class);
  }

  private static final String FILE_NAME = "config.dat";
  private static final Config INSTANCE;
  static {
    File f = new File(FILE_NAME);
    if (f.exists()) {
      Config obj = null;
      try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
        obj = (Config) stream.readObject();
      } catch (IOException | ClassNotFoundException ex) {
        System.err.println("Error found when loading config file! Error: " + ex.getMessage());
        System.exit(1);
      }
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

  private List<Project> projectsCache;
  private Parser parser;
  private String logFileName, backupFileName;
  private int defaultTaskId;
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

  public void update() {
    projectsCache = PMAWrapper.getProjects();
  }

  private void initDefaults() {
    this.logFileName = "log.dat";
    this.backupFileName = "log.bkp.dat";
    this.defaultTaskId = -1;
    this.defaultDescription = null;
    this.parser = Setup.defaultParser();
    this.update();
  }

  public String getLogFileName() {
    return this.logFileName;
  }

  public String getBackupFileName() {
    return backupFileName;
  }

  public int getDefaultTaskId() {
    return defaultTaskId;
  }

  public String getDefaultDescription() {
    return defaultDescription;
  }

  public List<Project> getProjects() {
    return this.projectsCache;
  }
}
