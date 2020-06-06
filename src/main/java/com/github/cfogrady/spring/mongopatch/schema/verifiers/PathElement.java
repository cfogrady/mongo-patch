package com.github.cfogrady.spring.mongopatch.schema.verifiers;

public class PathElement {
    public final PathType pathType;
    public final String path;

    public PathElement(String path, PathType pathType) {
        this.path = path;
        this.pathType = pathType;
    }
}
