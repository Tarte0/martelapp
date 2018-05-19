package martelapp.test.Fragment.Analyse;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.GrapheHelper;
import martelapp.test.Class.VectorShapeRenderer;
import martelapp.test.R;

/**
 * Created by cimin on 24/04/2018.
 */

public class AnalyseGrapheCarteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_analyse_parcelle, null);

        ScatterChart scatterChart = view.findViewById(R.id.scatter_chart_carte);

        GrapheHelper.getScatterChartAnalyseCarte(view.getContext(), scatterChart);

        return view;
    }
}
