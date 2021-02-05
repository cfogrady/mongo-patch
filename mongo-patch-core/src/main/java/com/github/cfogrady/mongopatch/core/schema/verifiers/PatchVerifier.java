package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.mongopatch.core.operations.PatchOperation;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;

import java.util.Map;

public abstract class PatchVerifier {

    public abstract void verify(PatchOperation operation, Map<String, JavaType> schema);

    protected String pathIsPartOfSchema(String path,
        Map<String, JavaType> schema) {
        if(schema.containsKey(path)) {
            return path;
        }
        return replaceElementKeysWithWildcard(path, schema);
    }

    private String replaceElementKeysWithWildcard(String path, Map<String, JavaType> schema) {
        String[] pathElements = path.substring(1).split("/");
        StringBuilder currentPathBuilder = new StringBuilder();
        JavaType lastSchemaType = null;
        for (String pathElement : pathElements) {
            String currentPath = currentPathBuilder.toString() + "/" + pathElement;
            if(!schema.containsKey(currentPath)) {
                if(lastSchemaType == null) {
                    throw new IllegalSchemaException(path + " is not part of the defined schema!");
                } else if (lastSchemaType.isArrayType() || lastSchemaType.isCollectionLikeType()
                        || lastSchemaType.isMapLikeType()) {
                    currentPathBuilder.append("/*");
                    if (!lastSchemaType.getContentType().isJavaLangObject()) {
                        lastSchemaType = null;
                    }
                } else {
                    throw new IllegalSchemaException(path + " is not part of the defined schema!");
                }
            } else {
                lastSchemaType = schema.get(currentPath);
                currentPathBuilder.append("/").append(pathElement);
            }
        }
        return currentPathBuilder.toString();
    }

    protected String getSchemaPath(boolean operatingOnElement, String definedPath) {
        String schemaPath;
        if (operatingOnElement) {
            schemaPath = definedPath.substring(0, definedPath.lastIndexOf("/"));
            while (schemaPath.endsWith("/*")) {
                schemaPath = schemaPath.substring(0, schemaPath.lastIndexOf("/"));
            }
        } else {
            schemaPath = definedPath;
        }
        return schemaPath;
    }
}
