package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;

public class MoveVerifier extends FromVerifier {

    @Override
    protected void verifyMorePathAndFrom(JavaType definedSchemaFromType) {
        if (definedSchemaFromType.isPrimitive()) {
            throw new IllegalSchemaException("From type is not nullable. Cannot be removed");
        }

    }
}
