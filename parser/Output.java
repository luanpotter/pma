package parser;

import java.io.*;
import java.util.*;

public class Output {
  
  private List<String> lines;

  public Output() {
    this.lines = new ArrayList<String>();
  }

  public Output(String... lines) {
    this.lines = Arrays.asList(lines);
  }

  public void add(String line) {
    this.lines.add(line);
  }

  public void append(Output o) {
    this.lines.addAll(o.lines);
  }

  public void print(PrintStream p) {
    for (String line : lines) {
      p.println(line);
    }
  }
}