package martelapp.test.Fragment.Analyse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import martelapp.test.Activity.AnalyseActivity;
import martelapp.test.Activity.ExerciceActivity;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.GrapheHelper;
import martelapp.test.Class.VectorShapeRenderer;
import martelapp.test.R;

/**
 * AnalyseGrapheCarteFragment : Carte des arbres martelés et conservés pendant l'exercice.
 */

public class AnalyseGrapheCarteFragment extends Fragment {

    View view;

    ScatterChart scatterChart;

    private boolean viewScatterChartCarteAdded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.view_page_analyse_parcelle, null);

            scatterChart = view.findViewById(R.id.scatter_chart_carte);

            if (!viewScatterChartCarteAdded) {
                ((AnalyseActivity) getActivity()).addViewPdf(scatterChart, 8);
                viewScatterChartCarteAdded = true;
            }

            GrapheHelper.getScatterChartAnalyseCarte(view.getContext(), scatterChart);
        }

        Button buttonTerminerAnalyse = view.findViewById(R.id.button_terminer_analyse);

        buttonTerminerAnalyse.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertDialogCustom));
                builder.setTitle("Êtes-vous sûr de vouloir terminer l'exercice ?");
                //builder.setMessage("Vous ne pourrez plus revenir en arrière");

                builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ((AnalyseActivity) getActivity()).getAllViewAndCreatePdf();
                    }
                });

                builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;
    }

    public boolean getViewScatterChartCarteAdeed(){
        return viewScatterChartCarteAdded;
    }
}
