package org.olive.pets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.olive.pets.Profile.DogProfileListActivity;

// 환경 설정 액티비티
public class SettingActivity extends AppCompatActivity {
    private Button btnDailyReport, btnMain, btnDogInfo, btnSetting;
    private Button btnBTSetting, btnManagerInfo, btnInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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
                    Intent i = new Intent(SettingActivity.this, DailyReportActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(SettingActivity.this, "pie.java 연결안됨", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        //btn_dog_info
        btnDogInfo = (Button) findViewById(R.id.btn_dog_info_mi);
        btnDogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, DogProfileListActivity.class);
                finish();
                startActivity(intent);

            }
        });


        //btn_setting
        btnSetting = (Button) findViewById(R.id.btn_setting_mi);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SettingActivity.class);
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
                Intent intent = new Intent(SettingActivity.this, BluetoothActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnInit = (Button) findViewById(R.id.btn_init_setting);

    }
}
