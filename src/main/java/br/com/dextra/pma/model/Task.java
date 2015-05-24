package br.com.dextra.pma.model;

import java.io.Serializable;

import lombok.Getter;

import org.jdom2.Element;

public class Task implements Serializable {

    private static final long serialVersionUID = 6384820773785566908L;

    @Getter
    private long id;

    @Getter
    private String name;

    @Getter
    private Project project;

    public Task(Project project, Element task) {
        this.id = Long.parseLong(task.getChild("id").getText());
        this.name = task.getChild("nome").getText();
        this.project = project;
    }

    @Override
    public String toString() {
        return this.id + " | " + this.project.getName() + " | " + this.name;
    }
}