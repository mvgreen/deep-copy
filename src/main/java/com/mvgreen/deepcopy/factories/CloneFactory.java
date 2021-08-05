package com.mvgreen.deepcopy.factories;

public abstract class CloneFactory<T> {

    private Class<T> cloneClass;

    public CloneFactory(Class<T> cloneClass) {
        this.cloneClass = cloneClass;
    }

    public abstract T clone(T src);

    public Class<T> getCloneClass() {
        return cloneClass;
    }
}
