package org.olive.pets.DB;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by KMJ on 2017-03-12.
 */

public class DogProfile extends RealmObject {
    public static final String DOG_ID = "dog_id";
    private static AtomicInteger INTEGER_COUNTER = new AtomicInteger(0);

    @PrimaryKey
    private int dog_id;
    private String dog_name;
    private int dog_age;
    private String dog_sex;
    private String dog_photo;
    private String dog_size;
    private PostureData posture_data;

    public DogProfile(){}
    // 생성자
    public void setDog(String dog_name, int dog_age, String dog_sex, String dog_size, String dog_photo) {
        //this.dog_id=increment();
        this.dog_name=dog_name;
        this.dog_age=dog_age;
        this.dog_sex=dog_sex;
        this.dog_size=dog_size;
        this.dog_photo=dog_photo;
        //PostureData pos= new PostureData();
        //posture_data=pos;
    }
    // auto increment 기능이 따로 없음에 주의
    public int getDogId() { return dog_id; }
    public void setDogId(int dog_id) { this.dog_id = dog_id; }

    public String getDogName() { return dog_name; }
    public void setDogName(String dog_name) {
        this.dog_name = dog_name;
    }
    
    public int getDogAge() {
        return dog_age;
    }
    public void setDogAge(int dog_age) {
        this.dog_age = dog_age;
    }

    public String getDogSex(){ return dog_sex; }
    public void setDogSex(String dog_sex) { this.dog_sex = dog_sex; }

    public String getDogSize(){ return dog_size; }
    public void setDogSize(String dog_size) { this.dog_size = dog_size; }

    public String getDogPhoto() { return dog_photo; }
    public void setDogPhoto(String dog_photo) { this.dog_photo = dog_photo; }

    //  create() & delete() needs to be called inside a transaction.
    public static void create(Realm realm) {
        create(realm, false);
    }

    static void create(Realm realm, boolean randomlyInsert) {
        Parent parent = realm.where(Parent.class).findFirst();
        RealmList<DogProfile> dogprofiles = parent.getdogProfileList();
        DogProfile dogprofile = realm.createObject(DogProfile.class, increment());
        if (randomlyInsert && dogprofiles.size() > 0) {
            Random rand = new Random();
            dogprofiles.listIterator(rand.nextInt(dogprofiles.size())).add(dogprofile);
        } else {
            dogprofiles.add(dogprofile);
        }
    }

    public static void delete(Realm realm, long id) {
        DogProfile dogprofile = realm.where(DogProfile.class).equalTo("dog_id", id).findFirst();
        // Otherwise it has been deleted already.
        if (dogprofile != null) {
            dogprofile.deleteFromRealm();
        }
    }

    private static int increment() {
        return INTEGER_COUNTER.getAndIncrement();
    }
}
