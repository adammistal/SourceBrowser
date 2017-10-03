package adammistal.sourcebrowser.injection.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import javax.inject.Named;
import javax.inject.Singleton;

import adammistal.sourcebrowser.data.DataManager;
import adammistal.sourcebrowser.data.helpers.ConnectivityHelper;
import adammistal.sourcebrowser.data.local.LinksDao;
import adammistal.sourcebrowser.data.remote.Downloader;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@Module(includes = {ViewModelsModule.class})
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public Resources provideResources() {
        return context.getResources();
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences("localPrefs", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    @Named("main")
    public Scheduler provideMainSchedulerProvider() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    @Named("work")
    public Scheduler provideWorkSchedulerProvider() {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    public DataManager provideGipharDataManager(LinksDao linksDao, Downloader downloader, ConnectivityHelper connectivityHelper, @Named("main") Scheduler mainScheduler, @Named("work") Scheduler workScheduler) {
        return new DataManager(downloader, connectivityHelper, linksDao, mainScheduler, workScheduler);
    }

    @Provides
    @Singleton
    public ConnectivityHelper provideConectivityHelper() {
        return new ConnectivityHelper(context);
    }
}
