package com.github.cfogrady.spring.mongopatch.operations;

public enum OperationType {
    add(true, false),
    remove(false, false),
    replace(true, false),
    copy(false, true),
    move(false, true),
    test(true, false);
    
    public final static String ADD_VALUE = "add";
    public final static String REMOVE_VALUE = "remove";
    public final static String REPLACE_VALUE = "replace";
    public final static String COPY_VALUE = "copy";
    public final static String MOVE_VALUE = "move";
    public final static String TEST_VALUE = "test";

    public final boolean hasValue;
    public final boolean hasFrom;

    OperationType(boolean hasValue, boolean hasFrom) {
        this.hasValue = hasValue;
        this.hasFrom = hasFrom;
    }
}
