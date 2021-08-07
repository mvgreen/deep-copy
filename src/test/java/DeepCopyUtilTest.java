import com.mvgreen.deepcopy.DeepCopyUtil;
import com.mvgreen.deepcopy.CloneFactory;
import dummies.DummyWithoutDefaultConstructor;
import dummies.EmptyDummy;
import dummies.InheritedCopyableDummy;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

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
            deepCopyUtil.deepCopy(emptyDummy, Collections.emptyMap());
        });
        assertThrows(IllegalArgumentException.class, () -> {
            deepCopyUtil.deepCopy(withoutDefaultConstructor, Collections.emptyMap());
        });

        deepCopyUtil.addCloneFactory(new CloneFactory<>(EmptyDummy.class, deepCopyUtil) {
            @Override
            public EmptyDummy clone(EmptyDummy src, Map<Object, Object> cloneReferences, Map<String, Object> params) {
                return new EmptyDummy();
            }
        });
        assertDoesNotThrow(() -> {
            deepCopyUtil.deepCopy(emptyDummy, Collections.emptyMap());
        });
        assertDoesNotThrow(() -> {
            deepCopyUtil.deepCopy(inherited, Collections.emptyMap());
        });
    }

}
