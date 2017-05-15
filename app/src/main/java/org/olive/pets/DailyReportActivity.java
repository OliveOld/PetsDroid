package org.olive.pets;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;

import org.olive.pets.PieChart.PieChartFrag;
import org.olive.pets.Profile.DogProfileListActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class DailyReportActivity extends AppCompatActivity{
    private Button btnDailyReport, btnMain, btnDogInfo, btnSetting;
    private HorizontalCalendar horizontalCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

        //btn_main
        btnMain = (Button) findViewById(R.id.btn_main_dr);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent intent = new Intent(DailyReportActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 여기부터 캘린더 설정
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        /** end after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        final Calendar defaultDate = Calendar.getInstance();
        defaultDate.add(Calendar.MONTH, -1);
        defaultDate.add(Calendar.DAY_OF_WEEK, +5);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(5)
                .dayNameFormat("EEE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .showDayName(true)
                .showMonthName(true)
                .defaultSelectedDate(defaultDate.getTime())
                .textColor(Color.LTGRAY, Color.WHITE)
                .selectedDateBackground(Color.TRANSPARENT)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                Toast.makeText(DailyReportActivity.this, DateFormat.getDateInstance().format(date) + " is selected!", Toast.LENGTH_SHORT).show();
            }

        });

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalCalendar.goToday(false);
            }
        });
        */
    }
}
