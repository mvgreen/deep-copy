package com.mvgreen.deepcopy;

import com.mvgreen.deepcopy.factories.CloneFactory;

import java.util.HashMap;
import java.util.Map;

public class DeepCopyUtil {

    private Map<Class<?>, CloneFactory<?>> cloneFactories = new HashMap<>();

    public <T> T deepCopy(T src) {
        return null;
    }

    public void addCloneFactory(CloneFactory<?> factory) {
        cloneFactories.put(factory.getType(), factory);
    }

    public void removeCloneFactory(Class<?> type) {
        cloneFactories.remove(type);
    }

}
