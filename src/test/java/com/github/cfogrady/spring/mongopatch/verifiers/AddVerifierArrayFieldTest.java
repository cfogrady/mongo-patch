package com.github.cfogrady.spring.mongopatch.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.spring.mongopatch.SchemaBuilderTest.TestClass;
import com.github.cfogrady.spring.mongopatch.operations.AddOperation;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.AddVerifier;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class AddVerifierArrayFieldTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AddVerifier addVerifier = new AddVerifier();

    @Test
    public void testListToArray() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/stringArray");
        List<?> myList = Lists.newArrayList("Test", "Test2");
        addOperation.setValue(myList);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetArray() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/stringArray");
        String[] myList = { "Test", "Test2" };
        addOperation.setValue(myList);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetArrayWithInvalidElement() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/stringArray");
        Object[] myArray = { "Test", "Test2", 5 };
        addOperation.setValue(myArray);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Expected a failure from invalid element type");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly  threw: ", ise);
        }
    }

    @Test
    public void testInvalidToArray() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/stringArray");
        int myList = 5;
        addOperation.setValue(myList);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Expected to fail");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testThatNewElementCanBeAddedToArray() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/stringArray");
        String newElement = "newElement";
        addOperation.setValue(newElement);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testInvalidElementToArray() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/stringArray");
        Object[] myList = { 5, "Test" };
        addOperation.setValue(myList);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Expected to fail");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }
}
