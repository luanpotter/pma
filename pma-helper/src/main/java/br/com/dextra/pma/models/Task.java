package br.com.dextra.pma.models;

import java.io.Serializable;

import org.jdom2.Element;

public class Task implements Serializable {

    private static final long serialVersionUID = 6384820773785566908L;

    private long id;
    private String name;

    public Task(Element task) {
        this.id = Long.parseLong(task.getChild("id").getText());
        this.name = task.getChild("nome").getText();
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.id + " | " + this.name;
    }
}