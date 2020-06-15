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

public class AddVerifierObjectListTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AddVerifier addVerifier = new AddVerifier();

    @Test
    public void testAddToObjectList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/listOfAnything");
        SchemaBuilderTest.OtherClass otherClass = new SchemaBuilderTest.OtherClass();
        addOperation.setValue(otherClass);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetElementOfList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/listOfAnything/1");
        SchemaBuilderTest.OtherClass otherClass = new SchemaBuilderTest.OtherClass();
        addOperation.setValue(otherClass);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetAnythingToElementOfList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/listOfAnything/1");
        String wrongValue = "wrongValue";
        addOperation.setValue(wrongValue);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testSetToElementOfList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/listOfAnything/1/otherField");
        String value = "test";
        addOperation.setValue(value);
        addVerifier.verify(addOperation, schema);
    }

    @Test
    public void testAddToElementOfList() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();
        AddOperation addOperation = new AddOperation();
        addOperation.setPath("/listOfAnything/1/otherField");
        int value = 5;
        addOperation.setValue(value);
        addVerifier.verify(addOperation, schema);
    }
}
