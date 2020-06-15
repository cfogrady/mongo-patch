package com.github.cfogrady.spring.mongopatch.verifiers;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cfogrady.spring.mongopatch.SchemaBuilderTest.TestClass;
import com.github.cfogrady.spring.mongopatch.operations.AddOperation;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.AddVerifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AddVerifierMapFieldTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AddVerifier addVerifier = new AddVerifier();

    @Test
    public void testSetElementOfArbitraryTypeMap() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/misc/myKey");
        String testValue = "We'll see";
        addOperation.setValue(testValue);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetArbitraryTypeMap() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/misc");
        Map<String, Object> testValue = new HashMap<>();
        addOperation.setValue(testValue);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetInvalidForMapField() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/misc");
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
    public void testAddToElementOfArbitraryTypeMap() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/misc/myKey/key1");
        String testValue = "We'll see";
        addOperation.setValue(testValue);
        addVerifier.verify(addOperation, schema);
    }
}
