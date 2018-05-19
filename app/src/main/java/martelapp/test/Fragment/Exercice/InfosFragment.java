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

    ViewPager viewPager;

    BottomNavigationView bottomNavigationView;
    TextView textViewInfos, textViewTitleInfos;
    LinearLayout layoutInfo;
    ImageButton previous, next;
    Button buttonGoToCarte;
    BarChart barChartDiametre;
    BarChart barChartNoteEco;
    PieChart pieChartEssence;


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
        layoutInfo = view.findViewById(R.id.layout_info);
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
                                textViewTitleInfos.setText(R.string.caracteristique_parcelle_caps);
                                textViewInfos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9));
                                layoutInfo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10));
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
                                layoutInfo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10));
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
                                layoutInfo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10));
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
                                layoutInfo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 7f));
                                textViewInfos.setVisibility(View.GONE);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.INVISIBLE);
                                buttonGoToCarte.setVisibility(View.VISIBLE);
                                barChartDiametre.setVisibility(View.GONE);
                                barChartNoteEco.setVisibility(View.GONE);
                                pieChartEssence.setVisibility(View.VISIBLE);
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