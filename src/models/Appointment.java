package models;

import date.Date;
import main.PMAWrapper;

import parser.Output;

public class Appointment {
  private long taskId;
  private int minutes;
  private String comment;

  public Appointment(long taskId, String comment) {
    this.taskId = taskId;
    this.minutes = -1;
    this.comment = comment;
  }

  public long getTask() {
    return this.taskId;
  }

  public void setTime(int minutes) {
    this.minutes = minutes;
  }

  public Output save(Date date) {
    return PMAWrapper.createTask(date, taskId, comment, minutes);
  }

  @Override
  public String toString() {
    return taskId + " -> " + minutes + "[" + comment + "]";
  }
}