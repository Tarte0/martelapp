package martelapp.test.Fragment.Analyse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import martelapp.test.Activity.AnalyseActivity;
import martelapp.test.Class.GrapheHelper;
import martelapp.test.Class.OnSwipeTouchListener;
import martelapp.test.R;


public class AnalyseGrapheNombreTigesFragment extends Fragment {

    ViewPager viewPager;
    View view;

    BottomNavigationView bottomNavigationView;
    TextView textAxe, textTitleGraphe;
    ImageButton previous, next;

    LinearLayout layoutPieChartNbTige;

    BarChart barChartEssenceAnalyse;
    BarChart barChartDiametreAnalyse;
    BarChart barChartNoteEcoAnalyse;

    PieChart pieChartNbTigesAvant;
    PieChart pieChartNbTigesApres;

    private boolean viewBarChartEssenceAdded = false;
    private boolean viewBarChartDiametreAdded = false;
    private boolean viewBarChartNoteEcologiqueAdded = false;
    private boolean viewPieChartEvolutionAdded = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.view_page_analyse_graphe_nombre_tiges, null);

            textAxe = (TextView) view.findViewById(R.id.text_axe);
            textTitleGraphe = (TextView) view.findViewById(R.id.title_graphe);

            bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation_graphe_nombre_tiges_analyse);
            previous = (ImageButton) view.findViewById(R.id.previousInfo);
            next = (ImageButton) view.findViewById(R.id.nextInfo);

            layoutPieChartNbTige = view.findViewById(R.id.layout_pie_chart_nb_tiges);


            barChartEssenceAnalyse = view.findViewById(R.id.bar_chart_essence_graphe);
            GrapheHelper.getBarChartAnalyseEssence(view.getContext(), barChartEssenceAnalyse);



            barChartDiametreAnalyse = view.findViewById(R.id.bar_chart_diametre_graphe);
            GrapheHelper.getBarChartAnalyseDiametre(view.getContext(), barChartDiametreAnalyse);


            barChartNoteEcoAnalyse = view.findViewById(R.id.bar_chart_note_eco_graphe);
            GrapheHelper.getBarChartAnalyseNoteEcologique(view.getContext(), barChartNoteEcoAnalyse);


            pieChartNbTigesAvant = view.findViewById(R.id.pie_chart_nb_tiges_avant_graphe);
            pieChartNbTigesApres = view.findViewById(R.id.pie_chart_nb_tiges_apres_graphe);
            GrapheHelper.getPieChartAnalyseAvantApresNbTigesCategorie(view.getContext(), pieChartNbTigesAvant, pieChartNbTigesApres);
        }
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
                    case R.id.action_graphe_evolution_nb_tiges:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_note_eco_analyse);
                        break;
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
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_evolution_nb_tiges);
                        break;
                    case R.id.action_graphe_evolution_nb_tiges:
                        break;
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
                    case R.id.action_graphe_evolution_nb_tiges:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_note_eco_analyse);
                        break;
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
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_evolution_nb_tiges);
                        break;
                    case R.id.action_graphe_evolution_nb_tiges:
                        break;
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

                                textTitleGraphe.setText(R.string.titre_nb_tige_essence);
                                textAxe.setText(R.string.axe_nombre_tiges);
                                textAxe.setVisibility(View.VISIBLE);


                                barChartEssenceAnalyse.setVisibility(View.VISIBLE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);

                                layoutPieChartNbTige.setVisibility(View.GONE);

                                if(!viewBarChartEssenceAdded){
                                    ((AnalyseActivity) getActivity()).addViewPdf(barChartEssenceAnalyse,3);
                                    viewBarChartEssenceAdded = true;
                                }
                                break;


                            case R.id.action_graphe_diametre_analyse:
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);

                                textTitleGraphe.setText(R.string.titre_nb_tige_diametre);
                                textAxe.setText(R.string.axe_nombre_tiges);
                                textAxe.setVisibility(View.VISIBLE);


                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.VISIBLE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);

                                layoutPieChartNbTige.setVisibility(View.GONE);

                                if(!viewBarChartDiametreAdded){
                                    ((AnalyseActivity) getActivity()).addViewPdf(barChartDiametreAnalyse,4);
                                    viewBarChartDiametreAdded = true;
                                }
                                break;


                            case R.id.action_graphe_note_eco_analyse:
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);

                                textTitleGraphe.setText(R.string.titre_nb_tige_note_eco);
                                textAxe.setText(R.string.axe_nombre_tiges);
                                textAxe.setVisibility(View.VISIBLE);

                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.VISIBLE);

                                layoutPieChartNbTige.setVisibility(View.GONE);

                                if(!viewBarChartNoteEcologiqueAdded){
                                    ((AnalyseActivity) getActivity()).addViewPdf(barChartNoteEcoAnalyse,5);
                                    viewBarChartNoteEcologiqueAdded = true;
                                }
                                break;


                            case R.id.action_graphe_evolution_nb_tiges:
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.INVISIBLE);

                                textTitleGraphe.setText(R.string.titre_nb_tige_categorie_diametre);
                                textAxe.setVisibility(View.GONE);

                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);

                                layoutPieChartNbTige.setVisibility(View.VISIBLE);

                                if(!viewPieChartEvolutionAdded){
                                    ((AnalyseActivity) getActivity()).addViewPdf(layoutPieChartNbTige,6);
                                    viewPieChartEvolutionAdded = true;
                                }

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
        bottomNavigationView.setSelectedItemId(R.id.action_graphe_essence_analyse);
    }

    public boolean getViewBarChartEssenceAdded(){
        return viewBarChartEssenceAdded;
    }

    public boolean getViewBarChartDiametreAdded(){
        return viewBarChartDiametreAdded;
    }

    public boolean getViewBarChartNoteEcologiqueAdded(){
        return viewBarChartNoteEcologiqueAdded;
    }

    public boolean getViewPieChartEvolutionAdded(){
        return viewPieChartEvolutionAdded;
    }


    public BottomNavigationView getBottomNavigationView(){
        return bottomNavigationView;
    }
}