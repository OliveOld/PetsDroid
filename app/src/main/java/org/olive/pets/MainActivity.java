package org.olive.pets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.olive.pets.DB.DogProfile;
import org.olive.pets.DB.Parent;
import org.olive.pets.Profile.DogProfileListActivity;
import org.olive.pets.Tutorial.IntroActivity;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity{

    static final String MAIN_FLAG = "mainflag"; // 해당 activity 실행 시 저장할 키 값
    private Button btnDailyReport, btnDogInfo, btnSetting;
    private ImageView ivdogImage;
    private TextView tvdogName, tvdogInfo;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences shPref = getSharedPreferences("MyPref", 0);

        int tutorialFlag = shPref.getInt("Flag", 0);         // 튜토리얼 완료를 체크
        int firstFlag = shPref.getInt("firstFlag", 0);      // 처음 실행을 체크
        int dogIdFlag = shPref.getInt("dogId", 1);          // 현재 선택된 강아지를 체크

        if (tutorialFlag == 0) {
            //튜토리얼 시작 전
            Realm.init(this);
            RealmConfiguration myConfig = new RealmConfiguration
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
            Realm.setDefaultConfiguration(myConfig);
            mRealm = Realm.getInstance(myConfig);

            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            //finish();
            startActivity(intent);
        } else {    // 튜토리얼이 끝났을 경우
            Toast toast = Toast.makeText(MainActivity.this, "튜토리얼 끝", Toast.LENGTH_SHORT);
            toast.show();
            // 튜토리얼 끝났을 경우 && 앱을 깔고  첫번째 실행일 경우
            if (firstFlag == 0) {
                firstFlag = 1;   // 첫번째 실행이 아니도록 플래그 변환
                SharedPreferences.Editor prefEditor = shPref.edit();
                prefEditor.putInt("firstFlag", firstFlag);
                prefEditor.commit();

                mRealm = Realm.getDefaultInstance();
            } else {
                //튜토리얼 끝났음 && 앱의 첫번째 실행이 아님 => mainactivity 실행 시 처음만 불려지도록 함..
                //Realm 초기화
                Realm.init(this);
                RealmConfiguration myConfig = new RealmConfiguration
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
                Realm.setDefaultConfiguration(myConfig);
                mRealm = Realm.getInstance(myConfig);
            }

            //btn_daily_report
            btnDailyReport = (Button) findViewById(R.id.btn_daily_report);
            btnDailyReport.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                        Intent i = new Intent(MainActivity.this, DailyReportActivity.class);
                        startActivity(i);
                    } catch (Exception e) {
                        Toast toast = Toast.makeText(MainActivity.this, "pie.java 연결안됨", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            });

            //btn_dog_info
            btnDogInfo = (Button) findViewById(R.id.btn_dog_info);
            btnDogInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DogProfileListActivity.class);
                    startActivity(intent);

                }
            });

            //btn_setting
            btnSetting = (Button) findViewById(R.id.btn_setting);
            btnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                }
            });
            loadDB();

        }
    }

    public void loadDB(){
        tvdogName = (TextView) findViewById(R.id.tv_my_dog_name);
        tvdogInfo = (TextView) findViewById(R.id.tv_my_dog_info);

        RealmResults<DogProfile> puppies = mRealm.where(DogProfile.class).findAll();

        if (puppies.size() == 0) {
            // 강아지 관련 DB없을 시 실행 > default 값 지정
            Toast.makeText(this, "강아지 프로필이 없습니다.", Toast.LENGTH_SHORT).show();

            tvdogName.setText("프로필 없음");
            tvdogInfo.setText("null");
        } else {
            // 강아지 관련 DB 있을 경우
            puppies = mRealm.where(DogProfile.class).findAll();
            //첫번째로등록 된 놈 보이기
            DogProfile myDog = puppies.first();
            //Toast.makeText(this, myDog.getDogId() + ":id", Toast.LENGTH_SHORT).show();

            String dogName = myDog.getDogName();
            int dogAge = myDog.getDogAge();
            String dogSex = myDog.getDogSex();
            String dir = myDog.getDogPhoto();

            // 사진 설정
            tvdogName.setText(dogName);
            tvdogInfo.setText(dogSex + "의 " + dogAge + "살 강아지");

            File imgFile = null;
            if (dir != null)
                imgFile = new File(dir);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ivdogImage = (ImageView) findViewById(R.id.iv_my_dog_image);
                ivdogImage.setImageBitmap(myBitmap);
            }
        }
    }
    @Override
    protected void onResume() {
        // 여기서 디비를 다시 읽어온다.
        super.onResume();
        loadDB();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /* 옵션 메뉴 관련 메소드 시작 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 옵션 메뉴 이어주기
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 옵션 메뉴의 아이템 눌렸을 때
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.bluetooth) {
            Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
            startActivity(intent);
            return true;

        }

        return true;
    }
}
