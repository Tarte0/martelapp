package martelapp.test.Fragment.Analyse;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.VectorShapeRenderer;
import martelapp.test.Formatter.IndexAxisValueLimitCharacterFormatter;
import martelapp.test.Formatter.PercentWithoutSmallValueFormatter;
import martelapp.test.Formatter.StackedBarFormatter;
import martelapp.test.R;

/**
 * Created by Baptiste on 18/05/2018.
 */

public class AnalyseGraphe {
    public static final int PETIT_BOIS = 25;
    public static final int GROS_BOIS = 40;


    public static void getBarChartAnalyseEssence(Context context, BarChart barChart){
        Cursor cur1, cur2;
        DatabaseHelper dbHelper;

        dbHelper = new DatabaseHelper(context);

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
        cur1 = dbHelper.getDataFromTable("DISTINCT " + DatabaseHelper.ESSENCE_ARBRE, DatabaseHelper.ARBRES_PARCELLE_TABLE + " ORDER BY " + DatabaseHelper.ESSENCE_ARBRE);
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
            cur2.close();
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

        BarDataSet barDataSet = new BarDataSet(entriesArbres, "");

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(context.getResources().getColor(R.color.colorBarBlue));
        colors.add(context.getResources().getColor(R.color.colorBarOrange));

        barDataSet.setColors(colors);

        barDataSet.setValueFormatter(new StackedBarFormatter(" | ", 0));

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        YAxis yAxisLeft = barChart.getAxisLeft();

        // Empêcher zoom
        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(false);


        // Afficher toutes les valeurs en X
        xAxis.setLabelCount(entriesEssences.size());

        // Axe des X affiche les essences
        xAxis.setValueFormatter(new IndexAxisValueLimitCharacterFormatter(entriesEssences, 20));
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

        barChart.getDescription().setText(context.getResources().getString(R.string.axe_essence));
        barChart.getDescription().setYOffset(-50f);
        barChart.getDescription().setTextSize(18f);
        barChart.getDescription().setTextColor(context.getResources().getColor(R.color.colorBlack));


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
        legende.setXEntrySpace(15f);

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

    public static void getBarChartAnalyseDiametre(Context context, BarChart barChart){
        Cursor cur1, cur2;
        DatabaseHelper dbHelper;

        dbHelper = new DatabaseHelper(context);

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
         *  ArrayList<String> entriesDiametre : Liste des diamètres pour afficher les labels sur chaque barre
         *  int diametre : on récupère le diamètre à chaque itération pour trier les données par diamètre
         *  int i : Défini une nouvelle entrée de données (par essence) sur l'axe x du graphe
         *  int nbArbreAvant : Nombre d'arbres initialement dans la parcelle par diamètre
         *  int nbArbreCoupe : Nombre d'arbres martelés par essence
         *  int nbArbreApresCoupe : Nombre d'arbres restant sur la parcelle après coupe par essence
         */
        ArrayList<BarEntry> entriesArbres = new ArrayList<>();
        ArrayList<String> entriesDiametre = new ArrayList<>();
        int diametre;
        int i = 0;
        int nbArbreAvant;
        int nbArbreCoupe;
        int nbArbreApresCoupe;


        /*
         *  Cursor cur1 : Cursor pointant sur tout les diamètres d'arbre dans la base de données
         *
         *  On trie le graphe par diamètre donc on parcourt jusqu'à
         *  ce que le cur1 n'ai plus de diamètre disponible
         */
        cur1 = dbHelper.getDataFromTable("DISTINCT " + DatabaseHelper.DIAMETRE_ARBRE, DatabaseHelper.ARBRES_PARCELLE_TABLE + " ORDER BY " + DatabaseHelper.DIAMETRE_ARBRE + " ASC");
        while(cur1.moveToNext()){

            // Récupération de le diamètre actuelle et ajout dans la liste des diamètres
            diametre = cur1.getInt(cur1.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE));
            entriesDiametre.add(Integer.toString(diametre));

            /*
             *  Récupération du nombre d'arbres martelés du diamètre actuel
             *  et enregistrement de ce nombre dans nbArbreCoupe
             */
            cur2 = dbHelper.getDataFromTableWithCondition("*",DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                    "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                            + " AND ap." + DatabaseHelper.DIAMETRE_ARBRE + " = " + diametre);
            cur2.moveToFirst();
            nbArbreCoupe = cur2.getCount();


            /*
             *  Récupération du nombre d'arbres restant dans la parcelle
             *  du diamètre actuel et enregistrement de ce nombre dans
             *  nbArbreApresCoupe
             *
             *  nbArbreApresCoupe prend le nombre d'arbre total dans la
             *  parcelle moins le nbArbreCoupe pour l'essence actuelle
             */
            cur2 = dbHelper.getDataFromTableWithCondition("*", DatabaseHelper.ARBRES_PARCELLE_TABLE, DatabaseHelper.DIAMETRE_ARBRE + " = " + diametre);
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


            cur2.close();
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

        BarDataSet barDataSet = new BarDataSet(entriesArbres, "");

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(context.getResources().getColor(R.color.colorBarBlue));
        colors.add(context.getResources().getColor(R.color.colorBarOrange));

        barDataSet.setColors(colors);

        barDataSet.setValueFormatter(new StackedBarFormatter(" | ", 0));

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);


        XAxis xAxis = barChart.getXAxis();
        YAxis yAxisLeft = barChart.getAxisLeft();

        // Empêcher zoom
        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(false);


        // Afficher toutes les valeurs en X
        xAxis.setLabelCount(entriesDiametre.size());

        // Axe des X affiche les essences
        xAxis.setValueFormatter(new IndexAxisValueFormatter(entriesDiametre));
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

        barChart.getDescription().setText(context.getResources().getString(R.string.axe_diametre_cm));
        barChart.getDescription().setYOffset(-50f);
        barChart.getDescription().setTextSize(18f);
        barChart.getDescription().setTextColor(context.getResources().getColor(R.color.colorBlack));



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
        legende.setXEntrySpace(15f);

        // Refresh le graphe
        barChart.invalidate();
    }

    public static void getBarChartAnalyseNoteEcologique(Context context, BarChart barChart){
        Cursor cur1, cur2;
        DatabaseHelper dbHelper;

        dbHelper = new DatabaseHelper(context);

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

            cur2.close();
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

        BarDataSet barDataSet = new BarDataSet(entriesArbres, "");

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(context.getResources().getColor(R.color.colorBarBlue));
        colors.add(context.getResources().getColor(R.color.colorBarOrange));

        barDataSet.setColors(colors);

        barDataSet.setValueFormatter(new StackedBarFormatter(" | ", 0));

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

        barChart.getDescription().setText(context.getResources().getString(R.string.axe_note_eco));
        barChart.getDescription().setYOffset(-50f);
        barChart.getDescription().setTextSize(18f);
        barChart.getDescription().setTextColor(context.getResources().getColor(R.color.colorBlack));

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
        legende.setXEntrySpace(15f);

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

    public static void getBarChartAnalyseVolume(Context context, BarChart barChart){
        Cursor cur1, cur2;
        DatabaseHelper dbHelper;

        dbHelper = new DatabaseHelper(context);

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
         *  ArrayList<String> entriesDiametre : Liste des diamètres pour afficher les labels sur chaque barre
         *  int diametre : on récupère le diamètre à chaque itération pour trier les données par diamètre
         *  int i : Défini une nouvelle entrée de données (par essence) sur l'axe x du graphe
         *  float volumeArbreAvant : Volume d'arbre initialement dans la parcelle par diamètre
         *  float volumeArbreCoupe : Volume d'arbre martelé par diamètre
         *  float volumeArbreApresCoupe : Volume d'arbre restant sur la parcelle après coupe par diamètre
         */
        ArrayList<BarEntry> entriesVolumeArbre = new ArrayList<>();
        ArrayList<String> entriesDiametre = new ArrayList<>();
        int diametre;
        int i = 0;
        float volumeArbreAvant;
        float volumeArbreCoupe;
        float volumeArbreApresCoupe;


        /*
         *  Cursor cur1 : Cursor pointant sur tout les diamètres d'arbre dans la base de données
         *
         *  On trie le graphe par diamètre donc on parcourt jusqu'à
         *  ce que le cur1 n'ai plus de diamètre disponible
         */
        cur1 = dbHelper.getDataFromTable("DISTINCT " + DatabaseHelper.DIAMETRE_ARBRE, DatabaseHelper.ARBRES_PARCELLE_TABLE + " ORDER BY " + DatabaseHelper.DIAMETRE_ARBRE + " ASC");
        while(cur1.moveToNext()){

            // Récupération de le diamètre actuelle et ajout dans la liste des diamètres
            diametre = cur1.getInt(cur1.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE));
            entriesDiametre.add(Integer.toString(diametre));

            /*
             *  Récupération du nombre d'arbres martelés du diamètre actuel
             *  et enregistrement de ce nombre dans nbArbreCoupe
             */
            cur2 = dbHelper.getDataFromTableWithCondition("*",DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                    "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                            + " AND ap." + DatabaseHelper.DIAMETRE_ARBRE + " = " + diametre);

            volumeArbreCoupe = 0f;
            while(cur2.moveToNext()){
                volumeArbreCoupe += cur2.getDouble(cur2.getColumnIndex(DatabaseHelper.VOLUME_COMMERCIAL));
            }


            /*
             *  Récupération du nombre d'arbres restant dans la parcelle
             *  du diamètre actuel et enregistrement de ce nombre dans
             *  nbArbreApresCoupe
             *
             *  nbArbreApresCoupe prend le nombre d'arbre total dans la
             *  parcelle moins le nbArbreCoupe pour l'essence actuelle
             */
            cur2 = dbHelper.getDataFromTableWithCondition("*", DatabaseHelper.ARBRES_PARCELLE_TABLE, DatabaseHelper.DIAMETRE_ARBRE + " = " + diametre);

            volumeArbreAvant = 0f;
            while(cur2.moveToNext()){
                volumeArbreAvant += cur2.getDouble(cur2.getColumnIndex(DatabaseHelper.VOLUME_COMMERCIAL));
            }

            volumeArbreApresCoupe = volumeArbreAvant - volumeArbreCoupe;

            /*
             *  On ajoute dans la liste des données du graphe les deux valeurs que l'on vient
             *  de calculer sous forme de tableau de float car on veut que les données soit
             *  sous forme de bar chart stack
             */
            entriesVolumeArbre.add(new BarEntry(i, new float[]{volumeArbreApresCoupe, volumeArbreCoupe}));

            i++;

            cur2.close();
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

        BarDataSet barDataSet = new BarDataSet(entriesVolumeArbre, "");

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(context.getResources().getColor(R.color.colorBarBlue));
        colors.add(context.getResources().getColor(R.color.colorBarOrange));

        barDataSet.setColors(colors);

        barDataSet.setValueFormatter(new StackedBarFormatter(" | ", 2));


        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);


        XAxis xAxis = barChart.getXAxis();
        YAxis yAxisLeft = barChart.getAxisLeft();

        // Empêcher zoom
        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(false);


        // Afficher toutes les valeurs en X
        xAxis.setLabelCount(entriesDiametre.size());

        // Axe des X affiche les essences
        xAxis.setValueFormatter(new IndexAxisValueFormatter(entriesDiametre));
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

        barChart.getDescription().setText(context.getResources().getString(R.string.axe_diametre_cm));
        barChart.getDescription().setYOffset(-50f);
        barChart.getDescription().setTextSize(18f);
        barChart.getDescription().setTextColor(context.getResources().getColor(R.color.colorBlack));


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
        legende.setXEntrySpace(15f);

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

    public static void getPieChartAnalyseAvantApresVolumeCategorie(Context context, PieChart pieChartAvant, PieChart pieChartApres){
        Cursor cur1;
        DatabaseHelper dbHelper;

        dbHelper = new DatabaseHelper(context);

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


        ArrayList<PieEntry> entriesVolumeAvant = new ArrayList<>();
        ArrayList<PieEntry> entriesVolumeApres = new ArrayList<>();
        ArrayList<String> entriesLabel = new ArrayList<>();

        String[] categorie_bois = {"PB", "BM", "GB"};

        /*
         *  float[][] volumeBois
         *
         *  indice 0 :
         *  - volumePBAvant
         *  - volumePBApres
         *  - pourcentagePBAvant
         *  - pourcentagePBApres
         *
         *  indice 1 :
         *  - volumeBMAvant
         *  - volumeBMApres
         *  - pourcentageBMAvant
         *  - pourcentageBMApres
         *
         *  indice 2 :
         *  - volumeGBAvant
         *  - volumeGBApres
         *  - pourcentageGBAvant
         *  - pourcentageGBApres

         */
        float[][] volumeBois = new float[3][4];
        float totalVolumeAvant;
        float totalVolumeApres;

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

        totalVolumeAvant = volumeBois[0][0] + volumeBois[1][0] + volumeBois[2][0];

        // APRES
        cur1 = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                        + " AND " + DatabaseHelper.DIAMETRE_ARBRE + " <= " + PETIT_BOIS);
        cur1.moveToFirst();
        volumeBois[0][1] =  volumeBois[0][0] - cur1.getFloat(0);

        cur1 = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                        + " AND " + DatabaseHelper.DIAMETRE_ARBRE + " > " + PETIT_BOIS + " AND " + DatabaseHelper.DIAMETRE_ARBRE + " <= " + GROS_BOIS);
        cur1.moveToFirst();
        volumeBois[1][1] = volumeBois[1][0] - cur1.getFloat(0);

        cur1 = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                        + " AND " + DatabaseHelper.DIAMETRE_ARBRE + " > " +  GROS_BOIS);
        cur1.moveToFirst();
        volumeBois[2][1] = volumeBois[2][0] - cur1.getFloat(0);

        totalVolumeApres = volumeBois[0][1] + volumeBois[1][1] + volumeBois[2][1];

        // Pourcentage volume Avant
        volumeBois[0][2] = (volumeBois[0][0] / totalVolumeAvant) * 100;

        volumeBois[1][2] = (volumeBois[1][0] / totalVolumeAvant) * 100;

        volumeBois[2][2] = (volumeBois[2][0] / totalVolumeAvant) * 100;


        // Pourcentage volume Apres
        volumeBois[0][3] = (volumeBois[0][1] / totalVolumeApres) * 100;

        volumeBois[1][3] = (volumeBois[1][1] / totalVolumeApres) * 100;

        volumeBois[2][3] = (volumeBois[2][1] / totalVolumeApres) * 100;


        for(int i = 0; i < 3; i++){
            entriesVolumeAvant.add(new PieEntry(volumeBois[i][2], i));
            entriesVolumeApres.add(new PieEntry(volumeBois[i][3], i));
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


        /*
         *##################################
         *########## GRAPHE AVANT ##########
         *##################################
         */
        PieDataSet pieDataSetAvant = new PieDataSet(entriesVolumeAvant, "");


        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(context.getResources().getColor(R.color.colorBarBlue));
        colors.add(context.getResources().getColor(R.color.colorBarOrange));
        colors.add(context.getResources().getColor(R.color.colorBarGreen));

        pieDataSetAvant.setColors(colors);

        pieDataSetAvant.setValueFormatter(new PercentWithoutSmallValueFormatter());


        PieData pieDataAvant = new PieData(pieDataSetAvant);
        pieChartAvant.setData(pieDataAvant);


        pieDataSetAvant.setValueTextSize(22f);
        pieDataSetAvant.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        pieDataSetAvant.setValueTextColor(context.getResources().getColor(R.color.colorWhite));

        pieChartAvant.getDescription().setText("Avant");
        pieChartAvant.getDescription().setTextSize(18f);
        pieChartAvant.getDescription().setPosition(70f,70f);

        pieChartAvant.setTouchEnabled(false);

        pieChartAvant.setExtraOffsets(10f,10f,10f,10f);

        // Désactiver le trou du pie chart
        pieChartAvant.setDrawHoleEnabled(false);




        /*
         *##################################
         *########## GRAPHE APRES ##########
         *##################################
         */
        PieDataSet pieDataSetApres = new PieDataSet(entriesVolumeApres, "");

        pieDataSetApres.setColors(colors);

        pieDataSetApres.setValueFormatter(new PercentWithoutSmallValueFormatter());


        PieData pieDataApres = new PieData(pieDataSetApres);
        pieChartApres.setData(pieDataApres);


        pieDataSetApres.setValueTextSize(22f);
        pieDataSetApres.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        pieDataSetApres.setValueTextColor(context.getResources().getColor(R.color.colorWhite));

        pieChartApres.getDescription().setText("Après");
        pieChartApres.getDescription().setTextSize(18f);
        pieChartApres.getDescription().setPosition(70f,70f);
        pieChartApres.setTouchEnabled(false);

        pieChartApres.setExtraOffsets(10f,10f,10f,10f);

        // Désactiver le trou du pie chart
        pieChartApres.setDrawHoleEnabled(false);






        /* ************************ LEGENDE ****************
         *
         *
         */
        // Récupération de la légende du graphe
        Legend legendeAvant = pieChartAvant.getLegend();
        Legend legendeApres = pieChartApres.getLegend();
        // Forme de la légende
        legendeAvant.setYOffset(20f);
        legendeAvant.setForm(Legend.LegendForm.SQUARE);
        legendeAvant.setTextSize(20f);
        legendeAvant.setFormSize(12f);
        legendeAvant.setXEntrySpace(15f);

        legendeApres.setYOffset(20f);
        legendeApres.setForm(Legend.LegendForm.SQUARE);
        legendeApres.setTextSize(20f);
        legendeApres.setFormSize(12f);
        legendeApres.setXEntrySpace(15f);

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
        legendeAvant.setCustom(legendeEntrees);
        legendeApres.setCustom(legendeEntrees);


        // Refresh le graphe
        pieChartAvant.invalidate();
        pieChartApres.invalidate();

    }

    public static void getGrapheAnalyseRaisons(Context context, BarChart barChart){
        Cursor cur1, cur2;
        DatabaseHelper dbHelper;

        dbHelper = new DatabaseHelper(context);


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
            if(!raison.equals(DatabaseHelper.BIODIVERSITE)) {

                entriesRaison.add(raison);

                /*
                *  Récupération du nombre de raison pour la raison actuelle
                *  et calcul du pourcentage de cette raison actuelle
                */
                cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.RAISON_TABLE, DatabaseHelper.RAISON + " = '" + raison + "'");
                cur2.moveToFirst();
                nbRaisonActuelle = cur2.getCount();
                percentageRaisonsActuel = ((float) (nbRaisonActuelle) / nbArbresMarteles) * 100;

                // Ajout dans la liste des données du graphe le pourcentage que l'on vient de calculer
                entriesRaisonPercentage.add(new BarEntry(i, percentageRaisonsActuel));

                i++;

                cur2.close();
            }
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

        BarDataSet barDataSet = new BarDataSet(entriesRaisonPercentage, "");

        barDataSet.setColors(context.getResources().getColor(R.color.colorBarBlue));


        barChart.setTouchEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        YAxis yAxisL = barChart.getAxisLeft();
        YAxis yAxisR = barChart.getAxisRight();

        yAxisL.setTextSize(18f);

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
        AxisBase axisBase = barChart.getAxisLeft();
        axisBase.setDrawGridLines(true);

        barDataSet.setValueFormatter(new PercentFormatter());

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        // Afficher les valeurs en X
        xAxis.setLabelCount(entriesRaison.size());


        // Axe des X affiche les raisons
        xAxis.setValueFormatter(new IndexAxisValueFormatter(entriesRaison));
        xAxis.setGranularityEnabled(true);

        // Axe des X en bas du graphe
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(18f);

        barChart.getDescription().setText(context.getResources().getString(R.string.axe_raison));
        barChart.getDescription().setYOffset(-50f);
        barChart.getDescription().setTextSize(18f);
        barChart.getDescription().setTextColor(context.getResources().getColor(R.color.colorBlack));

        //barChart.setViewPortOffsets(30f,0f,20f, 55f);

        // Ne pas dessiner la grille de fond
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);

        // Ne pas afficher de légende
        barChart.getLegend().setEnabled(false);

        // Axe des Y droit désactivé
        barChart.getAxisRight().setEnabled(false);

        barChart.setExtraBottomOffset(50f);
        // Refresh le graphe
        barChart.invalidate();
    }

    public static void getScatterChartAnalyseCarte(Context context, ScatterChart scatterChart){
        Cursor cur;
        DatabaseHelper dbHelper;

        dbHelper = new DatabaseHelper(context);

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
         *  ArrayList<Entry> entriesPositionArbreConserve : Liste des position des arbres CONSERVES pour le graphe
         *  ArrayList<Entry> entriesPositionArbreEco : Liste des position des arbres ECOLOGIQUE pour le graphe
         *  float x, y : variable intermédiaire pour enregistrer la position dans les listes
         *  int noteEco : note écologique d'un arbre pour savoir dans quelle liste l'enregistrer
         */
        ArrayList<Entry> entriesPositionArbreNonMartele = new ArrayList<>();
        ArrayList<Entry> entriesPositionArbreMartele = new ArrayList<>();
        ArrayList<Entry> entriesPositionArbreConserve = new ArrayList<>();
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
         *  Récupération de la position x, y des arbres CONSERVES
         */
        cur = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac",
                "ap." + dbHelper.NUMERO_ARBRE_PARC + " = ac." + dbHelper.NUMERO_ARBRE_CONS);
        while(cur.moveToNext()){
            x = (float) cur.getDouble(cur.getColumnIndex(DatabaseHelper.COORD_X_ARBRE));
            y = (float) cur.getDouble(cur.getColumnIndex(DatabaseHelper.COORD_Y_ARBRE));

            entriesPositionArbreConserve.add(new Entry(x, y));
        }
        cur.close();
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

        Collections.sort(entriesPositionArbreNonMartele, new EntryXComparator());
        Collections.sort(entriesPositionArbreEco, new EntryXComparator());
        Collections.sort(entriesPositionArbreMartele, new EntryXComparator());


        ArrayList<IScatterDataSet> listScatterData = new ArrayList<>();
        ScatterDataSet scatterDataSetNonMartele = new ScatterDataSet(entriesPositionArbreNonMartele, "");
        ScatterDataSet scatterDataSetNonMarteleEco = new ScatterDataSet(entriesPositionArbreEco, "");
        ScatterDataSet scatterDataSetMartele = new ScatterDataSet(entriesPositionArbreMartele, "");



        // Couleur des arbres
        scatterDataSetNonMartele.setColor(ColorTemplate.JOYFUL_COLORS[3]);
        scatterDataSetNonMarteleEco.setColor(ColorTemplate.JOYFUL_COLORS[1]);


        scatterDataSetMartele.setShapeRenderer(new VectorShapeRenderer(context, R.drawable.marteau_tab));

        scatterDataSetNonMartele.setDrawValues(false);
        scatterDataSetNonMarteleEco.setDrawValues(false);
        scatterDataSetMartele.setDrawValues(false);



        // Forme des arbres non martelés = cercle
        scatterDataSetNonMartele.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        // Forme des arbres non martelés Eco = cercle
        scatterDataSetNonMarteleEco.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        // Forme des arbres martelés = X
        //scatterDataSetMartele.setScatterShape(ScatterChart.ScatterShape.X);
        // Forme des arbres conservés = cercle



        listScatterData.add(scatterDataSetNonMartele);
        listScatterData.add(scatterDataSetNonMarteleEco);
        listScatterData.add(scatterDataSetMartele);

        if(!(entriesPositionArbreConserve.isEmpty())) {
            Collections.sort(entriesPositionArbreConserve, new EntryXComparator());
            ScatterDataSet scatterDataSetConserve = new ScatterDataSet(entriesPositionArbreConserve, "");
            scatterDataSetConserve.setShapeRenderer(new VectorShapeRenderer(context, R.drawable.info_eco));
            scatterDataSetConserve.setDrawValues(false);
            //scatterDataSetConserve.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
            listScatterData.add(scatterDataSetConserve);
        }

        ScatterData scatterData = new ScatterData(listScatterData);

        scatterChart.setHardwareAccelerationEnabled(false);
        scatterChart.setData(scatterData);

        // Empêcher zoom
        //scatterChart.setScaleEnabled(false);

        scatterChart.setPinchZoom(true);

        // Désactiver le clic
        //scatterChart.setTouchEnabled(false);

        // Enlever indication de la sélection d'un point lorsqu'on appuie sur le graphe
        scatterChart.getData().setHighlightEnabled(false);

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
        legende.setXEntrySpace(15f);


        LegendEntry entree1 = new LegendEntry();
        LegendEntry entree2 = new LegendEntry();

        entree1.label = "Arbres";
        entree1.formColor = ColorTemplate.JOYFUL_COLORS[3];

        entree2.label = "Arbres avec note écologique ≥ 6";
        entree2.formColor = ColorTemplate.JOYFUL_COLORS[1];

        // Listes des Entrées de la légende
        ArrayList<LegendEntry> legendeEntrees = new ArrayList<>();
        legendeEntrees.add(entree1);
        legendeEntrees.add(entree2);

        // Set la légende avec les entrées
        legende.setCustom(legendeEntrees);
        // Refresh le graphe
        scatterChart.invalidate();
    }
}
