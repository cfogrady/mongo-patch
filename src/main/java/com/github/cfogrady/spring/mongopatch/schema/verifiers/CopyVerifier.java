package com.github.cfogrady.spring.mongopatch.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;

public class CopyVerifier extends FromVerifier {

    @Override
    protected void verifyMorePathAndFrom(JavaType definedSchemaFromType) {
        // No Op
    }
}
