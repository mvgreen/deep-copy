package entities.dummies;

import com.mvgreen.deepcopy.annotations.DeepCopyable;

import java.util.Objects;

@DeepCopyable
public class DeepCopyableDummy {

    double randomNumber = Math.random();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeepCopyableDummy)) {
            return false;
        }
        DeepCopyableDummy other = (DeepCopyableDummy) o;
        return Double.compare(other.randomNumber, randomNumber) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(randomNumber);
    }
}
