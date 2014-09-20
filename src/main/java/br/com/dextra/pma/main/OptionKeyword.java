package br.com.dextra.pma.main;

import br.com.dextra.pma.parser.Keyword;

public enum OptionKeyword implements Keyword {
    LIST("option:list"), GET("option:get"), SET("option:set");

    public String controller;

    private OptionKeyword(String controller) {
        this.controller = controller;
    }

    public String getController() {
        return this.controller;
    }

}