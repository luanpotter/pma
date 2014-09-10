package parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Serializable;

public abstract class Context implements Serializable {

  private Parser parser;
  private Caller caller;

  public abstract void emptyLineHandler();

  public void setup(Parser parser, Caller caller) {
    this.parser = parser;
    this.caller = caller;
  }

  public Parser getParser() {
    return this.parser;
  }

  public Caller getCaller() {
    return this.caller;
  }

  public void main() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
      while(true) {
        String line = reader.readLine();
        if (line.isEmpty()) {
          emptyLineHandler();
        } else {
          execute(line.split(" "));
        }
      }
    } catch (IOException ex) {
      System.err.println("Unexpected Exception: " + ex.getMessage());
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public void execute(String[] params) {
    caller.callAndPrint(parser.parse(params));
  }
}