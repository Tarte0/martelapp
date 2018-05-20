package martelapp.test.Fragment.Analyse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;

import martelapp.test.Activity.AnalyseActivity;
import martelapp.test.Class.GrapheHelper;
import martelapp.test.R;

/**
 * Created by Baptiste on 19/05/2018.
 */

public class AnalyseGrapheRaisonsFragment extends Fragment {

    BarChart barChart;

    private boolean viewBarChartRaisonsAdded = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.view_page_analyse_raisons, null);

        barChart = view.findViewById(R.id.bar_chart_pourcentage_raison);

        GrapheHelper.getBarChartAnalyseRaisons(view.getContext(), barChart);

        if(!viewBarChartRaisonsAdded){
            ((AnalyseActivity) getActivity()).addViewPdf(barChart,7);
            viewBarChartRaisonsAdded = true;
        }

        return view;
    }

    public boolean getViewBarChartRaisonsAdded(){
        return viewBarChartRaisonsAdded;
    }
}
