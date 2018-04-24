package martelapp.test.Fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by cimin on 04/04/2018.
 */

public class ParcelleFragment extends Fragment implements OnChartValueSelectedListener {

    DatabaseHelper dbHelper;
    Cursor cur;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_parcelle, null);


        int nbArbreParcelle;
        float surfaceParcelle;

        dbHelper = new DatabaseHelper(view.getContext());


        /*
         *
         *
         *
         *######################################################################
         *################### GESTION DES DONNEES DU GRAPHES ###################
         *######################################################################
         *
         *
         *
         */

        /*
         *  ArrayList<Entry> entriesPositionArbreNonMartele : Liste des position des arbres NON MARTELES pour le graphe
         *  ArrayList<Entry> entriesPositionArbreMartele : Liste des position des arbres MARTELES pour le graphe
         *  ArrayList<Entry> entriesPositionArbreEco : Liste des position des arbres ECOLOGIQUE pour le graphe
         *  float x, y : variable intermédiaire pour enregistrer la position dans les listes
         *  int noteEco : note écologique d'un arbre pour savoir dans quelle liste l'enregistrer
         */
        ArrayList<Entry> entriesPositionArbre = new ArrayList<>();
        float x, y;

        /*
         *  Récupération de la position x, y des arbres NON MARTELES
         *  et vérification de la note écologique pour différencier les
         *  arbres écologiques et les autres
         */
        cur = dbHelper.getAllDataFromTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);
        while (cur.moveToNext()) {
            x = (float) cur.getDouble(cur.getColumnIndex(DatabaseHelper.COORD_X_ARBRE));
            y = (float) cur.getDouble(cur.getColumnIndex(DatabaseHelper.COORD_Y_ARBRE));

            entriesPositionArbre.add(new Entry(x, y));
        }



        /*
         *
         *
         *
         *#######################################################################
         *#################### GESTION DE LA FORME DU GRAPHE ####################
         *#######################################################################
         *
         *
         *
         */


        ScatterDataSet scatterDataSet = new ScatterDataSet(entriesPositionArbre, "Arbres parcelle");

        // Couleur des arbres
        scatterDataSet.setColor(ColorTemplate.JOYFUL_COLORS[3]);


        // Forme des arbres non martelés = cercle
        scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);

        ScatterData scatterData = new ScatterData(scatterDataSet);

        ScatterChart scatterChart = view.findViewById(R.id.scatter_chart_parcelle);
        scatterChart.setData(scatterData);

        // Empêcher zoom
        scatterChart.setScaleEnabled(false);

        // Désactiver le clique
        scatterChart.setTouchEnabled(false);

        // Enlever "description label"
        scatterChart.getDescription().setEnabled(false);

        // Axe des X en bas du graphe
        scatterChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // Axe des Y droit désactivé
        scatterChart.getAxisRight().setEnabled(false);

        // Enlever espace entre axe des X et les stack bars
        scatterChart.getAxisLeft().setDrawZeroLine(true);
        scatterChart.getAxisLeft().setAxisMinimum(0f);

        // Ne pas dessiner la grille de fond
        scatterChart.getAxisLeft().setDrawGridLines(false);
        scatterChart.getXAxis().setDrawGridLines(false);

        // Récupération de la légende du graphe
        Legend legende = scatterChart.getLegend();

        // Forme de la légende
        legende.setForm(Legend.LegendForm.CIRCLE);
        legende.setTextSize(20f);
        legende.setFormSize(12f);

        // Refresh le graphe
        scatterChart.invalidate();


        nbArbreParcelle = cur.getCount();

        cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
        cur.moveToFirst();
        surfaceParcelle = cur.getFloat(cur.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));

        TextView textInfoParcelle = view.findViewById(R.id.text_info_parcelle);
        textInfoParcelle.setText("Surface\n" + Float.toString(surfaceParcelle) + " ha\n\n"
                                + "Nombre d'arbres\n" + Integer.toString(nbArbreParcelle));

        return view;
}

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


}


/* tri données pour le zoom
https://github.com/PhilJay/MPAndroidChart/issues/718
 */