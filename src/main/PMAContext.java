package main;

import parser.Context;

public class PMAContext extends Context {

  @Override
  public void emptyLineHandler() {
    System.exit(0);
  }
}