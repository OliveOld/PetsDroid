package olive.Pets;

import olive.Pets.Chart.TimePieChart;
import olive.Pets.DB.DogProfile;
import olive.Pets.DB.Parent;
import olive.Pets.DB.PostureData;
import olive.Pets.Activity.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import olive.Pets.ViewModel.StorageVM;


public class MainActivity
        extends AppCompatActivity
        implements OnChartValueSelectedListener
{
    // 해당 activity 실행 시 저장할 키 값
    static final String MAIN_FLAG = "PetsApp";

    private Button btnDailyReport, btnDogInfo, btnSetting;
    private ImageView ivdogImage;
    private TextView tvdogName;
    private TextView tvdogInfo;
    TimePieChart chart;

    SharedPreferences shPref;


    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;


    void init(){
        // 액션바 title 지정
        getSupportActionBar().setTitle(" ");
        // 액션바 투명하게 해주기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 색상넣기(투명색상 들어감)
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ff0000")));
        // 왼쪽 화살표 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvdogName = (TextView) findViewById(R.id.tv_my_dog_name);
        tvdogInfo = (TextView) findViewById(R.id.tv_my_dog_info);
        ivdogImage = (ImageView) findViewById(R.id.iv_my_dog_image);

        btnDogInfo = (Button) findViewById(R.id.btn_dog_info);
        btnSetting = (Button) findViewById(R.id.btn_setting);
        btnDailyReport = (Button) findViewById(R.id.btn_daily_report);


        shPref = this.getSharedPreferences("MainPref", 0);
        chart = new TimePieChart((PieChart) findViewById(R.id.piechart_main));

        // mainactivity 실행 시 처음만 불려지도록 함..
        StorageVM.setInstance(this);

        // ---- ---- ---- ---- ----

        btnDogInfo.setOnClickListener( new Transit<olive.Pets.Activity.ProfileList>(this) );
        btnSetting.setOnClickListener( new Transit<olive.Pets.Activity.Setting>(this) );
        btnDailyReport.setOnClickListener(new Transit<olive.Pets.Activity.DailyReport>(this));

        SharedPreferences.Editor shEditor = shPref.edit();
        shEditor.putBoolean("didTutorial", false);
        shEditor.putBoolean("isFirst", true);
        shEditor.apply();

    }

    void loadProfile()
    {
        RealmResults<DogProfile> puppies = StorageVM.getInstance().puppies();

        if (puppies.size() == 0) {
            // 강아지 관련 DB없을 시 실행 > default 값 지정
            tvdogName.setText("프로필 없음");
            tvdogInfo.setText("null");

        } else {

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

            File imgFile = (dir != null)? new File(dir): null;
            if (imgFile!= null && imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ivdogImage.setImageBitmap(myBitmap);
            }
        }
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.init();

        SharedPreferences.Editor shEditor = shPref.edit();

        // 처음 실행을 체크
        boolean isFirst = shPref.getBoolean("isFirst", false);
        if (isFirst) {
//            Intent intent = new Intent(MainActivity.this, Intro.class);
//            startActivity(intent);

            shEditor.putBoolean("isFirst", false);
            shEditor.apply();
        }

        // 튜토리얼 완료를 체크
        boolean didTutorial = shPref.getBoolean("didTutorial", true);
        if(didTutorial == false) {

            // Start tutorial...
            // Finish tutorial...

            shEditor.putBoolean("didTutorial", true);
            shEditor.apply();
        }

        loadProfile();

        chart.setCenterText("Pets");
        chart.setDescription("MainActivity");

        this.refreshChart();
    }

    @Override
    protected void onResume() {
        // 여기서 디비를 다시 읽어온다.
        super.onResume();

        this.refreshChart();

//        loadDB();
        // if(dlDrawer.isDrawerOpen(R.id.sildmenu))
        //     dlDrawer.closeDrawer(R.id.sildmenu);
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

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
        Intent intent;
        if (item.getItemId() == R.id.bluetooth) {
            intent = new Intent(MainActivity.this, olive.Pets.Activity.Bluetooth.class);
            startActivity(intent);
        } else {
            //파이차트테스트 버튼(bluetooth_pietest)
                try {
                    intent = new Intent(MainActivity.this, olive.Pets.Activity.PieChartActivity.class);
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                    Toast toast = Toast.makeText(MainActivity.this, "pie.java 생성실패", Toast.LENGTH_SHORT);
                    toast.show();
                }
        }
        return true;
    }


    public void refreshChart()
    {
        RealmResults<PostureData> posData = StorageVM.getInstance().postures();

        float[] ratio = {0,1,2,3,4};

        if (posData.size() != 0){
            PostureData pos_data = posData.last();
            ratio[0]= (float) pos_data.getLieSide()+pos_data.getLie()+pos_data.getLieBacke();
            ratio[1]= (float) pos_data.getStand()+pos_data.getSit();
            ratio[2]= (float) pos_data.getWalk();
            ratio[3]= (float) pos_data.getRun();
            ratio[4]=(float)pos_data.getUnknown();
        }
        chart.setDataSet(ratio, "자세분류");
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}
