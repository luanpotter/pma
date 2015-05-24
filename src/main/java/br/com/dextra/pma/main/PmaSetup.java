package br.com.dextra.pma.main;

import xyz.luan.console.fn.FnSetup;
import xyz.luan.console.parser.Application;
import xyz.luan.console.parser.Parser;
import br.com.dextra.pma.utils.SimpleObjectAccess;

public class PmaSetup extends FnSetup<PmaContext> {

    private static final String PARSER_FILE_NAME = "parser.dat";

    public PmaSetup() {
        super("br.com.dextra.pma.controller");
    }

    public static void main(String[] args) {
        create().run(args);
    }

    public static Application create() {
        return new PmaSetup().setupApplication(new PmaContext());
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