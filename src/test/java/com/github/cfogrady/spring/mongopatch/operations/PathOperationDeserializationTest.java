package com.github.cfogrady.spring.mongopatch.operations;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cfogrady.spring.mongopatch.operations.OperationWithValue;
import com.github.cfogrady.spring.mongopatch.operations.OperationType;
import com.github.cfogrady.spring.mongopatch.operations.PatchOperation;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathOperationDeserializationTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testParseFromString() throws JsonMappingException, JsonProcessingException {
        String jsonString = "["
                + "{\"op\": \"add\", \"path\": \"/hello\", \"value\": \"world\"},"
                + "{\"op\": \"add\", \"path\": \"/array\", \"value\": [\"first\", \"second\"]},"
                + "{\"op\": \"add\", \"path\": \"/object\", \"value\": {\"key\": \"value\"}},"
                + "{\"op\": \"remove\", \"path\": \"/erase\"},"
                + "{\"op\": \"replace\", \"path\": \"/setting\", \"value\": \"America\"},"
                + "{\"op\": \"copy\", \"from\": \"/original\", \"path\": \"/new\"},"
                + "{\"op\": \"move\", \"from\": \"/old\", \"path\": \"/fresh\"},"
                + "{\"op\": \"test\", \"path\": \"/setting\", \"value\": \"America\"}"
                + "]";
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, PatchOperation.class);
        List<PatchOperation> patchOperations = objectMapper.readValue(jsonString, type);
        Assertions.assertEquals(8, patchOperations.size());
        logger.info("Verifying first operation.");
        verifyPatchOperation(OperationType.add, "/hello", "world", null, patchOperations.get(0));
        logger.info("Verifying second operation.");
        verifyPatchOperation(OperationType.add, "/array", Lists.newArrayList("first", "second"), null,
            patchOperations.get(1));
        logger.info("Verifying third operation.");
        Map<String, Object> object = new HashMap<>();
        object.put("key", "value");
        verifyPatchOperation(OperationType.add, "/object", object, null,
            patchOperations.get(2));
        logger.info("Verifying fourth operation.");
        verifyPatchOperation(OperationType.remove, "/erase", null, null, patchOperations.get(3));
        logger.info("Verifying fifth operation.");
        verifyPatchOperation(OperationType.replace, "/setting", "America", null, patchOperations.get(4));
        logger.info("Verifying sixth operation.");
        verifyPatchOperation(OperationType.copy, "/new", null, "/original", patchOperations.get(5));
        logger.info("Verifying seventh operation.");
        verifyPatchOperation(OperationType.move, "/fresh", null, "/old", patchOperations.get(6));
        logger.info("Verifying eighth operation.");
        verifyPatchOperation(OperationType.test, "/setting", "America", null, patchOperations.get(7));
        logger.info("Everything works!");
    }

    public void verifyPatchOperation(
        OperationType operationExpected,
        String pathExpected,
        Object valueExpected,
        String fromExpected,
        PatchOperation instruction) {
        OperationType actualOperation = instruction.getOperationType();
        Assertions.assertEquals(operationExpected, actualOperation, "Operation mismatch!");
        Assertions.assertEquals(pathExpected, instruction.getPath(), "Path mismatch!");
        Assertions.assertEquals(valueExpected != null, actualOperation.hasValue,
            "Value Expectation mismatch!");
        if (actualOperation.hasValue) {
            Assertions.assertEquals(valueExpected, ((OperationWithValue) instruction).getValue(),
                "Value mismatch!");
        }
        Assertions.assertEquals(fromExpected != null, actualOperation.hasFrom,
            "From Expectation mismatch!");
        if (actualOperation.hasFrom) {
            Assertions.assertEquals(fromExpected, ((OperationWithFrom) instruction).getFrom(),
                "From mismatch!");
        }

    }
}
