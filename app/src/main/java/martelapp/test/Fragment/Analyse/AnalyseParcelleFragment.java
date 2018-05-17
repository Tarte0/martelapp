package martelapp.test.Fragment.Analyse;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.VectorShapeRenderer;
import martelapp.test.R;

/**
 * Created by cimin on 24/04/2018.
 */

public class AnalyseParcelleFragment extends Fragment {
    Cursor cur;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_analyse_parcelle, null);

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


        scatterDataSetMartele.setShapeRenderer(new VectorShapeRenderer(view.getContext(), R.drawable.marteau_tab));

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
            scatterDataSetConserve.setShapeRenderer(new VectorShapeRenderer(view.getContext(), R.drawable.info_eco));
            scatterDataSetConserve.setDrawValues(false);
            //scatterDataSetConserve.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
            listScatterData.add(scatterDataSetConserve);
        }

        ScatterData scatterData = new ScatterData(listScatterData);

        ScatterChart scatterChart = view.findViewById(R.id.scatter_chart_carte);
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


        // ArrayList contenant les textes des états des arbres pour la légende
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("Arbres non martelés");
        titleList.add("Arbres écologiques");

        // Listes des Entrées de la légende
        ArrayList<LegendEntry> legendeEntrees = new ArrayList<>();
        for (int k = 0; k < titleList.size(); k++) {
            // Création d'une nouvelle entrée de légende
            LegendEntry entree = new LegendEntry();
            // Récupération de la couleur "k" de l'arrayList colors
            entree.formColor = ColorTemplate.JOYFUL_COLORS[3];
            // Récupération du label "k" de l'arrayList titleList
            entree.label = titleList.get(k);
            legendeEntrees.add(entree);
        }

        // Set la légende avec les entrées
        legende.setCustom(legendeEntrees);
        // Refresh le graphe
        scatterChart.invalidate();

        return view;
    }
}
