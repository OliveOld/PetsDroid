package olive.Pets.ViewModel;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import olive.Pets.DB.PostureData;
import olive.Pets.R;

/**
 * Created by luncl on 6/3/2017.
 */

public class ChartVM
        implements OnChartValueSelectedListener
{
    StorageVM storage;
    PieChart chart;

    public ChartVM()
    {
//        chart = new PieChart();
        chart.setUsePercentValues(true);

        //원안의 텍스트
        chart.setCenterText(generateCenterSpannableText());

        // y값
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

        // 밑에 무슨 값인지 표시해 주는거
        PieDataSet dataSet = new PieDataSet(yvalues, "자세분류상세");

        RealmResults<PostureData> posture = null;

        float posture_lie = 25;
        float posture_stand=25;
        float posture_walk=25;
        float posture_run=25;
        float posture_etc=25;

        if (posture.size() != 0){
            PostureData pos_data = posture.last();
            posture_lie = (float) pos_data.getLieSide()+pos_data.getLie()+pos_data.getLieBacke();
            posture_stand = (float) pos_data.getStand()+pos_data.getSit();
            posture_walk = (float) pos_data.getWalk();
            posture_run = (float) pos_data.getRun();
            posture_etc=(float)pos_data.getUnknown();
        }
        // entry(값(%), 인덱스)
        yvalues.add(new PieEntry(posture_lie, 0)); //lie
        yvalues.add(new PieEntry(posture_stand, 1)); //sit/stand
        yvalues.add(new PieEntry(posture_walk, 2)); // walk
        yvalues.add(new PieEntry(posture_run, 3)); //run
        yvalues.add(new PieEntry(posture_etc, 4)); //etc


        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("lie");
        xVals.add("sit/stand");
        xVals.add("walk");
        xVals.add("run");
        xVals.add("ETC");

        int white = 0x00000000; // 투명

        // 밑에 value값 정의 생성됨
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        chart.setData(data);

        // pieChart.setDescription("하루동안강아지는무엇을했을까요?");

        // 파이차트 생성부분
        chart.setDrawHoleEnabled(true);
        chart.setTransparentCircleRadius(10f); // 원주율
        chart.setHoleRadius(50f); // 원안에 크기
        chart.setHoleColor(white);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(15f); // 파이차트 숫자 텍스트 크기
        data.setValueTextColor(Color.WHITE);
        chart.setOnChartValueSelectedListener(this);

        chart.animateXY(1400, 1400);
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

    /**
     *
     * @param e
     * @param h
     */
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        // ...
    }

    /**
     *
     */
    @Override
    public void onNothingSelected() {
        // ...
    }

}
