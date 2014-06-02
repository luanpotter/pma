public class Config {

  public static class Words {
    public Map<String, Keyword> aliases;
    public Map<String, String> conectors;
  }

  private static final String FILE_NAME = "config.dat";
  private static final Config INSTANCE;
  static {
    File f = new File(FILE_NAME);
    try {
      if (f.exists()) {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
          INSTANCE = stream.readObject();
        }        
      } else {
        INSTANCE = new Config();
        INSTANCE.init();
        INSTANCE.saveConfig();
      }
    } catch (IOException ex) {
      System.err.println("Error found when loading config file! Error: " + ex.getMessage());
      System.exit(1);
    }
  }

  private Words words;
  private List<Action> actions;
  private Map<String, Task> tasks;
  private String logFileName, backupFileName;

  private Config() { }

  public static void saveData() throws IOException {
    try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
      stream.writeObject(INSTANCE);
    }
  }

  public static Call[] parse(String[] args) {
    return INSTANCE.parse(args);
  }

  private Call[] parse(String[] args) {
    if (args.length < 1) {
      return null;
    }

    for (Action a : action) {
      Map<String, String> args = a.parse(args, words);
      if (args != null) {
        return { new Call(a.getKeyword(), args) }; //todo review
      }
    }

    //todo review
    if (args.length == 1) {
      return new Task(aliases.get(args[0]));
    } else {
      return null;
    }
  }

  private void init() {
    this.words = new Words();
    this.defaultFileNames();

    this.defaultAliases();
    this.defaultConectors();

    this.defaultActions();
    this.defaultTasks();
  }

  private void defaultFileNames() {
    logFileName = "log.dat";
    backupFileName = "log.bkp.dat";
  }

  private void defaultTasks() {
    tasks = new HashMap<>();
  }

  private void defaultActions() {
    actions = new ArrayList<>();
    actions.add(new Action(Keyword.HERE));
    actions.add(new Action(Keyword.EXIT));
    actions.add(new Action(Keyword.EXIT, ":to taskNameOrId"));
    actions.add(new Action(Keyword.SAVE));
    actions.add(new Action(Keyword.LOG));
    actions.add(new Action(Keyword.LOG, ":backup"));
    actions.add(new Action(Keyword.TODAY));
    actions.add(new Action(Keyword.LIST));
    actions.add(new Action(Keyword.LIST, ":projects"));
    actions.add(new Action(Keyword.LIST, ":tasks"));
    actions.add(new Action(Keyword.LIST, ":tasks :from projectId"));
  }

  private void defaultConectors() {
    words.conectors = new HashMap<>();
    words.conectors.put("to", ":to");
    words.conectors.put("para", ":to");

    words.conectors.put("from", ":from");
    words.conectors.put("de", ":from");

    words.conectors.put("backup", ":backup");

    words.conectors.put("projects", ":projects");
    words.conectors.put("projetos", ":projects");
    
    words.conectors.put("tasks", ":tasks");
    words.conectors.put("tarefas", ":tasks");
  }

  private void defaultAliases() {
    words.aliases = new HashMap<>();
    words.aliases.put("here", Keyword.HERE);
    words.aliases.put("cheguei", Keyword.HERE);

    words.aliases.put("exit", Keyword.EXIT);
    words.aliases.put("sai", Keyword.EXIT);

    words.aliases.put("save", Keyword.SAVE);
    words.aliases.put("salvar", Keyword.SAVE);

    words.aliases.put("log", Keyword.LOG);

    words.aliases.put("today", Keyword.TODAY);
    words.aliases.put("hoje", Keyword.TODAY);

    words.aliases.put("list", Keyword.LIST);
    words.aliases.put("listar", Keyword.LIST);
  }
}
