package id.co.qualitas.qubes.interfaces;

public interface CallbackOnResult<T>{
    void onFinish(T result);
    void onFailed();
}
