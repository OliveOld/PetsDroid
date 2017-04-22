package org.olive.pets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.olive.pets.DB.DogProfile;
import org.olive.pets.DB.Parent;
import org.olive.pets.Profile.DogInfoActivity;
import org.olive.pets.Profile.DogInfoEditActivity;
import org.olive.pets.chart.PieChartActivity;
import org.olive.pets.tutorial.IntroActivity;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private Button btnDailyReport, btnMain, btnDogInfo, btnSetting;

    private ImageView ivdogImage;
    private TextView tvdogName;
    private TextView tvdogInfo;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences shPref = getSharedPreferences("MyPref", 0);
        // 튜토리얼 완료를 체크
        int tutorialFlag = shPref.getInt("Flag", 0);
        int firstFlag = shPref.getInt("firstFlag", 0);

        if(tutorialFlag == 0) {
            // 튜토리얼 실행 전
            setContentView(R.layout.activity_main);
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

            Intent intent=new Intent(MainActivity.this,IntroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else {
            // 튜토리얼 끝났을 경우
            // 앱의 첫번째 실행일 경우
            if(firstFlag==0 )
            {
                mRealm = Realm.getDefaultInstance();
                firstFlag = 1;   // 첫번째 실행이 아님
                SharedPreferences.Editor prefEditor = shPref.edit();
                prefEditor.putInt("firstFlag", firstFlag);
                prefEditor.commit();
            } else {
                // Realm 설정
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
            //btn_main
            btnMain = (Button) findViewById(R.id.btn_main);
            btnMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            //btn_daily_report
            btnDailyReport = (Button) findViewById(R.id.btn_daily_report);
            btnDailyReport.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                        Intent i = new Intent(MainActivity.this, PieChartActivity.class);
                        startActivity(i);
                        Toast toast = Toast.makeText(MainActivity.this, "pie.java 연결성공", Toast.LENGTH_SHORT);
                        toast.show();
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
                    Intent intent = new Intent(MainActivity.this, DogInfoActivity.class);
                    startActivity(intent);

                }
            });

            //btn_setting
            btnSetting = (Button) findViewById(R.id.btn_setting);
            btnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ManagerInfoActivity.class);
                    startActivity(intent);
                }
            });

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
                Toast.makeText(this, myDog.getDogId() + ":id", Toast.LENGTH_SHORT).show();

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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
