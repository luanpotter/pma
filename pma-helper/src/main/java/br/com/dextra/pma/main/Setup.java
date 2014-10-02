package br.com.dextra.pma.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xyz.luan.console.parser.Application;
import xyz.luan.console.parser.Console;
import xyz.luan.console.parser.DefaultConsole;
import xyz.luan.console.parser.Parser;
import xyz.luan.console.parser.actions.InvalidAction;
import xyz.luan.console.parser.call.Caller;
import xyz.luan.console.parser.callable.Callable;
import xyz.luan.console.parser.config.ConfigController;
import xyz.luan.console.parser.config.HelpController;
import br.com.dextra.pma.controllers.AliasesController;
import br.com.dextra.pma.controllers.InfoController;
import br.com.dextra.pma.controllers.LoggingController;
import br.com.dextra.pma.controllers.OptionsController;
import br.com.dextra.pma.controllers.ParserController;
import br.com.dextra.pma.utils.SimpleObjectAccess;

public final class Setup {

    /*static {
        ExceptionHandler.HANDLERS.put(NotLoggedIn.class, e -> new Output(e.getMessage()));
        ExceptionHandler.HANDLERS.put(InvalidFormatException.class, e -> new Output(e.getMessage()));
    }*/ // TODO << reado ExceptionHandler

    private Setup() {
        throw new RuntimeException("Should not be instanciated.");
    }

    public static Application setupApplication() {
        final PmaContext context = new PmaContext();
        final Console console = new DefaultConsole();

        Caller caller;
        try {
            caller = defaultCaller(context, console);
        } catch (InvalidAction e) {
            throw new RuntimeException(e);
        }
        Parser parser = getParser();
        context.setup(parser, caller);

        return new PmaApplication(console, context);
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

    public static Caller defaultCaller(PmaContext context, Console console) throws InvalidAction {
        Caller caller = new Caller();
        caller.registerClass("config", new ConfigController().setup(context, console));
        caller.registerClass("help", new HelpController().setup(context, console));
        caller.registerClass("logging", new LoggingController().setup(context, console));
        caller.registerClass("parser", new ParserController().setup(context, console));
        caller.registerClass("info", new InfoController().setup(context, console));
        caller.registerClass("option", new OptionsController().setup(context, console));
        caller.registerClass("aliases", new AliasesController().setup(context, console));

        return caller;
    }

    private static ArrayList<Callable> defaultCallables() {
        ArrayList<Callable> callables = new ArrayList<>();

        ParserController.defaultCallables("parser", callables);
        LoggingController.defaultCallables("logging", callables);
        InfoController.defaultCallables("info", callables);
        OptionsController.defaultCallables("option", callables);
        AliasesController.defaultCallables("aliases", callables);

        ConfigController.defaultActions("config", callables);
        HelpController.defaultActions("help", callables);

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