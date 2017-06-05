package olive.Pets.Struct;

import io.realm.RealmList;
import io.realm.RealmObject;

// @// TODO: 6/5/2017 Tracing required. Is this class mandatory?
// @// TODO: 6/5/2017 Finalization
public class Parent
        extends RealmObject
{
    @SuppressWarnings("unused")
    private RealmList<DogProfile> profileList;

    public RealmList<DogProfile> getdogProfileList() {
        return profileList;
    }
}