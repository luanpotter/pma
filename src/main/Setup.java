package main;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import parser.*;
import parser.config.*;

public final class Setup {
  
  private Setup() {
    throw new RuntimeException("Should not be instanciated.");
  }

  public static PMAContext setupContext() {
    return setupContext(defaultParser());
  }

  public static PMAContext setupContext(Parser parser) {
    PMAContext context = new PMAContext();
    Caller caller = defaultCaller(context);
    context.setup(parser, caller);
    return context;
  }

  public static Parser defaultParser() {
    return new Parser(defaultAliases(), defaultCallables());
  }

  public static Caller defaultCaller(PMAContext context) {
    Caller caller = new Caller();
    caller.registerClass("config", new ConfigController().setContext(context));
    caller.registerClass("help", new HelpController().setContext(context));
    caller.registerClass("pma", new LoggingController().setContext(context));

    return caller;
  }

  private static ArrayList<Callable> defaultCallables() {
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

  private static Map<String, String> defaultAliases() {
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