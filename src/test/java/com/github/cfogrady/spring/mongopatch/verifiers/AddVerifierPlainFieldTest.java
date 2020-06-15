package com.github.cfogrady.spring.mongopatch.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.spring.mongopatch.SchemaBuilderTest;
import com.github.cfogrady.spring.mongopatch.SchemaBuilderTest.TestClass;
import com.github.cfogrady.spring.mongopatch.operations.AddOperation;
import com.github.cfogrady.spring.mongopatch.operations.OperationType;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.AddVerifier;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddVerifierPlainFieldTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AddVerifier addVerifier = new AddVerifier();

    @Test
    public void testSetPlainField() {
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/field");
        addOperation.setValue("test");
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testNonExistentField() {
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
    public void testInvalidType() {
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
    public void testEnumType() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/enumType");
        OperationType operation = OperationType.copy;
        addOperation.setValue(operation);
        addVerifier.verify(addOperation, schema);
    }

    public static enum TestEnum {
        TEST1,
        TEST2,
        TEST3;
    }

    @Test
    public void testInvalidEnumType() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/enumType");
        TestEnum type = TestEnum.TEST1;
        addOperation.setValue(type);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Bad enum type");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testSetElementOfDoubleEmbedding() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClassesByKey/myKey/finals/2/finalCountDown");
        String value = "Oh Yeah!";
        addOperation.setValue(value);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetInvalidOfDoubleEmbedding() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClassesByKey/myKey/finals/2/finalCountDown");
        Integer wrongValue = 35;
        addOperation.setValue(wrongValue);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Should not allow Integer in this field");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }
}
