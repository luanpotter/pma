package br.com.dextra.pma.models;

import java.io.Serializable;
import java.util.List;

import br.com.dextra.pma.main.PMAWrapper;

public class Project implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -464630186990092875L;
    private long id;
    private String client;
    private String name;

    private List<Task> tasks;

    public Project(String projectInfo) {
        String[] parts = projectInfo.split("\\|");
        assert parts.length >= 3;

        this.client = parts[0].trim();
        this.name = parts[1];
        for (int i = 2; i < parts.length - 1; i++) {
            this.name += "|" + parts[i];
        }
        this.name = this.name.trim().replace("_", " ");
        this.id = Long.parseLong(parts[parts.length - 1].trim());

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

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    @Override
    public String toString() {
        return this.id + " | " + this.client + " | " + this.name;
    }
}