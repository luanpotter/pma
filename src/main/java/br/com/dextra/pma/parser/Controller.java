package br.com.dextra.pma.parser;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller<T extends Context> {

    protected T context;

    public Controller<T> setContext(T context) {
        this.context = context;
        return this;
    }

    public static void empty(Map<String, String> args) {
        if (args.size() != 0) {
            throw new IllegalArgumentException("Invalid parameters. No parameters allowed, but " + args.size() + " found.");
        }
    }

    public static void optional(Map<String, String> args, String... optional) {
        main: for (String key : args.keySet()) {
            for (String current : optional) {
                if (current.equals(key)) {
                    continue main;
                }
            }
            throw new IllegalArgumentException("Invalid parameters. Parameter " + key + " not allowed here.");
        }
    }

    public static Map<String, String> required(Map<String, String> args, String... required) {
        Map<String, String> argsCopy = new HashMap<>(args);
        for (String arg : required) {
            if (argsCopy.remove(arg) == null) {
                throw new IllegalArgumentException("Invalid parameters. Required parameter " + arg + " not found.");
            }
        }
        return argsCopy;
    }

}