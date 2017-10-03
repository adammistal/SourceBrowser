package adammistal.sourcebrowser.injection.module;

import android.arch.persistence.room.Room;
import android.content.Context;


import javax.inject.Singleton;

import adammistal.sourcebrowser.SourceBrowserApplication;
import adammistal.sourcebrowser.data.local.LinksDao;
import adammistal.sourcebrowser.data.local.LinksDatabase;
import dagger.Module;
import dagger.Provides;


@Module
public class DBModule {

    private Context context;

    public DBModule(SourceBrowserApplication application) {
        this.context = application;
    }

    @Provides
    @Singleton
    public LinksDatabase provideLinksDatabase() {
        return Room.databaseBuilder(context, LinksDatabase.class, "Links.db").build();
    }

    @Provides
    @Singleton
    public LinksDao provideLinksDao(LinksDatabase database) {
        return database.pharmaciesDao();
    }
}
