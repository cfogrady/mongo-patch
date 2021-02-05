package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.mongopatch.core.SchemaBuilderTest.TestClass;
import com.github.cfogrady.mongopatch.core.operations.CopyOperation;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;
import com.github.cfogrady.mongopatch.core.schema.verifiers.CopyVerifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CopyVerifierTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static CopyVerifier copyVerifier;

    @BeforeAll
    public static void setup() {
        copyVerifier = new CopyVerifier();
    }

    @Test
    public void testThatCopyWorks() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        CopyOperation operation = new CopyOperation();
        operation.setPath("/field");
        operation.setFrom("/otherClass/otherField");
        copyVerifier.verify(operation, schema);
    }

    @Test
    public void testThatCopyWorksAgainstSubElements() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        CopyOperation operation = new CopyOperation();
        operation.setPath("/otherClasses/1/otherField");
        operation.setFrom("/otherClasses/2/otherField");
        copyVerifier.verify(operation, schema);
    }

    @Test
    public void testThatCopyFailsForInvalidFieldType() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        CopyOperation operation = new CopyOperation();
        operation.setPath("/field");
        operation.setFrom("/otherClass/otherNumber");
        try {
            copyVerifier.verify(operation, schema);
            Assertions.fail("Cannot copy an integer into a string!");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }
}
