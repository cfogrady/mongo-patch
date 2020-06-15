package com.github.cfogrady.spring.mongopatch;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.cfogrady.spring.mongopatch.operations.OperationType;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class SchemaBuilderTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testThatBaseSchemaBuildingWorks()
    {
        Map<String, JavaType> schema = TestClass.generateTestSchema();

        Assertions.assertEquals(29, schema.size());

        Assertions.assertTrue(schema.containsKey("/field"));
        Assertions.assertEquals(TypeFactory.defaultInstance().constructType(String.class),
            schema.get("/field"));
        Assertions.assertTrue(schema.containsKey("/number"));
        Assertions.assertEquals(TypeFactory.defaultInstance().constructType(int.class),
            schema.get("/number"));
        Assertions.assertTrue(schema.containsKey("/otherNumber"));
        Assertions.assertEquals(TypeFactory.defaultInstance().constructType(Integer.class),
            schema.get("/otherNumber"));
        Assertions.assertTrue(schema.containsKey("/simpleList"));
        Assertions.assertEquals(
            TypeFactory.defaultInstance().constructCollectionType(List.class, String.class),
            schema.get("/simpleList"));
        Assertions.assertTrue(schema.containsKey("/intArray"));
        Assertions.assertEquals(
            TypeFactory.defaultInstance().constructType(int[].class),
            schema.get("/intArray"));
        Assertions.assertTrue(schema.containsKey("/otherClass"));
        Assertions.assertEquals(
            TypeFactory.defaultInstance().constructType(OtherClass.class),
            schema.get("/otherClass"));
        Assertions.assertTrue(schema.containsKey("/otherClasses"));
        Assertions.assertEquals(
            TypeFactory.defaultInstance().constructCollectionType(List.class, OtherClass.class),
            schema.get("/otherClasses"));
        Assertions.assertTrue(schema.containsKey("/otherClassesByKey"));
        Assertions.assertEquals(
            TypeFactory.defaultInstance().constructMapLikeType(Map.class, String.class, OtherClass.class),
            schema.get("/otherClassesByKey"));
        Assertions.assertTrue(schema.containsKey("/otherClassArray"));
        Assertions.assertEquals(
            TypeFactory.defaultInstance().constructType(OtherClass[].class),
            schema.get("/otherClassArray"));
        Assertions.assertTrue(schema.containsKey("/stringArray"));
        Assertions.assertEquals(
            TypeFactory.defaultInstance().constructType(String[].class),
            schema.get("/stringArray"));
        Assertions.assertTrue(schema.containsKey("/misc"));
        Assertions.assertEquals(
            TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class),
            schema.get("/misc"));

        Assertions.assertTrue(schema.containsKey("/enumType"));
        Assertions.assertEquals(
            TypeFactory.defaultInstance().constructType(OperationType.class),
            schema.get("/enumType"));
    }

    @Test
    public void testThatEmbeddedSchemaWorks() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();

        Assertions.assertTrue(schema.containsKey("/otherClass/otherField"));
        Assertions.assertEquals(TypeFactory.defaultInstance().constructType(String.class),
            schema.get("/otherClass/otherField"));
    }

    @Test
    public void testWildcardPathForListInSchema() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();

        Assertions.assertTrue(schema.containsKey("/otherClasses/*/otherField"));
        Assertions.assertEquals(TypeFactory.defaultInstance().constructType(String.class),
            schema.get("/otherClasses/*/otherField"));
    }

    @Test
    public void testWildcardPathForArrayInSchema() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();

        Assertions.assertTrue(schema.containsKey("/otherClassArray/*/otherField"));
        Assertions.assertEquals(TypeFactory.defaultInstance().constructType(String.class),
            schema.get("/otherClassArray/*/otherField"));
    }

    @Test
    public void testWildcardPathForMapInSchema() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();

        Assertions.assertTrue(schema.containsKey("/otherClassesByKey/*/otherField"));
        Assertions.assertEquals(TypeFactory.defaultInstance().constructType(String.class),
            schema.get("/otherClassesByKey/*/otherField"));
    }

    @Test
    public void testDoubleWildcardPathForEmbed() {
        Map<String, JavaType> schema = TestClass.generateTestSchema();

        Assertions.assertTrue(schema.containsKey("/otherClassesByKey/*/finals/*/finalCountDown"));
        Assertions.assertEquals(TypeFactory.defaultInstance().constructType(String.class),
            schema.get("/otherClassesByKey/*/finals/*/finalCountDown"));
    }

    @Test
    public void testRecursiveSchema() {
        try {
            SchemaBuilder.buildSchema(Recursive.class);
            Assertions.fail("Haha... Like we would get here even if it worked");
        } catch (IllegalSchemaException ise) {
            logger.info("Cannot handle recursive. Correctly threw: ", ise);
        }
    }

    public static class TestClass {
        public String field;
        int number;
        @SuppressWarnings("unused")
        private Integer otherNumber;
        protected List<String> simpleList;
        int[] intArray;
        OtherClass otherClass;
        List<OtherClass> otherClasses;
        List<Object> listOfAnything;
        Map<String, OtherClass> otherClassesByKey;
        OtherClass[] otherClassArray;
        String[] stringArray;
        Map<String, Object> misc;
        OperationType enumType;
        public static Map<String, JavaType> generateTestSchema() {
            return SchemaBuilder.buildSchema(TestClass.class);
        }
    }

    public static class OtherClass {
        String otherField;
        int otherNumber;
        List<FinalClass> finals;
    }

    public static class FinalClass {
        String finalCountDown;
    }

    public static class Recursive {
        String hello;
        Recursive blowItUp;
    }
}
