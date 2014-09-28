package br.com.dextra.pma.models;

import xyz.luan.console.parser.Output;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.main.Wrapper;

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
        return Wrapper.createTask(date, taskId, comment, minutes);
    }

    @Override
    public String toString() {
        return taskId + " -> " + minutes + "[" + comment + "]";
    }
}