package main;

import models.Project;
import models.Task;
import utils.SimpleObjectAccess;

import java.util.function.Predicate;
import java.io.Serializable;
import java.util.*;

public class Projects implements Serializable {

  private static final String FILE_NAME = "projs.dat";

  private List<Project> projectsCache;

  public static Projects readOrCreate() {
    Projects projs = SimpleObjectAccess.<Projects>readFrom(FILE_NAME);
    if (projs != null) {
      return projs;
    } else {
      return new Projects().update();
    }
  }

  public void save() {
    SimpleObjectAccess.saveTo(FILE_NAME, this);
  }

  public Projects update() {
    projectsCache = PMAWrapper.getProjects();
    this.save();
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

  public Task getTaskWithoutAlias(String nameOrId) {
    return getTask(null, nameOrId);
  }

  public Task getTask(Aliases aliases, String nameOrId) {
    try {
      Long id = Long.parseLong(nameOrId);
      return getTaskById(id);
    } catch (NumberFormatException ex) {
      Long aliasedId = aliases == null ? null : aliases.getTaskByAlias(nameOrId);
      if (aliasedId != null) {
        return getTaskById(aliasedId);
      }
      return getTaskByName(nameOrId);
    }
  }

  private Task getTaskById(final long id) {
    return getTaskThat(t -> t.getId() == id);
  }

  private Task getTaskByName(final String name) {
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