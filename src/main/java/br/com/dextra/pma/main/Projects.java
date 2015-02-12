package br.com.dextra.pma.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import xyz.luan.console.parser.Console;
import br.com.dextra.pma.exceptions.ProjectsMissingException;
import br.com.dextra.pma.models.Project;
import br.com.dextra.pma.models.Task;
import br.com.dextra.pma.utils.SimpleObjectAccess;

public class Projects implements Serializable {

    private static final long serialVersionUID = 220688288441408437L;

    private static final String FILE_NAME = "projs.dat";

    private List<Project> projectsCache;

    private Projects() {
        this.projectsCache = null;
    }

    public static Projects readOrCreate() {
        Projects projs = SimpleObjectAccess.<Projects> readFrom(FILE_NAME);
        if (projs != null) {
            return projs;
        } else {
            return new Projects();
        }
    }

    public void save() {
        SimpleObjectAccess.saveTo(FILE_NAME, this);
    }

    public Projects update(Console console) {
        projectsCache = Wrapper.getProjects(console);
        this.save();
        return this;
    }

    public List<Project> getProjects() {
        assertInitialized();
        return this.projectsCache;
    }

    public List<Task> getTasks() {
        assertInitialized();
        List<Task> tasks = new ArrayList<>();
        for (Project p : projectsCache) {
            tasks.addAll(p.getTasks());
        }
        return tasks;
    }

    public Task getTaskWithoutAlias(String nameOrId) {
        return getTask(null, nameOrId);
    }

    public Task getTask(Aliases aliases, String nameOrId) {
        try {
            Long id = Long.parseLong(nameOrId);
            return getTaskById(id);
        } catch (NumberFormatException ex) {
            Long aliasedId = aliases == null ? null : aliases.getTaskByAlias(nameOrId);
            if (aliasedId != null) {
                return getTaskById(aliasedId);
            }
            return getTaskByName(nameOrId);
        }
    }

    private Task getTaskById(final long id) {
        return getTaskThat(t -> t.getId() == id);
    }

    private Task getTaskByName(final String name) {
        return getTaskThat(t -> t.getName().equals(name));
    }

    private Task getTaskThat(Predicate<Task> pred) {
        assertInitialized();
        for (Project p : projectsCache) {
            for (Task t : p.getTasks()) {
                if (pred.test(t)) {
                    return t;
                }
            }
        }
        return null;
    }

    public Project getProject(String nameOrId) {
        try {
            long id = Long.parseLong(nameOrId);
            return getProjectById(id);
        } catch (NumberFormatException ex) {
            return getProjectByName(nameOrId);
        }
    }

    public Project getProjectById(final long id) {
        return getProjectThat(p -> p.getId() == id);
    }

    public Project getProjectByName(final String name) {
        return getProjectThat(p -> p.getName().equals(name));
    }

    private Project getProjectThat(Predicate<Project> pred) {
        assertInitialized();
        for (Project p : projectsCache) {
            if (pred.test(p)) {
                return p;
            }
        }
        return null;
    }

    private void assertInitialized() {
        if (!isInitialized()) {
            throw new ProjectsMissingException();
        }
    }

    public boolean isInitialized() {
        return this.projectsCache != null;
    }
}