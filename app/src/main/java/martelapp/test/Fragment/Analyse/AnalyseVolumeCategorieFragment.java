package martelapp.test.Fragment.Analyse;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

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
        colors.add(getResources().getColor(R.color.colorBarBlue));
        colors.add(getResources().getColor(R.color.colorBarOrange));
        colors.add(getResources().getColor(R.color.colorBarViolet));

        pieDataSetAvant.setColors(colors);

        pieDataSetAvant.setValueFormatter(new PercentWithoutSmallValueFormatter());


        PieChart pieChartAvant = view.findViewById(R.id.pie_chart_volume_categorie_diametre_avant);
        PieData pieDataAvant = new PieData(pieDataSetAvant);
        pieChartAvant.setData(pieDataAvant);


        pieChartAvant.getDescription().setText("Avant");
        pieChartAvant.getDescription().setTextSize(18f);
        pieChartAvant.getDescription().setPosition(70f,70f);

        pieChartAvant.setTouchEnabled(false);

        pieChartAvant.setExtraOffsets(10f,10f,10f,10f);

        // Désactiver le trou du pie chart
        pieChartAvant.setDrawHoleEnabled(false);




        /*
         *##################################
         *########## GRAPHE AVANT ##########
         *##################################
         */
        PieDataSet pieDataSetApres = new PieDataSet(entriesVolumeApres, "");

        pieDataSetApres.setColors(colors);

        pieDataSetApres.setValueFormatter(new PercentWithoutSmallValueFormatter());


        PieChart pieChartApres = view.findViewById(R.id.pie_chart_volume_categorie_diametre_apres);
        PieData pieDataApres = new PieData(pieDataSetApres);
        pieChartApres.setData(pieDataApres);



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

        return view;
    }
}
