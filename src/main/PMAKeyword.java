package main;

import parser.*;

public enum PMAKeyword implements Keyword {
  HERE("pma:here"),
  EXIT("pma:exit"),
  SAVE("pma:save"),
  LOG("pma:log"),
  TODAY("pma:today"),
  LIST("pma:list");

  public String controller;

  private PMAKeyword(String controller) {
    this.controller = controller;
  }

  public String getController() {
    return this.controller;
  }

}