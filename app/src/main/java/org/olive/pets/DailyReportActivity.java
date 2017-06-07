package org.olive.pets;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.olive.pets.DB.PostureData;
import org.olive.pets.Profile.DogProfileListActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class DailyReportActivity extends Activity implements OnChartValueSelectedListener {
    Realm mRealm;
    private Button btnDailyReport, btnMain, btnDogInfo, btnSetting;
    private HorizontalCalendar horizontalCalendar;

    String selectedDate;
    float posture_lie, posture_stand, posture_walk, posture_run=25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

        // 액션바 투명하게 해주기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


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
                .textColor(Color.LTGRAY, Color.BLACK)
                .selectedDateBackground(Color.WHITE)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                Toast.makeText(DailyReportActivity.this, DateFormat.getDateInstance().format(date) + " is selected!", Toast.LENGTH_SHORT).show();
                // 해당 날짜의 날짜 가져오기...
                // 데이터베이스 불러오기...
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd");
                selectedDate = sdf1.format(date);

                piechart();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalCalendar.goToday(false);
            }
        });
    }


    public void piechart() {

        RealmResults<PostureData> posture = mRealm.where(PostureData.class).equalTo("date", selectedDate).findAll();

        PieChart pieChart = (PieChart) findViewById(R.id.piechart_daily_report); //  원소
        pieChart.setUsePercentValues(true);

        //원안의 텍스트
        pieChart.setCenterText(generateCenterSpannableText());

        // y값
        ArrayList<Entry> yvalues = new ArrayList<Entry>();

        // 밑에 무슨 값인지 표시해 주는거
        PieDataSet dataSet = new PieDataSet(yvalues, " ");

        float posture_lie = 0;
        float posture_stand=0;
        float posture_walk=0;
        float posture_run=0;
        float posture_etc=0;
        float total_act=0;

        if (posture.size() == 0)
        {
            pieChart.setData(generateEmptyPieData());
            pieChart.setHighlightPerTapEnabled(false);
            pieChart.setDescription("  ");
            return;
        }
        else{
            PostureData pos_data = posture.last();
            posture_lie = (float) pos_data.getLieSide()+pos_data.getLie()+pos_data.getLieBacke();
            posture_stand = (float) pos_data.getStand()+pos_data.getSit();
            posture_walk = (float) pos_data.getWalk();
            posture_run = (float) pos_data.getRun();
            posture_etc=(float)pos_data.getUnknown();

            total_act=posture_stand+posture_walk+posture_run;

        // entry(값(%), 인덱스)
        if(posture_lie!=0)
            yvalues.add(new Entry(posture_lie, 0)); //lie
        if(posture_lie!=0)
            yvalues.add(new Entry(posture_stand, 1)); //sit/stand
        if(posture_lie!=0)
            yvalues.add(new Entry(posture_walk, 2)); // walk
        if(posture_lie!=0)
            yvalues.add(new Entry(posture_run, 3)); //run
        if(posture_lie!=0)
            yvalues.add(new Entry(posture_etc, 4)); //etc


        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("lie");
        xVals.add("sit/stand");
        xVals.add("walk");
        xVals.add("run");
        xVals.add("ETC");

        int white = 0x00000000; // 투명

        // 밑에 value값 정의 생성됨
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);


        pieChart.setRotationEnabled(true); //????
        pieChart.setDescription("  ");


        // 파이차트 생성부분
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(10f); // 원주율
        pieChart.setHoleRadius(50f); // 원안에 크기
        pieChart.setHoleColor(white);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(15f); // 파이차트 숫자 텍스트 크기
        data.setValueTextColor(Color.WHITE);
        pieChart.setOnChartValueSelectedListener(this);


        pieChart.animateXY(1400, 1400);

        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
        }

    }

    /*****************************piechart_method_start********************************/
    @Override
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

    protected PieData generateEmptyPieData() {
        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        xVals.add(" ");
        yVals.add(new Entry((float) 1, 1));

        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "";
            }
        });

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(153, 153, 153));
        pieDataSet.setColors(colors);

        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(12f);

        PieData pieData = new PieData(xVals, pieDataSet);

        return pieData;
    }
    /*****************************piechart_method_end********************************/
}
