package com.github.cfogrady.spring.mongopatch;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.cfogrady.spring.mongopatch.operations.OperationType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class SchemaBuilderTest {

    @Test
    public void testThatBaseSchemaBuildingWorks()
    {
        Map<String, JavaType> schema = TestClass.generateTestSchema();

        Assertions.assertEquals(14, schema.size());

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

    public static class TestClass {
        public String field;
        int number;
        @SuppressWarnings("unused")
        private Integer otherNumber;
        protected List<String> simpleList;
        int[] intArray;
        OtherClass otherClass;
        List<OtherClass> otherClasses;
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
    }
}
