package com.github.cfogrady.spring.mongopatch.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.spring.mongopatch.SchemaBuilderTest;
import com.github.cfogrady.spring.mongopatch.SchemaBuilderTest.TestClass;
import com.github.cfogrady.spring.mongopatch.operations.AddOperation;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.AddVerifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AddVerifierOtherClassListTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AddVerifier addVerifier = new AddVerifier();

    @Test
    public void testAddToOtherClassList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClasses");
        SchemaBuilderTest.OtherClass otherClass = new SchemaBuilderTest.OtherClass();
        addOperation.setValue(otherClass);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetElementOfList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClasses/1");
        SchemaBuilderTest.OtherClass otherClass = new SchemaBuilderTest.OtherClass();
        addOperation.setValue(otherClass);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetInvalidToElementOfList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClasses/1");
        String wrongValue = "wrongValue";
        addOperation.setValue(wrongValue);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Should not allow Strings in this list");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testAddToElementOfList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClasses/1/otherField");
        String value = "test";
        addOperation.setValue(value);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testAddInvalidToElementOfList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClasses/1/otherField");
        int value = 5;
        addOperation.setValue(value);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Bad type to be added");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }
}
