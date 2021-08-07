package com.mvgreen.deepcopy;

import java.util.Map;

public abstract class CloneFactory<T> {

    public static final String PARAM_COPY_ITEMS = "PARAM_COPY_ITEMS";
    public static final String PARAM_COPY_ITEMS_RECURSIVELY = "PARAM_COPY_ITEMS_RECURSIVELY";

    private DeepCopyUtil deepCopyUtil;

    public CloneFactory(DeepCopyUtil deepCopyUtil) {
        this.deepCopyUtil = deepCopyUtil;
    }

    public abstract T clone(T src, Map<Object, Object> cloneReferences, Map<String, Object> params);

    protected <K> K cloneMember(K src, Map<Object, Object> cloneReferences, Map<String, Object> params) {
        return deepCopyUtil.deepCopy(src, cloneReferences, params);
    }

}