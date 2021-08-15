package entities;

import com.mvgreen.deepcopy.annotations.CopyMode;
import com.mvgreen.deepcopy.annotations.DeepCopyable;
import entities.dummies.DeepCopyableDummy;

import java.util.*;

@DeepCopyable
public class MapsEntity {

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true, copyKeys = true)
    Map<DeepCopyableDummy, DeepCopyableDummy> hashMap = new HashMap<>();

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true, copyKeys = true)
    Map<DeepCopyableDummy, DeepCopyableDummy> linkedMap = new LinkedHashMap<>();

    @CopyMode(value = CopyMode.Mode.DEEP, copyKeys = true)
    Map<DeepCopyableDummy, DeepCopyableDummy> shallowItemsMap = new HashMap<>();

    @CopyMode(value = CopyMode.Mode.DEEP, copyItems = true)
    Map<DeepCopyableDummy, DeepCopyableDummy> shallowKeysMap = new HashMap<>();

    public static MapsEntity newInstance() {
        MapsEntity entity = new MapsEntity();

        entity.hashMap.put(null, new DeepCopyableDummy());
        entity.hashMap.put(new DeepCopyableDummy(), new DeepCopyableDummy());
        entity.hashMap.put(new DeepCopyableDummy(), null);

        entity.linkedMap.put(null, new DeepCopyableDummy());
        entity.linkedMap.put(new DeepCopyableDummy(), new DeepCopyableDummy());
        entity.linkedMap.put(new DeepCopyableDummy(), null);

        entity.shallowItemsMap.put(null, new DeepCopyableDummy());
        entity.shallowItemsMap.put(new DeepCopyableDummy(), new DeepCopyableDummy());
        entity.shallowItemsMap.put(new DeepCopyableDummy(), null);

        entity.shallowKeysMap.put(null, new DeepCopyableDummy());
        entity.shallowKeysMap.put(new DeepCopyableDummy(), new DeepCopyableDummy());
        entity.shallowKeysMap.put(new DeepCopyableDummy(), null);
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MapsEntity)) {
            return false;
        }
        MapsEntity other = (MapsEntity) o;
        return Objects.equals(hashMap, other.hashMap) && checkCopyMethod(hashMap, other.hashMap, true, true)
                && Objects.equals(linkedMap, other.linkedMap) && checkCopyMethod(linkedMap, other.linkedMap, true, true)
                && Objects.equals(shallowItemsMap, other.shallowItemsMap) && checkCopyMethod(shallowItemsMap, other.shallowItemsMap, false, true)
                && Objects.equals(shallowKeysMap, other.shallowKeysMap) && checkCopyMethod(shallowKeysMap, other.shallowKeysMap, true, false);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashMap, linkedMap, shallowItemsMap, shallowKeysMap);
    }

    private boolean checkCopyMethod(Map m1, Map m2, boolean cloneItems, boolean cloneKeys) {
        if (m1 == null && m2 == null) {
            return true;
        }
        if (m1 == m2 || m1 == null || m2 == null) {
            return false;
        }
        Iterator iter1 = m1.keySet().iterator();
        Iterator iter2 = m2.keySet().iterator();

        while (iter1.hasNext()) {
            Object key1 = iter1.next();
            Object key2 = iter2.next();
            Object item1 = m1.get(key1);
            Object item2 = m2.get(key2);

            if (cloneKeys && key1 == key2 && key1 != null) {
                return false;
            }
            if (!cloneKeys && key1 != key2) {
                return false;
            }

            if (cloneItems && item1 == item2 && item1 != null) {
                return false;
            }
            if (!cloneItems && item1 != item2) {
                return false;
            }
        }
        return true;
    }

}
