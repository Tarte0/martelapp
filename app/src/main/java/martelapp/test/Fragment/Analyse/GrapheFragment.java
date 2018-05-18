package martelapp.test.Fragment.Analyse;

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
import com.github.mikephil.charting.charts.ScatterChart;
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
import martelapp.test.Class.OnSwipeTouchListener;
import martelapp.test.Formatter.StackedBarFormatter;
import martelapp.test.Formatter.WithoutSmallValueFormatter;
import martelapp.test.R;


public class GrapheFragment extends Fragment {

    ViewPager viewPager;

    BottomNavigationView bottomNavigationView;
    TextView textAxe, textTitleGraphe;
    LinearLayout layoutGrapheButton, layoutGraphe, layoutPieChartVolumeCategorie;
    ImageButton previous, next;
    Button buttonTerminerAnalyse;

    BarChart barChartEssenceAnalyse;
    BarChart barChartDiametreAnalyse;
    BarChart barChartNoteEcoAnalyse;
    BarChart barChartVolumeAnalyse;
    BarChart barChartRaisonAnalyse;

    PieChart pieChartVolumeAvantAnalyse;
    PieChart pieChartVolumeApresAnalyse;

    ScatterChart scatterChartCarteAnalyse;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_graphe, null);

        textAxe = (TextView) view.findViewById(R.id.text_axe);
        textTitleGraphe = (TextView) view.findViewById(R.id.title_graphe);
        layoutGrapheButton = view.findViewById(R.id.layout_graphe_button);
        layoutGraphe = view.findViewById(R.id.layout_graphe);
        layoutPieChartVolumeCategorie = view.findViewById(R.id.layout_pie_chart_volume);

        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation_graphe);
        previous = (ImageButton) view.findViewById(R.id.previousInfo);
        next = (ImageButton) view.findViewById(R.id.nextInfo);
        buttonTerminerAnalyse = (Button) view.findViewById((R.id.button_terminer_analyse_save_pdf));


        barChartEssenceAnalyse = view.findViewById(R.id.bar_chart_essence_graphe);
        barChartDiametreAnalyse = view.findViewById(R.id.bar_chart_diametre_graphe);
        barChartNoteEcoAnalyse = view.findViewById(R.id.bar_chart_note_eco_graphe);
        barChartVolumeAnalyse = view.findViewById(R.id.bar_chart_volume_graphe);
        barChartRaisonAnalyse = view.findViewById(R.id.bar_chart_raison_graphe);

        pieChartVolumeAvantAnalyse = view.findViewById(R.id.pie_chart_volume_avant_graphe);
        pieChartVolumeApresAnalyse = view.findViewById(R.id.pie_chart_volume_apres_graphe);

        scatterChartCarteAnalyse = view.findViewById(R.id.scatter_chart_carte_graphe);

        AnalyseGraphe.getBarChartAnalyseEssence(view.getContext(), barChartEssenceAnalyse);
        AnalyseGraphe.getBarChartAnalyseDiametre(view.getContext(), barChartDiametreAnalyse);
        AnalyseGraphe.getBarChartAnalyseNoteEcologique(view.getContext(), barChartNoteEcoAnalyse);
        AnalyseGraphe.getBarChartAnalyseVolume(view.getContext(), barChartVolumeAnalyse);
        AnalyseGraphe.getGrapheAnalyseRaisons(view.getContext(), barChartRaisonAnalyse);

        AnalyseGraphe.getPieChartAnalyseAvantApresVolumeCategorie(view.getContext(), pieChartVolumeAvantAnalyse, pieChartVolumeApresAnalyse);

        AnalyseGraphe.getScatterChartAnalyseCarte(view.getContext(), scatterChartCarteAnalyse);

        buttonTerminerAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //on gere le swipe gauche et droite (un peu brute)
        view.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
            public void onSwipeRight() {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_graphe_essence_analyse:
                        break;
                    case R.id.action_graphe_diametre_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_essence_analyse);
                        break;
                    case R.id.action_graphe_note_eco_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_diametre_analyse);
                        break;
                    case R.id.action_graphe_volume_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_note_eco_analyse);
                        break;
                    /*case R.id.action_graphe_volume_categorie_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_volume_analyse);
                        break;

                    case R.id.action_graphe_raisons_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_raisons_analyse);
                        break;

                    case R.id.action_graphe_carte_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_carte_analyse);
                        break;*/
                }

            }

            public void onSwipeLeft() {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_graphe_essence_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_diametre_analyse);
                        break;
                    case R.id.action_graphe_diametre_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_note_eco_analyse);
                        break;
                    case R.id.action_graphe_note_eco_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_volume_analyse);
                        break;
                    case R.id.action_graphe_volume_analyse:
                        //bottomNavigationView.setSelectedItemId(R.id.action_graphe_volume_categorie_analyse);
                        break;
                    /*case R.id.action_graphe_volume_categorie_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_raisons_analyse);
                        break;
                    case R.id.action_graphe_raisons_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_carte_analyse);
                        break;
                    case R.id.action_graphe_carte_analyse:
                        break;*/
                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_graphe_essence_analyse:
                        break;
                    case R.id.action_graphe_diametre_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_essence_analyse);
                        break;
                    case R.id.action_graphe_note_eco_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_diametre_analyse);
                        break;
                    case R.id.action_graphe_volume_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_note_eco_analyse);
                        break;
                    /*case R.id.action_graphe_volume_categorie_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_volume_analyse);
                        break;

                    case R.id.action_graphe_raisons_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_raisons_analyse);
                        break;

                    case R.id.action_graphe_carte_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_carte_analyse);
                        break;*/
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_graphe_essence_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_diametre_analyse);
                        break;
                    case R.id.action_graphe_diametre_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_note_eco_analyse);
                        break;
                    case R.id.action_graphe_note_eco_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_volume_analyse);
                        break;
                    case R.id.action_graphe_volume_analyse:
                        //bottomNavigationView.setSelectedItemId(R.id.action_graphe_volume_categorie_analyse);
                        break;
                    /*case R.id.action_graphe_volume_categorie_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_raisons_analyse);
                        break;
                    case R.id.action_graphe_raisons_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_carte_analyse);
                        break;
                    case R.id.action_graphe_carte_analyse:
                        break;*/
                }
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_graphe_essence_analyse:
                                previous.setVisibility(View.INVISIBLE);
                                next.setVisibility(View.VISIBLE);

                                textTitleGraphe.setText(R.string.caracteristique_parcelle_caps);
                                textAxe.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));
                                textAxe.setText(R.string.axe_nombre_tiges);

                                layoutGraphe.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));

                                barChartEssenceAnalyse.setVisibility(View.VISIBLE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);
                                barChartVolumeAnalyse.setVisibility(View.GONE);
                                barChartRaisonAnalyse.setVisibility(View.GONE);

                                layoutPieChartVolumeCategorie.setVisibility(View.GONE);

                                scatterChartCarteAnalyse.setVisibility(View.GONE);

                                buttonTerminerAnalyse.setVisibility(View.GONE);
                                break;


                            case R.id.action_graphe_diametre_analyse:
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);

                                textTitleGraphe.setText(R.string.caracteristique_parcelle_caps);
                                textAxe.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));
                                textAxe.setText(R.string.axe_nombre_tiges);

                                layoutGraphe.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));

                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.VISIBLE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);
                                barChartVolumeAnalyse.setVisibility(View.GONE);
                                barChartRaisonAnalyse.setVisibility(View.GONE);

                                layoutPieChartVolumeCategorie.setVisibility(View.GONE);

                                scatterChartCarteAnalyse.setVisibility(View.GONE);

                                buttonTerminerAnalyse.setVisibility(View.GONE);
                                break;


                            case R.id.action_graphe_note_eco_analyse:
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);

                                textTitleGraphe.setText(R.string.caracteristique_parcelle_caps);
                                textAxe.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));
                                textAxe.setText(R.string.axe_nombre_tiges);

                                layoutGraphe.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));

                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.VISIBLE);
                                barChartVolumeAnalyse.setVisibility(View.GONE);
                                barChartRaisonAnalyse.setVisibility(View.GONE);

                                layoutPieChartVolumeCategorie.setVisibility(View.GONE);

                                scatterChartCarteAnalyse.setVisibility(View.GONE);

                                buttonTerminerAnalyse.setVisibility(View.GONE);
                                break;


                            case R.id.action_graphe_volume_analyse:
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);

                                textTitleGraphe.setText(R.string.caracteristique_parcelle_caps);
                                textAxe.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));
                                textAxe.setText(R.string.axe_nombre_tiges);

                                layoutGraphe.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));

                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);
                                barChartVolumeAnalyse.setVisibility(View.VISIBLE);
                                barChartRaisonAnalyse.setVisibility(View.GONE);

                                layoutPieChartVolumeCategorie.setVisibility(View.GONE);

                                scatterChartCarteAnalyse.setVisibility(View.GONE);

                                buttonTerminerAnalyse.setVisibility(View.GONE);
                                break;


                            /*case R.id.action_graphe_volume_categorie_analyse:
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);

                                textTitleGraphe.setText(R.string.caracteristique_parcelle_caps);
                                textAxe.setVisibility(View.GONE);

                                layoutGraphe.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));

                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);
                                barChartVolumeAnalyse.setVisibility(View.GONE);
                                barChartRaisonAnalyse.setVisibility(View.GONE);

                                layoutPieChartVolumeCategorie.setVisibility(View.VISIBLE);

                                scatterChartCarteAnalyse.setVisibility(View.GONE);

                                buttonTerminerAnalyse.setVisibility(View.GONE);
                                break;



                            case R.id.action_graphe_raisons_analyse:
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);

                                textTitleGraphe.setText(R.string.caracteristique_parcelle_caps);
                                textAxe.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));
                                textAxe.setText(R.string.axe_nombre_tiges);

                                layoutGraphe.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10f));

                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);
                                barChartVolumeAnalyse.setVisibility(View.GONE);
                                barChartRaisonAnalyse.setVisibility(View.VISIBLE);

                                layoutPieChartVolumeCategorie.setVisibility(View.GONE);

                                scatterChartCarteAnalyse.setVisibility(View.GONE);

                                buttonTerminerAnalyse.setVisibility(View.GONE);
                                break;



                            case R.id.action_graphe_carte_analyse:
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.INVISIBLE);

                                textTitleGraphe.setText(R.string.caracteristique_parcelle_caps);
                                textAxe.setVisibility(View.GONE);

                                layoutGraphe.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 7f));

                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);
                                barChartVolumeAnalyse.setVisibility(View.GONE);
                                barChartRaisonAnalyse.setVisibility(View.GONE);

                                layoutPieChartVolumeCategorie.setVisibility(View.GONE);

                                scatterChartCarteAnalyse.setVisibility(View.VISIBLE);

                                buttonTerminerAnalyse.setVisibility(View.VISIBLE);
                                break;*/
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
        bottomNavigationView.setSelectedItemId(R.id.action_graphe_essence_analyse);
    }

}