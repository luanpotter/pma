package br.com.dextra.pma.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.main.Wrapper;

public class Appointment implements Serializable {

    private static final long serialVersionUID = 6206472230468718564L;

    public static final String DEFAULT_DESC = ".";
    public static long INTERVAL_TASK = -1l;

    @Getter
    private long taskId;

    @Getter
    private Time duration;

    private List<String> comments;

    public Appointment(long taskId) {
        this.taskId = taskId;
        this.duration = new Time();
        this.comments = new ArrayList<>();
    }

    public long getTask() {
        return this.taskId;
    }

    public void addTime(int minutes) {
        this.duration.addMinutes(minutes);
    }

    public void addDescription(String description) {
        if (!DEFAULT_DESC.equals(description)) {
            this.comments.add(description);
        }
    }

    public String getComment() {
        if (this.comments.isEmpty()) {
            return DEFAULT_DESC;
        }
        return this.comments.stream().collect(Collectors.joining("\n"));
    }

    public String save(Date date) {
        return Wrapper.createTask(date, taskId, getComment(), duration);
    }

    @Override
    public String toString() {
        return taskId + " -> " + duration + (getComment().equals(DEFAULT_DESC) ? "" : "[" + getComment() + "]");
    }
}