package com.github.cfogrady.mongopatch.core.schema.verifiers;

import com.github.cfogrady.mongopatch.core.operations.OperationType;

/**
 * Contains all the verifiers for various patch operations.
 *
 */
public class PatchVerifierContainer {

    public static final PatchVerifierContainer defaultInstance = new PatchVerifierContainer();

    private final PatchVerifier addVerifier;
    private final PatchVerifier removeVerifier;
    private final PatchVerifier replaceVerifier;
    private final PatchVerifier copyVerifier;
    private final PatchVerifier moveVerifier;
    private final PatchVerifier testVerifier;

    public PatchVerifierContainer() {
        this(new AddVerifier(), new RemoveVerifier(), new ReplaceVerifier(), new CopyVerifier(),
            new MoveVerifier(), new TestVerifier());
    }

    public PatchVerifierContainer(PatchVerifier addVerifier,
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
