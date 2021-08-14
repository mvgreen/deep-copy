package entities;

import com.mvgreen.deepcopy.annotations.CopyMode;
import com.mvgreen.deepcopy.annotations.DeepCopyable;

import java.util.*;

@DeepCopyable
public class CollectionsEntity {

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    List<PrimitivesEntity> arrayList = new ArrayList<>();

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    List<PrimitivesEntity> linkedList = new LinkedList<>();

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    List<PrimitivesEntity> vector = new Vector<>();

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    List<PrimitivesEntity> stack = new Stack<>();

    public static CollectionsEntity newInstance() {
        CollectionsEntity entity = new CollectionsEntity();

        List<PrimitivesEntity> data = new ArrayList<>();
        data.add(null);
        data.add(PrimitivesEntity.newInstance());

        entity.arrayList.addAll(data);
        entity.linkedList.addAll(data);
        entity.vector.addAll(data);
        entity.stack.addAll(data);

        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CollectionsEntity)) {
            return false;
        }
        CollectionsEntity other = (CollectionsEntity) o;
        return Objects.equals(arrayList, other.arrayList)
                && Objects.equals(linkedList, other.linkedList)
                && Objects.equals(vector, other.vector)
                && Objects.equals(stack, other.stack);
    }

    public boolean sameElementsInCollections() {
        if (arrayList.size() != linkedList.size() || arrayList.size() != vector.size() || arrayList.size() != stack.size()) {
            return false;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) != linkedList.get(i) || arrayList.get(i) != vector.get(i) || arrayList.get(i) != stack.get(i)) {
                return false;
            }
        }
        return true;
    }
}
