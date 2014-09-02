package org.daveware.passwordmaker;

public class PasswordGenerationException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public PasswordGenerationException(String s) {
        super(s);
    }

    public PasswordGenerationException(Throwable t) {
        super(t);
    }
}
