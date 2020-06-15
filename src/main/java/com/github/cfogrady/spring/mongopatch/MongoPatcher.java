package com.github.cfogrady.spring.mongopatch;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.spring.mongopatch.operations.PatchOperation;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.PatchVerifierFactory;

import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Map;

public class MongoPatcher {

    private final Map<String, JavaType> schema;
    private final PatchVerifierFactory patchVerifierFactory;

    public MongoPatcher(Class<?> schemaConstraint) {
        this(schemaConstraint, null);
    }

    public MongoPatcher(Class<?> schemaConstraint, PatchVerifierFactory patchVerifierFactory) {
        if (schemaConstraint != null) {
            schema = SchemaBuilder.buildSchema(schemaConstraint);
        } else {
            schema = null;
        }
        if (patchVerifierFactory != null) {
            this.patchVerifierFactory = patchVerifierFactory;
        } else {
            this.patchVerifierFactory = PatchVerifierFactory.defaultInstance;
        }
    }

    public Update convertToUpdate(List<PatchOperation> patch, Object originalEntity) {
        return convertToUpdate(patch, originalEntity, schema != null);
    }

    public Update convertToUpdate(List<PatchOperation> patch, Object originalEntity,
        boolean constrainBySchema) {
        if (constrainBySchema) {
            verifyAgainstSchema(patch, originalEntity);
        }
        Update update = new Update();
        return update;
    }

    public void verifyAgainstSchema(List<PatchOperation> patch, Object originalEntity) {
        if (schema == null) {
            throw new UnsupportedOperationException(
                    "Cannot verify against a schema if none have been specified");
        }
        if (originalEntity == null) {
            throw new IllegalArgumentException("OriginalEntity must be present for test verification");
        }
        for (PatchOperation operation : patch) {
            patchVerifierFactory.getPatchVerifier(operation.getOperationType()).verify(operation, schema);

        }

    }
}
