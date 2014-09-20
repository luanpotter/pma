package main;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import controllers.*;
import parser.*;
import parser.config.*;
import utils.MapBuilder;
import utils.SimpleObjectAccess;
import main.PMAParser.InvalidFormatException;

public final class Setup {
  
  static {
    Parser.KEYWORD_LIST.add(main.PMAKeyword.class);
    ExceptionHandler.HANDLERS.put(NotLoggedIn.class, e -> new Output(e.getMessage()));
    ExceptionHandler.HANDLERS.put(InvalidFormatException.class, e -> new Output(e.getMessage()));
  }

  private Setup() {
    throw new RuntimeException("Should not be instanciated.");
  }

  public static PMAContext setupContext() {
    PMAContext context = new PMAContext();
    Caller caller = defaultCaller(context);
    Parser parser = getParser();
    context.setup(parser, caller);
    return context;
  }

  private static final String PARSER_FILE_NAME = "parser.dat";

  public static void saveParser(Parser parser) {
    SimpleObjectAccess.saveTo(PARSER_FILE_NAME, parser);
  }

  public static Parser getParser() {
    Parser read = SimpleObjectAccess.readFrom(PARSER_FILE_NAME);
    if (read != null) {
      return read;
    } else {
      return defaultParser();
    }
  }

  public static Parser defaultParser() {
    return new Parser(defaultAliases(), defaultCallables());
  }

  public static Caller defaultCaller(PMAContext context) {
    Caller caller = new Caller();
    caller.registerClass("config", new ConfigController().setContext(context));
    caller.registerClass("help", new HelpController().setContext(context));
    caller.registerClass("logging", new LoggingController().setContext(context));
    caller.registerClass("parser", new ParserController().setContext(context));
    caller.registerClass("info", new InfoController().setContext(context));
    caller.registerClass("option", new OptionsController().setContext(context));
    caller.registerClass("aliases", new AliasesController().setContext(context));

    return caller;
  }

  private static ArrayList<Callable> defaultCallables() {
    ArrayList<Callable> callables = new ArrayList<>();
    callables.add(new Action(PMAKeyword.HERE, new Pattern(":here"), "Start counting on default task"));
    callables.add(new Action(PMAKeyword.HERE, new Pattern(":here taskNameOrId", true), "Start counting on taskNameOrId task"));
    callables.add(new Action(PMAKeyword.EXIT, new Pattern(":exit"), "Exit to break"));
    callables.add(new Action(PMAKeyword.SAVE, new Pattern(":save"), "Save current day on web service"));
    callables.add(new Action(PMAKeyword.LOG, new Pattern(":log"), "Show current log"));
    callables.add(new Action(PMAKeyword.LOG, new Pattern(":log :backup"), MapBuilder.<String, String>from("backup", "true"), "Show current log backup"));
    callables.add(new Action(PMAKeyword.TODAY, new Pattern(":today"), "Show what has been done today already"));
    callables.add(new Action(PMAKeyword.LIST, new Pattern(":list :projects"), MapBuilder.<String, String>from("type", "projects"), "List all projects"));
    callables.add(new Action(PMAKeyword.LIST, new Pattern(":list :tasks"), MapBuilder.<String, String>from("type", "tasks"), "List all tasks"));
    callables.add(new Action(PMAKeyword.LIST, new Pattern(":list :tasks :from projectNameOrId"), MapBuilder.<String, String>from("type", "tasks"), "List all tasks from projectNameOrId project"));
    callables.add(new Action(PMAKeyword.UPDATE, new Pattern(":update"), "Update the list of projects and tasks"));
    // TODO \/ \/ \/
    //callables.add(new Action(PMAKeyword.LOGIN, new Pattern(":login"), "Login with system user"));
    //callables.add(new Action(PMAKeyword.LOGIN, new Pattern(":login username"), "Login with specified user"));

    callables.add(new Action(OptionKeyword.LIST, new Pattern(":options"), "List all options with their values"));
    callables.add(new Action(OptionKeyword.GET, new Pattern(":options :get option"), "Return the current value of option"));
    callables.add(new Action(OptionKeyword.SET, new Pattern(":options :set option value", true), "Set the value of option to value"));

    callables.add(new Action(AliasesKeyword.LIST, new Pattern(":aliases"), "List all aliases with their values"));
    callables.add(new Action(AliasesKeyword.GET, new Pattern(":aliases :get alias"), "Return the current value of alias"));
    callables.add(new Action(AliasesKeyword.SET, new Pattern(":aliases :set alias taskNameOrId", true), "Set the value of alias to taskNameOrId"));

    callables.addAll(ConfigController.getDefaultActions());
    callables.addAll(HelpController.getDefaultActions());

    return callables;
  }

  private static Map<String, String> defaultAliases() {
    Map<String, String> aliases = new HashMap<>();

    aliases.put("options", ":options");
    aliases.put("get", ":get");
    aliases.put("set", ":set");

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

    aliases.put("update", ":update");
    aliases.put("atualizar", ":update");

    return aliases;
  }
}