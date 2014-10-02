package br.com.dextra.pma.main;

import xyz.luan.console.parser.Application;
import xyz.luan.console.parser.Console;
import xyz.luan.console.parser.Context;
import xyz.luan.console.parser.call.CallResult;

final class PmaApplication extends Application {
    private final Console console;
    private final PmaContext context;

    public PmaApplication(Console console, PmaContext context) {
        this.console = console;
        this.context = context;
    }
    
    public static void main(String[] args) {
        Setup.setupApplication().run(args);
    }

    @Override
    public Console createConsole() {
        return console;
    }

    @Override
    public Context createContext() {
        return context;
    }

    @Override
    public String startMessage() {
        return "Ohayou!";
    }

    @Override
    public String endMessage() {
        return "Bye bye...";
    }

    @Override
    public CallResult emptyLineHandler() {
        return CallResult.QUIT;
    }
}