package olive.Pets.Activity;

import olive.Pets.Chart.TimePieChart;
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
    TimePieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_simple_pie);  //  .xml

        {
            PieChart pie = (PieChart) findViewById(R.id.piechart_test);
            chart = new TimePieChart(pie);
        }
        chart.setCenterText("Pets");        //원안의 텍스트

        // y값
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

        // entry(값(%), 인덱스)
        yvalues.add(new PieEntry(10)); //lie
        yvalues.add(new PieEntry(10)); //sit/stand
        yvalues.add(new PieEntry(20)); // walk
        yvalues.add(new PieEntry(50)); //run

        // 밑에 무슨 값인지 표시해 주는거
        chart.setDataSet(new PieDataSet(yvalues, "자세분류"));
        chart.setValueSelectedListener(this);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h)
    {
    }

    @Override
    public void onNothingSelected()
    {
    }

}

