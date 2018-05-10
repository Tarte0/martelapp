package martelapp.test.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by cimin on 24/04/2018.
 */

public class AnalyseRaisonsFragment extends Fragment {
    Cursor cur1, cur2;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_analyse_raisons, null);

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
         *  ArrayList<BarEntry> entriesRaisonPercentage : Liste où sont enregistrées toutes les données pour le graphe
         *  ArrayList<String> entriesRaison : Liste des raisons pour afficher les labels sur chaque barre
         *  String raison : On récupère les raisons à chaque itération pour trier les données par raison
         *  int i : Défini une nouvelle entrée de données (par essence) sur l'axe x du graphe
         *  int nbRaisonActuelle : Nombre d'occurrence pour une certaine raison
         *  int nbArbresMarteles : On récupère le nombre d'arbres martelés pour calculer le pourcentage
         *  float percentageRaisonActuel : calcul du pourcentage pour une raison donnée
         */
        ArrayList<BarEntry> entriesRaisonPercentage = new ArrayList<>();
        ArrayList<String> entriesRaison = new ArrayList<>();
        String raison;
        int i = 0;
        int nbRaisonActuelle;
        int nbArbresMarteles;
        float percentageRaisonsActuel;

        // Récupération du nombre total de raisons
        cur1 = dbHelper.getAllDataFromTable(DatabaseHelper.ARBRES_MARTELES_TABLE);
        cur1.moveToFirst();
        nbArbresMarteles = cur1.getCount();

        /*
         *  Cursor cur1 : Cursor pointant sur toutes les raisons possibles
         *
         *  On trie le graphe par raison donc on parcourt jusqu'à
         *  ce que le cur1 n'ai plus de raisons disponible
         */
        cur1 = dbHelper.getDataFromTable("DISTINCT " + DatabaseHelper.RAISON, DatabaseHelper.RAISON_TABLE + " ORDER BY " + DatabaseHelper.RAISON);
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
            percentageRaisonsActuel = ((float)(nbRaisonActuelle) / nbArbresMarteles) * 100;

            // Ajout dans la liste des données du graphe le pourcentage que l'on vient de calculer
            entriesRaisonPercentage.add(new BarEntry(i, percentageRaisonsActuel));

            i++;

        }
        cur1.close();
        cur2.close();
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

        BarChart barChartRaisonPercentage = view.findViewById(R.id.bar_chart_pourcentage_raison);
        BarDataSet barDataSet = new BarDataSet(entriesRaisonPercentage, "");

        barDataSet.setColors(getResources().getColor(R.color.colorBarBlue));


        barChartRaisonPercentage.setTouchEnabled(false);

        XAxis xAxis = barChartRaisonPercentage.getXAxis();
        YAxis yAxisL = barChartRaisonPercentage.getAxisLeft();
        YAxis yAxisR = barChartRaisonPercentage.getAxisRight();

        yAxisL.setTextSize(13f);

        yAxisL.setAxisMinimum(0f);
        yAxisL.setAxisMaximum(105f);

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
        xAxis.setGranularityEnabled(true);

        // Axe des X en bas du graphe
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(13f);

        barChartRaisonPercentage.getDescription().setText(getResources().getString(R.string.axe_raison));
        barChartRaisonPercentage.getDescription().setYOffset(-40f);
        barChartRaisonPercentage.getDescription().setTextSize(16f);
        barChartRaisonPercentage.getDescription().setTextColor(getResources().getColor(R.color.colorBlack));

        barChartRaisonPercentage.setViewPortOffsets(30f,0f,20f, 55f);

        // Ne pas dessiner la grille de fond
        barChartRaisonPercentage.getAxisLeft().setDrawGridLines(false);
        barChartRaisonPercentage.getXAxis().setDrawGridLines(false);

        // Ne pas afficher de légende
        barChartRaisonPercentage.getLegend().setEnabled(false);

        // Axe des Y droit désactivé
        barChartRaisonPercentage.getAxisRight().setEnabled(false);

        barChartRaisonPercentage.setExtraBottomOffset(15f);
        // Refresh le graphe
        barChartRaisonPercentage.invalidate();

        return view;
    }
}
