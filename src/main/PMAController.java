package main;

import parser.*;

public class PMAController implements Controller {

  private Parser parser;

  public PMAController(Parser parser) {
    this.parser = parser;
  }
}