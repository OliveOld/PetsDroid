package org.olive.pets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.olive.pets.Profile.DogProfileListActivity;

public class DailyReportActivity extends AppCompatActivity{
    private Button btnDailyReport, btnMain, btnDogInfo, btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);


        //btn_main
        btnMain = (Button) findViewById(R.id.btn_main_dr);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(DailyReportActivity.this, MainActivity.class);
                Toast toast = Toast.makeText(DailyReportActivity.this, "액티비티 넘어간다", Toast.LENGTH_SHORT);
                toast.show();
              //  startActivity(intent);
                finish();
            }
        });

        //btn_daily_report
        btnDailyReport = (Button) findViewById(R.id.btn_daily_report_dr);
        btnDailyReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(DailyReportActivity.this, DailyReportActivity.class);
                    startActivity(i);
                    finish();

                    // Toast toast = Toast.makeText(MainActivity.this, "pie.java 연결성공", Toast.LENGTH_SHORT);
                    // toast.show();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(DailyReportActivity.this, "pie.java 연결안됨", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        //btn_dog_info
        btnDogInfo = (Button) findViewById(R.id.btn_dog_info_dr);
        btnDogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyReportActivity.this, DogProfileListActivity.class);
                finish();
                startActivity(intent);

            }
        });

        //btn_setting
        btnSetting = (Button) findViewById(R.id.btn_setting_dr);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyReportActivity.this, ManagerInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
