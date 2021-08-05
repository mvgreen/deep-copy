import com.mvgreen.deepcopy.DeepCopyUtil;
import com.mvgreen.deepcopy.factories.CloneFactory;
import dummies.DummyWithoutDefaultConstructor;
import dummies.EmptyDummy;
import dummies.InheritedCopyableDummy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        deepCopyUtil.addCloneFactory(new CloneFactory<>(EmptyDummy.class) {
            @Override
            public EmptyDummy clone(EmptyDummy src) {
                return new EmptyDummy();
            }
        });
        assertDoesNotThrow(() -> {
            deepCopyUtil.deepCopy(emptyDummy);
        });
        assertDoesNotThrow(() -> {
            deepCopyUtil.deepCopy(inherited);
        });
    }

}
