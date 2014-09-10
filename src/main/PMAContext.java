package main;

import parser.Context;

public class PMAContext extends Context {

  private Projects projects;
  private Options options;

  public PMAContext() {
    this.projects = Projects.readOrCreate();
    this.options = Options.readOrCreate();
  }

  public static void main(String[] args) {
    Config.createContext().main();
  }

  public Options o() {
    return this.options;
  }

  public Projects p() {
    return this.projects;
  }

  @Override
  public void emptyLineHandler() {
    System.exit(0);
  }
}