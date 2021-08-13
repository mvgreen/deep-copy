package entities;

import com.mvgreen.deepcopy.annotations.CopyMode;
import com.mvgreen.deepcopy.annotations.DeepCopyable;
import entities.dummies.EmptyDummy;

import java.util.Arrays;

@DeepCopyable
public class ArraysEntity {

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    int[] intArray = new int[]{1, 2, 3};

    @CopyMode(value = CopyMode.Mode.SHALLOW, copyItems = true)
    Integer[] integerArray = new Integer[]{1, null, 3};

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    Integer[][] deepIntegerArray = new Integer[][]{
            {Integer.valueOf(1), null, 3},
            null
    };

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    SomeEnum[] enumArray = new SomeEnum[] {
            SomeEnum.IS_ENUM, SomeEnum.IS_NOT_ENUM
    };

    @CopyMode(value = CopyMode.Mode.DEEP)
    EmptyDummy[] entityArray = new EmptyDummy[] {
            new EmptyDummy(), null
    };

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    PrimitivesEntity[] copyableEntityArray = new PrimitivesEntity[] {
            new PrimitivesEntity(), null
    };

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ArraysEntity)) {
            return false;
        }
        ArraysEntity other = (ArraysEntity) o;
        return Arrays.equals(intArray, other.intArray) && intArray != other.intArray
                && Arrays.equals(integerArray, other.integerArray)
                && Arrays.deepEquals(deepIntegerArray, other.deepIntegerArray)
                && deepIntegerArray != other.deepIntegerArray && deepIntegerArray[0] != other.deepIntegerArray[0]
                && Arrays.equals(enumArray, other.enumArray) && enumArray != other.enumArray
                && Arrays.equals(entityArray, other.entityArray) && entityArray != other.entityArray
                && Arrays.equals(copyableEntityArray, other.copyableEntityArray) && copyableEntityArray != other.copyableEntityArray;
    }

}
