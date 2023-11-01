package id.co.qualitas.qubes.interfaces;

public interface LocationRequestCallback<T,A> {
    void onFinish(T address, A coordinate);
}
