package com.github.cfogrady.spring.mongopatch.operations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoveOperation extends PatchOperation {
    @JsonProperty(OperationFields.OPERATION_FIELD)
    public OperationType getOperationType() {
        return OperationType.remove;
    }
}
