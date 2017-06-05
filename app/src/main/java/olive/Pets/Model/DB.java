package olive.Pets.Model;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import olive.Pets.Struct.DailyReport;
import olive.Pets.Struct.DogProfile;
import olive.Pets.Struct.Parent;

/**
 * Created by luncl on 6/3/2017.
 */

// @// TODO: 6/5/2017 realm-based implementation
// @// TODO: 6/5/2017 File loading
public class DB
{
    Realm realm;

    private DB(Context ctx)
    {
        Realm.init(ctx);
        RealmConfiguration cfg = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(Parent.class);
                    }
                })
                .name("PetTrack.realm")
                .build();
        Realm.setDefaultConfiguration(cfg);
        realm = Realm.getInstance(cfg);
    }

    /**
     *
     * @return
     */
    List<DailyReport> reportDaily()
    {
        return null;
    };

    /**
     *
     * @return
     */
    List<DogProfile> profiles()
    {
        return null;
    }

    public RealmResults<DailyReport> postures()
    {
        if (realm != null){
            return realm.where(DailyReport.class).findAll();
        }
        return null;
    }

    public RealmResults<DogProfile> puppies()
    {
        if (realm != null) {
            return realm.where(DogProfile.class).findAll();
        }
        return null;
    }

}
