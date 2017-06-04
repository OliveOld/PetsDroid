package olive.Pets.ViewModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import olive.Pets.DB.DogProfile;
import olive.Pets.DB.Parent;
import olive.Pets.DB.PostureData;
import olive.Pets.R;

/**
 * Created by luncl on 6/3/2017.
 */

public class StorageVM
{

    Realm realm;

    static StorageVM gStorage;

    private StorageVM(Context ctx)
    {
        //튜토리얼 시작 전
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

    public synchronized static void setInstance(Context ctx)
    {
        gStorage = new StorageVM(ctx);
    }

    public synchronized static StorageVM getInstance()
    {
        return gStorage;
    }


    public RealmResults<PostureData> postures()
    {
        if (realm != null){
            return realm.where(PostureData.class).findAll();
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

    /**
     *
     */
    public void loadDB(){
//        tvdogName = (TextView) findViewById(R.id.tv_my_dog_name);
//        tvdogInfo = (TextView) findViewById(R.id.tv_my_dog_info);

//        RealmResults<DogProfile> puppies = mRealm.where(DogProfile.class).findAll();

//        if (puppies.size() == 0) {
            // 강아지 관련 DB없을 시 실행 > default 값 지정
//            tvdogName.setText("프로필 없음");
//            tvdogInfo.setText("null");
//        } else {
            // 강아지 관련 DB 있을 경우
//            puppies = mRealm.where(DogProfile.class).findAll();
            //첫번째로등록 된 놈 보이기
//            DogProfile myDog = puppies.first();
            //Toast.makeText(this, myDog.getDogId() + ":id", Toast.LENGTH_SHORT).show();

//            String dogName = myDog.getDogName();
//            int dogAge = myDog.getDogAge();
//            String dogSex = myDog.getDogSex();
//            String dir = myDog.getDogPhoto();

            // 사진 설정
//            tvdogName.setText(dogName);
//            tvdogInfo.setText(dogSex + "의 " + dogAge + "살 강아지");

//            File imgFile = null;
//            if (dir != null)
//                imgFile = new File(dir);
//            if(imgFile == null){return;}
//            if (imgFile.exists()) {
//                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                ivdogImage = (ImageView) findViewById(R.id.iv_my_dog_image);
//                ivdogImage.setImageBitmap(myBitmap);
//        return;
    }

}
