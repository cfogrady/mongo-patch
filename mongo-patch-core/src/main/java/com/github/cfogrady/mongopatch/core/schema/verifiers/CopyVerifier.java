package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;

public class CopyVerifier extends FromVerifier {

    @Override
    protected void verifyMorePathAndFrom(JavaType definedSchemaFromType) {
        // No Op
    }
}
