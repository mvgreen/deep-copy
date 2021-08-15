package entities;

import com.mvgreen.deepcopy.annotations.CopyMode;
import com.mvgreen.deepcopy.annotations.DeepCopyable;
import entities.dummies.DeepCopyableDummy;

import java.util.*;

@DeepCopyable
public class SetsEntity {

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    Set<DeepCopyableDummy> hashSet = new HashSet<>();

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    Set<DeepCopyableDummy> linkedHashSet = new HashSet<>();

    @CopyMode(value = CopyMode.Mode.DEEP)
    Set<DeepCopyableDummy> shallowItemsSet = new HashSet<>();

    public static SetsEntity newInstance() {
        SetsEntity entity = new SetsEntity();

        entity.hashSet.add(null);
        entity.hashSet.add(new DeepCopyableDummy());

        entity.linkedHashSet.add(null);
        entity.linkedHashSet.add(new DeepCopyableDummy());

        entity.shallowItemsSet.add(null);
        entity.shallowItemsSet.add(new DeepCopyableDummy());

        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SetsEntity)) {
            return false;
        }
        SetsEntity other = (SetsEntity) o;
        return Objects.equals(hashSet, other.hashSet) && hashSet != other.hashSet
                && clonedItems(hashSet, other.hashSet)
                && Objects.equals(linkedHashSet, other.linkedHashSet) && linkedHashSet != other.linkedHashSet
                && clonedItems(linkedHashSet, other.linkedHashSet)
                && Objects.equals(shallowItemsSet, other.shallowItemsSet) && shallowItemsSet != other.shallowItemsSet
                && sameItems(shallowItemsSet, other.shallowItemsSet);
    }

    private boolean clonedItems(Set<DeepCopyableDummy> s1, Set<DeepCopyableDummy> s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 == s2 || s1 == null || s2 == null || s1.size() != s2.size()) {
            return false;
        }

        Iterator<DeepCopyableDummy> iterator1 = s1.iterator();
        Iterator<DeepCopyableDummy> iterator2 = s2.iterator();

        while (iterator1.hasNext()) {
            DeepCopyableDummy next1 = iterator1.next();
            DeepCopyableDummy next2 = iterator2.next();
            if (next1 == next2 && next1 != null) {
                return false;
            }
        }
        return true;
    }

    private boolean sameItems(Set<DeepCopyableDummy> s1, Set<DeepCopyableDummy> s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 == s2 || s1 == null || s2 == null) {
            return false;
        }
        Iterator<DeepCopyableDummy> iterator1 = s1.iterator();
        Iterator<DeepCopyableDummy> iterator2 = s2.iterator();

        while (iterator1.hasNext()) {
            DeepCopyableDummy next1 = iterator1.next();
            DeepCopyableDummy next2 = iterator2.next();
            if (next1 != next2) {
                return false;
            }
        }
        return true;
    }

}
