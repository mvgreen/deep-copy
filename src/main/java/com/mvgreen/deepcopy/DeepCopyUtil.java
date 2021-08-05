package com.mvgreen.deepcopy;

import com.mvgreen.deepcopy.annotations.DeepCopyable;
import com.mvgreen.deepcopy.factories.CloneFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class DeepCopyUtil {

    private Map<Class<?>, CloneFactory<?>> cloneFactories = new HashMap<>();

    public <T> T deepCopy(T src) {
        validateArgument(src);
        return null;
    }

    private <T> void validateArgument(T arg) {
        if (arg == null) {
            throw new NullPointerException("argument is null");
        }

        Class<?> klass = arg.getClass();
        if (!klass.isAnnotationPresent(DeepCopyable.class) && !cloneFactories.containsKey(klass)) {
            throw new IllegalArgumentException("class " + klass +
                    " is not annotated with @DeepCopyable and does not have a registered factory");
        }

        boolean found = false;
        for (Constructor<?> constructor : klass.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("class " + klass + " does not have a constructor with no parameters");
        }
    }

    public void addCloneFactory(CloneFactory<?> factory) {
        cloneFactories.put(factory.getCloneClass(), factory);
    }

    public void removeCloneFactory(Class<?> type) {
        cloneFactories.remove(type);
    }

}
