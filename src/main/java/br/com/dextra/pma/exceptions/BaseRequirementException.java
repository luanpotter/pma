package br.com.dextra.pma.exceptions;

public abstract class BaseRequirementException extends RuntimeException {

    private static final long serialVersionUID = 5261191363434291861L;

    protected BaseRequirementException(String message) {
        super(message);
    }
}
