package com.mvgreen.deepcopy;

import com.mvgreen.deepcopy.exceptions.CloneException;

import java.util.Map;

public abstract class CloneFactory<T> {

    public static final String PARAM_COPY_ITEMS = "PARAM_COPY_ITEMS";
    public static final String PARAM_COPY_ITEMS_RECURSIVELY = "PARAM_COPY_ITEMS_RECURSIVELY";

    protected DeepCopyUtil deepCopyUtil;

    public CloneFactory(DeepCopyUtil deepCopyUtil) {
        this.deepCopyUtil = deepCopyUtil;
    }

    public abstract T clone(T src, Map<Object, Object> cloneReferences, Map<String, Object> params) throws CloneException;

    protected <K> K cloneMember(K src, Map<Object, Object> cloneReferences, Map<String, Object> params) {
        return deepCopyUtil.deepCopy(src, cloneReferences, params);
    }

}
