package com.github.cfogrady.spring.mongopatch.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.spring.mongopatch.SchemaBuilderTest.TestClass;
import com.github.cfogrady.spring.mongopatch.operations.RemoveOperation;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.RemoveVerifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RemoveVerifierTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RemoveVerifier removeVerifier = new RemoveVerifier();

    @Test
    public void testRemoveList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        RemoveOperation removeOperation = new RemoveOperation();
        removeOperation.setPath("/simpleList");
        removeVerifier.verify(removeOperation, schema);
    }

    @Test
    public void testRemoveInvalidField() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        RemoveOperation removeOperation = new RemoveOperation();
        removeOperation.setPath("/fakeList");
        try {
            removeVerifier.verify(removeOperation, schema);
            Assertions.fail("This field doesn't exist. Cannot remove it.");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testRemovePrimitiveField() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        RemoveOperation removeOperation = new RemoveOperation();
        removeOperation.setPath("/number");
        try {
            removeVerifier.verify(removeOperation, schema);
            Assertions.fail("This field cannot be nulled. Cannot be removed.");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }
}
