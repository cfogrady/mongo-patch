package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;

public class ReplaceVerifier extends ValueVerifier {

    @Override
    protected void verifyValueIsCorrectType(String path, JavaType expectedType, JavaType valueType) {
        // if replace operation, this should fail if the expectedType is an array and the valueType isn't
        if (!valueType.isCollectionLikeType() && !valueType.isArrayType()) {
            throw new IllegalSchemaException(path + " expected to be an array or list like type.");
        }

    }
}
