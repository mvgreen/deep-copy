package com.mvgreen.deepcopy;

import com.mvgreen.deepcopy.annotations.CopyMode;
import com.mvgreen.deepcopy.annotations.DeepCopyable;
import com.mvgreen.deepcopy.exceptions.CloneException;
import com.mvgreen.deepcopy.factories.ArrayFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DeepCopyUtil {

    private Map<Class<?>, CloneFactory<?>> cloneFactories = new HashMap<>();

    public void addCloneFactory(CloneFactory<?> factory, Class<?> klass) {
        cloneFactories.put(klass, factory);
    }

    public void removeCloneFactory(Class<?> type) {
        cloneFactories.remove(type);
    }

    public DeepCopyUtil() {
        cloneFactories.put(Array.class, new ArrayFactory(this));
    }

    public <T> T deepCopy(T src, Map<String, Object> params) throws CloneException {
        Map<Object, Object> cloneReferences = new HashMap<>();
        return deepCopy(src, cloneReferences, params == null ? Collections.emptyMap() : params);
    }

    protected <T> T deepCopy(T src, Map<Object, Object> cloneReferences, Map<String, Object> params) {
        validateArgument(src);
        if (cloneReferences.containsKey(src)) {
            return (T) cloneReferences.get(src);
        } else if (src.getClass().isAnnotationPresent(DeepCopyable.class)) {
            return copyDeepCopyableObject(src, cloneReferences, params);
        } else {
            return copyObjectWithFactory(src, cloneReferences, params);
        }
    }

    private <T> T copyDeepCopyableObject(T src, Map<Object, Object> cloneReferences, Map<String, Object> params) {
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
                    copyField(field, src, clone, cloneReferences, params);
                }
                fieldsContainer = fieldsContainer.getSuperclass();
            } while (fieldsContainer != null);
        } catch (Exception e) {
            throw new CloneException(e);
        }

        return clone;
    }

    private <T> void copyField(Field field, T src, T clone, Map<Object, Object> cloneReferences,
                               Map<String, Object> params) throws IllegalAccessException {
        if (Modifier.isStatic(field.getModifiers())) {
            return;
        }

        field.setAccessible(true);
        Object srcFieldValue = field.get(src);
        if (srcFieldValue == null) {
            field.set(clone, null);
            return;
        }

        boolean copyItems = false;
        CopyMode.Mode copyMode;
        if (field.isAnnotationPresent(CopyMode.class)) {
            CopyMode annotation = field.getAnnotation(CopyMode.class);
            copyItems = annotation.copyItems();
            copyMode = annotation.value();
        } else {
            Class<?> klass = srcFieldValue.getClass();

            if (klass.isAnnotationPresent(DeepCopyable.class) || cloneFactories.containsKey(klass)) {
                copyMode = CopyMode.Mode.DEEP;
            } else {
                copyMode = CopyMode.Mode.SHALLOW;
            }
        }

        if (isPrimitiveOrWrapperOrString(srcFieldValue.getClass())) {
            copyMode = CopyMode.Mode.SHALLOW;
        }

        switch (copyMode) {
            case DEEP:
                Map<String, Object> newParams = new HashMap<>(params);
                newParams.put(CloneFactory.PARAM_COPY_ITEMS, copyItems);

                Object fieldClone = deepCopy(srcFieldValue, cloneReferences, newParams);
                field.set(clone, fieldClone);
                break;
            case SHALLOW:
                field.set(clone, srcFieldValue);
                break;
            case SKIP:
                break;
        }
    }

    private <T> T copyObjectWithFactory(T src, Map<Object, Object> cloneReferences, Map<String, Object> params) {
        CloneFactory<T> factory;
        if (src.getClass().isArray()) {
            factory = (CloneFactory<T>) cloneFactories.get(Array.class);
        } else {
            factory = (CloneFactory<T>) cloneFactories.get(src.getClass());
        }
        return factory.clone(src, cloneReferences, params);
    }

    private <T> void validateArgument(T arg) {
        if (arg == null) {
            throw new NullPointerException("argument is null");
        }

        Class<?> klass = arg.getClass();
        if (klass.isArray() || isPrimitiveOrWrapperOrString(klass)) {
            return;
        }
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

    private boolean isPrimitiveOrWrapperOrString(Class<?> klass) {
        return (klass.isPrimitive() && klass != void.class) ||
                klass == Double.class || klass == Float.class || klass == Long.class ||
                klass == Integer.class || klass == Short.class || klass == Character.class ||
                klass == Byte.class || klass == Boolean.class || klass == String.class;
    }

}
