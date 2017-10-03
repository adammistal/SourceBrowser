package adammistal.sourcebrowser.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


import adammistal.sourcebrowser.data.local.models.LinkModel;

@Database(entities = {LinkModel.class}, version = 1)
public abstract class LinksDatabase extends RoomDatabase {

    public abstract LinksDao pharmaciesDao();
}
