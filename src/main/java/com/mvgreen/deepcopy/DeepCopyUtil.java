package com.mvgreen.deepcopy;

import com.mvgreen.deepcopy.annotations.CopyMode;
import com.mvgreen.deepcopy.annotations.DeepCopyable;
import com.mvgreen.deepcopy.exceptions.CloneException;
import com.mvgreen.deepcopy.factories.CloneFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class DeepCopyUtil {

    private Map<Class<?>, CloneFactory<?>> cloneFactories = new HashMap<>();

    public void addCloneFactory(CloneFactory<?> factory) {
        cloneFactories.put(factory.getCloneClass(), factory);
    }

    public void removeCloneFactory(Class<?> type) {
        cloneFactories.remove(type);
    }

    public <T> T deepCopy(T src) throws CloneException {
        validateArgument(src);
        Map<Object, Object> cloneReferences = new HashMap<>();
        return deepCopy(src, cloneReferences);
    }

    private <T> T deepCopy(T src, Map<Object, Object> cloneReferences) {
        if (src.getClass().isAnnotationPresent(DeepCopyable.class)) {
            return copyDeepCopyableObject(src, cloneReferences);
        } else {
            return copyObjectWithFactory(src, cloneReferences);
        }
    }

    private <T> T copyDeepCopyableObject(T src, Map<Object, Object> cloneReferences) {
        Class<?> klass = src.getClass();
        T clone;
        try {
            Constructor<?> constructor = klass.getDeclaredConstructor();
            constructor.setAccessible(true);
            //noinspection unchecked
            clone = (T) constructor.newInstance();
        } catch (Exception e) {
            throw new CloneException(e);
        }

        cloneReferences.put(src, clone);

        try {
            Class<?> fieldsContainer = klass;
            do {
                for (Field field : fieldsContainer.getDeclaredFields()) {
                    copyField(field, src, clone, cloneReferences);
                }
                fieldsContainer = fieldsContainer.getSuperclass();
            } while (fieldsContainer != null);
        } catch (Exception e) {
            throw new CloneException(e);
        }

        return clone;
    }

    private <T> void copyField(Field field, T src, T clone, Map<Object, Object> cloneReferences) throws IllegalAccessException {
        if (Modifier.isStatic(field.getModifiers())) {
            return;
        }

        CopyMode.Mode copyMode;
        if (field.isAnnotationPresent(CopyMode.class)) {
            copyMode = field.getAnnotation(CopyMode.class).value();
        } else {
            Class<?> klass = src.getClass();
            if (klass.isAnnotationPresent(DeepCopyable.class) || cloneFactories.containsKey(klass)) {
                copyMode = CopyMode.Mode.DEEP;
            } else {
                copyMode = CopyMode.Mode.SHALLOW;
            }
        }

        field.setAccessible(true);
        Object srcFieldValue = field.get(src);
        if (srcFieldValue == null) {
            return;
        }
        if (cloneReferences.containsKey(srcFieldValue)) {
            field.set(clone, cloneReferences.get(srcFieldValue));
            return;
        }

        switch (copyMode) {
            case DEEP:
                Object fieldClone = deepCopy(srcFieldValue, cloneReferences);
                field.set(clone, fieldClone);
                break;
            case SHALLOW:
                field.set(clone, srcFieldValue);
                break;
            case SKIP:
                break;
        }
    }

    // TODO
    private <T> T copyObjectWithFactory(T src, Map<Object, Object> cloneReferences) {
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

}
