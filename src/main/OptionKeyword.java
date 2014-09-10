package main;

import parser.*;

public enum OptionKeyword implements Keyword {
  LIST("option:list"),
  GET("option:get"),
  SET("option:set");

  public String controller;

  private PMAKeyword(String controller) {
    this.controller = controller;
  }

  public String getController() {
    return this.controller;
  }

}