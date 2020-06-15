package com.github.cfogrady.spring.mongopatch.operations;

public class TestOperationFailureException extends IllegalStateException {

    /**
     * 
     */
    private static final long serialVersionUID = -2850727605942760351L;

    public TestOperationFailureException() {
        super();
    }

    public TestOperationFailureException(String message) {
        super(message);
    }

    public TestOperationFailureException(Throwable e) {
        super(e);
    }

    public TestOperationFailureException(String message, Throwable e) {
        super(message, e);
    }
}
