package com.mvgreen.deepcopy.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface CopyMode {

    enum Mode {
        DEEP,
        SHALLOW
    }

    Mode mode();

}
