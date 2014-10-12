package br.com.dextra.pma.main;

import xyz.luan.console.fn.FnApplication;
import xyz.luan.console.fn.FnSetup;
import xyz.luan.console.parser.Application;
import xyz.luan.console.parser.Console;
import xyz.luan.console.parser.Context;
import xyz.luan.console.parser.Parser;
import br.com.dextra.pma.main.Wrapper.InvalidLoginException;
import br.com.dextra.pma.utils.SimpleObjectAccess;

public class PmaSetup extends FnSetup<PmaContext> {

    public PmaSetup() {
        super("br.com.dextra.pma.controllers");
    }

    private static final String PARSER_FILE_NAME = "parser.dat";

    public static void main(String[] args) {
        create().run(args);
    }
    
    public static Application create() {
        return new PmaSetup().setupApplication(new PmaContext());
    }
    
    @Override
    protected Application createApplication(Console console, PmaContext context) {
        return new FnApplication<PmaContext>(console, context) {
            @Override
            protected void loop(Console console, Context c) {
                boolean needUpdating = tryLogginInIfNeeded(console);
                if (needUpdating) {
                    console.result("Sucessfully logged in. Now wait until your projects and tasks are retrived...");
                    context.p().update();
                    console.result("All done! Type help if you need any help.");
                }
                super.loop(console, context);
            }

            private boolean tryLogginInIfNeeded(Console console) {
                try {
                    return Wrapper.requestLoginIfNeeded(console);
                } catch (InvalidLoginException e) {
                    console.error("Invalid login/password!");
                    return tryLogginInIfNeeded(console);
                }
            }
        };
    }
    
    @Override
    public Parser getParser() {
        Parser p = readParser();
        if (p != null) {
            return p;
        }
        return defaultParser();
    }

    public static Parser readParser() {
        return SimpleObjectAccess.readFrom(PARSER_FILE_NAME);
    }
    
    public static void saveParser(Parser parser) {
        SimpleObjectAccess.saveTo(PARSER_FILE_NAME, parser);
    }


}