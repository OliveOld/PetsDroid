package org.olive.pets;

import org.olive.pets.DB.DogProfile;

import io.realm.RealmList;
import io.realm.RealmObject;


public class Parent extends RealmObject {
    @SuppressWarnings("unused")
    private RealmList<DogProfile> dogProfileList;

    public RealmList<DogProfile> getdogProfileList() {
        return dogProfileList;
    }
}