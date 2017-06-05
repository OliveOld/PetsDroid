package olive.Pets.Activity;

import olive.Pets.R;

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
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class DailyReport
        extends Activity
        implements OnChartValueSelectedListener
{
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
        // 색상넣기(투명색상 들어감)
    //    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ff0000")));
        // 왼쪽 화살표 버튼
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    Intent i = new Intent(DailyReport.this, DailyReport.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(DailyReport.this, "pie.java 연결안됨", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //btn_dog_info
        btnDogInfo = (Button) findViewById(R.id.btn_dog_info_dr);
        btnDogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyReport.this, ProfileList.class);
                finish();
                startActivity(intent);
            }
        });

        //btn_setting
        btnSetting = (Button) findViewById(R.id.btn_setting_dr);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyReport.this, Setting.class);
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
                .textColor(Color.LTGRAY, Color.WHITE)
                .selectedDateBackground(Color.TRANSPARENT)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                Toast.makeText(DailyReport.this, DateFormat.getDateInstance().format(date) + " is selected!", Toast.LENGTH_SHORT).show();
                // 해당 날짜의 날짜 가져오기...
                // 데이터베이스 불러오기...
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd");
                selectedDate = sdf1.format(date);

                /*****************************piechart_start********************************/


                RealmResults<PostureData> posture = mRealm.where(PostureData.class).equalTo("date", selectedDate).findAll();




                if (posture.size() == 0)
                {
                    // 데이터가 없다고 표시하기
                }
                else{

                    PieChart pieChart = (PieChart) findViewById(R.id.piechart_daily_report); //  원소
                    pieChart.setUsePercentValues(true);

                    //원안의 텍스트
                    pieChart.setCenterText(generateCenterSpannableText());

                    // y값
                    ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

                    // 밑에 무슨 값인지 표시해 주는거
                    PieDataSet dataSet = new PieDataSet(yvalues, "자세분류상세");

                    PostureData pos_data = posture.first();
                    posture_lie = (float) pos_data.getLie();
                    posture_stand = (float) pos_data.getStand();
                    posture_walk = (float) pos_data.getWalk();
                    posture_run = (float) pos_data.getSit();

                    // entry(값(%), 인덱스)
                    yvalues.add(new PieEntry(posture_lie, 0)); //lie
                    yvalues.add(new PieEntry(posture_stand, 1)); //sit/stand
                    yvalues.add(new PieEntry(posture_walk, 2)); // walk
                    yvalues.add(new PieEntry(posture_run, 3)); //run

                    ArrayList<String> xVals = new ArrayList<String>();
                    xVals.add("lie");
                    xVals.add("sit/stand");
                    xVals.add("walk");
                    xVals.add("run");

                    // 밑에 value값 정의 생성됨
//                    PieData data = new PieData(xVals, dataSet);
                    PieData data = new PieData(dataSet);
                    data.setValueFormatter(new PercentFormatter());
                    pieChart.setData(data);

//                    pieChart.setDescription("하루동안강아지는무엇을했을까요?");
                    Description desc = new Description();
                    desc.setText("하루동안강아지는무엇을했을까요?");
                    pieChart.setDescription(desc);

                    // 파이차트 생성부분
                    pieChart.setDrawHoleEnabled(true);
                    pieChart.setTransparentCircleRadius(10f); // 원주율
                    pieChart.setHoleRadius(50f); // 원안에 크기

                    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                    data.setValueTextSize(15f);
                    data.setValueTextColor(Color.WHITE);
                    pieChart.setOnChartValueSelectedListener(DailyReport.this);

                    pieChart.animateXY(1400, 1400);

                }

                /*****************************piechart_end********************************/
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getData() + ", xIndex: " + e.getX());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("PetTrack\ndeveloped by olive_old");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 9, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 9, s.length() - 13, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 9, s.length() - 13, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 9, s.length() - 13, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 9, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 9, s.length(), 0);
        return s;
    }
    /*****************************piechart_method_end********************************/
}
