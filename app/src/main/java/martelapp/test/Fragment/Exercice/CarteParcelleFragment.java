package martelapp.test.Fragment.Exercice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import martelapp.test.Class.GrapheHelper;
import martelapp.test.R;

/**
 * CarteParcelleFragment est un fragment contenant la Carte de la parcelle.
 * On différencie les essences par des points de couleurs différentes.
 */

public class CarteParcelleFragment extends Fragment implements OnChartValueSelectedListener {

    View view;

    BubbleChart bubbleChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view == null) {
            view = inflater.inflate(R.layout.view_page_parcelle, null);

            bubbleChart = view.findViewById(R.id.bubble_chart_parcelle);

            // Affichage du BubbleChart correspondant à la carte de la parcelle
            GrapheHelper.getBubbleChartInfosCarte(view.getContext(), bubbleChart);
        }
        return view;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


}
