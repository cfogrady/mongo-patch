package com.github.cfogrady.mongopatch.core.operations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveOperation extends PatchOperation implements OperationWithFrom {
    @JsonProperty(OperationFields.FROM_FIELD)
    private String from;

    @JsonProperty(OperationFields.OPERATION_FIELD)
    public OperationType getOperationType() {
        return OperationType.move;
    }

    @JsonProperty(OperationFields.FROM_FIELD)
    public String getFrom() {
        return from;
    }

    @JsonProperty(OperationFields.FROM_FIELD)
    public void setFrom(String from) {
        this.from = from;
    }
}
