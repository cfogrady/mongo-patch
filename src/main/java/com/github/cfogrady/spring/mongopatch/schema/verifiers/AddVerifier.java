package com.github.cfogrady.spring.mongopatch.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.cfogrady.spring.mongopatch.operations.AddOperation;
import com.github.cfogrady.spring.mongopatch.operations.PatchOperation;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AddVerifier extends PatchVerifier {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void verify(PatchOperation operation, Map<String, JavaType> schema) {
        AddOperation addOperation = (AddOperation) (operation);
        PathElement pathElement = this.pathIsPartOfSchema(addOperation, schema).orElseThrow(
            () -> new IllegalSchemaException(operation.getPath() + " is not part of the defined schema!"));
        logger.debug("Placing element {}, against {}. Should be using: {}", operation.getPath(),
            pathElement.path, pathElement.pathType);
        JavaType definedSchemaType = schema.get(pathElement.path);
        Class<?> valueType = addOperation.getValue().getClass();
        if (pathElement.pathType == PathType.LIST) {
            JavaType elementType = definedSchemaType.getContentType();
            verifySingleElementTypeMatch(pathElement.path, elementType, addOperation.getValue());
        } else if (pathElement.pathType == PathType.MAP) {

        } else {
            //primitive or Direct Class
            verifySingleElementTypeMatch(pathElement.path, definedSchemaType, addOperation.getValue());
        }

    }

    private void verifySingleElementTypeMatch(String path, JavaType expectedType, Object value) {
        Class<?> valueType = value.getClass();
        if (expectedType.isPrimitive()) {
            if (!ClassUtils.isAssignable(expectedType.getRawClass(), valueType, true)) {
                throw new IllegalSchemaException(
                        path + " should be a " + expectedType.getRawClass()
                                + " but received " + valueType);
            }
        } else if (expectedType.isCollectionLikeType() || expectedType.isArrayType()) {
            JavaType valueJavaType = TypeFactory.defaultInstance().constructType(valueType);
            if (!valueJavaType.isCollectionLikeType() && !valueJavaType.isArrayType()) {
                if (expectedType.hasContentType()) {
                    JavaType contentType = expectedType.getContentType();
                    if (!ClassUtils.isAssignable(valueType, contentType.getRawClass(), true)) {
                        throw new IllegalSchemaException(
                                path + " expected to be an array or list like type.");
                    }
                } else {
                    logger.debug("Adding {} to list or array of objects", valueType);
                }
            }
            JavaType contentType = expectedType.getContentType();
            if ((expectedType.hasGenericTypes() || expectedType.isArrayType())
                    && !contentType.isJavaLangObject()) {
                if (valueJavaType.isCollectionLikeType()) {
                    for (Object obj : ((Iterable<?>) value)) {
                        if (!ClassUtils.isAssignable(obj.getClass(), contentType.getRawClass(), true)) {
                            throw new IllegalSchemaException(
                                    path + " expected to have elements of type " + contentType.getRawClass()
                                            + " but received " + valueType);
                        }
                    }
                } else if (valueJavaType.isArrayType()) {
                    for (Object obj : ((Object[]) value)) {
                        if (!ClassUtils.isAssignable(obj.getClass(), contentType.getRawClass(), true)) {
                            throw new IllegalSchemaException(
                                    path + " expected to have elements of type " + contentType.getRawClass()
                                            + " but received " + valueType);
                        }
                    }
                }
            }
        } else if (!ClassUtils.isAssignable(valueType, expectedType.getRawClass(), true)) {
            throw new IllegalSchemaException(
                    path + " should be a " + expectedType.getRawClass()
                            + " but received " + valueType);
        }
    }
}
