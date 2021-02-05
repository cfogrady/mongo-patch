package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.mongopatch.core.operations.OperationWithFrom;
import com.github.cfogrady.mongopatch.core.operations.PatchOperation;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class FromVerifier extends PatchVerifier {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void verify(PatchOperation operation, Map<String, JavaType> schema) {
        String definedPath = this.pathIsPartOfSchema(operation.getPath(), schema);

        boolean operatingOnElement = definedPath.endsWith("/*");
        String schemaPath = getSchemaPath(operatingOnElement, definedPath);

        JavaType definedSchemaType = schema.get(schemaPath);
        logger.debug("Operating using {}, against {}. Should be using: {}", operation.getPath(),
            definedPath, definedSchemaType.getRawClass());

        // Verify the from
        String fromPath = ((OperationWithFrom) operation).getFrom();
        String definedFromPath = this.pathIsPartOfSchema(fromPath, schema);

        boolean operatingOnFromElement = definedFromPath.endsWith("/*");
        String schemaFromPath = getSchemaPath(operatingOnFromElement, definedFromPath);

        JavaType definedSchemaFromType = schema.get(schemaFromPath);

        if (!ClassUtils.isAssignable(definedSchemaFromType.getRawClass(), definedSchemaType.getRawClass(),
            true)) {
            throw new IllegalSchemaException("From type is different from Path type");
        }
        verifyMorePathAndFrom(definedSchemaFromType);
    }

    protected abstract void verifyMorePathAndFrom(JavaType definedSchemaFromType);
}
