package main;

import parser.Context;

public class PMAContext extends Context {

  public static void main(String[] args) {
    Config.createContext().main();
  }

  @Override
  public void emptyLineHandler() {
    System.exit(0);
  }
}