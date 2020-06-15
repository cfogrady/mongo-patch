package com.github.cfogrady.spring.mongopatch.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;

public class ReplaceVerifier extends ValueVerifier {

    @Override
    protected void verifyValueIsCorrectType(String path, JavaType expectedType, JavaType valueType) {
        // if replace operation, this should fail if the expectedType is an array and the valueType isn't
        if (!valueType.isCollectionLikeType() && !valueType.isArrayType()) {
            throw new IllegalSchemaException(path + " expected to be an array or list like type.");
        }

    }
}
