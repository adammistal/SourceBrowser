package adammistal.sourcebrowser.rx;


import android.util.Log;

import io.reactivex.observers.DisposableObserver;

public abstract class SimpleDisposableObserver<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {
        Log.d("_SDO_", "onNext");
    }

    @Override
    public void onError(Throwable e) {
        Log.d("_SDO_", "onError:" + e);
    }

    @Override
    public void onComplete() {
        Log.d("_SDO_", "onComplete");
    }
}

