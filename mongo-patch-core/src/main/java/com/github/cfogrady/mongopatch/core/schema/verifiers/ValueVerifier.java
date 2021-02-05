package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.cfogrady.mongopatch.core.operations.OperationWithValue;
import com.github.cfogrady.mongopatch.core.operations.PatchOperation;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class ValueVerifier extends PatchVerifier {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void verify(PatchOperation operation, Map<String, JavaType> schema) {
        String definedPath = this.pathIsPartOfSchema(operation.getPath(), schema);

        boolean operatingOnElement = definedPath.endsWith("/*");
        String schemaPath = getSchemaPath(operatingOnElement, definedPath);

        JavaType definedSchemaType = schema.get(schemaPath);
        logger.debug("Operating using {}, against {}. Should be using: {}", operation.getPath(),
            definedPath, definedSchemaType.getRawClass());

        verifyValueOperation(operation, definedSchemaType, operatingOnElement, definedPath);
    }

    protected void verifyValueOperation(PatchOperation operation, JavaType definedSchemaType,
        boolean operatingOnElement, String definedPath) {
        OperationWithValue operationWithValue = (OperationWithValue) (operation);
        if (definedSchemaType.hasContentType()
                && operatingOnElement) {
            JavaType elementType = definedSchemaType.getContentType();
            verifySingleElementTypeMatch(definedPath, elementType, operationWithValue.getValue());
        } else {
            //primitive or Direct Class
            verifySingleElementTypeMatch(definedPath, definedSchemaType, operationWithValue.getValue());
        }
    }

    private void verifySingleElementTypeMatch(String path, JavaType expectedType, Object value) {
        Class<?> valueType = value.getClass();
        if (expectedType.isCollectionLikeType() || expectedType.isArrayType()) {
            verifyCollectionOrArrayElement(path, expectedType, value);
        } else if (!ClassUtils.isAssignable(valueType, expectedType.getRawClass(), true)) {
            throw new IllegalSchemaException(
                    path + " should be a " + expectedType.getRawClass()
                            + " but received " + valueType);
        }
    }

    private void verifyCollectionOrArrayElement(String path, JavaType expectedType, Object value) {
        Class<?> valueType = value.getClass();
        JavaType valueJavaType = TypeFactory.defaultInstance().constructType(valueType);

        // Verify value type is list or collection, or if not that element is of correct type for schema
        verifyValueIsCorrectType(path, expectedType, valueJavaType);

        // verify that objects match list type if setting a list to a list.
        if (elementsNeedVerified(expectedType)) {
            verifyElementsOfContentListMatch(path, expectedType, valueJavaType, valueType, value);
        }
    }

    abstract protected void verifyValueIsCorrectType(String path, JavaType expectedType,
        JavaType valueJavaType);

    private boolean elementsNeedVerified(JavaType expectedType) {
        JavaType contentContentType = expectedType.getContentType();
        return ((expectedType.hasGenericTypes() || expectedType.isArrayType())
                && !contentContentType.isJavaLangObject());
    }

    private void verifyElementsOfContentListMatch(String path, JavaType expectedType, JavaType valueJavaType,
        Class<?> valueType, Object value) {
        JavaType contentContentType = expectedType.getContentType();
        if (valueJavaType.isCollectionLikeType()) {
            for (Object obj : ((Iterable<?>) value)) {
                if (!ClassUtils.isAssignable(obj.getClass(), contentContentType.getRawClass(), true)) {
                    throw new IllegalSchemaException(
                            path + " expected to have elements of type " + contentContentType.getRawClass()
                                    + " but received " + obj.getClass());
                }
            }
        } else if (valueJavaType.isArrayType()) {
            for (Object obj : ((Object[]) value)) {
                if (!ClassUtils.isAssignable(obj.getClass(), contentContentType.getRawClass(), true)) {
                    throw new IllegalSchemaException(
                            path + " expected to have elements of type " + contentContentType.getRawClass()
                                    + " but received " + obj.getClass());
                }
            }
        }
    }
}
