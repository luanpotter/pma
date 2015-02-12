package br.com.dextra.pma.exceptions;

public class ProjectsMissingException extends BaseRequirementException {

    private static final long serialVersionUID = 3879623392021152855L;

    public ProjectsMissingException() {
        super("Projects need to be fecth! Run update to fetch projects and then this command again.");
    }
}
