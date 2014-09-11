package models;

import date.*;
import main.PMAWrapper;

import parser.Output;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;
import java.io.Serializable;

public class Day implements Serializable {

  private Date date;
  private Time startTime;
  private Time endTime;
  private Time interval;
  private List<Appointment> appointments;

  public Day(Date date, Time start, Time end, Time interval, Map<Long, Appointment> appointments) {
    this.date = date;
    this.startTime = start;
    this.endTime = end;
    this.interval = interval;

    this.appointments = new ArrayList<>();
    for (Long task : appointments.keySet()) {
      this.appointments.add(appointments.get(task));
    }
  }

  public Output save() {
    Output out = PMAWrapper.createDay(date, startTime, endTime, interval);
    for (Appointment a : appointments) {
      out.append(a.save(date));
    }
    return out;
  }

  @Override
  public String toString() {
    return "[" + date + "] from: " + startTime + " to " + endTime + " except for " + interval + "; tasks: " + Arrays.toString(appointments.toArray());
  }
}