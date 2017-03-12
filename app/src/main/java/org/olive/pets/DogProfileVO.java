import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by KMJ on 2017-03-12.
 */

public class DogProfileVO extends RealmObject {
    
    @PrimaryKey
    private String dog_name;
    private int dog_age;
    
    public String getDogName() {
        return dog_name;
    }
    
    public void setDogName(String dog_name) {
        this.dog_name = dog_name;
    }
    
    public int int getDogAge() {
        return dog_age;
    }
    
    public void setDogAge(int dog_age) {
        this.dog_age = dog_age;
    }
}
