package main;

import parser.*;

public enum PMAKeyword implements Keyword {
  HERE("here"),
  EXIT("exit"),
  SAVE("save"),
  LOG("log"),
  TODAY("today"),
  LIST("list");

  public String controller;

  private PMAKeyword(String controller) {
    this.controller = controller;
  }

  public String getController() {
    return this.controller;
  }

}