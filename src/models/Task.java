package models;

import java.io.Serializable;

public class Task implements Serializable {

  private int id;
  private String name;

  public Task(String projectInfo) {
    String[] parts = projectInfo.split("\\|");
    assert parts.length == 2;

    this.name = parts[0].trim();
    this.id = Integer.parseInt(parts[1].trim());
  }

  public String getName() {
    return this.name;
  }
}