package ileinterdite.util;

import java.util.Objects;

public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tuple) {
            Tuple tu = (Tuple) obj;
            return x.equals(tu.x) && y.equals(tu.y);
        } else {
            return false;
        }
    }
}