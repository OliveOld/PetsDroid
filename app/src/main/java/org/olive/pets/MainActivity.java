package org.olive.pets;

import android.content.Intent;
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

import org.olive.pets.chart.PieChartActivity;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {
    private Button dailyReport;
    private Button dailyActivity;
    private Button dailyWorkRate;
    private Button managerInfo;

    private ImageView ivdogImage;
    private TextView tvdogName;
    private TextView tvdogInfo;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dailyReport =(Button)findViewById(R.id.daily_report);
        dailyReport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DailyReportActivity.class);
                startActivity(intent);

            }
        });

        dailyActivity=(Button)findViewById(R.id.daily_activity);
        dailyActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, DailyActivityActivity.class);
                startActivity(intent);
            }
        });

        dailyWorkRate=(Button)findViewById(R.id.daily_work_rate);
        dailyWorkRate.setOnClickListener(new View.OnClickListener(){

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

        managerInfo=(Button)findViewById(R.id.manager_info);
        managerInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ManagerInfoActivity.class);
                startActivity(intent);
            }
        });

        /*
        dogInfo=(ImageButton)findViewById(R.id.config);
        dogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DogInfoActivity.class);
                startActivity(intent);
            }
        });
        */

        tvdogName =(TextView)findViewById(R.id.dog_name);
        tvdogInfo=(TextView)findViewById(R.id.dog_info);

        mRealm.init(this);


        RealmConfiguration myConfig = new RealmConfiguration.Builder()
                .name("myrealm.realm")
                .build();
        Realm mRealm = Realm.getInstance(myConfig);

        DogProfileVO myDog = mRealm.where(DogProfileVO.class).equalTo("id", 1).findFirst();


        // Realm 객체 생성 => default
        //한번만 실행
        if(myDog==null) {
            Toast.makeText(this, "강아지 프로필이 업습니다.", Toast.LENGTH_SHORT).show();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    DogProfileVO myDog = realm.createObject(DogProfileVO.class, 1);
                    myDog.setDogName("Seobin");
                    myDog.setDogAge(26);
                    myDog.setDogSex("female");
                    myDog.setDogPhoto("null");
                }
            });

            myDog = mRealm.where(DogProfileVO.class).equalTo("id", 1).findFirst();

            String dogName = myDog.getDogName();
            int dogAge = myDog.getDogAge();
            String dogSex = myDog.getDogSex();

            tvdogName.setText(dogName);
            tvdogInfo.setText(dogSex + "의 " + dogAge + "살 강아지");
        }
        else {

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
                ivdogImage = (ImageView)findViewById(R.id.dog_image);
                ivdogImage.setImageBitmap(myBitmap);
            }

        }
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
            Intent intent = new Intent(MainActivity.this, BTActivity.class);
            startActivity(intent);
            return true;
        } else {
            //강아지 프로필 수정으로 이동
            Intent intent = new Intent(MainActivity.this, DogInfoActivity.class);
            startActivity(intent);
            return true;
        }
    }

}
