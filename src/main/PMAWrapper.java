package main;

import java.io.IOException;

import date.*;

public final class PMAWrapper {

  private PMAWrapper() { throw new RuntimeException("Should not be instanciated."); }

  public static int getTaskId(String name) {
    return -1; // TODO
  }

  public static void createDay(Date date, Time start, Time end, int interval) {
    String command_day = "./pma_create_day -d " + date + " -s " + start + " -e " + end + " -i " + interval;
    runCommand(command_day);

    int totalTime = end.getDifference(start) - interval;
    createTask(date, -1, "asdasd", totalTime);
  }

  public static void createTask(Date date, int taskId, String description, int duration) {
    String command_task = "./pma_create_task -d " + date + " -e " + duration + " -s concluded " + taskId + " " + description;
    runCommand(command_task);
  }

  private static void runCommand(String command) {
    System.out.println("Command generated: " + command);
    try {
      Runtime.getRuntime().exec(command).waitFor();
    } catch (InterruptedException | IOException e) {
      PMAHelper.halt("Unexpected error! Unable to run command: " + e.getMessage());
    }
  }

}