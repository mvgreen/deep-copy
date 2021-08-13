package entities;

import com.mvgreen.deepcopy.annotations.CopyMode;
import com.mvgreen.deepcopy.annotations.DeepCopyable;

import java.util.Objects;

@DeepCopyable
public class PrimitivesEntity {

    private int i1 = 4;
    @CopyMode(CopyMode.Mode.DEEP)  // redundant annotation: primitive type
    private int i2 = 5;

    private Boolean b1 = true;
    @CopyMode(CopyMode.Mode.DEEP)  // redundant annotation: wrapper type
    private Boolean b2 = null;

    private String s1 = "string1";
    @CopyMode(CopyMode.Mode.DEEP)  // redundant annotation: String
    private String s2 = "string2";

    private SomeEnum e1 = SomeEnum.IS_ENUM;
    @CopyMode(CopyMode.Mode.DEEP)  // redundant annotation: enum
    private SomeEnum e2 = SomeEnum.IS_NOT_ENUM;

    private final Integer finalInt;

    public PrimitivesEntity(int finalInt) {
        this.finalInt = finalInt;
    }

    public PrimitivesEntity() {
        finalInt = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PrimitivesEntity)) {
            return false;
        }
        PrimitivesEntity other = (PrimitivesEntity) o;
        return i1 == other.i1 && i2 == other.i2
                && Objects.equals(b1, other.b1) && Objects.equals(b2, other.b2)
                && Objects.equals(s1, other.s1) && Objects.equals(s2, other.s2)
                && Objects.equals(e1, other.e1) && Objects.equals(e2, other.e2)
                && Objects.equals(finalInt, other.finalInt);
    }

}
