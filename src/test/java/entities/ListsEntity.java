package entities;

import com.mvgreen.deepcopy.annotations.CopyMode;
import com.mvgreen.deepcopy.annotations.DeepCopyable;
import entities.dummies.DeepCopyableDummy;

import java.util.*;

@DeepCopyable
public class ListsEntity {

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    List<DeepCopyableDummy> arrayList = new ArrayList<>();

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    List<DeepCopyableDummy> linkedList = new LinkedList<>();

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    List<DeepCopyableDummy> vector = new Vector<>();

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    List<DeepCopyableDummy> stack = new Stack<>();


    @CopyMode(value = CopyMode.Mode.DEEP)
    List<DeepCopyableDummy> shallowItemsList = new ArrayList<>();

    public static ListsEntity newInstance() {
        ListsEntity entity = new ListsEntity();

        entity.arrayList.add(null);
        entity.arrayList.add(new DeepCopyableDummy());

        entity.linkedList.add(null);
        entity.linkedList.add(new DeepCopyableDummy());

        entity.vector.add(null);
        entity.vector.add(new DeepCopyableDummy());

        entity.stack.add(null);
        entity.stack.add(new DeepCopyableDummy());

        entity.shallowItemsList.add(null);
        entity.shallowItemsList.add(new DeepCopyableDummy());

        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ListsEntity)) {
            return false;
        }
        ListsEntity other = (ListsEntity) o;
        return Objects.equals(arrayList, other.arrayList) && clonedItems(arrayList, other.arrayList)
                && Objects.equals(linkedList, other.linkedList) && clonedItems(linkedList, other.linkedList)
                && Objects.equals(vector, other.vector) && clonedItems(vector, other.vector)
                && Objects.equals(stack, other.stack) && clonedItems(stack, other.stack)
                && Objects.equals(shallowItemsList, other.shallowItemsList)
                && sameItems(shallowItemsList, other.shallowItemsList);
    }

    private boolean clonedItems(List<DeepCopyableDummy> l1, List<DeepCopyableDummy> l2) {
        if (l1 == null && l2 == null) {
            return true;
        }
        if (l1 == l2 || l1 == null || l2 == null) {
            return false;
        }
        for (int i = 0; i < l1.size(); i++) {
            if (l1.get(i) == l2.get(i) && l1.get(i) != null) {
                return false;
            }
        }
        return true;
    }

    private boolean sameItems(List<DeepCopyableDummy> l1, List<DeepCopyableDummy> l2) {
        if (l1 == null && l2 == null) {
            return true;
        }
        if (l1 == l2 || l1 == null || l2 == null) {
            return false;
        }
        for (int i = 0; i < l1.size(); i++) {
            if (l1.get(i) != l2.get(i)) {
                return false;
            }
        }
        return true;
    }

}
