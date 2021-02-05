package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.mongopatch.core.SchemaBuilderTest.TestClass;
import com.github.cfogrady.mongopatch.core.operations.AddOperation;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;
import com.github.cfogrady.mongopatch.core.schema.verifiers.AddVerifier;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddVerifierListFieldTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AddVerifier addVerifier = new AddVerifier();

    @Test
    public void testSetList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/simpleList");
        List<String> myList = Lists.newArrayList("Test1", "Test2");
        addOperation.setValue(myList);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testArrayToList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/simpleList");
        String[] myList = { "Test1", "Test2" };
        addOperation.setValue(myList);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testThatNewElementCanBeAddedToList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/simpleList");
        String newElement = "newElement";
        addOperation.setValue(newElement);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetListWithInvalidElement() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/simpleList");
        List<?> myList = Lists.newArrayList("Test1", "Test2", 5);
        addOperation.setValue(myList);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Expected a failure from invalid element type");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly  threw: ", ise);
        }
    }

    @Test
    public void testBadListReplacementt() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/simpleList");
        Map<String, String> myList = new HashMap<>();
        myList.put("key", "value");
        addOperation.setValue(myList);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Expected to fail");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testIncorrectListElement() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/simpleList");
        List<?> myList = Lists.newArrayList("Test", 56);
        addOperation.setValue(myList);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Expected to fail");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }
}
