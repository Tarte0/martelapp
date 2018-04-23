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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.StackedBarFormatter;
import martelapp.test.R;

/*
 *  Graphes représentant le nombre d'arbre sur la parcelle en
 *  fonction des essences, avec ce qui a été coupé et ce qu'il reste
 */

public class AnalyseGraphe0Activity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    Cursor cur1, cur2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_graphe0);

        dbHelper = new DatabaseHelper(getApplicationContext());


        /*
         *  Création de la première ligne du tableau correspondant aux headers.
         *  Ajout de chaque valeur du tableau de String headers dans chaque
         *  colonne de cette ligne
         */
        TableLayout tableau_coupe_essence = findViewById(R.id.tableau_coupe_essence);

        TableRow tableRow = new TableRow(getApplicationContext());
        tableau_coupe_essence.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tableau_coupe_essence.setBackgroundColor(Color.GRAY);

        String[] headers = {"Essences", "Avant", "Coupe", "Après"};

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
         *  ArrayList<BarEntry> entriesArbres : Liste où sont enregistrées toutes les données pour le graphe
         *  ArrayList<String> entriesEssences : Liste des essences pour afficher les labels sur chaque barre
         *  String essence : on récupère l'essence à chaque itération pour trier les données par essence
         *  int i : Défini une nouvelle entrée de données (par essence) sur l'axe x du graphe
         *  int nbArbreAvant : Nombre d'arbres initialement dans la parcelle par essence
         *  int nbArbreCoupe : Nombre d'arbres martelés par essence
         *  int nbArbreApresCoupe : Nombre d'arbres restant sur la parcelle après coupe par essence
         */
        ArrayList<BarEntry> entriesArbres = new ArrayList<>();
        ArrayList<String> entriesEssences = new ArrayList<>();
        String essence;
        int i = 0;
        int nbArbreAvant;
        int nbArbreCoupe;
        int nbArbreApresCoupe;

        /*
         *  Cursor cur1 : Cursor pointant sur toutes les essences disponibles dans la base de données
         *
         *  On trie le graphe par essence donc on parcourt jusqu'à
         *  ce que le cur1 n'ai plus d'essence disponible
         */
        cur1 = dbHelper.getDataFromTable("DISTINCT " + DatabaseHelper.ESSENCE_ARBRE, DatabaseHelper.ARBRES_PARCELLE_TABLE);
        while(cur1.moveToNext()){

            // Récupération de l'essence actuelle et ajout dans la liste des essences
            essence = cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE));
            entriesEssences.add(essence);

            /*
             *  Récupération du nombre d'arbres martelés de l'essence actuelle
             *  et enregistrement de ce nombre dans nbArbreCoupe
             */
            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                    "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                            + " AND ap." + DatabaseHelper.ESSENCE_ARBRE + " = '" + essence + "'");
            cur2.moveToFirst();
            nbArbreCoupe = cur2.getCount();


            /*
             *  Récupération du nombre d'arbres restant dans la parcelle
             *  de l'essence actuelle et enregistrement de ce nombre dans
             *  nbArbreApresCoupe
             *
             *  nbArbreApresCoupe prend le nombre d'arbre total dans la
             *  parcelle moins le nbArbreCoupe pour l'essence actuelle
             */
            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE, DatabaseHelper.ESSENCE_ARBRE + " = '" + essence + "'");
            cur2.moveToFirst();
            nbArbreAvant = cur2.getCount();
            nbArbreApresCoupe = nbArbreAvant - nbArbreCoupe;

            /*
             *  On ajoute dans la liste des données du graphe les deux valeurs que l'on vient
             *  de calculer sous forme de tableau de float car on veut que les données soit
             *  sous forme de bar chart stack
             */
            entriesArbres.add(new BarEntry(i, new float[]{nbArbreApresCoupe, nbArbreCoupe}));

            i++;



            /*
             *  Création d'une nouvelle ligne dans le tableau
             *  et ajout de chaque valeur du tableau de String row
             *  dans chaque colonne du tableau pour cette ligne
             */
            tableRow = new TableRow(getApplicationContext());
            tableau_coupe_essence.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            String[] row = {essence, Integer.toString(nbArbreAvant), Integer.toString(nbArbreCoupe), Integer.toString(nbArbreApresCoupe)};

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

        BarDataSet barDataSet = new BarDataSet(entriesArbres, "");

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ColorTemplate.JOYFUL_COLORS[3]);
        colors.add(ColorTemplate.JOYFUL_COLORS[2]);

        barDataSet.setColors(colors);

        //barDataSet.setValueFormatter(new IntegerFormatter());
        barDataSet.setValueFormatter(new StackedBarFormatter(" | ", 0));


        BarChart barChart = findViewById(R.id.bar_chart_coupe_essence);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);


        XAxis xAxis = barChart.getXAxis();
        YAxis yAxisLeft = barChart.getAxisLeft();

        // Empêcher zoom
        barChart.setScaleEnabled(false);


        // Afficher toutes les valeurs en X
        xAxis.setLabelCount(entriesEssences.size());

        // Axe des X affiche les essences
        xAxis.setValueFormatter(new IndexAxisValueFormatter(entriesEssences));
        // Axe des X en bas du graphe
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Axe des Y droit désactivé
        barChart.getAxisRight().setEnabled(false);


        // Ne pas dessiner la grille de fond
        yAxisLeft.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);

        // Enlever espace entre axe des X et les stack bars
        yAxisLeft.setDrawZeroLine(true);
        yAxisLeft.setAxisMinimum(0f);

        // Enlever "description label"
        barChart.getDescription().setEnabled(false);


        //barChart.getXAxis().setDrawLabels(true);

        /* ************************ LEGENDE ****************
         *
         *
         */
        // Récupération de la légende du graphe
        Legend legende = barChart.getLegend();
        // Forme de la légende
        legende.setForm(Legend.LegendForm.SQUARE);
        // Taille de la forme de la légende
        legende.setFormSize(10f);
        // Taille du texte de la légende
        legende.setTextSize(12f);

        // Position de la légende (changer si besoin)
        //legende.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        // ArrayList contenant les textes des états des arbres pour la légende
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("Arbres Restants");
        titleList.add("Arbres Marteles");

        // Listes des Entrées de la légende
        ArrayList<LegendEntry> legendeEntrees = new ArrayList<>();
        for (int k = 0; k < titleList.size(); k++) {
            // Création d'une nouvelle entrée de légende
            LegendEntry entree = new LegendEntry();
            // Récupération de la couleur "k" de l'arrayList colors
            entree.formColor = colors.get(k);
            // Récupération du label "k" de l'arrayList titleList
            entree.label = titleList.get(k);
            legendeEntrees.add(entree);
        }

        // Set la légende avec les entrées
        legende.setCustom(legendeEntrees);

        // Refresh le graphe
        barChart.invalidate();
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
