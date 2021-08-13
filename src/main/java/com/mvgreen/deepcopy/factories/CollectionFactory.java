package com.mvgreen.deepcopy.factories;

import com.mvgreen.deepcopy.CloneFactory;
import com.mvgreen.deepcopy.DeepCopyUtil;
import com.mvgreen.deepcopy.exceptions.CloneException;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CollectionFactory extends CloneFactory<Collection<?>> {

    public CollectionFactory(DeepCopyUtil deepCopyUtil) {
        super(deepCopyUtil);
    }

    @Override
    public Collection<?> clone(Collection<?> src, Map<Object, Object> cloneReferences, Map<String, Object> params) throws CloneException {
        Collection clone = instantiateCollection(src);
        cloneReferences.put(src, clone);

        boolean copyItems = (boolean) params.getOrDefault(CloneFactory.PARAM_COPY_ITEMS, false);
        boolean copyItemsRecursively = (boolean) params.getOrDefault(CloneFactory.PARAM_COPY_ITEMS_RECURSIVELY, false);

        if (copyItems || copyItemsRecursively) {
            HashMap<String, Object> newParams = new HashMap<>(params);
            if (!copyItemsRecursively) {
                newParams.remove(CloneFactory.PARAM_COPY_ITEMS);
            }
            for (Object item : src) {
                clone.add(cloneMember(item, cloneReferences, newParams));
            }
        } else {
            clone.addAll(src);
        }

        return clone;
    }

    private Collection instantiateCollection(Collection<?> src) throws CloneException {
        Class<? extends Collection<?>> srcClass = (Class<? extends Collection<?>>) src.getClass();
        try {
            for (Constructor<?> constructor : srcClass.getDeclaredConstructors()) {
                if (constructor.getParameterCount() == 0) {
                    constructor.setAccessible(true);
                    return (Collection<?>) constructor.newInstance();
                }
            }
        } catch (Exception e) {
            throw new CloneException(e);
        }
        throw new CloneException(srcClass + " does not have a constructor with no parameters");
    }

}
