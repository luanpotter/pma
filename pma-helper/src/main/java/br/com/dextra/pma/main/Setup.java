package br.com.dextra.pma.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xyz.luan.console.parser.Callable;
import xyz.luan.console.parser.Caller;
import xyz.luan.console.parser.ExceptionHandler;
import xyz.luan.console.parser.Output;
import xyz.luan.console.parser.Parser;
import xyz.luan.console.parser.actions.InvalidAction;
import xyz.luan.console.parser.config.ConfigController;
import xyz.luan.console.parser.config.HelpController;
import br.com.dextra.pma.controllers.AliasesController;
import br.com.dextra.pma.controllers.InfoController;
import br.com.dextra.pma.controllers.LoggingController;
import br.com.dextra.pma.controllers.OptionsController;
import br.com.dextra.pma.controllers.ParserController;
import br.com.dextra.pma.main.PMAParser.InvalidFormatException;
import br.com.dextra.pma.utils.SimpleObjectAccess;

public final class Setup {

    static {
        ExceptionHandler.HANDLERS.put(NotLoggedIn.class, e -> new Output(e.getMessage()));
        ExceptionHandler.HANDLERS.put(InvalidFormatException.class, e -> new Output(e.getMessage()));
    }

    private Setup() {
        throw new RuntimeException("Should not be instanciated.");
    }

    public static PMAContext setupContext() throws InvalidAction {
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

    public static Caller defaultCaller(PMAContext context) throws InvalidAction {
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