package entities;

import com.mvgreen.deepcopy.annotations.CopyMode;
import com.mvgreen.deepcopy.annotations.DeepCopyable;
import entities.dummies.EmptyDummy;

import java.util.Arrays;

@DeepCopyable
public class ArraysEntity {

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    int[] intArray;

    @CopyMode(value = CopyMode.Mode.SHALLOW, copyItems = true)
    Integer[] integerArray;

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    Integer[][] deepIntegerArray;

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    SomeEnum[] enumArray;

    @CopyMode(value = CopyMode.Mode.DEEP)
    EmptyDummy[] entityArray;

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    PrimitivesEntity[] copyableEntityArray;

    public static ArraysEntity newInstance() {
        ArraysEntity entity = new ArraysEntity();
        entity.intArray = new int[]{1, 2, 3};
        entity.integerArray = new Integer[]{1, null, 3};
        entity.deepIntegerArray = new Integer[][]{
                {Integer.valueOf(1), null, 3},
                null
        };
        entity.enumArray = new SomeEnum[]{
                SomeEnum.IS_ENUM, SomeEnum.IS_NOT_ENUM
        };
        entity.entityArray = new EmptyDummy[]{
                new EmptyDummy(), null
        };
        entity.copyableEntityArray = new PrimitivesEntity[]{
                new PrimitivesEntity(), null
        };
        return entity;
    }

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
