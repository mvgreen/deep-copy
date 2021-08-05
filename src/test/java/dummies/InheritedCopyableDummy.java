package dummies;

public class InheritedCopyableDummy extends DummyWithoutDefaultConstructor{

    public InheritedCopyableDummy(int arg) {
        super(arg);
    }

    private InheritedCopyableDummy() {
        super(0);
    }

}
