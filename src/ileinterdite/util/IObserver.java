package ileinterdite.util;

public interface IObserver<T> {
    void update(IObservable<T> o, T message);
}
