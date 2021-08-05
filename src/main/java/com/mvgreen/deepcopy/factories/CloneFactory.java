package com.mvgreen.deepcopy.factories;

public interface CloneFactory<T> {

    T clone(T src);

    Class<T> getType();

}
