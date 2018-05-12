package martelapp.test.Fragment.Analyse;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Formatter.StackedBarFormatter;
import martelapp.test.R;

/**
 * Created by cimin on 24/04/2018.
 */

public class AnalyseTigesEcoFragment extends Fragment {
    Cursor cur1, cur2;
    DatabaseHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_tiges_eco, null);

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
         *  ArrayList<BarEntry> entriesArbres : Liste où sont enregistrées toutes les données pour le graphe
         *  ArrayList<String> entriesNoteEco : Liste des notes écologique pour afficher les labels sur chaque barre
         *  int noteEco : on récupère la note écologique à chaque itération pour trier les données par note écologique
         *  int i : Défini une nouvelle entrée de données (par essence) sur l'axe x du graphe
         *  int nbArbreAvant : Nombre d'arbres initialement dans la parcelle par note écologique
         *  int nbArbreCoupe : Nombre d'arbres martelés par note écologique
         *  int nbArbreApresCoupe : Nombre d'arbres restant sur la parcelle après coupe par note écologique
         */
        ArrayList<BarEntry> entriesArbres = new ArrayList<>();
        ArrayList<String> entriesNoteEco = new ArrayList<>();
        int noteEco;
        int i = 0;
        int nbArbreAvant;
        int nbArbreCoupe;
        int nbArbreApresCoupe;

        /*
         *  Cursor cur1 : Cursor pointant sur toutes les notes écologique dans la base de données des arbres de la parcelle
         *
         *  On trie le graphe par note écologique donc on parcourt jusqu'à
         *  ce que le cur1 n'ai plus de notes écologique disponible
         */
        cur1 = dbHelper.getDataFromTable("DISTINCT " + DatabaseHelper.NOTE_ECO_ARBRE, DatabaseHelper.ARBRES_PARCELLE_TABLE + " ORDER BY " + DatabaseHelper.NOTE_ECO_ARBRE + " ASC");
        while(cur1.moveToNext()){

            // Récupération de la note écologique actuelle et ajout dans la liste des notes écologique
            noteEco = cur1.getInt(cur1.getColumnIndex(DatabaseHelper.NOTE_ECO_ARBRE));
            entriesNoteEco.add(Integer.toString(noteEco));

            /*
             *  Récupération du nombre d'arbres martelés de la note écologique actuelle
             *  et enregistrement de ce nombre dans nbArbreCoupe
             */
            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                    "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                            + " AND ap." + DatabaseHelper.NOTE_ECO_ARBRE + " = " + noteEco);
            cur2.moveToFirst();
            nbArbreCoupe = cur2.getCount();


            /*
             *  Récupération du nombre d'arbres restant dans la parcelle
             *  de la note écologique actuelle et enregistrement de ce nombre dans
             *  nbArbreApresCoupe
             *
             *  nbArbreApresCoupe prend le nombre d'arbre total dans la
             *  parcelle moins le nbArbreCoupe pour la note actuelle actuelle
             */
            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE, DatabaseHelper.NOTE_ECO_ARBRE + " = " + noteEco);
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

        BarDataSet barDataSet = new BarDataSet(entriesArbres, "");

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorBarBlue));
        colors.add(getResources().getColor(R.color.colorBarOrange));

        barDataSet.setColors(colors);

        barDataSet.setValueFormatter(new StackedBarFormatter(" | ", 0));


        BarChart barChart = view.findViewById(R.id.bar_chart_note_eco);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);


        XAxis xAxis = barChart.getXAxis();
        YAxis yAxisLeft = barChart.getAxisLeft();

        // Empêcher zoom
        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(false);


        // Afficher toutes les valeurs en X
        xAxis.setLabelCount(entriesNoteEco.size());

        // Axe des X affiche les essences
        xAxis.setValueFormatter(new IndexAxisValueFormatter(entriesNoteEco));
        // Axe des X en bas du graphe
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setTextSize(18f);
        yAxisLeft.setTextSize(18f);

        // Axe des Y droit désactivé
        barChart.getAxisRight().setEnabled(false);


        // Ne pas dessiner la grille de fond
        yAxisLeft.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);

        // Enlever espace entre axe des X et les stack bars
        yAxisLeft.setDrawZeroLine(true);
        yAxisLeft.setAxisMinimum(0f);

        barChart.getDescription().setText(getResources().getString(R.string.axe_note_eco));
        barChart.getDescription().setYOffset(-50f);
        barChart.getDescription().setTextSize(18f);
        barChart.getDescription().setTextColor(getResources().getColor(R.color.colorBlack));

        //barChart.getXAxis().setDrawLabels(true);

        /* ************************ LEGENDE ****************
         *
         *
         */
        // Récupération de la légende du graphe
        Legend legende = barChart.getLegend();
        // Forme de la légende
        legende.setForm(Legend.LegendForm.SQUARE);
        legende.setTextSize(20f);
        legende.setFormSize(12f);

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

        return view;
    }
}
