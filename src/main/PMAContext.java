package main;

import parser.Context;

public class PMAContext extends Context {

  private Projects projects;

  public PMAContext() {
    this.projects = Projects.readOrCreate();
  }

  public static void main(String[] args) {
    Config.createContext().main();
  }

  public Projects p() {
    return this.projects;
  }

  @Override
  public void emptyLineHandler() {
    System.exit(0);
  }
}