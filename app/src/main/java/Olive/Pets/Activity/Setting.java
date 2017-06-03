package Olive.Pets.Activity;

import Olive.Pets.Activity.Tutorial.*;
import Olive.Pets.BLE.BeanPacket;
import Olive.Pets.Profile.*;
import Olive.Pets.R;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;



// 환경 설정 액티비티
public class Setting
        extends AppCompatActivity
{
    private Button btnDailyReport, btnMain, btnDogInfo, btnSetting;
    private Button btnBTSetting, btnManagerInfo, btnInit, btnData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //**********************actionbar_start**************************//
        // 액션바 투명하게 해주기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 색상넣기(투명색상 들어감)
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ff0000")));
        // 왼쪽 화살표 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 액션바 title 지정
        getSupportActionBar().setTitle("Pet'Droid");

        //**********************actionbar_start**************************//

        //btn_main
        btnMain = (Button) findViewById(R.id.btn_main_mi);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //btn_daily_report
        btnDailyReport = (Button) findViewById(R.id.btn_daily_report_mi);
        btnDailyReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(Setting.this, DailyReport.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(Setting.this, "pie.java 연결안됨", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });


        //btn_dog_info
        btnDogInfo = (Button) findViewById(R.id.btn_dog_info_mi);
        btnDogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, ProfileList.class);
                finish();
                startActivity(intent);

            }
        });


        //btn_setting
        btnSetting = (Button) findViewById(R.id.btn_setting_mi);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, Setting.class);
                finish();
                startActivity(intent);
            }
        });




        // 버튼 세팅
        btnManagerInfo = (Button) findViewById(R.id.btn_manager_info_setting);

        // 블루투스 화면으로 넘어가기
        btnBTSetting = (Button) findViewById(R.id.btn_bt_setting);
        btnBTSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, Bluetooth.class);
                finish();
                startActivity(intent);
            }
        });

        btnInit = (Button) findViewById(R.id.btn_init_setting);

        // 데이터 콜렉트 화면으로 넘어가기
        btnData = (Button) findViewById(R.id.btn_collect_data);
        btnData = (Button) findViewById(R.id.btn_collect_data);
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this,
                        Olive.Pets.Activity.Tutorial.CollectTrainingSet.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
