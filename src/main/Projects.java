package main;

import models.Project;
import models.Task;
import utils.SimpleObjectAccess;

import java.util.function.Predicate;
import java.io.Serializable;
import java.util.*;

public class Projects implements Serializable {

  private static final String FILE_NAME = "data.dat";

  private List<Project> projectsCache;

  public static Projects readOrCreate() {
    Projects projs = SimpleObjectAccess.<Projects>readFrom(FILE_NAME);
    if (projs != null) {
      return projs;
    } else {
      Projects p = new Projects().update();
      SimpleObjectAccess.saveTo(FILE_NAME, p);
      return p;
    }
  }

  public Projects update() {
    projectsCache = PMAWrapper.getProjects();
    return this;
  }

  public List<Project> getProjects() {
    return this.projectsCache;
  }

  public List<Task> getTasks() {
    List<Task> tasks = new ArrayList<>();
    for (Project p : projectsCache) {
      tasks.addAll(p.getTasks());
    }
    return tasks;
  }

  public Task getTask(String nameOrId) {
    try {
      long id = Long.parseLong(nameOrId);
      return getTaskById(id);
    } catch (NumberFormatException ex) {
      return getTaskByName(nameOrId);
    }
  }

  public Task getTaskById(final long id) {
    return getTaskThat(t -> t.getId() == id);
  }

  public Task getTaskByName(final String name) {
    return getTaskThat(t -> t.getName().equals(name));
  }

  private Task getTaskThat(Predicate<Task> pred) {
    for (Project p : projectsCache) {
      for (Task t : p.getTasks()) {
        if (pred.test(t)) {
          return t;
        }
      }
    }
    return null;
  }

  public Project getProject(String nameOrId) {
    try {
      long id = Long.parseLong(nameOrId);
      return getProjectById(id);
    } catch (NumberFormatException ex) {
      return getProjectByName(nameOrId);
    }
  }

  public Project getProjectById(final long id) {
    return getProjectThat(p -> p.getId() == id);
  }

  public Project getProjectByName(final String name) {
    return getProjectThat(p -> p.getName().equals(name));
  }

  private Project getProjectThat(Predicate<Project> pred) {
    for (Project p : projectsCache) {
      if (pred.test(p)) {
        return p;
      }
    }
    return null;
  }
}