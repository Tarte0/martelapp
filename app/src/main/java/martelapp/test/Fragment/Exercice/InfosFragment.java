package martelapp.test.Fragment.Exercice;

import android.content.Context;
import android.database.Cursor;
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

import java.text.DecimalFormat;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.GrapheHelper;
import martelapp.test.Class.OnSwipeTouchListener;
import martelapp.test.R;


/**
 * InfosFragment est un framgment contenant 4 pages :
 * - Caractéristiques : Caractéristiques de la parcelle.
 * - Diamètre : Graphique du nombre de tiges par classe de diamètre.
 * - Note écologique : Graphique du nombre de tiges par note écologique.
 * - Essence : Graphique du nombre de tiges par essence.
 */
public class InfosFragment extends Fragment {

    View view;

    ViewPager viewPager;

    // Barre de navigation entre les pages
    BottomNavigationView bottomNavigationView;
    TextView textViewCaracteristiques, textViewTitleInfos;
    // Boutons latéraux permettant de naviguer entre les pages
    ImageButton previous, next;
    // Bouton permettant d'aller vers le fragment CarteParcelleFragment
    Button buttonGoToCarte;
    BarChart barChartVolume;
    BarChart barChartNoteEco;
    PieChart pieChartEssence;

    LinearLayout layout_caracteristique;
    LinearLayout layout_graphe_info_diametre;
    LinearLayout layout_graphe_info_note_eco;
    LinearLayout layout_graphe_info_essence;

    DecimalFormat df;

    int altitude = 0,
        densiteVivantMortPied = 0,
        densiteVivantMortPiedHa = 0;

    double surfaceParcelle = 0f,
            volumeVivantMortPied = 0f,
            volumeVivantMortPiedHa = 0f,
            volumeMortSol = 0f,
            volumeMortSolHa = 0f;

    String habitat = "";

    String caracteristique = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.view_page_infos, null);

            textViewCaracteristiques = view.findViewById(R.id.text_view_caracteristique);
            textViewTitleInfos = view.findViewById(R.id.titleInfo);

            layout_caracteristique = view.findViewById(R.id.layout_caracteristique);
            layout_graphe_info_diametre = view.findViewById(R.id.layout_graphe_info_diametre);
            layout_graphe_info_note_eco = view.findViewById(R.id.layout_graphe_info_note_eco);
            layout_graphe_info_essence = view.findViewById(R.id.layout_graphe_info_essence);

            bottomNavigationView = view.findViewById(R.id.bottom_navigation_info);
            previous = view.findViewById(R.id.previousInfo);
            next = view.findViewById(R.id.nextInfo);
            buttonGoToCarte = view.findViewById(R.id.button_go_to_carte);
            barChartVolume = view.findViewById(R.id.bar_chart_volume_info);
            barChartNoteEco = view.findViewById(R.id.bar_chart_note_eco_info);
            pieChartEssence = view.findViewById(R.id.pie_chart_essence_info);

            df = new DecimalFormat("#0.00");

            // Récupération des caractéristiques de la parcelle
            caracteristiqueParcelle(view.getContext());

            // Ajouts des graphiques
            GrapheHelper.getBarChartInfosVolume(view.getContext(), barChartVolume);
            GrapheHelper.getBarChartInfosNoteEcologique(view.getContext(), barChartNoteEco);
            GrapheHelper.getPieChartInfosEssence(view.getContext(), pieChartEssence);

            // Affichage des caractéristiques, on affiche inconnu lorsque le volume de bois mort au sol est inconnu
            if ((int) volumeMortSolHa == 0) {
                caracteristique = String.format("• altitude : %d mètres\n\n"
                                + "• habitat naturel : %s\n\n"
                                + "• surface : %s ha\n\n"
                                + "• densité (vivants et morts sur pied) : %d tiges/ha (Soit %d tiges pour notre parcelle)\n\n"
                                + "• volume : %d m3/ha (Soit %d m3 pour notre parcelle)\n\n"
                                + "• volume de bois mort au sol : inconnu",
                        altitude, habitat, df.format(surfaceParcelle), densiteVivantMortPiedHa, densiteVivantMortPied, (int) volumeVivantMortPiedHa, (int) volumeVivantMortPied);
            } else {
                caracteristique = String.format("• altitude : %d mètres\n\n"
                                + "• habitat naturel : %s\n\n"
                                + "• surface : %s ha\n\n"
                                + "• densité (vivants et morts sur pied) : %d tiges/ha (Soit %d tiges pour notre parcelle)\n\n"
                                + "• volume : %d m3/ha (Soit %d m3 pour notre parcelle)\n\n"
                                + "• volume de bois mort au sol : %d m3/ha (Soit %d m3 pour notre parcelle)",
                        altitude, habitat, df.format(surfaceParcelle), densiteVivantMortPiedHa, densiteVivantMortPied, (int) volumeVivantMortPiedHa, (int) volumeVivantMortPied, (int) volumeMortSolHa, (int) volumeMortSol);
            }

            buttonGoToCarte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

                }
            });

            //on gere le swipe gauche et droite
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


            // On gere le click du bouton page précédente
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

            // On gere le click du bouton page suivante
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


            // traitement de l'affichage en fonction de la page dans laquelle on est
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_carte_id:
                                    previous.setVisibility(View.INVISIBLE);
                                    next.setVisibility(View.VISIBLE);

                                    textViewTitleInfos.setText(R.string.caracteristique_parcelle_caps);

                                    textViewCaracteristiques.setText(caracteristique);

                                    buttonGoToCarte.setVisibility(View.GONE);

                                    // on affiche uniquemement layout_caracteristique
                                    layout_caracteristique.setVisibility(View.VISIBLE);
                                    layout_graphe_info_diametre.setVisibility(View.GONE);
                                    layout_graphe_info_note_eco.setVisibility(View.GONE);
                                    layout_graphe_info_essence.setVisibility(View.GONE);
                                    break;
                                case R.id.action_graphe_diametre:
                                    previous.setVisibility(View.VISIBLE);
                                    next.setVisibility(View.VISIBLE);

                                    textViewTitleInfos.setText(R.string.titre_graphe_volume_diametre_info);


                                    buttonGoToCarte.setVisibility(View.GONE);

                                    // on affiche uniquemement layout_graphe_info_diametre
                                    layout_caracteristique.setVisibility(View.GONE);
                                    layout_graphe_info_diametre.setVisibility(View.VISIBLE);
                                    layout_graphe_info_note_eco.setVisibility(View.GONE);
                                    layout_graphe_info_essence.setVisibility(View.GONE);

                                    break;
                                case R.id.action_graphe_note_eco:
                                    previous.setVisibility(View.VISIBLE);
                                    next.setVisibility(View.VISIBLE);

                                    textViewTitleInfos.setText(R.string.titre_graphe_nbtige_noteeco_info);


                                    buttonGoToCarte.setVisibility(View.GONE);

                                    // on affiche uniquemement layout_graphe_info_note_eco
                                    layout_caracteristique.setVisibility(View.GONE);
                                    layout_graphe_info_diametre.setVisibility(View.GONE);
                                    layout_graphe_info_note_eco.setVisibility(View.VISIBLE);
                                    layout_graphe_info_essence.setVisibility(View.GONE);
                                    break;
                                case R.id.action_graphe_essence:
                                    previous.setVisibility(View.VISIBLE);
                                    next.setVisibility(View.INVISIBLE);

                                    textViewTitleInfos.setText(R.string.titre_graphe_nbtige_essence_info);

                                    buttonGoToCarte.setVisibility(View.VISIBLE);

                                    // on affiche uniquemement layout_graphe_info_essence
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



    // Calcul des caractéristiques de la parcelle
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


        // Valeurs ramenées à l'hectare
        if(surfaceParcelle != 0f) {
            volumeVivantMortPiedHa = volumeVivantMortPied / surfaceParcelle;
            volumeMortSolHa = volumeMortSol / surfaceParcelle;

            densiteVivantMortPiedHa = (int) (densiteVivantMortPied / surfaceParcelle);
        }
        dbHelper.close();
        cur1.close();
    }
}