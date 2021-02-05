package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddVerifier extends ValueVerifier {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void verifyValueIsCorrectType(String path, JavaType expectedType, JavaType valueType) {
        if (!valueType.isCollectionLikeType() && !valueType.isArrayType()) {
            if (expectedType.hasContentType() && !expectedType.getContentType().isJavaLangObject()) {
                JavaType contentType = expectedType.getContentType();
                if (!ClassUtils.isAssignable(valueType.getRawClass(), contentType.getRawClass(), true)) {
                    throw new IllegalSchemaException(
                            path + " expected to be an array or list like type.");
                }
            } else {
                logger.debug("Adding {} to list or array of objects", valueType.getRawClass());
            }
        }

    }

}
