package adammistal.sourcebrowser.ui.content;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import javax.inject.Inject;

import adammistal.sourcebrowser.R;
import adammistal.sourcebrowser.data.DataManager;
import adammistal.sourcebrowser.data.remote.exceptions.HttpException;
import adammistal.sourcebrowser.data.remote.exceptions.NoInternetException;
import adammistal.sourcebrowser.data.remote.models.ProgressInfo;
import adammistal.sourcebrowser.ui.base.BaseViewModel;
import adammistal.sourcebrowser.utils.Validation;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;


public class ContentViewModel extends BaseViewModel {
    enum State {
        LOADING, LOADED, ERROR
    }

    private final PublishSubject<State> state;
    private final PublishSubject<String> content;
    private final PublishSubject<String> message;
    private final Observable<ProgressInfo> progress;
    private final DataManager dataManager;
    private final Resources resources;
    private String loadedUrl;

    @Inject
    ContentViewModel(DataManager dataManager, Resources resources) {
        super();
        this.resources = resources;
        this.dataManager = dataManager;
        state = PublishSubject.create();
        content = PublishSubject.create();
        progress = dataManager.getProgress();
        message = PublishSubject.create();
    }

    public void loadUrl(String url) {
        if (loadedUrl != null && loadedUrl.equals(url)) {
            message.onNext(resources.getString(R.string.url_already_loaded));
            return;
        }
        loadedUrl = url;
        boolean correctUrl = validateUrl(url);
        if (!correctUrl)
            return;
        state.onNext(State.LOADING);
        subscribe(dataManager.fetchUrlSource(url), new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String sourceCode) {
                content.onNext(sourceCode);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("onError", "e:" + e);
                loadedUrl = null;
                state.onNext(State.ERROR);
                if (e instanceof HttpException) {
                    HttpException httpException = (HttpException) e;
                    message.onNext(resources.getString(R.string.http_error_no_link_in_db));
                    content.onNext(String.format(resources.getString(R.string.http_error_code), httpException.code()));
                } else if (e instanceof NoInternetException) {
                    message.onNext(resources.getString(R.string.no_internet_no_link_in_db));
                } else {
                    message.onNext(resources.getString(R.string.error) + e);
                }
            }

            @Override
            public void onComplete() {
                state.onNext(State.LOADED);
            }
        });
    }

    private boolean validateUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            message.onNext(resources.getString(R.string.message_get_url));
            state.onNext(State.ERROR);
            return false;
        } else if (!Validation.isUrl(url)) {
            message.onNext(resources.getString(R.string.message_invalid_url));
            state.onNext(State.ERROR);
            return false;
        } else {
            return true;
        }
    }

    Observable<State> getState() {
        return state;
    }

    Observable<String> getContent() {
        return content;
    }

    Observable<ProgressInfo> getProgress() {
        return progress;
    }

    Observable<String> getMessage() {
        return message;
    }
}