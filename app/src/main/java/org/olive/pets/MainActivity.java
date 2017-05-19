package org.olive.pets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.olive.pets.BLE.BeanActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;


import org.olive.pets.Chart.PieChart_Activity;

import org.olive.pets.DB.DogProfile;
import org.olive.pets.DB.Parent;
import org.olive.pets.DB.PostureData;
import org.olive.pets.Profile.DogProfileListActivity;
import org.olive.pets.Tutorial.IntroActivity;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;




public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {


    static final String MAIN_FLAG = "mainflag"; // 해당 activity 실행 시 저장할 키 값
    private Button btnDailyReport, btnDogInfo, btnSetting;
    private ImageView ivdogImage;
    private TextView tvdogName, tvdogInfo;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액션바 투명하게 해주기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 색상넣기(투명색상 들어감)
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ff0000")));
       // 왼쪽 화살표 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


                       Intent j = new Intent(MainActivity.this, DailyReportActivity.class);
                        startActivity(j);


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


        //**********************piechart**************************//

        PieChart pieChart = (PieChart) findViewById(R.id.piechart_main); //  원소
        pieChart.setUsePercentValues(true);

        //원안의 텍스트
        pieChart.setCenterText(generateCenterSpannableText());


        // y값
        ArrayList<Entry> yvalues = new ArrayList<Entry>();

        // 밑에 무슨 값인지 표시해 주는거
        PieDataSet dataSet = new PieDataSet(yvalues, "자세분류상세");


        RealmResults<PostureData> posture = mRealm.where(PostureData.class).findAll();

        float posture_lie = 25;
        float posture_stand=25;
        float posture_walk=25;
        float posture_run=25;

        if (posture.size() == 0)
        {   }
        else{

                PostureData pos_data = posture.first();

                posture_lie = (float) pos_data.getLieTime();
                posture_stand = (float) pos_data.getStandTime();
                posture_walk = (float) pos_data.getWalkTime();
                posture_run = (float) pos_data.getRunTime();
        }
        // entry(값(%), 인덱스)
        yvalues.add(new Entry(posture_lie, 0)); //lie
        yvalues.add(new Entry(posture_stand, 1)); //sit/stand
        yvalues.add(new Entry(posture_walk, 2)); // walk
        yvalues.add(new Entry(posture_run, 3)); //run


        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("lie");
        xVals.add("sit/stand");
        xVals.add("walk");
        xVals.add("run");


        // 밑에 value값 정의 생성됨
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);

        pieChart.setDescription("하루동안강아지는무엇을했을까요?");

        // 파이차트 생성부분
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(10f); // 원주율
        pieChart.setHoleRadius(50f); // 원안에 크기

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(15f); // 파이차트 숫자 텍스트 크기
        data.setValueTextColor(Color.WHITE);
        pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateXY(1400, 1400);

        //*****************PieChart_end**********************//

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

           Intent intent = new Intent(MainActivity.this, BeanActivity.class);

            startActivity(intent);
            return true;

        }

        //파이차트테스트 버튼
        if(id==R.id.bluetooth_pietest)
        {
            try {
                Intent intent = new Intent(MainActivity.this, PieChart_Activity.class);
                startActivity(intent);
                return true;
            }
            catch (Exception e)
            {
                Toast toast = Toast.makeText(MainActivity.this, "pie.java 생성실패", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        return true;
    }




    /***************piechart_method_end****************/

    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("PetTrack\ndeveloped by Olive_old");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 9, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 9, s.length() - 13, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 9, s.length() - 13, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 9, s.length() - 13, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 9, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 9, s.length(), 0);
        return s;
    }



    /***************piechart_method_end****************/

}
