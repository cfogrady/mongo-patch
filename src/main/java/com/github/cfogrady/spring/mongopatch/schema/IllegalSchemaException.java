package com.github.cfogrady.spring.mongopatch.schema;

public class IllegalSchemaException extends IllegalArgumentException {
    /**
     * 
     */
    private static final long serialVersionUID = 2950740492328890637L;

    public IllegalSchemaException() {
        super();
    }

    public IllegalSchemaException(String message) {
        super(message);
    }

    public IllegalSchemaException(Throwable e) {
        super(e);
    }

    public IllegalSchemaException(String message, Throwable e) {
        super(message, e);
    }

}
