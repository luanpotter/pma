package br.com.dextra.pma.main;

public class NotLoggedIn extends RuntimeException {

    private static final long serialVersionUID = -8161967444299311195L;

    public NotLoggedIn() {
        super("Not logged in! Run pma_token to log in.");
    }
}