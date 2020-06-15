package com.github.cfogrady.spring.mongopatch;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.cfogrady.spring.mongopatch.schema.IllegalSchemaException;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import org.springframework.data.annotation.TypeAlias;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

class SchemaBuilder {

    static final JavaType TYPE_ALIAS = TypeFactory.defaultInstance().constructType(TypeAlias.class);

    private static boolean typeHasMoreSchema(JavaType type) {
        if (type.isPrimitive()) {
            return false;
        } else if (type.isArrayType()) {
            return false;
        } else if (type.isTypeOrSubTypeOf(String.class)) {
            return false;
        } else if (type.isCollectionLikeType()) {
            return false;
        } else if (type.isMapLikeType()) {
            return false;
        } else if (type.isTypeOrSubTypeOf(Number.class)) {
            return false;
        } else if (type.isTypeOrSubTypeOf(Character.class)) {
            return false;
        } else if (type.isEnumType()) {
            return false;
        }
        return true;
    }

    private static boolean typeWithSubSchema(JavaType type) {
        if (type.hasContentType() && typeHasMoreSchema(type.getContentType())) {
            return true;
        }
        return false;
    }

    static Map<String, JavaType> buildSchema(Class<?> schemaConstraint) {
        Map<Type, List<String>> pathsOfJavaTypes = new HashMap<>();
        Map<String, JavaType> schema = new HashMap<>();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        TypeAlias typeAlias = schemaConstraint.getAnnotation(TypeAlias.class);
        if (typeAlias != null) {
            schema.put(typeAlias.value(), TYPE_ALIAS);
        }
        Queue<PathAndFields> pathsWithFields = Queues
            .<PathAndFields>newLinkedBlockingQueue();
        pathsWithFields.add(new PathAndFields("", schemaConstraint.getDeclaredFields()));
        pathsOfJavaTypes.put(schemaConstraint, Lists.newArrayList(""));

        while (!pathsWithFields.isEmpty()) {
            PathAndFields pathAndFields = pathsWithFields.poll();
            for (Field field : pathAndFields.fields) {
                String fieldName = "/" + field.getName();
                String newPath = pathAndFields.path + fieldName;
                JavaType fieldType;
                verifyNoTypeRecursion(pathsOfJavaTypes, field.getGenericType(), pathAndFields.path, newPath);
                pathsOfJavaTypes.get(field.getGenericType()).add(newPath);
                fieldType = typeFactory.constructType(field.getGenericType());
                if (typeWithSubSchema(fieldType)) {
                    pathsWithFields.add(
                        new PathAndFields(newPath + "/*",
                                fieldType.getContentType().getRawClass().getDeclaredFields()));
                } else if (typeHasMoreSchema(fieldType)) {
                    pathsWithFields.add(
                        new PathAndFields(newPath,
                                field.getType().getDeclaredFields()));
                }
                schema.put(newPath, fieldType);
            }
        }
        return schema;
    }

    private final static void verifyNoTypeRecursion(Map<Type, List<String>> pathsByType, Type newType,
        String parentPath, String newPath) {
        if (!pathsByType.containsKey(newType)) {
            pathsByType.put(newType, new ArrayList<>());
        } else {
            List<String> paths = pathsByType.get(newType);
            for (String path : paths) {
                if (parentPath.startsWith(path)) {
                    throw new IllegalSchemaException(
                            "Recursion detected for " + newType + " at " + newPath);
                }
            }
        }
    }

    private static class PathAndFields {
        public final String path;
        public final Field[] fields;

        public PathAndFields(String path, Field[] fields) {
            this.path = path;
            this.fields = fields;
        }
    }
}
