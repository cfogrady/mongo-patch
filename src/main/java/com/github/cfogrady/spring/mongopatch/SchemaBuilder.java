package com.github.cfogrady.spring.mongopatch;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Queues;

import org.springframework.data.annotation.TypeAlias;

import java.lang.reflect.Field;
import java.util.HashMap;
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

    static Map<String, JavaType> buildSchema(Class<?> schemaConstraint) {
        Map<String, JavaType> schema = new HashMap<>();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        TypeAlias typeAlias = schemaConstraint.getAnnotation(TypeAlias.class);
        if (typeAlias != null) {
            schema.put(typeAlias.value(), TYPE_ALIAS);
        }
        Queue<PathAndFields> pathsWithFields = Queues
            .<PathAndFields>newLinkedBlockingQueue();
        pathsWithFields.add(new PathAndFields("", schemaConstraint.getDeclaredFields()));

        while (!pathsWithFields.isEmpty()) {
            PathAndFields pathAndFields = pathsWithFields.poll();
            for (Field field : pathAndFields.fields) {
                String fieldName = "/" + field.getName();
                JavaType fieldType;
                fieldType = typeFactory.constructType(field.getGenericType());
                /*if(Collection.class.isAssignableFrom(field.getType())) {
                    fieldType = typeFactory.constructCollectionType(field.getType(), field.getG)
                } else {
                    
                }*/
                if(typeHasMoreSchema(fieldType)) {
                    pathsWithFields.add(
                        new PathAndFields(pathAndFields.path + fieldName,
                                field.getType().getDeclaredFields()));
                }
                schema.put(pathAndFields.path + fieldName, fieldType);
            }
        }
        return schema;
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
