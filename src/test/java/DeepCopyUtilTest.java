import com.mvgreen.deepcopy.CloneFactory;
import com.mvgreen.deepcopy.DeepCopyUtil;
import com.mvgreen.deepcopy.annotations.DeepCopyable;
import entities.*;
import entities.dummies.DummyWithoutDefaultConstructor;
import entities.dummies.EmptyDummy;
import entities.dummies.InheritedCopyableDummy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class DeepCopyUtilTest {

    @Test
    public void deepCopy_whenValidates_throwsProperException() {
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        EmptyDummy emptyDummy = new EmptyDummy();
        DummyWithoutDefaultConstructor withoutDefaultConstructor = new DummyWithoutDefaultConstructor(0);
        DummyWithoutDefaultConstructor inherited = new InheritedCopyableDummy(0);

        assertThrows(IllegalArgumentException.class, () -> {
            deepCopyUtil.deepCopy(emptyDummy);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            deepCopyUtil.deepCopy(withoutDefaultConstructor);
        });

        deepCopyUtil.addCloneFactory(new CloneFactory<EmptyDummy>(deepCopyUtil) {
            @Override
            public EmptyDummy clone(EmptyDummy src, Map<Object, Object> cloneReferences, Map<String, Object> params) {
                return new EmptyDummy();
            }
        }, EmptyDummy.class);
        assertDoesNotThrow(() -> {
            deepCopyUtil.deepCopy(emptyDummy, Collections.emptyMap());
        });
        assertDoesNotThrow(() -> {
            deepCopyUtil.deepCopy(inherited, Collections.emptyMap());
        });
    }

    @Test
    public void deepCopy_primitivesAndStringsAndEnums_areCopiedCorrectly() {
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        PrimitivesEntity entity = PrimitivesEntity.newInstance();

        PrimitivesEntity clone = deepCopyUtil.deepCopy(entity);

        assertNotSame(entity, clone);
        assertEquals(entity, clone);
    }

    @Test
    public void deepCopy_fieldsOfInnerClass_areCopiedCorrectly() {
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        OuterEntity entity = OuterEntity.newInstance();

        OuterEntity clone = deepCopyUtil.deepCopy(entity);

        assertNotSame(entity, clone);
        assertEquals(entity, clone);
    }

    @Test
    public void deepCopy_standaloneInnerObjects_areCopiedCorrectly() {
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        OuterEntity.SecondInnerEntity entity = OuterEntity.newInstance().createSecondInnerEntity();

        OuterEntity.SecondInnerEntity clone = deepCopyUtil.deepCopy(entity);

        assertNotSame(entity, clone);
        assertEquals(entity, clone);
    }

    @Test
    public void deepCopy_referencedInnerObjects_areCopiedCorrectly() {
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        OuterEntity.SecondInnerEntity entity = OuterEntity.newInstance().secondInnerEntity;

        OuterEntity.SecondInnerEntity clone = deepCopyUtil.deepCopy(entity);

        assertNotSame(entity, clone);
        assertEquals(entity, clone);
    }

    @Test
    public void deepCopy_nestedObjects_areDifferentiatedFromInner() {
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        OuterEntity.StaticInnerEntity entity = new OuterEntity.StaticInnerEntity(OuterEntity.newInstance());

        assertThrows(IllegalArgumentException.class, () -> deepCopyUtil.deepCopy(entity));
    }

    @Test
    public void deepCopy_localClasses_areDeepCopyable() {
        @DeepCopyable
        class LocalClass {
            OuterEntity outerEntity;

            public LocalClass() {
            }

            @Override
            public boolean equals(Object o) {
                if (!(o instanceof LocalClass)) {
                    return false;
                }
                LocalClass other = (LocalClass) o;
                return Objects.equals(outerEntity, other.outerEntity)
                        && outerEntity != other.outerEntity;
            }

        }
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        LocalClass entity = new LocalClass();
        entity.outerEntity = OuterEntity.newInstance();

        LocalClass clone = deepCopyUtil.deepCopy(entity);

        assertNotSame(entity, clone);
        assertEquals(entity, clone);
    }

    @Test
    public void deepCopy_arrayFields_areDeepCopyable() {
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        ArraysEntity entity = ArraysEntity.newInstance();

        ArraysEntity clone = deepCopyUtil.deepCopy(entity);

        assertNotSame(entity, clone);
        assertEquals(entity, clone);
    }

    @Test
    public void deepCopy_standardListFields_areDeepCopyable() {
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        ListsEntity entity = ListsEntity.newInstance();

        ListsEntity clone = deepCopyUtil.deepCopy(entity);

        assertNotSame(entity, clone);
        assertEquals(entity, clone);
    }

    @Test
    public void deepCopy_standardSetFields_areDeepCopyable() {
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        SetsEntity entity = SetsEntity.newInstance();

        SetsEntity clone = deepCopyUtil.deepCopy(entity);

        assertNotSame(entity, clone);
        assertEquals(entity, clone);
    }

    @Test
    public void deepCopy_standardMapFields_areDeepCopyable() {
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        MapsEntity entity = MapsEntity.newInstance();

        MapsEntity clone = deepCopyUtil.deepCopy(entity);

        assertNotSame(entity, clone);
        assertEquals(entity, clone);
    }

    @Test
    public void deepCopy_testEntity_isDeepCopyable() {
        DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
        ArrayList<String> books = new ArrayList<>();
        books.add("Snail on the Slope");
        books.add("");
        books.add(null);
        Man man = new Man("John", 34, books);

        Man clone = deepCopyUtil.deepCopy(man);

        assertNotSame(man, clone);
        assertNotSame(man.getFavoriteBooks(), clone.getFavoriteBooks());
        assertEquals(man, clone);
    }

}
