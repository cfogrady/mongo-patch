package com.github.cfogrady.spring.mongopatch.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.spring.mongopatch.SchemaBuilderTest;
import com.github.cfogrady.spring.mongopatch.SchemaBuilderTest.TestClass;
import com.github.cfogrady.spring.mongopatch.operations.AddOperation;
import com.github.cfogrady.spring.mongopatch.operations.OperationType;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.AddVerifier;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.PathType;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddVerifierTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AddVerifier addVerifier = new AddVerifier();

    @Test
    public void testAddVerifierPlainField() {
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/field");
        addOperation.setValue("test");
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testInvalidField() {
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/madeup");
        addOperation.setValue("test");
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Failed to throw for illegal exception!");
        } catch (IllegalSchemaException ise) {
            logger.info("Correct exception thrown", ise);
        }
    }

    @Test
    public void testInvalidBasicType() {
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/field");
        addOperation.setValue(35);
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Failed to throw for illegal exception!");
        } catch (IllegalSchemaException ise) {
            logger.info("Correct exception thrown", ise);
        }
    }

    @Test
    public void testPrimitiveVsBoxed() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/number");
        addOperation.setValue(Integer.valueOf(35));
        addVerifier.verify(addOperation, schema);
        addOperation.setPath("/otherNumber");
        addOperation.setValue(35);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testIncorrectPrimitive() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/number");
        addOperation.setValue(Long.valueOf(56));
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Failed to throw for illegal exception!");
        } catch (IllegalSchemaException ise) {
            logger.info("Correct exception thrown", ise);
        }
    }

    @Test
    public void testNumberInString() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/field");
        addOperation.setValue(56);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Failed to throw for illegal exception!");
        } catch (IllegalSchemaException ise) {
            logger.info("Correct exception thrown", ise);
        }
    }

    @Test
    public void testOtherClass() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClass");
        SchemaBuilderTest.OtherClass otherClass = new SchemaBuilderTest.OtherClass();
        addOperation.setValue(otherClass);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testAddList() {
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
    public void testArrayToArray() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/stringArray");
        String[] myList = { "Test", "Test2" };
        addOperation.setValue(myList);
        addVerifier.verify(addOperation, schema);
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

    @Test
    public void testEnumType() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/enumType");
        OperationType operation = OperationType.copy;
        addOperation.setValue(operation);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testInvalidEnumType() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/enumType");
        PathType type = PathType.DIRECT;
        addOperation.setValue(type);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Bad enum type");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }
}
