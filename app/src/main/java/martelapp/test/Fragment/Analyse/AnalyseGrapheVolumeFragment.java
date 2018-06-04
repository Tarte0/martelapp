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

/**
 * AnalyseGrapheVolumeFragment : Graphique de la Répartition du prélèvement en volume par classe de diamètre
 */

public class AnalyseGrapheVolumeFragment extends Fragment {

    View view;
    BarChart barChart;

    private boolean viewBarChartVolumeAdded = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.view_page_analyse_graphe_volume, null);

            barChart = view.findViewById(R.id.bar_chart_volume_graphe);

            // Ajout du graphique
            GrapheHelper.getBarChartAnalyseVolume(view.getContext(), barChart);

            // Ajout de la view dans la liste des View de AnalyseActivity pour la création des pdf
            if (!viewBarChartVolumeAdded) {
                ((AnalyseActivity) getActivity()).addViewPdf(barChart, 2);
                viewBarChartVolumeAdded = true;
            }
        }
        return view;
    }

    public boolean getViewBarChartVolumeAdded(){
        return viewBarChartVolumeAdded;
    }
}