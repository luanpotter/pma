package br.com.dextra.pma.main;

import xyz.luan.console.parser.Context;

public class PmaContext extends Context {

    private static final long serialVersionUID = -4203815905025892292L;

    private Projects projects;
    private Options options;
    private Aliases aliases;

    public PmaContext() {
        this.projects = Projects.readOrCreate();
        this.options = Options.readOrCreate();
        this.aliases = Aliases.readOrCreate();
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
        PmaSetup.saveParser(this.parser);
    }
}