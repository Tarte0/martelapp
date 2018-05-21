package martelapp.test.Fragment.Exercice;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import martelapp.test.Class.AdaptableColorSet;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.GrapheHelper;
import martelapp.test.Class.OnSwipeTouchListener;
import martelapp.test.Formatter.StackedBarFormatter;
import martelapp.test.Formatter.WithoutSmallValueFormatter;
import martelapp.test.R;

public class InfosFragment extends Fragment {

    View view;

    ViewPager viewPager;

    BottomNavigationView bottomNavigationView;
    TextView textViewCaracteristiques, textViewTitleInfos;
    ImageButton previous, next;
    Button buttonGoToCarte;
    BarChart barChartDiametre;
    BarChart barChartNoteEco;
    PieChart pieChartEssence;

    LinearLayout layout_caracteristique;
    LinearLayout layout_graphe_info_diametre;
    LinearLayout layout_graphe_info_note_eco;
    LinearLayout layout_graphe_info_essence;

    LinearLayout.LayoutParams layoutParamsTextViewInfoCaracteristique;
    LinearLayout.LayoutParams layoutParamsTextViewInfoGraphe;

    LinearLayout.LayoutParams layoutParamsLinearLayoutGraphe;
    LinearLayout.LayoutParams layoutParamsLinearLayoutAvecBouton;

    DecimalFormat df;

    int altitude = 0,
            densiteVivantMortPied = 0,
            densiteMortSol = 0;

    double surfaceParcelle = 0f,
            volumeVivantMortPied = 0f,
            volumeMortSol = 0f;

    String habitat = "";

    String caracteristique = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view == null) {
            view = inflater.inflate(R.layout.view_page_infos, null);

            textViewCaracteristiques = (TextView) view.findViewById(R.id.text_view_caracteristique);
            textViewTitleInfos = (TextView) view.findViewById(R.id.titleInfo);

            layout_caracteristique = view.findViewById(R.id.layout_caracteristique);
            layout_graphe_info_diametre = view.findViewById(R.id.layout_graphe_info_diametre);
            layout_graphe_info_note_eco = view.findViewById(R.id.layout_graphe_info_note_eco);
            layout_graphe_info_essence = view.findViewById(R.id.layout_graphe_info_essence);

            bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation_info);
            previous = (ImageButton) view.findViewById(R.id.previousInfo);
            next = (ImageButton) view.findViewById(R.id.nextInfo);
            buttonGoToCarte = (Button) view.findViewById((R.id.button_go_to_carte));
            barChartDiametre = view.findViewById(R.id.bar_chart_diametre_info);
            barChartNoteEco = view.findViewById(R.id.bar_chart_note_eco_info);
            pieChartEssence = view.findViewById(R.id.pie_chart_essence_info);

            df = new DecimalFormat("#0.00");


            caracteristiqueParcelle(view.getContext());

            GrapheHelper.getBarChartInfosDiametre(view.getContext(), barChartDiametre);
            GrapheHelper.getBarChartInfosNoteEcologique(view.getContext(), barChartNoteEco);
            GrapheHelper.getPieChartInfosEssence(view.getContext(), pieChartEssence);

            caracteristique = String.format("• altitude : %d mètres\n\n"
                            + "• habitat naturel : %s\n\n"
                            + "• surface : %s ha\n\n"
                            + "• densité (vivants et morts sur pied) : %d tiges/ha\n\n"
                            + "• volume : %d m3/ha\n\n"
                            + "• densité de bois mort au sol : %d tiges/ha\n\n"
                            + "• volume de bois mort au sol : %d m3/ha",
                    altitude, habitat, df.format(surfaceParcelle), densiteVivantMortPied, (int) volumeVivantMortPied, densiteMortSol, (int) volumeMortSol);

            layoutParamsTextViewInfoCaracteristique = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9);
            layoutParamsTextViewInfoGraphe = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f);
            layoutParamsLinearLayoutGraphe = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10);
            layoutParamsLinearLayoutAvecBouton = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 7f);


            buttonGoToCarte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

                }
            });

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
                                    previous.setVisibility(View.INVISIBLE);
                                    next.setVisibility(View.VISIBLE);

                                    textViewTitleInfos.setText(R.string.caracteristique_parcelle_caps);
                                /*
                                textViewInfos.setLayoutParams(layoutParamsTextViewInfoCaracteristique);
                                layoutInfo.setLayoutParams(layoutParamsLinearLayoutGraphe);
                                */
                                    textViewCaracteristiques.setText(caracteristique);

                                    buttonGoToCarte.setVisibility(View.GONE);

                                    layout_caracteristique.setVisibility(View.VISIBLE);
                                    layout_graphe_info_diametre.setVisibility(View.GONE);
                                    layout_graphe_info_note_eco.setVisibility(View.GONE);
                                    layout_graphe_info_essence.setVisibility(View.GONE);
                                    break;
                                case R.id.action_graphe_diametre:
                                    previous.setVisibility(View.VISIBLE);
                                    next.setVisibility(View.VISIBLE);

                                    textViewTitleInfos.setText(R.string.titre_graphe_nbtige_diametre_info);
                                /*
                                layoutInfo.setLayoutParams(layoutParamsLinearLayoutGraphe);
                                textViewInfos.setLayoutParams(layoutParamsTextViewInfoGraphe);
                                */

                                    buttonGoToCarte.setVisibility(View.GONE);

                                    layout_caracteristique.setVisibility(View.GONE);
                                    layout_graphe_info_diametre.setVisibility(View.VISIBLE);
                                    layout_graphe_info_note_eco.setVisibility(View.GONE);
                                    layout_graphe_info_essence.setVisibility(View.GONE);

                                    break;
                                case R.id.action_graphe_note_eco:
                                    previous.setVisibility(View.VISIBLE);
                                    next.setVisibility(View.VISIBLE);

                                    textViewTitleInfos.setText(R.string.titre_graphe_nbtige_noteeco_info);

                                /*
                                layoutInfo.setLayoutParams(layoutParamsLinearLayoutGraphe);
                                textViewInfos.setLayoutParams(layoutParamsTextViewInfoGraphe);
                                */
                                    buttonGoToCarte.setVisibility(View.GONE);

                                    layout_caracteristique.setVisibility(View.GONE);
                                    layout_graphe_info_diametre.setVisibility(View.GONE);
                                    layout_graphe_info_note_eco.setVisibility(View.VISIBLE);
                                    layout_graphe_info_essence.setVisibility(View.GONE);
                                    break;
                                case R.id.action_graphe_essence:
                                    previous.setVisibility(View.VISIBLE);
                                    next.setVisibility(View.INVISIBLE);

                                    textViewTitleInfos.setText(R.string.titre_graphe_nbtige_essence_info);

                                    //layoutInfo.setLayoutParams(layoutParamsLinearLayoutAvecBouton);

                                    buttonGoToCarte.setVisibility(View.VISIBLE);

                                    layout_caracteristique.setVisibility(View.GONE);
                                    layout_graphe_info_diametre.setVisibility(View.GONE);
                                    layout_graphe_info_note_eco.setVisibility(View.GONE);
                                    layout_graphe_info_essence.setVisibility(View.VISIBLE);
                                    break;
                            }
                            return true;
                        }
                    });

        }
        return view;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    public void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.action_carte_id);
    }


    private void caracteristiqueParcelle(Context context) {
        DatabaseHelper dbHelper;
        Cursor cur1;

        dbHelper = new DatabaseHelper(context);

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

        dbHelper.close();
        cur1.close();
    }
}