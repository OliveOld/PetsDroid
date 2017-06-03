package Olive.Pets.DB;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Parent
        extends RealmObject
{
    @SuppressWarnings("unused")
    private RealmList<DogProfile> profileList;

    public RealmList<DogProfile> getdogProfileList() {
        return profileList;
    }
}