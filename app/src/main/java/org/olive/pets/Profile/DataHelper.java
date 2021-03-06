package org.olive.pets.Profile;

import org.olive.pets.DB.DogProfile;

import java.util.Collection;

import io.realm.Realm;

public class DataHelper {

    public static void addItemAsync(Realm realm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DogProfile.create(realm);
            }
        });
    }

    public static void deleteItemAsync(Realm realm, final long id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DogProfile.delete(realm, id);
            }
        });
    }

    public static void deleteItemsAsync(Realm realm, Collection<Integer> ids) {
        // Create an new array to avoid concurrency problem.
        final Integer[] idsToDelete = new Integer[ids.size()];
        ids.toArray(idsToDelete);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Integer id : idsToDelete) {
                    DogProfile.delete(realm, id);
                }
            }
        });
    }
}