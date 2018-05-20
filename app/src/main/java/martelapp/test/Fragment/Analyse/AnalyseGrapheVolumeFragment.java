package martelapp.test.Fragment.Analyse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import martelapp.test.Activity.AnalyseActivity;
import martelapp.test.Class.GrapheHelper;
import martelapp.test.Class.OnSwipeTouchListener;
import martelapp.test.R;


public class AnalyseGrapheVolumeFragment extends Fragment {

    BarChart barChart;

    private boolean viewBarChartVolumeAdded = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_analyse_graphe_volume, null);

        barChart = view.findViewById(R.id.bar_chart_volume_graphe);

        GrapheHelper.getBarChartAnalyseVolume(view.getContext(), barChart);

        if(!viewBarChartVolumeAdded){
            ((AnalyseActivity) getActivity()).addViewPdf(barChart,2);
            viewBarChartVolumeAdded = true;
        }

        return view;
    }

    public boolean getViewBarChartVolumeAdded(){
        return viewBarChartVolumeAdded;
    }
}