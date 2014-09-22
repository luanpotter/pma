package br.com.dextra.pma.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import xyz.luan.console.parser.Output;
import br.com.dextra.pma.date.Date;
import br.com.dextra.pma.date.Time;
import br.com.dextra.pma.models.Project;
import br.com.dextra.pma.models.Task;

public final class PMAWrapper {

    private PMAWrapper() {
        throw new RuntimeException("Should not be instanciated.");
    }

    public static boolean login(String user) {
        throw new RuntimeException("Not implemented yet! Login outside the project via ./pma-scripts/bin/pma_token");
    }

    public static List<Project> getProjects() {
        final List<Project> projects = new ArrayList<>();
        consumeOutput("pma_projects", project -> projects.add(new Project(project)));
        return projects;
    }

    public static List<Task> getTasksFromProject(long projectId) {
        final List<Task> tasks = new ArrayList<>();
        consumeOutput("pma_tasks " + projectId, task -> tasks.add(new Task(task)));
        return tasks;
    }

    public static Output createDay(Date date, Time start, Time end, Time interval) {
        String command = "pma_create_day -d " + date + " -s " + start + " -e " + end + " -i " + interval;
        final Output out = new Output();
        consumeOutput(command, s -> out.add(s));
        return out;
    }

    public static Output createTask(Date date, long taskId, String description, int duration) {
        String command = "pma_create_task -d " + date + " -e " + duration + " -s concluded " + taskId + " " + description;
        final Output out = new Output();
        consumeOutput(command, s -> out.add(s));
        return out;
    }

    private static void consumeOutput(String command, Consumer<String> consumer) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(runCommand(command), "ISO-8859-1"))) {
            String line;
            while ((line = r.readLine()) != null && !line.isEmpty()) {
                if (line.equals("token inválido")) {
                    throw new NotLoggedIn();
                } else {
                    consumer.accept(line);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Unable to consume command output.", ex);
        }
    }

    private static InputStream runCommand(String command) {
        try {
            String fullCommand = "../pma-scripts/bin/" + command;
            // TODO log command somehow
            Process process = Runtime.getRuntime().exec(fullCommand);
            process.waitFor();
            return process.getInputStream();
        } catch (InterruptedException | IOException ex) {
            throw new RuntimeException("Unable to run command: " + command, ex);
        }
    }

}
