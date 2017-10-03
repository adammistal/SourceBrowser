package adammistal.sourcebrowser.data.remote;


import adammistal.sourcebrowser.data.remote.interceptors.ProgressInterceptor;
import adammistal.sourcebrowser.data.remote.models.ProgressInfo;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Downloader implements ProgressListener {

    private final OkHttpClient client;
    private final PublishSubject<ProgressInfo> progress = PublishSubject.create();

    public Downloader(OkHttpClient client, ProgressInterceptor progressInterceptor) {
        this.client = client;
        progressInterceptor.setProgressListener(this);
    }

    public Observable<Response> getLinkSource(String url) {
        return Observable.just(url).flatMap(new Function<String, ObservableSource<Response>>() {
            @Override
            public ObservableSource<Response> apply(@NonNull String url) throws Exception {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .addHeader("Accept-Encoding", "identity")
                            .addHeader("Accept", "text/html, application/xhtml+xml, application/xml;q=0.9, */*;q=0.8")
                            .build();
                    Response response = client.newCall(request).execute();
                    return Observable.just(response);
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @Override
    public void update(long bytesRead, long contentLength, boolean done) {
        progress.onNext(new ProgressInfo(bytesRead, contentLength));
    }

    public Observable<ProgressInfo> getProgress() {
        return progress;
    }
}
