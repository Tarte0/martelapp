package martelapp.test.Activity;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;


/*
 *  Graphe représentant la parcelle avec les arbres martelés et non martelés
 */

public class AnalyseGraphe2Activity extends AppCompatActivity {

    Cursor cur;
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_graphe2);

        dbHelper = new DatabaseHelper(getApplicationContext());


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
        ArrayList<Entry> entriesPositionArbreNonMartele = new ArrayList<>();
        ArrayList<Entry> entriesPositionArbreMartele = new ArrayList<>();
        ArrayList<Entry> entriesPositionArbreEco = new ArrayList<>();
        float x, y;
        int noteEco;

        /*
         *  Récupération de la position x, y des arbres NON MARTELES
         *  et vérification de la note écologique pour différencier les
         *  arbres écologiques et les autres
         */
        cur = dbHelper.getAllDataFromTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);
        while(cur.moveToNext()){
            x = (float) cur.getDouble(cur.getColumnIndex(DatabaseHelper.COORD_X_ARBRE));
            y = (float) cur.getDouble(cur.getColumnIndex(DatabaseHelper.COORD_Y_ARBRE));
            noteEco = cur.getInt(cur.getColumnIndex(DatabaseHelper.NOTE_ECO_ARBRE));

            /*
             *  Vérification de la note écologique pour les enregistrer dans la bonne liste
             *  afin de les différencier sur le graphe
             */
            if(noteEco<6) {
                entriesPositionArbreNonMartele.add(new Entry(x, y));
            }
            else{
                entriesPositionArbreEco.add(new Entry(x, y));
            }
        }


        /*
         *  Récupération de la position x, y des arbres MARTELES
         */
        cur = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART);
        while(cur.moveToNext()){
            x = (float) cur.getDouble(cur.getColumnIndex(DatabaseHelper.COORD_X_ARBRE));
            y = (float) cur.getDouble(cur.getColumnIndex(DatabaseHelper.COORD_Y_ARBRE));

            entriesPositionArbreMartele.add(new Entry(x, y));
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


        ArrayList<IScatterDataSet> listScatterData = new ArrayList<>();
        ScatterDataSet scatterDataSetNonMartele = new ScatterDataSet(entriesPositionArbreNonMartele, "Arbres non martelés");
        ScatterDataSet scatterDataSetNonMarteleEco = new ScatterDataSet(entriesPositionArbreEco, "Arbres Ecologiques");
        ScatterDataSet scatterDataSetMartele = new ScatterDataSet(entriesPositionArbreMartele, "Arbres martelés (X)");


        // Couleur des arbres
        scatterDataSetNonMartele.setColor(ColorTemplate.JOYFUL_COLORS[3]);
        scatterDataSetNonMarteleEco.setColor(ColorTemplate.JOYFUL_COLORS[1]);
        scatterDataSetMartele.setColor(Color.RED);


        // Forme des arbres non martelés = cercle
        scatterDataSetNonMartele.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        // Forme des arbres non martelés Eco = cercle
        scatterDataSetNonMarteleEco.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        // Forme des arbres martelés = croix
        scatterDataSetMartele.setScatterShape(ScatterChart.ScatterShape.X);

        listScatterData.add(scatterDataSetNonMartele);
        listScatterData.add(scatterDataSetNonMarteleEco);
        listScatterData.add(scatterDataSetMartele);


        ScatterData scatterData = new ScatterData(listScatterData);

        ScatterChart scatterChart = findViewById(R.id.scatter_chart_carte);
        scatterChart.setData(scatterData);

        // Empêcher zoom
        scatterChart.setScaleEnabled(false);

        // Désactiver le clique
        scatterChart.setClickable(false);

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

        legende.setFormSize(20f);
        legende.setTextSize(20f);

        // Refresh le graphe
        scatterChart.invalidate();
    }
}
