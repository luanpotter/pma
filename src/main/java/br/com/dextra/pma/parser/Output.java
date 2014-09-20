package br.com.dextra.pma.parser;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO add status code and make conditional tasks
public class Output {

    private List<String> lines;
    private int tabLevel;

    private static final String TAB_STRING = "\t";

    public Output() {
        this.lines = new ArrayList<String>();
        this.tabLevel = 0;
    }

    public Output(String... lines) {
        this.lines = new ArrayList<>(Arrays.asList(lines));
        this.tabLevel = 0;
    }

    private StringBuilder preBuiltTabs = new StringBuilder();

    public void tabReset() {
        this.tabLevel = 0;
        this.preBuiltTabs.setLength(0);
    }

    public void tabIn() {
        this.tabLevel++;
        this.preBuiltTabs.append(TAB_STRING);
    }

    public void tabOut() {
        if (this.tabLevel == 0) {
            throw new RuntimeException("Trying to tab out when tab level is already zero! Did you forget to tabIn somewhere?");
        }
        this.tabLevel--;
        this.preBuiltTabs.setLength(this.preBuiltTabs.length() - TAB_STRING.length());
    }

    public void add(String line) {
        this.lines.add(getTabs() + line);
    }

    public void append(Output o) {
        for (String line : o.lines) {
            this.lines.add(getTabs() + line);
        }
    }

    private String getTabs() {
        return preBuiltTabs.toString();
    }

    public void print(PrintStream p) {
        for (String line : lines) {
            p.println(line);
        }
    }
}