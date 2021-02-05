package com.github.cfogrady.mongopatch.core.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = OperationFields.OPERATION_FIELD)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddOperation.class, name = OperationType.ADD_VALUE),
        @JsonSubTypes.Type(value = RemoveOperation.class, name = OperationType.REMOVE_VALUE),
        @JsonSubTypes.Type(value = ReplaceOperation.class, name = OperationType.REPLACE_VALUE),
        @JsonSubTypes.Type(value = CopyOperation.class, name = OperationType.COPY_VALUE),
        @JsonSubTypes.Type(value = MoveOperation.class, name = OperationType.MOVE_VALUE),
        @JsonSubTypes.Type(value = TestOperation.class, name = OperationType.TEST_VALUE),
})
public abstract class PatchOperation {

    @JsonProperty(OperationFields.OPERATION_FIELD)
    public abstract OperationType getOperationType();

    @JsonProperty(OperationFields.PATH_FIELD)
    private String path;

    @JsonProperty(OperationFields.PATH_FIELD)
    public String getPath() {
        return path;
    }

    @JsonProperty(OperationFields.PATH_FIELD)
    public void setPath(String path) {
        this.path = path;
    }

}
