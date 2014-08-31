package models;

import java.util.List;
import java.io.Serializable;

import main.PMAWrapper;

public class Project implements Serializable {

  private int id;
  private String client;
  private String name;

  private List<Task> tasks;

  public Project(String projectInfo) {
    String[] parts = projectInfo.split("\\|");
    assert parts.length == 3;

    this.client = parts[0].trim();
    this.name = parts[1].trim();
    this.id = Integer.parseInt(parts[2].trim());

    this.tasks = PMAWrapper.getTasksFromProject(this.id);
  }

  public Task getTaskByName(String name) {
    for (Task t : tasks) {
      if (t.getName().equals(name)) {
        return t;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return this.id + " | " + this.client + " | " + this.name;
  }
}