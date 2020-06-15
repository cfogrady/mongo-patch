package com.github.cfogrady.spring.mongopatch.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplaceVerifier extends ValueVerifier {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void verifyValueIsCorrectType(String path, JavaType expectedType, JavaType valueType) {
        // if replace operation, this should fail if the expectedType is an array and the valueType isn't
        if (!valueType.isCollectionLikeType() && !valueType.isArrayType()) {
            throw new IllegalSchemaException(path + " expected to be an array or list like type.");
        }

    }
}
