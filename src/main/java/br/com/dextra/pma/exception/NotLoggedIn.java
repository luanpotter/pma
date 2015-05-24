package br.com.dextra.pma.exception;

public class NotLoggedIn extends BaseRequirementException {

    private static final long serialVersionUID = -8161967444299311195L;

    public NotLoggedIn() {
        super("Not logged in! Run login to log in and then run this command again.");
    }
}