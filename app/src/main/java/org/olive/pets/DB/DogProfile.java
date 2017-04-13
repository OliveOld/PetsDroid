package org.olive.pets.DB;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by KMJ on 2017-03-12.
 */

public class DogProfile extends RealmObject {
    
    @PrimaryKey
    private int dog_id;
    private String dog_name;
    private int dog_age;
    private String dog_sex;
    private String dog_photo;
    private PostureData posture_data;

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

    public String getDogPhoto() { return dog_photo; }
    public void setDogPhoto(String dog_photo) { this.dog_photo = dog_photo; }
}
