package br.com.dextra.pma.main;

import xyz.luan.console.parser.Context;

public class PMAContext extends Context {

    private static final long serialVersionUID = -4203815905025892292L;

    private Projects projects;
    private Options options;
    private Aliases aliases;

    public PMAContext() {
        this.projects = Projects.readOrCreate();
        this.options = Options.readOrCreate();
        this.aliases = Aliases.readOrCreate();
    }

    public static void main(String[] args) {
        Setup.setupContext().main();
    }

    public Aliases a() {
        return this.aliases;
    }

    public Options o() {
        return this.options;
    }

    public Projects p() {
        return this.projects;
    }

    public void saveParser() {
        Setup.saveParser(this.parser);
    }

    @Override
    public void quit(int status) {
        print("Bye, bye!");
        saveParser();
        super.quit(status);
    }

    @Override
    public void emptyLineHandler() {
        quit(0);
    }
}