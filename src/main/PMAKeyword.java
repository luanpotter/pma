package main;

import parser.*;

public enum PMAKeyword implements Keyword {
  HERE("logging:here"),
  EXIT("logging:exit"),
  SAVE("parser:save"),
  LOG("parser:log"),
  TODAY("parser:today"),
  LIST("info:list");

  public String controller;

  private PMAKeyword(String controller) {
    this.controller = controller;
  }

  public String getController() {
    return this.controller;
  }

}