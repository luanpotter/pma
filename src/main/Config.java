package main;

import parser.*;
import parser.config.*;
import java.io.*;
import java.util.*;

public class Config implements Serializable {

  static { setupCallerConstants(); }
  private static void setupCallerConstants() {
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
      INSTANCE.init();
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

  private Config() { }

  public static void saveData() {
    try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
      stream.writeObject(INSTANCE);
    } catch (IOException ex) {
      System.err.println("Error found when saving new default config file! You can still use the program, but configs won't be saved. Error: " + ex.getMessage());
    }
  }

  public static Caller setupCaller() {
    Caller caller = new Caller();
    caller.registerClass("config", new ConfigController(INSTANCE.parser));
    caller.registerClass("help", new HelpController(INSTANCE.parser));

    return caller;
  }

  public static Call[] parse(String[] args) {
    return INSTANCE.parser.parse(args);
  }

  private void init() {
    this.defaultFileNames();
    this.defaultParser();
  }

  private void defaultFileNames() {
    logFileName = "log.dat";
    backupFileName = "log.bkp.dat";
  }

  private void defaultParser() {
    this.parser = new Parser(this.defaultAliases(), this.defaultCallables());
  }

  private ArrayList<Callable> defaultCallables() {
    ArrayList<Callable> callables = new ArrayList<>();
    callables.add(new Action(PMAKeyword.HERE, new Pattern(":here"), "Start counting on default task"));
    callables.add(new Action(PMAKeyword.HERE, new Pattern(":here taskNameOrId"), "Start counting on taskNameOrId task"));
    callables.add(new Action(PMAKeyword.EXIT, new Pattern(":exit"), "Exit to break"));
    callables.add(new Action(PMAKeyword.EXIT, new Pattern(":exit :to taskNameOrId"), "Exit current task and start counting on taskNameOrId task"));
    callables.add(new Action(PMAKeyword.SAVE, new Pattern(":save"), "Save current day on web service"));
    callables.add(new Action(PMAKeyword.LOG, new Pattern(":log"), "Show current log"));
    callables.add(new Action(PMAKeyword.LOG, new Pattern(":log :backup"), "Show current log backup"));
    callables.add(new Action(PMAKeyword.TODAY, new Pattern(":today"), "Show what has been done today already"));
    callables.add(new Action(PMAKeyword.LIST, new Pattern(":list :projects"), "List all projects"));
    callables.add(new Action(PMAKeyword.LIST, new Pattern(":list :tasks"), "List all tasks"));
    callables.add(new Action(PMAKeyword.LIST, new Pattern(":list :tasks :from projectId"), "List all tasks from projectId project"));

    callables.addAll(ConfigController.getDefaultActions());
    callables.addAll(HelpController.getDefaultActions());

    return callables;
  }

  private Map<String, String> defaultAliases() {
    Map<String, String> aliases = new HashMap<>();
    aliases.put("here", ":here");
    aliases.put("cheguei", ":cheguei");

    aliases.put("exit", ":exit");
    aliases.put("sai", ":exit");

    aliases.put("save", ":save");
    aliases.put("salvar", ":save");

    aliases.put("log", ":log");

    aliases.put("today", ":today");
    aliases.put("today", ":today");

    aliases.put("list", ":list");
    aliases.put("listar", ":list");

    aliases.put("for", ":for");
    aliases.put("em", ":for");

    aliases.put("from", ":from");
    aliases.put("de", ":from");

    aliases.put("to", ":to");
    aliases.put("para", ":to");

    aliases.put("backup", ":backup");

    aliases.put("projects", ":projects");
    aliases.put("projetos", ":projects");
    
    aliases.put("tasks", ":tasks");
    aliases.put("tarefas", ":tasks");

    aliases.put("help", ":help");
    aliases.put("ajuda", ":help");

    aliases.put("config", ":config");
    aliases.put("configurar", ":config");

    aliases.put("keywords", ":keywords");
    aliases.put("palavras-chave", ":keywords");

    aliases.put("aliases", ":aliases");
    aliases.put("nomes", ":aliases");

    aliases.put("add", ":add");
    aliases.put("adicionar", ":add");

    return aliases;
  }
}
