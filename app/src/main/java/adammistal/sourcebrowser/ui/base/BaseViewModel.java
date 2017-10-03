package adammistal.sourcebrowser.ui.base;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public abstract class BaseViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable;

    public BaseViewModel() {
        compositeDisposable = new CompositeDisposable();
    }

    protected <T> void subscribe(Observable<T> observable, DisposableObserver<T> disposableObserver) {
        compositeDisposable.add(observable
                .subscribeWith(disposableObserver));
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        Log.d("_BVM_", "onCleared:" + compositeDisposable.size());
        compositeDisposable = null;
        super.onCleared();
    }
}
