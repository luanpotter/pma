package main;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

import date.*;
import models.*;

public final class PMAWrapper {

  private PMAWrapper() { throw new RuntimeException("Should not be instanciated."); }

  public static List<Project> getProjects() {
    final List<Project> projects = new ArrayList<>();
    consumeOutput("pma_projects", project -> projects.add(new Project(project)));
    return projects;
  }

  public static List<Task> getTasksFromProject(long projectId) {
    final List<Task> tasks = new ArrayList<>();
    consumeOutput("pma_tasks " + projectId, task -> tasks.add(new Task(task)));
    return tasks;
  }

  public static void createDay(Date date, Time start, Time end, int interval) {
    String command = "pma_create_day -d " + date + " -s " + start + " -e " + end + " -i " + interval;
    runCommand(command);

    int totalTime = end.getDifference(start) - interval;
    createTask(date, -1, "asdasd", totalTime);
  }

  public static void createTask(Date date, int taskId, String description, int duration) {
    String command = "pma_create_task -d " + date + " -e " + duration + " -s concluded " + taskId + " " + description;
    runCommand(command);
  }

  private static void consumeOutput(String command, Consumer<String> consumer) {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(runCommand(command), "ISO-8859-1"))) {
      String line;
      while((line = r.readLine()) != null && !line.isEmpty()) {
        if (line.equals("token inválido")) {
          throw new NotLoggedIn();
        } else {
          consumer.accept(line);
        }
      }
    } catch (IOException ex) {
      PMAHelper.halt("Unexpected error! Unable to parse results: " + ex.getMessage());
    }
  }

  private static InputStream runCommand(String command) {
    try {
      String fullCommand = "./pma-scripts/bin/" + command;
      System.out.println(fullCommand);
      Process process = Runtime.getRuntime().exec(fullCommand);
      process.waitFor();
      return process.getInputStream();
    } catch (InterruptedException | IOException e) {
      PMAHelper.halt("Unexpected error! Unable to run command: " + e.getMessage());
      return null;
    }
  }

}
