package martelapp.test.Activity;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

public class AnalyseGraphe1Activity extends AppCompatActivity {

    Cursor cur1, cur2;
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_graphe1);

        dbHelper = new DatabaseHelper(getApplicationContext());
        DecimalFormat df = new DecimalFormat("#0.0");

        /*
         *  Création de la première ligne du tableau correspondant aux headers.
         *  Ajout de chaque valeur du tableau de String headers dans chaque
         *  colonne de cette ligne
         */
        TableLayout tableau_coupe_essence = findViewById(R.id.tableau_pourcentage_raison);

        TableRow tableRow = new TableRow(getApplicationContext());
        tableau_coupe_essence.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tableau_coupe_essence.setBackgroundColor(Color.GRAY);

        String[] headers = {"Raisons", "Nombre", "Pourcentage"};

        tableRow.setLayoutParams(new TableRow.LayoutParams(headers.length));

        for(int j = 0; j < headers.length; j++){
            TextView text = createTextView(false, j == headers.length - 1);
            text.setText(headers[j]);
            text.setGravity(Gravity.CENTER);
            text.setTextColor(Color.BLACK);
            text.setTypeface(null, Typeface.BOLD);
            tableRow.addView(text, j);
        }


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
         *  ArrayList<BarEntry> entriesRaisonPercentage : Liste où sont enregistrées toutes les données pour le graphe
         *  ArrayList<String> entriesRaison : Liste des raisons pour afficher les labels sur chaque barre
         *  String raison : On récupère les raisons à chaque itération pour trier les données par raison
         *  int i : Défini une nouvelle entrée de données (par essence) sur l'axe x du graphe
         *  int nbRaisonActuelle : Nombre d'occurrence pour une certaine raison
         *  int nbRaisonsTotal : On récupère le nombre de raisons total pour calculer le pourcentage
         *  float percentageRaisonActuel : calcul du pourcentage pour une raison donnée
         */
        ArrayList<BarEntry> entriesRaisonPercentage = new ArrayList<>();
        ArrayList<String> entriesRaison = new ArrayList<>();
        String raison;
        int i = 0;
        int nbRaisonActuelle;
        int nbRaisonsTotal;
        float percentageRaisonsActuel;

        // Récupération du nombre total de raisons
        cur1 = dbHelper.getAllDataFromTable(DatabaseHelper.RAISON_TABLE);
        cur1.moveToFirst();
        nbRaisonsTotal = cur1.getCount();

        /*
         *  Cursor cur1 : Cursor pointant sur toutes les raisons possibles
         *
         *  On trie le graphe par raison donc on parcourt jusqu'à
         *  ce que le cur1 n'ai plus de raisons disponible
         */
        cur1 = dbHelper.getDataFromTable("DISTINCT " + DatabaseHelper.RAISON, DatabaseHelper.RAISON_TABLE);
        while(cur1.moveToNext()){

            // Récupération de la raison actuelle et ajout dans la liste des raisons
            raison = cur1.getString(cur1.getColumnIndex(DatabaseHelper.RAISON));
            entriesRaison.add(raison);

            /*
             *  Récupération du nombre de raison pour la raison actuelle
             *  et calcul du pourcentage de cette raison actuelle
             */
            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.RAISON_TABLE, DatabaseHelper.RAISON + " = '" + raison + "'");
            cur2.moveToFirst();
            nbRaisonActuelle = cur2.getCount();
            percentageRaisonsActuel = ((float)(nbRaisonActuelle) / nbRaisonsTotal) * 100;

            // Ajout dans la liste des données du graphe le pourcentage que l'on vient de calculer
            entriesRaisonPercentage.add(new BarEntry(i, percentageRaisonsActuel));

            i++;

            /*
             *  Création d'une nouvelle ligne dans le tableau
             *  et ajout de chaque valeur du tableau de String row
             *  dans chaque colonne du tableau pour cette ligne
             */
            tableRow = new TableRow(getApplicationContext());
            tableau_coupe_essence.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            String[] row = {raison, Integer.toString(nbRaisonActuelle), df.format(percentageRaisonsActuel) + " %"};

            for(int j = 0; j < row.length; j++){
                TextView text = createTextView(i == cur1.getCount(), j == row.length - 1);
                text.setText(row[j]);
                if(j == 0){
                    text.setTypeface(null, Typeface.BOLD);
                }
                text.setGravity(Gravity.CENTER);
                text.setTextColor(Color.BLACK);
                tableRow.addView(text, j);
            }
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

        BarChart barChartRaisonPercentage = findViewById(R.id.bar_chart_pourcentage_raison);
        BarDataSet barDataSet = new BarDataSet(entriesRaisonPercentage, "Pourcentage Raisons");

        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS[3]);

        //For pinch zoom
        barChartRaisonPercentage.setPinchZoom(false);

        //To enable/disable barChart scaling upon 2 fingers expansion on the chart
        barChartRaisonPercentage.setScaleXEnabled(false);
        barChartRaisonPercentage.setScaleYEnabled(false);

        //To disable double tap zoom
        barChartRaisonPercentage.setDoubleTapToZoomEnabled(false);

        barChartRaisonPercentage.animateY(3000);

        XAxis xAxis = barChartRaisonPercentage.getXAxis();
        YAxis yAxisL = barChartRaisonPercentage.getAxisLeft();
        YAxis yAxisR = barChartRaisonPercentage.getAxisRight();

        yAxisL.setAxisMinimum(0f);
        yAxisL.setAxisMaximum(100f);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(true);
        yAxisL.setDrawAxisLine(true);
        yAxisL.setDrawLabels(true);
        yAxisL.setDrawZeroLine(true);
        yAxisR.setDrawGridLines(false);
        yAxisR.setDrawAxisLine(true);
        yAxisR.setDrawLabels(false);
        //remove horizontal lines
        AxisBase axisBase = barChartRaisonPercentage.getAxisLeft();
        axisBase.setDrawGridLines(true);

        barDataSet.setValueFormatter(new PercentFormatter());

        BarData barData = new BarData(barDataSet);
        barChartRaisonPercentage.setData(barData);

        // Afficher les valeurs en X
        xAxis.setLabelCount(entriesRaison.size());
        // Axe des X affiche les raisons
        xAxis.setValueFormatter(new IndexAxisValueFormatter(entriesRaison));

        // Axe des X en bas du graphe
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Enlever "description label"
        barChartRaisonPercentage.getDescription().setEnabled(false);

        // Ne pas dessiner la grille de fond
        barChartRaisonPercentage.getAxisLeft().setDrawGridLines(false);
        barChartRaisonPercentage.getXAxis().setDrawGridLines(false);

        // Ne pas afficher de légende
        barChartRaisonPercentage.getLegend().setEnabled(false);

        // Axe des Y droit désactivé
        barChartRaisonPercentage.getAxisRight().setEnabled(false);

        // Refresh le graphe
        barChartRaisonPercentage.invalidate();
    }

    private TextView createTextView(boolean endline, boolean endcolumn){
        TextView text = new TextView(getApplicationContext(), null, R.style.frag3HeaderCol);
        int bottom = endline ? 1 : 0;
        int right = endcolumn ? 1 : 0;

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.3f);
        params.setMargins(1, 1, right, bottom);
        text.setLayoutParams(params);
        text.setPadding(4, 4, 10, 4);
        text.setBackgroundColor(Color.WHITE);
        return text;
    }
}