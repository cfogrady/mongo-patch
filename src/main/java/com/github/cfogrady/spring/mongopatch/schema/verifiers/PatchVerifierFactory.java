package com.github.cfogrady.spring.mongopatch.schema.verifiers;

import com.github.cfogrady.spring.mongopatch.operations.OperationType;

public class PatchVerifierFactory {

    public static final PatchVerifierFactory defaultInstance = new PatchVerifierFactory();

    private final PatchVerifier addVerifier;
    private final PatchVerifier removeVerifier;
    private final PatchVerifier replaceVerifier;
    private final PatchVerifier copyVerifier;
    private final PatchVerifier moveVerifier;
    private final PatchVerifier testVerifier;

    public PatchVerifierFactory() {
        this(new AddVerifier(), new RemoveVerifier(), new ReplaceVerifier(), new CopyVerifier(),
            new MoveVerifier(), new TestVerifier());
    }

    public PatchVerifierFactory(PatchVerifier addVerifier,
        PatchVerifier removeVerifier,
        PatchVerifier replaceVerifier,
        PatchVerifier copyVerifier,
        PatchVerifier moveVerifier,
        PatchVerifier testVerifier) {
        this.addVerifier = addVerifier;
        this.removeVerifier = removeVerifier;
        this.replaceVerifier = replaceVerifier;
        this.copyVerifier = copyVerifier;
        this.moveVerifier = moveVerifier;
        this.testVerifier = testVerifier;
    }

    public PatchVerifier getPatchVerifier(OperationType operationType) {
        switch (operationType) {
        case add:
            return addVerifier;
        case copy:
            return copyVerifier;
        case move:
            return moveVerifier;
        case remove:
            return removeVerifier;
        case replace:
            return replaceVerifier;
        case test:
            return testVerifier;
        default:
            throw new UnsupportedOperationException(
                    "Unable to verify " + operationType.name() + " operations.");
        }
    }
}
