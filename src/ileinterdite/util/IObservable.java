package ileinterdite.util;

public interface IObservable<T> {
    void addObserver(IObserver<T> o);
    void removeObserver(IObserver<T> o);
    void notifyObservers(T message);
}
