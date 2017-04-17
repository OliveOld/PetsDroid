package org.olive.pets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.olive.pets.DB.DogProfile;
import org.olive.pets.chart.PieChartActivity;
import org.olive.pets.tutorial.IntroActivity;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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

        // 튜토리얼 완료를 체크
        SharedPreferences shPref = getSharedPreferences("MyPref", 0);
        int tutorialFlag = shPref.getInt("Flag", 0);

        /*
        if(tutorialFlag == 0) {    // 튜토리얼을 끝내지 못한 경우
            Intent intent=new Intent(MainActivity.this,IntroActivity.class);
            startActivity(intent);
        }
        */

        //btn_main
        btnMain=(Button)findViewById(R.id.btn_main);
        btnMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //btn_daily_report
        btnDailyReport=(Button)findViewById(R.id.btn_daily_report);
        btnDailyReport.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(MainActivity.this, PieChartActivity.class);
                    startActivity(i);
                    Toast toast = Toast.makeText(MainActivity.this, "pie.java 연결성공", Toast.LENGTH_SHORT);
                    toast.show();
                }

                catch (Exception e) {
                    Toast toast = Toast.makeText(MainActivity.this, "pie.java 연결안됨", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        //btn_dog_info
        btnDogInfo =(Button)findViewById(R.id.btn_dog_info);
        btnDogInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DogInfoActivity.class);
                startActivity(intent);

            }
        });

        //btn_setting
        btnSetting=(Button)findViewById(R.id.btn_setting);
        btnSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ManagerInfoActivity.class);
                startActivity(intent);
            }
        });

        tvdogName =(TextView)findViewById(R.id.tv_my_dog_name);
        tvdogInfo=(TextView)findViewById(R.id.tv_my_dog_info);

        // 전 어플리케이션을 통틀어 Realm을 초기화
        // Context.getFilesDir()에 "PetTrack.realm"란 이름으로 Realm 파일이 위치한다
        mRealm.init(this);
        RealmConfiguration myConfig = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .name("PetTrack.realm")
                .build();
        mRealm = Realm.getInstance(myConfig);

        DogProfile myDog = mRealm.where(DogProfile.class).equalTo("dog_id", 1).findFirst();

        // Realm 객체 생성 => default값을 아래에 지정
        if(myDog==null) {
            // 강아지 관련 DB없을 시 실행 > default 값 지정
            Toast.makeText(this, "강아지 프로필이 없습니다.", Toast.LENGTH_SHORT).show();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    DogProfile myDog = realm.createObject(DogProfile.class, 1);
                    myDog.setDogName("Seobin");
                    myDog.setDogAge(26);
                    myDog.setDogSex("female");
                    myDog.setDogPhoto("null");
                }
            });

            /*
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    DogProfile myDog = realm.createObject(DogProfile.class, 2);
                    myDog.setDogName("Tayeon");
                    myDog.setDogAge(25);
                    myDog.setDogSex("female");
                    myDog.setDogPhoto("null");
                }
            });


            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    DogProfile myDog = realm.createObject(DogProfile.class, 3);
                    myDog.setDogName("Jerry");
                    myDog.setDogAge(1);
                    myDog.setDogSex("female");
                    myDog.setDogPhoto("null");
                }
            });
            */

            myDog = mRealm.where(DogProfile.class).equalTo("id", 1).findFirst();

            String dogName = myDog.getDogName();
            int dogAge = myDog.getDogAge();
            String dogSex = myDog.getDogSex();

            tvdogName.setText(dogName);
            tvdogInfo.setText(dogSex + "의 " + dogAge + "살 강아지");
        }
        else {
            // 강아지 관련 DB있을 시
            String dogName = myDog.getDogName();
            int dogAge = myDog.getDogAge();
            String dogSex = myDog.getDogSex();
            String dir = myDog.getDogPhoto();

            // 사진 설정
            tvdogName.setText(dogName);
            tvdogInfo.setText(dogSex + "의 " + dogAge + "살 강아지");

            File imgFile = new  File(dir);

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ivdogImage = (ImageView)findViewById(R.id.iv_my_dog_image);
                ivdogImage.setImageBitmap(myBitmap);
            }
        }
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

        } /*else {
            //강아지 프로필 수정으로 이동
            Intent intent = new Intent(MainActivity.this, DogInfoEditActivity.class);
            startActivity(intent);
            return true;
        }
        */
        return true;
    }

}
