package com.github.cfogrady.spring.mongopatch.verifiers;

import com.github.cfogrady.spring.mongopatch.operations.OperationType;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.AddVerifier;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.CopyVerifier;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.MoveVerifier;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.PatchVerifier;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.PatchVerifierFactory;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.RemoveVerifier;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.ReplaceVerifier;
import com.github.cfogrady.spring.mongopatch.schema.verifiers.TestVerifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PatchVerifierFactoryTest {

    private static PatchVerifierFactory patchVerifierFactory;

    @BeforeAll
    public static void setup() {
        patchVerifierFactory = new PatchVerifierFactory();
    }

    @Test
    public void testAddOperation() {
        PatchVerifier patchVerifier = patchVerifierFactory.getPatchVerifier(OperationType.add);
        Assertions.assertEquals(AddVerifier.class, patchVerifier.getClass());
    }

    @Test
    public void testReplaceOperation() {
        PatchVerifier patchVerifier = patchVerifierFactory.getPatchVerifier(OperationType.replace);
        Assertions.assertEquals(ReplaceVerifier.class, patchVerifier.getClass());
    }

    @Test
    public void testCopyOperation() {
        PatchVerifier patchVerifier = patchVerifierFactory.getPatchVerifier(OperationType.copy);
        Assertions.assertEquals(CopyVerifier.class, patchVerifier.getClass());
    }

    @Test
    public void testMoveOperation() {
        PatchVerifier patchVerifier = patchVerifierFactory.getPatchVerifier(OperationType.move);
        Assertions.assertEquals(MoveVerifier.class, patchVerifier.getClass());
    }

    @Test
    public void testRemoveOperation() {
        PatchVerifier patchVerifier = patchVerifierFactory.getPatchVerifier(OperationType.remove);
        Assertions.assertEquals(RemoveVerifier.class, patchVerifier.getClass());
    }

    @Test
    public void testTestOperation() {
        PatchVerifier patchVerifier = patchVerifierFactory.getPatchVerifier(OperationType.test);
        Assertions.assertEquals(TestVerifier.class, patchVerifier.getClass());
    }
}
