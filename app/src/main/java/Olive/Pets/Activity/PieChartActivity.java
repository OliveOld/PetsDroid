package olive.Pets.Activity;

import olive.Pets.R;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

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

import java.util.ArrayList;


/**
 * Created by seobink on 2017-05-16.
 */

public class PieChartActivity
        extends Activity
        implements OnChartValueSelectedListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_simple_pie);  //  .xml


        PieChart pieChart = (PieChart) findViewById(R.id.piechart_test); //  원소
        pieChart.setUsePercentValues(true);

        //원안의 텍스트
        pieChart.setCenterText(generateCenterSpannableText());


        // y값
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

        // 밑에 무슨 값인지 표시해 주는거
        PieDataSet dataSet = new PieDataSet(yvalues, "자세분류상세");

        // entry(값(%), 인덱스)
        yvalues.add(new PieEntry(10, 0)); //lie
        yvalues.add(new PieEntry(10, 1)); //sit/stand
        yvalues.add(new PieEntry(20, 2)); // walk
        yvalues.add(new PieEntry(50, 3)); //run



        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("lie");
        xVals.add("sit/stand");
        xVals.add("walk");
        xVals.add("run");


        // 밑에 value값 정의 생성됨
        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);

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
        pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateXY(1400, 1400);

    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED", "Value: " + e.getX());
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
}

