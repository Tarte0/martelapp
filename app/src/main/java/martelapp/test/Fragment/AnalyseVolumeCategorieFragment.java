package martelapp.test.Fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;

import martelapp.test.Class.ChartHelper;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Formatter.PercentWithoutSmallValueFormatter;
import martelapp.test.R;

/**
 * Created by cimin on 24/04/2018.
 */

public class AnalyseVolumeCategorieFragment extends Fragment {
    public static final int PETIT_BOIS = 25;
    public static final int GROS_BOIS = 40;

    DatabaseHelper dbHelper;
    Cursor cur1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_volume_categories, null);

        dbHelper = new DatabaseHelper(view.getContext());
        DecimalFormat df = new DecimalFormat("#0.00");

        /*
         *  Création de la première ligne du tableau correspondant aux headers.
         *  Ajout de chaque valeur du tableau de String headers dans chaque
         *  colonne de cette ligne
         */
        TableLayout tableau_coupe_essence = view.findViewById(R.id.tableau_volume_categorie_diametre);

        TableRow tableRow = new TableRow(view.getContext());
        tableau_coupe_essence.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tableau_coupe_essence.setBackgroundColor(Color.GRAY);

        String[] headers = {"", "Avant", "Coupe", "Après"};

        tableRow.setLayoutParams(new TableRow.LayoutParams(headers.length));

        for (int j = 0; j < headers.length; j++) {
            TextView text = ChartHelper.createTextView(false, j == headers.length - 1, view);
            text.setText(headers[j]);
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


        ArrayList<PieEntry> entriesVolume = new ArrayList<>();
        ArrayList<String> entriesLabel = new ArrayList<>();

        String[] categorie_bois = {"PB", "BM", "GB", "V total(m3)"};

        /*
         *  float[][] volumeBois
         *
         *  indice 0 :
         *  - volumePBAvant
         *  - volumePBCoupe
         *  - volumePBApres
         *  - pourcentagePBCoupe
         *
         *  indice 1 :
         *  - volumeBMAvant
         *  - volumeBMCoupe
         *  - volumeBMApres
         *  - pourcentageBMCoupe
         *
         *  indice 2 :
         *  - volumeGBAvant
         *  - volumeGBCoupe
         *  - volumeGBApres
         *  - pourcentageGBCoupe
         *
         *  indice 3 :
         *  - totalVolumeAvant
         *  - totalVolumeCoupe
         *  - totalVolumeApres
         */
        float[][] volumeBois = new float[4][4];

        // AVANT
        cur1 = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")", DatabaseHelper.ARBRES_PARCELLE_TABLE,
                DatabaseHelper.DIAMETRE_ARBRE + " <= " + PETIT_BOIS);
        cur1.moveToFirst();
        volumeBois[0][0] = cur1.getFloat(0);

        cur1 = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")", DatabaseHelper.ARBRES_PARCELLE_TABLE,
                DatabaseHelper.DIAMETRE_ARBRE + " > " + PETIT_BOIS + " AND " + DatabaseHelper.DIAMETRE_ARBRE + " <= " + GROS_BOIS);
        cur1.moveToFirst();
        volumeBois[1][0] = cur1.getFloat(0);


        cur1 = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")", DatabaseHelper.ARBRES_PARCELLE_TABLE,
                DatabaseHelper.DIAMETRE_ARBRE + " > " +  GROS_BOIS);
        cur1.moveToFirst();
        volumeBois[2][0] = cur1.getFloat(0);

        volumeBois[3][0] = volumeBois[0][0] + volumeBois[1][0] + volumeBois[2][0];

        // COUPE
        cur1 = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                        + " AND " + DatabaseHelper.DIAMETRE_ARBRE + " <= " + PETIT_BOIS);
        cur1.moveToFirst();
        volumeBois[0][1] = cur1.getFloat(0);

        cur1 = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                        + " AND " + DatabaseHelper.DIAMETRE_ARBRE + " > " + PETIT_BOIS + " AND " + DatabaseHelper.DIAMETRE_ARBRE + " <= " + GROS_BOIS);
        cur1.moveToFirst();
        volumeBois[1][1] = cur1.getFloat(0);

        cur1 = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                        + " AND " + DatabaseHelper.DIAMETRE_ARBRE + " > " +  GROS_BOIS);
        cur1.moveToFirst();
        volumeBois[2][1] = cur1.getFloat(0);

        volumeBois[3][1] = volumeBois[0][1] + volumeBois[1][1] + volumeBois[2][1];

        // Volume Apres coupe
        volumeBois[0][2] = volumeBois[0][0] - volumeBois[0][1];

        volumeBois[1][2] = volumeBois[1][0] - volumeBois[1][1];

        volumeBois[2][2] = volumeBois[2][0] - volumeBois[2][1];

        volumeBois[3][2] = volumeBois[0][2] + volumeBois[1][2] + volumeBois[2][2];

        // Pourcentage volume
        volumeBois[3][1] = volumeBois[0][1] + volumeBois[1][1] + volumeBois[2][1];

        volumeBois[0][3] = (volumeBois[0][1] / volumeBois[3][1]) * 100;

        volumeBois[1][3] = (volumeBois[1][1] / volumeBois[3][1]) * 100;

        volumeBois[2][3] = (volumeBois[2][1] / volumeBois[3][1]) * 100;

        /*
         *  Création d'une nouvelle ligne dans le tableau
         *  et ajout de chaque valeur du tableau de String row
         *  dans chaque colonne du tableau pour cette ligne
         */
        for(int i = 0; i < 4; i++) {

            tableRow = new TableRow(view.getContext());
            tableau_coupe_essence.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            String[] row = {categorie_bois[i], df.format(volumeBois[i][0]), df.format(volumeBois[i][1]), df.format(volumeBois[i][2])};

            for (int j = 0; j < row.length; j++) {
                TextView text = ChartHelper.createTextView(i == 3, j == row.length - 1, view);
                text.setText(row[j]);
                if (j == 0) {
                    text.setTypeface(null, Typeface.BOLD);
                }
                tableRow.addView(text, j);
            }
        }

        for(int i = 0; i < 3; i++){
            entriesVolume.add(new PieEntry(volumeBois[i][3], i));
            entriesLabel.add(categorie_bois[i]);
        }

        cur1.close();
        dbHelper.close();



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

        PieDataSet pieDataSet = new PieDataSet(entriesVolume, "");


        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorBarBlue));
        colors.add(getResources().getColor(R.color.colorBarOrange));
        colors.add(getResources().getColor(R.color.colorBarViolet));

        pieDataSet.setColors(colors);

        pieDataSet.setValueFormatter(new PercentWithoutSmallValueFormatter());


        PieChart pieChart = view.findViewById(R.id.pie_chart_volume_categorie_diametre);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);


        // Enlever "description label"
        pieChart.getDescription().setEnabled(false);
        pieChart.setTouchEnabled(false);

        // Désactiver le trou du pie chart
        pieChart.setDrawHoleEnabled(false);


        //barChart.getXAxis().setDrawLabels(true);

        /* ************************ LEGENDE ****************
         *
         *
         */
        // Récupération de la légende du graphe
        Legend legende = pieChart.getLegend();
        // Forme de la légende
        legende.setYOffset(20f);
        legende.setForm(Legend.LegendForm.SQUARE);
        legende.setTextSize(20f);
        legende.setFormSize(12f);


        // Listes des Entrées de la légende
        ArrayList<LegendEntry> legendeEntrees = new ArrayList<>();
        for (int k = 0; k < entriesLabel.size(); k++) {
            // Création d'une nouvelle entrée de légende
            LegendEntry entree = new LegendEntry();
            // Récupération de la couleur "k" de l'arrayList colors
            entree.formColor = colors.get(k);
            // Récupération du label "k" de l'arrayList titleList
            entree.label = entriesLabel.get(k);
            legendeEntrees.add(entree);
        }

        // Set la légende avec les entrées
        legende.setCustom(legendeEntrees);


        // Refresh le graphe
        pieChart.invalidate();

        return view;
    }
}
