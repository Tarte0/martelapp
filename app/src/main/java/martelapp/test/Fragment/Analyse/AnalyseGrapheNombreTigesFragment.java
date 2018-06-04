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

/**
 * AnalyseGrapheNombreTigesFragment fragment contenant 4 pages :
 *                  - Essence : Graphique de la Répartition du prélèvement en nombre de tiges par essence.
 *                  - Diamètre : Graphique de la Répartition du prélèvement en nombre de tiges par classe de diamètre.
 *                  - Note écologique : Graphique de la Répartition du prélèvement en nombre de tiges par note écologique.
 *                  - Classe de diamètre : Graphique de l'évolution du nombre de tiges par classe de diamètre (Graphique avant et après martelage).
 */

public class AnalyseGrapheNombreTigesFragment extends Fragment {

    ViewPager viewPager;
    View view;

    // Barre de navigation entre les pages
    BottomNavigationView bottomNavigationView;
    TextView textAxe, textTitleGraphe;
    // Boutons latéraux permettant de naviguer entre les pages
    ImageButton previous, next;

    // layout contenant les pieChart de l'évolution du nombre de tiges par classe de diamètre (Graphique avant et après martelage)
    LinearLayout layoutPieChartNbTige;

    // Graphiques de la répartition du prélevement
    BarChart barChartEssenceAnalyse;
    BarChart barChartDiametreAnalyse;
    BarChart barChartNoteEcoAnalyse;

    //Graphiques de l'évolution du nombre de tiges par classe de diamètre
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

            textAxe = view.findViewById(R.id.text_axe);
            textTitleGraphe = view.findViewById(R.id.title_graphe);

            bottomNavigationView = view.findViewById(R.id.bottom_navigation_graphe_nombre_tiges_analyse);
            previous = view.findViewById(R.id.previousInfo);
            next = view.findViewById(R.id.nextInfo);

            layoutPieChartNbTige = view.findViewById(R.id.layout_pie_chart_nb_tiges);


            // Ajouts des graphiques
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

        //on gere le swipe gauche et droite
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


        // On gere le click du bouton page précédente
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


        // On gere le click du bouton page suivante
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


        // traitement de l'affichage en fonction de la page dans laquelle on est
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


                                // On affiche uniquement barChartEssenceAnalyse
                                barChartEssenceAnalyse.setVisibility(View.VISIBLE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);

                                layoutPieChartNbTige.setVisibility(View.GONE);

                                // Ajout de la view dans la liste des View de AnalyseActivity pour la création des pdf
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


                                // On affiche uniquement barChartDiametreAnalyse
                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.VISIBLE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);

                                layoutPieChartNbTige.setVisibility(View.GONE);

                                // Ajout de la view dans la liste des View de AnalyseActivity pour la création des pdf
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

                                // On affiche uniquement barChartNoteEcoAnalyse
                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.VISIBLE);

                                layoutPieChartNbTige.setVisibility(View.GONE);

                                // Ajout de la view dans la liste des View de AnalyseActivity pour la création des pdf
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

                                // On affiche uniquement layoutPieChartNbTige
                                barChartEssenceAnalyse.setVisibility(View.GONE);
                                barChartDiametreAnalyse.setVisibility(View.GONE);
                                barChartNoteEcoAnalyse.setVisibility(View.GONE);

                                layoutPieChartNbTige.setVisibility(View.VISIBLE);

                                // Ajout de la view dans la liste des View de AnalyseActivity pour la création des pdf
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