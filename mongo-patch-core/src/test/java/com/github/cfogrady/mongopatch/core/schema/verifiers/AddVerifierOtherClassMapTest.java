package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.mongopatch.core.SchemaBuilderTest;
import com.github.cfogrady.mongopatch.core.SchemaBuilderTest.TestClass;
import com.github.cfogrady.mongopatch.core.operations.AddOperation;
import com.github.cfogrady.mongopatch.core.schema.IllegalSchemaException;
import com.github.cfogrady.mongopatch.core.schema.verifiers.AddVerifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AddVerifierOtherClassMapTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AddVerifier addVerifier = new AddVerifier();

    @Test
    public void testSetElementOfMap() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClassesByKey/myKey");
        SchemaBuilderTest.OtherClass otherClass = new SchemaBuilderTest.OtherClass();
        addOperation.setValue(otherClass);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetMap() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClassesByKey");
        Map<String, SchemaBuilderTest.OtherClass> myMap = new HashMap<>();
        addOperation.setValue(myMap);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetInvalidForMapField() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClassesByKey");
        String test = "testValue";
        addOperation.setValue(test);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Shouldn't be able to set String in Map");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testSetInvalidToElementOfMap() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClassesByKey/myKey");
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
    public void testAddToElementOfMap() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClassesByKey/myKey/otherField");
        String value = "test";
        addOperation.setValue(value);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testAddInvalidToElementOfMap() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClassesByKey/myKey/otherField");
        int value = 5;
        addOperation.setValue(value);
        try {
            addVerifier.verify(addOperation, schema);
            Assertions.fail("Bad type to be added");
        } catch (IllegalSchemaException ise) {
            logger.info("Correctly threw: ", ise);
        }
    }

    @Test
    public void testAddInvalidFieldToElementOfMap() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/otherClassesByKey/myKey/theOtherFoot");
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
