package martelapp.test.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

import martelapp.test.R;

/**
 * Created by cimin on 04/04/2018.
 */

public class ParcelleFragment extends Fragment implements OnChartValueSelectedListener {
    private BubbleChart mChart; //faux graph pour tester l'aspect
    BubbleDataSet dataset;      // on n'utilisera pas de bubbleChart si possible
    BubbleData datasets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_parcelle, null);

        mChart  = (BubbleChart) view.findViewById(R.id.bubbleChart);
        mChart.getDescription().setEnabled(false);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(true);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setMaxVisibleValueCount(200);
        mChart.setPinchZoom(true);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        YAxis yl = mChart.getAxisLeft();
        yl.setSpaceTop(30f);
        yl.setSpaceBottom(30f);
        yl.setDrawZeroLine(false);

        mChart.getAxisRight().setEnabled(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        ArrayList<BubbleEntry> data = new ArrayList<BubbleEntry>();
        Random rand = new Random();
        BubbleEntry be;
        for(int i=0; i< 200; i++){
            float val = (float) (Math.random() * 100f);
            float size = (float) (Math.random() * 15f);
            be = new BubbleEntry(i,val,size);
            data.add(be);
        }

        dataset = new BubbleDataSet(data, "arbres");
        dataset.setDrawIcons(false);
        dataset.setColor(ColorTemplate.COLORFUL_COLORS[0], 130);
        dataset.setDrawValues(true);

        datasets = new BubbleData(dataset);
        datasets.setValueTextSize(0f);
        datasets.setValueTextColor(Color.WHITE);
        datasets.setHighlightCircleWidth(0.25f);

        mChart.setData(datasets);
        mChart.invalidate();

        return view;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
