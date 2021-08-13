package entities;

import com.mvgreen.deepcopy.annotations.DeepCopyable;

@DeepCopyable  // has no effect
enum SomeEnum {
    IS_ENUM,
    IS_NOT_ENUM {
        // IS_NOT_ENUM.getClass().isEnum() will return false because it extends SomeEnum
    }
}
