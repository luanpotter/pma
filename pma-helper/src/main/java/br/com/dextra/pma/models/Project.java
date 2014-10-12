package br.com.dextra.pma.models;

import java.io.Serializable;
import java.util.List;

import org.jdom2.Element;

import br.com.dextra.pma.main.Wrapper;

public class Project implements Serializable {

    private static final long serialVersionUID = -464630186990092875L;

    private long id;
    private String client;
    private String name;

    private List<Task> tasks;

    public Project(Element project) {
        this.id = Long.parseLong(project.getChild("id").getText());
        this.client = project.getChild("cliente").getText();
        this.name = project.getChild("nome").getText();

        this.tasks = Wrapper.getTasksFromProject(this);
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