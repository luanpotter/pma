package br.com.dextra.pma.main;

import br.com.dextra.pma.parser.Keyword;

public enum AliasesKeyword implements Keyword {
    LIST("aliases:list"), GET("aliases:get"), SET("aliases:set");

    public String controller;

    private AliasesKeyword(String controller) {
        this.controller = controller;
    }

    public String getController() {
        return this.controller;
    }

}