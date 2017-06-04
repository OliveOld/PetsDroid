package olive.Pets.Chart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import io.realm.RealmResults;
import olive.Pets.DB.PostureData;

/**
 * Chart modifier
 */
public class TimePieChart
{
    PieChart chart;

    public TimePieChart(PieChart pie)
    {
        chart = pie;

        chart.setUsePercentValues(true);

        // 파이차트 생성부분
        chart.setDrawHoleEnabled(true);
        chart.setTransparentCircleRadius(10f); // 원주율
        chart.setHoleRadius(50f); // 원안에 크기

        final int White = 0x00000000; // 투명
        chart.setHoleColor(White);
        chart.animateXY(1400, 1400);
    }

    public void setValueSelectedListener(OnChartValueSelectedListener listener)
    {
        if(chart != null)
            chart.setOnChartValueSelectedListener(listener);
    }

    public void setDescription(String text)
    {
        if(chart == null){
            return;
        }
        Description desc = new Description();
        desc.setText(text);
        chart.setDescription(desc);
    }

    public void setDataSet(float[] values, String label){
        ArrayList<PieEntry> list = new ArrayList<PieEntry>();

        for(int i = 0; i< values.length; i++){
            list.add(new PieEntry(values[i]));
        }

        PieDataSet set = new PieDataSet(list, label);
        this.setDataSet(set);
    }

    public void setDataSet(PieDataSet set)
    {
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);

        // 밑에 value값 정의 생성됨
        PieData data = new PieData(set);
        data.setValueFormatter(new PercentFormatter());

        data.setValueTextSize(15f); // 파이차트 숫자 텍스트 크기
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);
    }

    /**
     * Generate Center text
     * @return
     */
    public void setCenterText(String text)
    {
        SpannableString s = new SpannableString(text);
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 9, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 9, s.length() - 13, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 9, s.length() - 13, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 9, s.length() - 13, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 9, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 9, s.length(), 0);

        chart.setCenterText(s);        //원안의 텍스트
    }

}
