package br.com.dextra.pma.parser;

public final class InvalidFormatException extends Exception {
    private static final long serialVersionUID = 376344169681487559L;

    public InvalidFormatException(String error, int line) {
        super("Invalid log file! " + error + " [at line " + line + "]");
    }
}