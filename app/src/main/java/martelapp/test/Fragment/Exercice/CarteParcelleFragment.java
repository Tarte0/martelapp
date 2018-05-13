package martelapp.test.Fragment.Exercice;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by cimin on 04/04/2018.
 */

public class CarteParcelleFragment extends Fragment implements OnChartValueSelectedListener {

    ArrayList<IBubbleDataSet> listBubbleData;
    DatabaseHelper dbHelper;
    Cursor cur1, cur2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_parcelle, null);

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

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorGraphePurple));
        colors.add(getResources().getColor(R.color.colorGrapheBlue));
        colors.add(getResources().getColor(R.color.colorGrapheLime));
        colors.add(getResources().getColor(R.color.colorGrapheRed));
        colors.add(getResources().getColor(R.color.colorGrapheLightPink));
        colors.add(getResources().getColor(R.color.colorGrapheGreen));
        colors.add(getResources().getColor(R.color.colorGrapheYellow));
        colors.add(getResources().getColor(R.color.colorGrapheOrange));
        colors.add(getResources().getColor(R.color.colorGrapheBlack));
        colors.add(getResources().getColor(R.color.colorGrapheBrown));
        colors.add(getResources().getColor(R.color.colorGrapheGrey));

        /*
         *  ArrayList<Entry> entriesPositionArbreNonMartele : Liste des position des arbres NON MARTELES pour le graphe
         *  ArrayList<Entry> entriesPositionArbreMartele : Liste des position des arbres MARTELES pour le graphe
         *  ArrayList<Entry> entriesPositionArbreEco : Liste des position des arbres ECOLOGIQUE pour le graphe
         *  float x, y : variable intermédiaire pour enregistrer la position dans les listes
         *  int noteEco : note écologique d'un arbre pour savoir dans quelle liste l'enregistrer
         */
        ArrayList<BubbleEntry> entriesPositionArbre;
        BubbleDataSet bubbleDataSet;
        //ArrayList<IBubbleDataSet> listBubbleData = new ArrayList<>();
        listBubbleData = new ArrayList<>();
        String essence;
        float x, y;
        int diametre;
        int i = 0;
        /*
         *  Récupération de la position x, y des arbres NON MARTELES
         *  et vérification de la note écologique pour différencier les
         *  arbres écologiques et les autres
         */
        cur1 = dbHelper.getDataFromTable("DISTINCT " + DatabaseHelper.ESSENCE_ARBRE, DatabaseHelper.ARBRES_PARCELLE_TABLE + " ORDER BY " + DatabaseHelper.ESSENCE_ARBRE);
        while (cur1.moveToNext()) {

            essence = cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE));

            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE,
                    DatabaseHelper.ESSENCE_ARBRE + " = '" + essence + "'");

            entriesPositionArbre = new ArrayList<>();
            while(cur2.moveToNext()) {

                x = (float) cur2.getDouble(cur2.getColumnIndex(DatabaseHelper.COORD_X_ARBRE));
                y = (float) cur2.getDouble(cur2.getColumnIndex(DatabaseHelper.COORD_Y_ARBRE));
                diametre = cur2.getInt(cur2.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE));
                entriesPositionArbre.add(new BubbleEntry(x, y, (float)diametre));
            }
            Collections.sort(entriesPositionArbre, new EntryXComparator());
            bubbleDataSet = new BubbleDataSet(entriesPositionArbre, essence);
            bubbleDataSet.setColor(colors.get(i % colors.size()));

            bubbleDataSet.setDrawValues(false);
            listBubbleData.add(bubbleDataSet);

            i++;
        }
        dbHelper.close();
        cur1.close();
        cur2.close();


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

        // Forme des arbres non martelés = cercle

        BubbleData bubbleData = new BubbleData(listBubbleData);

        BubbleChart bubbleChart = view.findViewById(R.id.bubble_chart_parcelle);
        bubbleChart.setHardwareAccelerationEnabled(false);

        bubbleChart.setData(bubbleData);


        // Empêcher zoom
        bubbleChart.setScaleEnabled(false);

        // Désactiver le clique
        bubbleChart.setTouchEnabled(false);

        // Enlever "description label"
        bubbleChart.getDescription().setEnabled(false);

        // Axe des X en bas du graphe
        bubbleChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // Axe des Y droit désactivé
        bubbleChart.getAxisRight().setEnabled(false);

        // Enlever espace entre axe des X et les stack bars
        bubbleChart.getAxisLeft().setDrawZeroLine(true);
        bubbleChart.getAxisLeft().setAxisMinimum(0f);

        // Ne pas dessiner la grille de fond
        bubbleChart.getAxisLeft().setDrawGridLines(false);
        bubbleChart.getXAxis().setDrawGridLines(false);

        // Récupération de la légende du graphe
        Legend legende = bubbleChart.getLegend();

        bubbleChart.setExtraOffsets(0f,0f,20f,0f);
        legende.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legende.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legende.setOrientation(Legend.LegendOrientation.VERTICAL);

        // Forme de la légende
        legende.setForm(Legend.LegendForm.CIRCLE);
        legende.setTextSize(20f);
        legende.setFormSize(12f);
        legende.setYEntrySpace(5f);
        // Refresh le graphe
        bubbleChart.invalidate();

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