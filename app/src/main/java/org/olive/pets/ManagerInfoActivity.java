package org.olive.pets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.olive.pets.Profile.DogProfileListActivity;

public class ManagerInfoActivity  extends AppCompatActivity {
    private Button btnDailyReport, btnMain, btnDogInfo, btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_info);

        //btn_main
        btnMain = (Button) findViewById(R.id.btn_main_mi);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(ManagerInfoActivity.this, MainActivity.class);
                Toast toast = Toast.makeText(ManagerInfoActivity.this, "액티비티 넘어간다", Toast.LENGTH_SHORT);
                toast.show();
               // startActivity(intent);
                finish();
            }
        });

        //btn_daily_report
        btnDailyReport = (Button) findViewById(R.id.btn_daily_report_mi);
        btnDailyReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(ManagerInfoActivity.this, DailyReportActivity.class);
                    startActivity(i);
                    finish();
                    // Toast toast = Toast.makeText(MainActivity.this, "pie.java 연결성공", Toast.LENGTH_SHORT);
                    // toast.show();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(ManagerInfoActivity.this, "pie.java 연결안됨", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        //btn_dog_info
        btnDogInfo = (Button) findViewById(R.id.btn_dog_info_mi);
        btnDogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerInfoActivity.this, DogProfileListActivity.class);
                finish();
                startActivity(intent);

            }
        });

        //btn_setting
        btnSetting = (Button) findViewById(R.id.btn_setting_mi);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerInfoActivity.this, ManagerInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
