package br.com.dextra.pma.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public abstract class Context implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -893098040420691058L;
    protected Parser parser;
    protected Caller caller;

    public abstract void emptyLineHandler();

    public void setup(Parser parser, Caller caller) {
        this.parser = parser;
        this.caller = caller;
    }

    public Parser getParser() {
        return this.parser;
    }

    public Caller getCaller() {
        return this.caller;
    }

    public void main() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            this.print("Ohayou!");
            while (true) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    emptyLineHandler();
                } else {
                    execute(line.split(" "));
                }
            }
        } catch (IOException ex) {
            System.err.println("Unexpected Exception: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void quit(int status) {
        System.exit(status);
    }

    public void print(String str) {
        this.print(new Output(str));
    }

    public void print(Output output) {
        output.print(System.out);
    }

    public void execute(String[] params) {
        print(caller.call(parser.parse(params)));
    }
}