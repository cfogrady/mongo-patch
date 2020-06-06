package com.github.cfogrady.spring.mongopatch.operations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestOperation extends PatchOperation implements OperationWithValue {
    @JsonProperty(OperationFields.VALUE_FIELD)
    private Object value;

    @JsonProperty(OperationFields.OPERATION_FIELD)
    public OperationType getOperationType() {
        return OperationType.test;
    }

    @JsonProperty(OperationFields.VALUE_FIELD)
    public Object getValue() {
        return value;
    }

    @JsonProperty(OperationFields.VALUE_FIELD)
    public void setValue(Object value) {
        this.value = value;
    }
}
