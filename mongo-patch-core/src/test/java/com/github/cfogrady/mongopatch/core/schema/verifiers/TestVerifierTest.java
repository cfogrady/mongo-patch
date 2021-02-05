package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.mongopatch.core.SchemaBuilderTest.TestClass;
import com.github.cfogrady.mongopatch.core.operations.TestOperation;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;
import com.github.cfogrady.mongopatch.core.schema.verifiers.TestVerifier;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestVerifierTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private TestVerifier testVerifier = new TestVerifier();

    @Test
    public void testValueAtList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        TestOperation testOperation = new TestOperation();
        testOperation.setPath("/simpleList");
        List<String> myList = Lists.newArrayList("Test1", "Test2");
        testOperation.setValue(myList);
        testVerifier.verify(testOperation, schema);
    }

    @Test
    public void testArrayValuesAgainstList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        TestOperation testOperation = new TestOperation();
        testOperation.setPath("/simpleList");
        String[] myList = { "Test1", "Test2" };
        testOperation.setValue(myList);
        testVerifier.verify(testOperation, schema);
    }

    @Test
    public void testThatElementCannotBeTestedAgainstList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        TestOperation testOperation = new TestOperation();
        testOperation.setPath("/simpleList");
        String newElement = "newElement";
        testOperation.setValue(newElement);
        try {
            testVerifier.verify(testOperation, schema);
            Assertions.fail("Replace should not accept adding elements to a list");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testInvalidListValue() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        TestOperation testOperation = new TestOperation();
        testOperation.setPath("/simpleList");
        Map<String, String> myList = new HashMap<>();
        myList.put("key", "value");
        testOperation.setValue(myList);
        try {
            testVerifier.verify(testOperation, schema);
            Assertions.fail("Expected to fail");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testIncorrectListElement() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        TestOperation testOperation = new TestOperation();
        testOperation.setPath("/simpleList");
        List<?> myList = Lists.newArrayList("Test", 56);
        testOperation.setValue(myList);
        try {
            testVerifier.verify(testOperation, schema);
            Assertions.fail("Expected to fail");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }
}
