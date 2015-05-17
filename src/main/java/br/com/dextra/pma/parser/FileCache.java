package br.com.dextra.pma.parser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class FileCache {
    private List<String> lines;
    private int lastSavedLine;

    public FileCache() {
        this.lines = new ArrayList<>();
        this.lastSavedLine = 0;
    }

    public void add(String line) {
        this.lines.add(line);
    }

    public void savedLine(int lastSavedLine) {
        this.lastSavedLine = lastSavedLine;
    }

    public void printRemainingFile(String fileName) {
        try (PrintWriter p = new PrintWriter(fileName)) {
            consumeRemainingFile(s -> p.println(s));
        } catch (IOException ex) {
            throw new RuntimeException("Unable to re-write file: " + fileName, ex);
        }
    }

    protected void consumeRemainingFile(Consumer<String> consumer) {
        for (int i = lastSavedLine; i < lines.size(); i++) {
            consumer.accept(lines.get(i));
        }
    }
}