package com.mvgreen.deepcopy.factories;

import com.mvgreen.deepcopy.CloneFactory;
import com.mvgreen.deepcopy.DeepCopyUtil;
import com.mvgreen.deepcopy.exceptions.CloneException;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapFactory extends CloneFactory<Map> {

    public static final String PARAM_COPY_KEYS = "PARAM_COPY_KEYS";
    public static final String PARAM_COPY_KEYS_RECURSIVELY = "PARAM_COPY_KEYS_RECURSIVELY";

    public MapFactory(DeepCopyUtil deepCopyUtil) {
        super(deepCopyUtil);
    }

    @Override
    public Map clone(Map src, Map<Object, Object> cloneReferences, Map<String, Object> params) throws CloneException {
        Map<Object, Object> clone = instantiateMap(src);
        cloneReferences.put(src, clone);

        boolean copyItems = (boolean) params.getOrDefault(CloneFactory.PARAM_COPY_ITEMS, false);
        boolean copyItemsRecursively = (boolean) params.getOrDefault(CloneFactory.PARAM_COPY_ITEMS_RECURSIVELY, false);
        boolean copyKeys = (boolean) params.getOrDefault(PARAM_COPY_KEYS, false);
        boolean copyKeysRecursively = (boolean) params.getOrDefault(PARAM_COPY_KEYS_RECURSIVELY, false);

        Map<String, Object> newParams = new HashMap<>(params);
        if (!copyItemsRecursively) {
            newParams.remove(CloneFactory.PARAM_COPY_ITEMS);
        }
        if (!copyKeysRecursively) {
            newParams.remove(PARAM_COPY_KEYS);
        }

        for (Object key : src.keySet()) {
            Object keyClone = key;
            Object itemClone = src.get(key);
            if (copyItems) {
                itemClone = itemClone == null ? null : cloneMember(src.get(key), cloneReferences, newParams);
            }
            if (copyKeys) {
                keyClone = keyClone == null ? null : cloneMember(key, cloneReferences, newParams);
            }

            clone.put(keyClone, itemClone);
        }

        return clone;
    }

    private Map<Object, Object> instantiateMap(Map src) throws CloneException {
        Class<? extends Map> srcClass = src.getClass();
        try {
            for (Constructor<?> constructor : srcClass.getDeclaredConstructors()) {
                if (constructor.getParameterCount() == 0) {
                    constructor.setAccessible(true);
                    return (Map) constructor.newInstance();
                }
            }
        } catch (Exception e) {
            throw new CloneException(e);
        }
        throw new CloneException(srcClass + " does not have a constructor with no parameters");
    }

}
