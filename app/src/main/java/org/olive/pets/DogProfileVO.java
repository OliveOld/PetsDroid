package org.olive.pets;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by KMJ on 2017-03-12.
 */

public class DogProfileVO extends RealmObject {
    
    @PrimaryKey
    private long id;
    private String dog_name;
    private int dog_age;
    private String dog_sex;
    private String dog_photo;

    // auto increment 기능이 따로 없음에 주의
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

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
