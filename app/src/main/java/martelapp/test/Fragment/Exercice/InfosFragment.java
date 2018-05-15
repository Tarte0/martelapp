package martelapp.test.Fragment.Exercice;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.xml.sax.DTDHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.OnSwipeTouchListener;
import martelapp.test.Formatter.PercentWithoutSmallValueFormatter;
import martelapp.test.Formatter.StackedBarFormatter;
import martelapp.test.Formatter.WithoutSmallValueFormatter;
import martelapp.test.R;

public class InfosFragment extends Fragment {

    ViewPager viewPager;

    BottomNavigationView bottomNavigationView;
    TextView textViewInfos, textViewTitleInfos;
    ImageButton previous, next;
    Button buttonGoToCarte;
    BarChart barChartDiametre;
    BarChart barChartNoteEco;
    PieChart pieChartEssence;

    DatabaseHelper dbHelper;
    Cursor cur1, cur2;

    DecimalFormat df;

    int altitude = 0,
            densiteVivantMortPied = 0,
            densiteMortSol = 0;

    double surfaceParcelle = 0f,
            volumeVivantMortPied = 0f,
            volumeMortSol = 0f;

    String habitat = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_infos, null);

        textViewInfos = (TextView) view.findViewById(R.id.textViewInfo);
        textViewTitleInfos = (TextView) view.findViewById(R.id.titleInfo);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation_info);
        previous = (ImageButton) view.findViewById(R.id.previousInfo);
        next = (ImageButton) view.findViewById(R.id.nextInfo);
        buttonGoToCarte = (Button) view.findViewById((R.id.button_go_to_carte));
        barChartDiametre = view.findViewById(R.id.bar_chart_diametre_info);
        barChartNoteEco = view.findViewById(R.id.bar_chart_note_eco_info);
        pieChartEssence = view.findViewById(R.id.pie_chart_essence_info);

        df = new DecimalFormat("#0.00");

        dbHelper = new DatabaseHelper(view.getContext());

        caracteristiqueParcelle();

        grapheDiametre(barChartDiametre);
        grapheNoteEcologique(barChartNoteEco);
        grapheEssence(pieChartEssence);

        dbHelper.close();
        cur1.close();
        cur2.close();

        //on gere le swipe gauche et droite (un peu brute)
        view.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
            public void onSwipeRight() {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_carte_id:
                        break;
                    case R.id.action_graphe_diametre:
                        bottomNavigationView.setSelectedItemId(R.id.action_carte_id);
                        break;
                    case R.id.action_graphe_note_eco:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_diametre);
                        break;
                    case R.id.action_graphe_essence:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_note_eco);
                        break;
                    case R.id.action_graphe_exploitabilite_rotation:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_essence);
                        break;
                }

            }

            public void onSwipeLeft() {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_carte_id:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_diametre);
                        break;
                    case R.id.action_graphe_diametre:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_note_eco);
                        break;
                    case R.id.action_graphe_note_eco:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_essence);
                        break;
                    case R.id.action_graphe_essence:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_exploitabilite_rotation);
                        break;
                    case R.id.action_graphe_exploitabilite_rotation:
                        break;
                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_carte_id:
                        break;
                    case R.id.action_graphe_diametre:
                        bottomNavigationView.setSelectedItemId(R.id.action_carte_id);
                        break;
                    case R.id.action_graphe_note_eco:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_diametre);
                        break;
                    case R.id.action_graphe_essence:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_note_eco);
                        break;
                    case R.id.action_graphe_exploitabilite_rotation:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_essence);
                        break;
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_carte_id:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_diametre);
                        break;
                    case R.id.action_graphe_diametre:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_note_eco);
                        break;
                    case R.id.action_graphe_note_eco:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_essence);
                        break;
                    case R.id.action_graphe_essence:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_exploitabilite_rotation);
                        break;
                    case R.id.action_graphe_exploitabilite_rotation:
                        break;
                }
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_carte_id:
                                textViewTitleInfos.setText(R.string.caracteristique_parcelle_caps);
                                textViewInfos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9));
                                textViewInfos.setTextSize(24f);
                                textViewInfos.setText(
                                        "• altitude : " + Integer.toString(altitude) + " mètres\n\n"
                                                + "• habitat naturel : " + habitat + "\n\n"
                                                + "• surface : " + df.format(surfaceParcelle) + " ha\n\n"
                                                + "• densité (vivants et morts sur pied) : " + Integer.toString(densiteVivantMortPied) + " tiges/ha\n\n"
                                                + "• volume : " + Integer.toString((int) volumeVivantMortPied) + " m3/ha\n\n"
                                                + "• densité de bois mort au sol : " + Integer.toString(densiteMortSol) + " tiges/ha\n\n"
                                                + "• volume de bois mort au sol : " + Integer.toString((int) volumeMortSol) + " m3/ha");
                                textViewInfos.setVisibility(View.VISIBLE);
                                previous.setVisibility(View.INVISIBLE);
                                next.setVisibility(View.VISIBLE);
                                buttonGoToCarte.setVisibility(View.GONE);
                                barChartDiametre.setVisibility(View.GONE);
                                barChartNoteEco.setVisibility(View.GONE);
                                pieChartEssence.setVisibility(View.GONE);
                                break;
                            case R.id.action_graphe_diametre:
                                textViewTitleInfos.setText(R.string.titre_graphe_nbtige_diametre_info);
                                textViewInfos.setVisibility(View.VISIBLE);
                                textViewInfos.setTextSize(18f);
                                textViewInfos.setText(R.string.axe_nombre_tiges);
                                textViewInfos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);
                                buttonGoToCarte.setVisibility(View.GONE);
                                barChartDiametre.setVisibility(View.VISIBLE);
                                barChartNoteEco.setVisibility(View.GONE);
                                pieChartEssence.setVisibility(View.GONE);
                                break;
                            case R.id.action_graphe_note_eco:
                                textViewTitleInfos.setText(R.string.titre_graphe_nbtige_noteeco_info);
                                textViewInfos.setVisibility(View.VISIBLE);
                                textViewInfos.setTextSize(18f);
                                textViewInfos.setText(R.string.axe_nombre_tiges);
                                textViewInfos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);
                                buttonGoToCarte.setVisibility(View.GONE);
                                barChartDiametre.setVisibility(View.GONE);
                                barChartNoteEco.setVisibility(View.VISIBLE);
                                pieChartEssence.setVisibility(View.GONE);
                                break;
                            case R.id.action_graphe_essence:
                                textViewTitleInfos.setText(R.string.titre_graphe_nbtige_essence_info);
                                textViewInfos.setVisibility(View.GONE);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);
                                buttonGoToCarte.setVisibility(View.GONE);
                                barChartDiametre.setVisibility(View.GONE);
                                barChartNoteEco.setVisibility(View.GONE);
                                pieChartEssence.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_graphe_exploitabilite_rotation:
                                textViewInfos.setTextSize(22f);
                                textViewInfos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9));
                                textViewInfos.setText(R.string.info_exploitabilite_rotation);
                                textViewTitleInfos.setText(R.string.information_supplementaire_caps);
                                textViewInfos.setVisibility(View.VISIBLE);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.INVISIBLE);
                                buttonGoToCarte.setVisibility(View.VISIBLE);
                                barChartDiametre.setVisibility(View.GONE);
                                barChartNoteEco.setVisibility(View.GONE);
                                pieChartEssence.setVisibility(View.GONE);
                                buttonGoToCarte.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });


        return view;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    public void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.action_carte_id);
    }


    private void caracteristiqueParcelle() {
        cur1 = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
        cur1.moveToFirst();

        altitude = (int) cur1.getDouble(cur1.getColumnIndex(DatabaseHelper.ALTITUDE_PARCELLE));
        surfaceParcelle = cur1.getDouble(cur1.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));
        habitat = cur1.getString(cur1.getColumnIndex(DatabaseHelper.HABITAT_PARCELLE));


        cur1 = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE,
                DatabaseHelper.ETAT_ARBRE + " = 'v'" +
                        " OR " + DatabaseHelper.ETAT_ARBRE + " = 'mp'");
        cur1.moveToFirst();
        volumeVivantMortPied = cur1.getDouble(0);

        cur1 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE,
                DatabaseHelper.ETAT_ARBRE + " = 'v'" +
                        " OR " + DatabaseHelper.ETAT_ARBRE + " = 'mp'");
        cur1.moveToFirst();
        densiteVivantMortPied = cur1.getCount();

        cur1 = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE,
                DatabaseHelper.ETAT_ARBRE + " = 'ms'");
        cur1.moveToFirst();
        volumeMortSol = cur1.getDouble(0);

        cur1 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE,
                DatabaseHelper.ETAT_ARBRE + " = 'ms'");
        cur1.moveToFirst();
        densiteMortSol = cur1.getCount();

        // Valeurs ramenées à l'hectare
        volumeVivantMortPied = volumeVivantMortPied / surfaceParcelle;
        volumeMortSol = volumeMortSol / surfaceParcelle;

        densiteVivantMortPied = (int) (densiteVivantMortPied / surfaceParcelle);
        densiteMortSol = (int) (densiteMortSol / surfaceParcelle);
    }

    private void grapheDiametre(BarChart barChart) {

        ArrayList<BarEntry> entriesArbres = new ArrayList<>();
        ArrayList<String> entriesDiametre = new ArrayList<>();
        int diametre;
        int i = 0;
        int nbArbreVivant;
        int nbArbreMortPied;
        int nbArbreMortSol;


        /*
         *  Cursor cur1 : Cursor pointant sur tout les diamètres d'arbre dans la base de données
         *
         *  On trie le graphe par diamètre donc on parcourt jusqu'à
         *  ce que le cur1 n'ai plus de diamètre disponible
         */
        cur1 = dbHelper.getDataFromTable("DISTINCT " + DatabaseHelper.DIAMETRE_ARBRE, DatabaseHelper.ARBRES_PARCELLE_TABLE + " ORDER BY " + DatabaseHelper.DIAMETRE_ARBRE + " ASC");
        while (cur1.moveToNext()) {

            // Récupération de le diamètre actuelle et ajout dans la liste des diamètres
            diametre = cur1.getInt(cur1.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE));
            entriesDiametre.add(Integer.toString(diametre));


            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE,
                    DatabaseHelper.DIAMETRE_ARBRE + " = " + diametre +
                            " AND " + DatabaseHelper.ETAT_ARBRE + " = 'v'");
            cur2.moveToFirst();
            nbArbreVivant = cur2.getCount();

            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE,
                    DatabaseHelper.DIAMETRE_ARBRE + " = " + diametre +
                            " AND " + DatabaseHelper.ETAT_ARBRE + " = 'mp'");
            cur2.moveToFirst();
            nbArbreMortPied = cur2.getCount();

            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE,
                    DatabaseHelper.DIAMETRE_ARBRE + " = " + diametre +
                            " AND " + DatabaseHelper.ETAT_ARBRE + " = 'ms'");
            cur2.moveToFirst();
            nbArbreMortSol = cur2.getCount();

            /*
             *  On ajoute dans la liste des données du graphe les deux valeurs que l'on vient
             *  de calculer sous forme de tableau de float car on veut que les données soit
             *  sous forme de bar chart stack
             */
            entriesArbres.add(new BarEntry(i, new float[]{nbArbreVivant, nbArbreMortPied, nbArbreMortSol}));

            i++;
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
        colors.add(getResources().getColor(R.color.colorBarBlue));
        colors.add(getResources().getColor(R.color.colorBarOrange));
        colors.add(getResources().getColor(R.color.colorBarViolet));

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

        barChart.getDescription().setText(getResources().getString(R.string.axe_diametre_cm));
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
        legende.setXEntrySpace(15f);
        // Position de la légende (changer si besoin)
        //legende.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        // ArrayList contenant les textes des états des arbres pour la légende
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("Arbres Vivant");
        titleList.add("Arbres Mort sur Pied");
        titleList.add("Arbres Mort sur Sol");

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

    private void grapheNoteEcologique(BarChart barChart) {

        ArrayList<BarEntry> entriesArbres = new ArrayList<>();
        ArrayList<String> entriesNoteEco = new ArrayList<>();
        int noteEco;
        int i = 0;
        int nbArbreVivant;
        int nbArbreMortPied;
        int nbArbreMortSol;

        /*
         *  Cursor cur1 : Cursor pointant sur toutes les notes écologique dans la base de données des arbres de la parcelle
         *
         *  On trie le graphe par note écologique donc on parcourt jusqu'à
         *  ce que le cur1 n'ai plus de notes écologique disponible
         */
        cur1 = dbHelper.getDataFromTable("DISTINCT " + DatabaseHelper.NOTE_ECO_ARBRE, DatabaseHelper.ARBRES_PARCELLE_TABLE + " ORDER BY " + DatabaseHelper.NOTE_ECO_ARBRE + " ASC");
        while (cur1.moveToNext()) {

            // Récupération de la note écologique actuelle et ajout dans la liste des notes écologique
            noteEco = cur1.getInt(cur1.getColumnIndex(DatabaseHelper.NOTE_ECO_ARBRE));
            entriesNoteEco.add(Integer.toString(noteEco));

            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE,
                    DatabaseHelper.NOTE_ECO_ARBRE + " = " + noteEco +
                            " AND " + DatabaseHelper.ETAT_ARBRE + " = 'v'");
            cur2.moveToFirst();
            nbArbreVivant = cur2.getCount();

            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE,
                    DatabaseHelper.NOTE_ECO_ARBRE + " = " + noteEco +
                            " AND " + DatabaseHelper.ETAT_ARBRE + " = 'mp'");
            cur2.moveToFirst();
            nbArbreMortPied = cur2.getCount();

            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE,
                    DatabaseHelper.NOTE_ECO_ARBRE + " = " + noteEco +
                            " AND " + DatabaseHelper.ETAT_ARBRE + " = 'ms'");
            cur2.moveToFirst();
            nbArbreMortSol = cur2.getCount();


            /*
             *  On ajoute dans la liste des données du graphe les deux valeurs que l'on vient
             *  de calculer sous forme de tableau de float car on veut que les données soit
             *  sous forme de bar chart stack
             */
            entriesArbres.add(new BarEntry(i, new float[]{nbArbreVivant, nbArbreMortPied, nbArbreMortSol}));

            i++;
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
        colors.add(getResources().getColor(R.color.colorBarBlue));
        colors.add(getResources().getColor(R.color.colorBarOrange));
        colors.add(getResources().getColor(R.color.colorBarViolet));

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
        legende.setXEntrySpace(15f);
        // Position de la légende (changer si besoin)
        //legende.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        // ArrayList contenant les textes des états des arbres pour la légende
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("Arbres Vivant");
        titleList.add("Arbres Mort sur Pied");
        titleList.add("Arbres Mort sur Sol");

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

    private void grapheEssence(PieChart pieChart) {

        ArrayList<PieEntry> entriesArbres = new ArrayList<>();
        ArrayList<String> entriesEssences = new ArrayList<>();
        String essence;
        int i = 0;
        int nbArbre;

        /*
         *  Cursor cur1 : Cursor pointant sur toutes les essences disponibles dans la base de données
         *
         *  On trie le graphe par essence donc on parcourt jusqu'à
         *  ce que le cur1 n'ai plus d'essence disponible
         */
        cur1 = dbHelper.getDataFromTable("DISTINCT " + DatabaseHelper.ESSENCE_ARBRE, DatabaseHelper.ARBRES_PARCELLE_TABLE + " ORDER BY " + DatabaseHelper.ESSENCE_ARBRE);
        while (cur1.moveToNext()) {

            // Récupération de l'essence actuelle et ajout dans la liste des essences
            essence = cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE));
            entriesEssences.add(essence);

            /*
             *  Récupération du nombre d'arbres martelés de l'essence actuelle
             *  et enregistrement de ce nombre dans nbArbreCoupe
             */
            cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE,
                    DatabaseHelper.ESSENCE_ARBRE + " = '" + essence + "'");
            cur2.moveToFirst();
            nbArbre = cur2.getCount();

            /*
             *  On ajoute dans la liste des données du graphe les deux valeurs que l'on vient
             *  de calculer sous forme de tableau de float car on veut que les données soit
             *  sous forme de bar chart stack
             */
            entriesArbres.add(new PieEntry(nbArbre, i));

            i++;
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


        /*
         *##################################
         *########## GRAPHE AVANT ##########
         *##################################
         */
        PieDataSet pieDataSet = new PieDataSet(entriesArbres, "");


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

        pieDataSet.setColors(colors);

        //pieDataSet.setDrawValues(false);
        pieDataSet.setValueTextSize(22f);
        pieDataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        pieDataSet.setValueTextColor(getResources().getColor(R.color.colorWhite));
        pieDataSet.setValueFormatter(new WithoutSmallValueFormatter());

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.getDescription().setText("Seulement les valeurs ≥ 5 sont affichées");
        pieChart.getDescription().setTextSize(18f);
        pieChart.getDescription().setXOffset(85f);
        pieChart.getDescription().setYOffset(-15f);

        // Enlever "description label"
        pieChart.setTouchEnabled(false);

        // Désactiver le trou du pie chart
        pieChart.setDrawHoleEnabled(false);


        pieChart.setExtraOffsets(0, 0, 80f, 10f);

        /* ************************ LEGENDE ****************
         *
         *
         */
        // Récupération de la légende du graphe
        Legend legende = pieChart.getLegend();
        // Forme de la légende

        legende.setXOffset(140f);
        legende.setYOffset(-140f);
        legende.setForm(Legend.LegendForm.SQUARE);
        legende.setTextSize(20f);
        legende.setFormSize(12f);


        // Listes des Entrées de la légende
        ArrayList<LegendEntry> legendeEntrees = new ArrayList<>();

        for (int k = 0; k < entriesEssences.size(); k++) {

            // Création d'une nouvelle entrée de légende
            LegendEntry entree = new LegendEntry();
            // Récupération de la couleur "k" de l'arrayList colors
            entree.formColor = colors.get(k);
            // Récupération du label "k" de l'arrayList titleList
            entree.label = entriesEssences.get(k);
// Integer.toString((int) entriesArbres.get(k).getValue()) + (entriesArbres.get(k).getValue() < 2 ? "tige" : "tiges")
            legendeEntrees.add(entree);
        }

        // Set la légende avec les entrées
        legende.setCustom(legendeEntrees);
        legende.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legende.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legende.setOrientation(Legend.LegendOrientation.VERTICAL);
        legende.setYEntrySpace(5f);
        legende.setDrawInside(false);

        // Refresh le graphe
        pieChart.invalidate();
    }
}
