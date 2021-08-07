package com.mvgreen.deepcopy.factories;

import com.mvgreen.deepcopy.CloneFactory;
import com.mvgreen.deepcopy.DeepCopyUtil;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ArrayFactory extends CloneFactory<Object> {

    public ArrayFactory(DeepCopyUtil deepCopyUtil) {
        super(deepCopyUtil);
    }

    @Override
    public Object clone(Object src, Map<Object, Object> cloneReferences, Map<String, Object> params) {
        int length = Array.getLength(src);
        Object clone = Array.newInstance(src.getClass().getComponentType(), length);
        cloneReferences.put(src, clone);

        boolean copyItems = (boolean) params.getOrDefault(CloneFactory.PARAM_COPY_ITEMS, false);
        boolean copyItemsRecursively = (boolean) params.getOrDefault(CloneFactory.PARAM_COPY_ITEMS_RECURSIVELY, false);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(src, i);
            Object itemClone;
            if (item.getClass().isArray()) {
                itemClone = clone(item, cloneReferences, params);
            } else if (copyItems || copyItemsRecursively) {
                Map<String, Object> newParams = new HashMap<>(params);
                if (!copyItemsRecursively) {
                    newParams.remove(CloneFactory.PARAM_COPY_ITEMS);
                }
                itemClone = cloneMember(item, cloneReferences, newParams);
            } else {
                itemClone = item;
            }
            cloneReferences.put(item, itemClone);
        }
        return clone;
    }
}
