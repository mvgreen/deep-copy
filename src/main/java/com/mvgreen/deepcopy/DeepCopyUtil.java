package com.mvgreen.deepcopy;

import com.mvgreen.deepcopy.annotations.CopyMode;
import com.mvgreen.deepcopy.annotations.DeepCopyable;
import com.mvgreen.deepcopy.exceptions.CloneException;
import com.mvgreen.deepcopy.factories.ArrayFactory;
import com.mvgreen.deepcopy.factories.CollectionFactory;
import com.mvgreen.deepcopy.factories.MapFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class DeepCopyUtil {

    private Map<Class<?>, CloneFactory<?>> cloneFactories = new HashMap<>();

    public DeepCopyUtil() {
        addCloneFactory(new ArrayFactory(this), Array.class);

        CollectionFactory collectionFactory = new CollectionFactory(this);
        addCloneFactory(collectionFactory, ArrayList.class);
        addCloneFactory(collectionFactory, LinkedList.class);
        addCloneFactory(collectionFactory, Vector.class);
        addCloneFactory(collectionFactory, Stack.class);

        addCloneFactory(collectionFactory, HashSet.class);
        addCloneFactory(collectionFactory, LinkedHashSet.class);

        MapFactory mapFactory = new MapFactory(this);
        addCloneFactory(mapFactory, HashMap.class);
        addCloneFactory(mapFactory, LinkedHashMap.class);
    }

    public void addCloneFactory(CloneFactory<?> factory, Class<?> klass) {
        cloneFactories.put(klass, factory);
    }

    public void removeCloneFactory(Class<?> type) {
        cloneFactories.remove(type);
    }

    public <T> T deepCopy(T src) throws CloneException {
        Map<Object, Object> cloneReferences = new HashMap<>();
        return deepCopy(src, cloneReferences, null);
    }

    public <T> T deepCopy(T src, Map<String, Object> params) throws CloneException {
        Map<Object, Object> cloneReferences = new HashMap<>();
        return deepCopy(src, cloneReferences, params);
    }

    public <T> T deepCopy(T src, Map<Object, Object> cloneReferences, Map<String, Object> params) {
        validateArgument(src);
        params = params == null ? Collections.emptyMap() : params;
        if (cloneReferences.containsKey(src)) {
            return (T) cloneReferences.get(src);
        } else if (isPrimitiveOrWrapperOrString(src.getClass()) || isEnum(src.getClass())) {
            return src;
        } else if (src.getClass().isAnnotationPresent(DeepCopyable.class)) {
            return copyDeepCopyableObject(src, cloneReferences, params);
        } else {
            return copyObjectWithFactory(src, cloneReferences, params);
        }
    }

    private <T> T copyDeepCopyableObject(T src, Map<Object, Object> cloneReferences, Map<String, Object> params) {
        Class<?> srcClass = src.getClass();
        T clone = null;
        try {
            if (!isInnerOrLocalClass(srcClass)) {
                Constructor<?> constructor = srcClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                //noinspection unchecked
                clone = (T) constructor.newInstance();
            } else {
                Object outerObject = getOuterObject(src);
                Object outerObjectClone = cloneOuterObject(outerObject, cloneReferences);

                // Special case: outer object is not cloned yet (or is not associated with the root object directly)
                // After the previous line, if the inner object was referenced somewhere in the outer, src's clone may appear in the map
                if (cloneReferences.containsKey(src)) {
                    //noinspection unchecked
                    return (T) cloneReferences.get(src);
                }
                for (Constructor<?> constructor : srcClass.getDeclaredConstructors()) {
                    if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].isAssignableFrom(outerObjectClone.getClass())) {
                        constructor.setAccessible(true);
                        //noinspection unchecked
                        clone = (T) constructor.newInstance(outerObjectClone);
                        break;
                    }
                }
                if (clone == null) {
                    throw new NoSuchMethodException("constructor with zero arguments not found");
                }
            }
        } catch (Exception e) {
            throw new CloneException(e);
        }

        cloneReferences.put(src, clone);

        try {
            Class<?> fieldsContainer = srcClass;
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
        boolean copyKeys = false;
        CopyMode.Mode copyMode;
        if (field.isAnnotationPresent(CopyMode.class)) {
            CopyMode annotation = field.getAnnotation(CopyMode.class);
            copyItems = annotation.copyItems();
            copyKeys = annotation.copyKeys();
            copyMode = annotation.value();
        } else {
            Class<?> klass = srcFieldValue.getClass();

            if (klass.isAnnotationPresent(DeepCopyable.class) || cloneFactories.containsKey(klass)) {
                copyMode = CopyMode.Mode.DEEP;
            } else {
                copyMode = CopyMode.Mode.SHALLOW;
            }
        }

        if (isPrimitiveOrWrapperOrString(srcFieldValue.getClass()) || isEnum(srcFieldValue.getClass())) {
            copyMode = CopyMode.Mode.SHALLOW;
        }

        switch (copyMode) {
            case DEEP:
                Map<String, Object> newParams = new HashMap<>(params);
                newParams.put(CloneFactory.PARAM_COPY_ITEMS, copyItems);
                newParams.put(MapFactory.PARAM_COPY_KEYS, copyKeys);

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
        if (klass.isArray() || isPrimitiveOrWrapperOrString(klass) || isEnum(klass)) {
            return;
        }
        if (!klass.isAnnotationPresent(DeepCopyable.class) && !cloneFactories.containsKey(klass)) {
            throw new IllegalArgumentException(klass +
                    " is not annotated with @DeepCopyable and does not have a registered factory");
        }

        boolean found = false;
        if (!isInnerOrLocalClass(klass)) {
            for (Constructor<?> constructor : klass.getDeclaredConstructors()) {
                if (constructor.getParameterCount() == 0) {
                    found = true;
                    break;
                }
            }
        } else {
            try {
                Object outerObject = getOuterObject(arg);
                Class<?> outerObjectClass = outerObject.getClass();
                for (Constructor<?> constructor : klass.getDeclaredConstructors()) {
                    if (constructor.getParameterCount() == 1) {
                        Class<?> paramClass = constructor.getParameterTypes()[0];
                        if (paramClass.equals(outerObjectClass)) {
                            found = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                throw new CloneException(e);
            }
        }
        if (!found) {
            throw new IllegalArgumentException(klass + " does not have a constructor with no parameters");
        }
    }

    private boolean isPrimitiveOrWrapperOrString(Class<?> klass) {
        return (klass.isPrimitive() && klass != void.class) ||
                klass == Double.class || klass == Float.class || klass == Long.class ||
                klass == Integer.class || klass == Short.class || klass == Character.class ||
                klass == Byte.class || klass == Boolean.class || klass == String.class;
    }

    private boolean isEnum(Class<?> klass) {
        if (klass.isEnum()) {
            return true;
        }
        Class<?> superClass = klass.getSuperclass();
        return superClass != null && superClass.isEnum();
    }

    private boolean isInnerOrLocalClass(Class<?> klass) {
        return klass.isLocalClass() || (klass.isMemberClass() && !Modifier.isStatic(klass.getModifiers()));
    }

    private Object getOuterObject(Object inner) throws NoSuchFieldException, IllegalAccessException {
        Class<?> klass = inner.getClass();
        // a dirty hack, but there's no better generic way to obtain the outer object
        Field outerObjectField = klass.getDeclaredField("this$0");
        outerObjectField.setAccessible(true);
        return outerObjectField.get(inner);
    }

    private Object cloneOuterObject(Object src, Map<Object, Object> cloneReferences) {
        if (cloneReferences.containsKey(src)) {
            return cloneReferences.get(src);
        }
        if (src.getClass().isAnnotationPresent(DeepCopyable.class) || cloneFactories.containsKey(src.getClass())) {
            return deepCopy(src, cloneReferences, null);
        }
        return src;
    }

}
