package org.olive.pets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.seobink.bluetoothkim.chart.PieChartActivity;

public class MainActivity extends AppCompatActivity {
    private Button dailyReport;
    private Button dailyActivity;
    private Button dailyWorkRate;
    private Button managerInfo;
    private ImageButton dogInfo;

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

        dogInfo=(ImageButton)findViewById(R.id.config);
        dogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DogInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}