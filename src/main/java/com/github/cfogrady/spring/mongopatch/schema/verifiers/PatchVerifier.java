package com.github.cfogrady.spring.mongopatch.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.spring.mongopatch.operations.PatchOperation;

import java.util.Map;
import java.util.Optional;

public abstract class PatchVerifier {
    abstract public void verify(PatchOperation operation, Map<String, JavaType> schema);

    protected Optional<PathElement> pathIsPartOfSchema(PatchOperation operation,
        Map<String, JavaType> schema) {
        String path = operation.getPath();
        int level = 0;
        if(schema.containsKey(path)) {
            return Optional.of(new PathElement(path, PathType.DIRECT));
        }
        while(path.contains("/")) {
            level++;
            path = path.substring(0, path.lastIndexOf('/'));
            if(schema.containsKey(path)) {
                JavaType type = schema.get(path);
                if(type.isMapLikeType()) {
                    return Optional.of(new PathElement(path, PathType.MAP));
                } else if ((type.isCollectionLikeType() || type.isArrayType()) && level == 1) {
                    return Optional.of(new PathElement(path, PathType.LIST));
                } else {
                    Optional.empty();
                }
            }
        }
        return Optional.empty();
    }
}
