package com.github.cfogrady.spring.mongopatch.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.spring.mongopatch.SchemaBuilderTest.TestClass;
import com.github.cfogrady.spring.mongopatch.operations.MoveOperation;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.MoveVerifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MoveVerifierTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static MoveVerifier moveVerifier;

    @BeforeAll
    public static void setup() {
        moveVerifier = new MoveVerifier();
    }

    @Test
    public void testThatCopyWorks() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        MoveOperation operation = new MoveOperation();
        operation.setPath("/field");
        operation.setFrom("/otherClass/otherField");
        moveVerifier.verify(operation, schema);
    }

    @Test
    public void testThatCopyWorksAgainstSubElements() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        MoveOperation operation = new MoveOperation();
        operation.setPath("/otherClasses/1/otherField");
        operation.setFrom("/otherClasses/2/otherField");
        moveVerifier.verify(operation, schema);
    }

    @Test
    public void testThatCopyFailsForInvalidFieldType() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        MoveOperation operation = new MoveOperation();
        operation.setPath("/field");
        operation.setFrom("/otherClass/otherNumber");
        try {
            moveVerifier.verify(operation, schema);
            Assertions.fail("Cannot copy an integer into a string!");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testThatMoveFailsForNonNullableFieldType() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        MoveOperation operation = new MoveOperation();
        operation.setPath("/otherNumber");
        operation.setFrom("/number");
        try {
            moveVerifier.verify(operation, schema);
            Assertions.fail("Cannot copy an integer into a string!");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }
}
