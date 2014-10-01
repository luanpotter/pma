package br.com.dextra.pma.main;

import xyz.luan.console.parser.Application;
import xyz.luan.console.parser.Console;
import xyz.luan.console.parser.Context;

final class PMAApplication extends Application {
    private final Console console;
    private final PMAContext context;

    public PMAApplication(Console console, PMAContext context) {
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
    public boolean emptyLineHandler() {
        return true; //i.e., quit
    }
}