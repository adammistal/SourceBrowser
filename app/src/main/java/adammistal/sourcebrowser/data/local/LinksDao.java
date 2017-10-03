package adammistal.sourcebrowser.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import adammistal.sourcebrowser.data.local.models.LinkModel;
import io.reactivex.Single;

@Dao
public interface LinksDao {

    @Query("SELECT * FROM links WHERE url = :url")
    Single<List<LinkModel>> getLink(String url);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveLinkSourceCode(LinkModel linkModel);
}
