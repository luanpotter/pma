package parser.config;

import parser.Keyword;

public enum ConfigKeyword implements Keyword {
  ALIASES("config:aliases");

  private String controller;

  private ConfigKeyword(String controller) {
    this.controller = controller;
  }

  public String getController() {
    return this.controller;
  }
}