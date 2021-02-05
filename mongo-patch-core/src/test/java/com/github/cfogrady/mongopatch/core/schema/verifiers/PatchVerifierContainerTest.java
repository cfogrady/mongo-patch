package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.github.cfogrady.mongopatch.core.operations.OperationType;
import com.github.cfogrady.mongopatch.core.schema.verifiers.AddVerifier;
import com.github.cfogrady.mongopatch.core.schema.verifiers.CopyVerifier;
import com.github.cfogrady.mongopatch.core.schema.verifiers.MoveVerifier;
import com.github.cfogrady.mongopatch.core.schema.verifiers.PatchVerifier;
import com.github.cfogrady.mongopatch.core.schema.verifiers.PatchVerifierContainer;
import com.github.cfogrady.mongopatch.core.schema.verifiers.RemoveVerifier;
import com.github.cfogrady.mongopatch.core.schema.verifiers.ReplaceVerifier;
import com.github.cfogrady.mongopatch.core.schema.verifiers.TestVerifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PatchVerifierContainerTest {

    private static PatchVerifierContainer patchVerifierContainer;

    @BeforeAll
    public static void setup() {
        patchVerifierContainer = new PatchVerifierContainer();
    }

    @Test
    public void testAddOperation() {
        PatchVerifier patchVerifier = patchVerifierContainer.getPatchVerifier(OperationType.add);
        Assertions.assertEquals(AddVerifier.class, patchVerifier.getClass());
    }

    @Test
    public void testReplaceOperation() {
        PatchVerifier patchVerifier = patchVerifierContainer.getPatchVerifier(OperationType.replace);
        Assertions.assertEquals(ReplaceVerifier.class, patchVerifier.getClass());
    }

    @Test
    public void testCopyOperation() {
        PatchVerifier patchVerifier = patchVerifierContainer.getPatchVerifier(OperationType.copy);
        Assertions.assertEquals(CopyVerifier.class, patchVerifier.getClass());
    }

    @Test
    public void testMoveOperation() {
        PatchVerifier patchVerifier = patchVerifierContainer.getPatchVerifier(OperationType.move);
        Assertions.assertEquals(MoveVerifier.class, patchVerifier.getClass());
    }

    @Test
    public void testRemoveOperation() {
        PatchVerifier patchVerifier = patchVerifierContainer.getPatchVerifier(OperationType.remove);
        Assertions.assertEquals(RemoveVerifier.class, patchVerifier.getClass());
    }

    @Test
    public void testTestOperation() {
        PatchVerifier patchVerifier = patchVerifierContainer.getPatchVerifier(OperationType.test);
        Assertions.assertEquals(TestVerifier.class, patchVerifier.getClass());
    }
}
