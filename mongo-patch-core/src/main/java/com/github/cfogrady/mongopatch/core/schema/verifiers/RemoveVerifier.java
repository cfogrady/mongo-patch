package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.mongopatch.core.operations.PatchOperation;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;

import java.util.Map;

public class RemoveVerifier extends PatchVerifier {

    @Override
    public void verify(PatchOperation operation, Map<String, JavaType> schema) {
        // Needs to validate path exists and that item is not nullable
        String definedPath = this.pathIsPartOfSchema(operation.getPath(), schema);

        boolean operatingOnElement = definedPath.endsWith("/*");
        String schemaPath = getSchemaPath(operatingOnElement, definedPath);

        JavaType definedSchemaType = schema.get(schemaPath);
        if (definedSchemaType.isPrimitive()) {
            throw new IllegalSchemaException("Cannot remove a primitive!");
        }
    }
}
