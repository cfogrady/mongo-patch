package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.mongopatch.core.SchemaBuilderTest.TestClass;
import com.github.cfogrady.mongopatch.core.operations.ReplaceOperation;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;
import com.github.cfogrady.mongopatch.core.schema.verifiers.ReplaceVerifier;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplaceVerifierListFieldTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ReplaceVerifier replaceVerifier = new ReplaceVerifier();

    @Test
    public void testSetList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        ReplaceOperation replaceOperation = new ReplaceOperation();
        replaceOperation.setPath("/simpleList");
        List<String> myList = Lists.newArrayList("Test1", "Test2");
        replaceOperation.setValue(myList);
        replaceVerifier.verify(replaceOperation, schema);
    }

    @Test
    public void testArrayToList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        ReplaceOperation replaceOperation = new ReplaceOperation();
        replaceOperation.setPath("/simpleList");
        String[] myList = { "Test1", "Test2" };
        replaceOperation.setValue(myList);
        replaceVerifier.verify(replaceOperation, schema);
    }

    @Test
    public void testThatNewElementCannotBeAddedToList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        ReplaceOperation replaceOperation = new ReplaceOperation();
        replaceOperation.setPath("/simpleList");
        String newElement = "newElement";
        replaceOperation.setValue(newElement);
        try {
            replaceVerifier.verify(replaceOperation, schema);
            Assertions.fail("Replace should not accept adding elements to a list");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testBadListReplacementt() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        ReplaceOperation replaceOperation = new ReplaceOperation();
        replaceOperation.setPath("/simpleList");
        Map<String, String> myList = new HashMap<>();
        myList.put("key", "value");
        replaceOperation.setValue(myList);
        try {
            replaceVerifier.verify(replaceOperation, schema);
            Assertions.fail("Expected to fail");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testIncorrectListElement() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        ReplaceOperation replaceOperation = new ReplaceOperation();
        replaceOperation.setPath("/simpleList");
        List<?> myList = Lists.newArrayList("Test", 56);
        replaceOperation.setValue(myList);
        try {
            replaceVerifier.verify(replaceOperation, schema);
            Assertions.fail("Expected to fail");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }
}
