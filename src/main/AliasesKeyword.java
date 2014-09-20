package main;

import parser.*;

public enum AliasesKeyword implements Keyword {
  LIST("aliases:list"),
  GET("aliases:get"),
  SET("aliases:set");

  public String controller;

  private AliasesKeyword(String controller) {
    this.controller = controller;
  }

  public String getController() {
    return this.controller;
  }

}