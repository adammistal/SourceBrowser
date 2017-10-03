package adammistal.sourcebrowser.data;


import java.util.List;

import adammistal.sourcebrowser.data.helpers.ConnectivityHelper;
import adammistal.sourcebrowser.data.local.LinksDao;
import adammistal.sourcebrowser.data.local.models.LinkModel;
import adammistal.sourcebrowser.data.remote.Downloader;
import adammistal.sourcebrowser.data.remote.exceptions.HttpException;
import adammistal.sourcebrowser.data.remote.exceptions.NoInternetException;
import adammistal.sourcebrowser.data.remote.models.ProgressInfo;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.Response;

public class DataManager {

    private final ConnectivityHelper connectivityHelper;
    private final LinksDao linksDao;
    private final Scheduler mainScheduler;
    private final Scheduler workScheduler;
    private final Downloader downloader;

    public DataManager(Downloader downloader, ConnectivityHelper connectivityHelper, LinksDao linksDao, Scheduler mainScheduler, Scheduler workScheduler) {
        this.connectivityHelper = connectivityHelper;
        this.downloader = downloader;
        this.linksDao = linksDao;
        this.mainScheduler = mainScheduler;
        this.workScheduler = workScheduler;
    }

    public Observable<ProgressInfo> getProgress() {
        return downloader.getProgress().observeOn(mainScheduler)
                .subscribeOn(workScheduler);
    }


    public Observable<String> fetchUrlSource(String url) {
        return linksDao.getLink(url).toObservable().flatMap(new Function<List<LinkModel>, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull List<LinkModel> linkModels) throws Exception {
                if (linkModels.size() == 0) {
                    if (connectivityHelper.isInternetConnected()) {
                        return downloadUrlSourceCode(url);
                    } else {
                        throw new NoInternetException();
                    }
                } else {
                    return Observable.just(linkModels.get(0).getContent());
                }
            }
        }).observeOn(mainScheduler)
                .subscribeOn(workScheduler);
    }

    private Observable<String> downloadUrlSourceCode(String url) {
        return downloader.getLinkSource(url).flatMap(new Function<Response, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Response response) throws Exception {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    linksDao.saveLinkSourceCode(new LinkModel(url, body, System.currentTimeMillis()));
                    return Observable.just(body);
                } else {
                    throw new HttpException(response);
                }
            }
        });
    }
}
