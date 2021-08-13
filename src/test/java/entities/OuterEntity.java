package entities;

import com.mvgreen.deepcopy.annotations.DeepCopyable;

import java.util.Objects;

@DeepCopyable
public class OuterEntity {

    private FirstInnerEntity firstInnerEntity = new FirstInnerEntity(5);

    // both objects hold a reference to each other
    public SecondInnerEntity secondInnerEntity = new SecondInnerEntity(5);

    // Outer object does not have a reference to returned value, but the returned one has a reference to Outer
    public SecondInnerEntity createSecondInnerEntity() {
        return new SecondInnerEntity(5);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OuterEntity)) {
            return false;
        }
        OuterEntity other = (OuterEntity) o;
        return Objects.equals(firstInnerEntity, other.firstInnerEntity)
                && firstInnerEntity != other.firstInnerEntity;
    }

    @DeepCopyable
    private class FirstInnerEntity {

        private int i = -5;

        FirstInnerEntity() {
        }

        FirstInnerEntity(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof FirstInnerEntity)) {
                return false;
            }
            FirstInnerEntity other = (FirstInnerEntity) o;
            return i == other.i;
        }

    }

    @DeepCopyable
    public class SecondInnerEntity {

        private int i = -5;

        private SecondInnerEntity() {
        }

        private SecondInnerEntity(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof SecondInnerEntity)) {
                return false;
            }
            SecondInnerEntity other = (SecondInnerEntity) o;
            return i == other.i;
        }

    }

    @DeepCopyable
    public static class StaticInnerEntity {

        // Pretend to be a non-static class
        private OuterEntity $0;

        public StaticInnerEntity(OuterEntity outerEntity) {
            this.$0 = outerEntity;
        }

    }

}
