package br.com.dextra.pma.models;

import java.io.Serializable;

public class Task implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6384820773785566908L;
    private long id;
    private String name;

    public Task(String projectInfo) {
        String[] parts = projectInfo.split("\\|");
        assert parts.length >= 2;

        this.name = parts[0].trim();
        for (int i = 1; i < parts.length - 1; i++) {
            this.name += "|" + parts[i];
        }
        this.name = this.name.trim().replace("_", " ");
        this.id = Long.parseLong(parts[parts.length - 1].trim());
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