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
