package martelapp.test.Fragment.Analyse;

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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;

import martelapp.test.Adapter.ArbresConservesAdapter;
import martelapp.test.Adapter.ArbresMartelesAdapter;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.OnSwipeTouchListener;
import martelapp.test.Fragment.Exercice.ChoixMartelageFragment;
import martelapp.test.R;

/**
 * Created by Baptiste on 19/05/2018.
 */

public class AnalyseListeArbresSelectionnesFragment extends Fragment {

    ViewPager viewPager;

    View mainView;

    BottomNavigationView bottomNavigationView;
    ImageButton previous, next;

    LinearLayout treeCardNumber;

    TextView textTypeListeArbres;
    TextView textComplementArbresMarteles;
    TextView textCouleurArbre;
    TextView textRaison;

    Cursor cur1, cur2;
    DatabaseHelper dbHelper;

    int nbArbresMarteles = 0;
    int nbArbresConserves = 0;

    ListView listeArbresMarteles;
    ListView listeArbresConserves;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_analyse_liste_arbres_selectionnes, null);

            bottomNavigationView = (BottomNavigationView) mainView.findViewById(R.id.bottom_navigation_liste_arbres_traites_analyse);
            previous = (ImageButton) mainView.findViewById(R.id.previousInfo);
            next = (ImageButton) mainView.findViewById(R.id.nextInfo);

            listeArbresMarteles = mainView.findViewById(R.id.liste_arbres_marteles_analyse);
            listeArbresConserves = mainView.findViewById(R.id.liste_arbres_conserves_analyse);

            treeCardNumber = (LinearLayout) mainView.findViewById(R.id.arbreLayout);

            textComplementArbresMarteles = mainView.findViewById(R.id.text_complement_arbres);
            textCouleurArbre = mainView.findViewById(R.id.text_couleur_arbre);
            textTypeListeArbres = mainView.findViewById(R.id.text_type_liste_arbres);

            textRaison = mainView.findViewById(R.id.raison_martele_card);
            textRaison.setTextSize(26f);

            dbHelper = new DatabaseHelper(mainView.getContext());

            getListeArbresMarteles();
            getListeArbresConserves();

            dbHelper.close();
        }
        //on gere le swipe gauche et droite (un peu brute)
        mainView.setOnTouchListener(new OnSwipeTouchListener(mainView.getContext()) {
            public void onSwipeRight() {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_liste_arbres_marteles:
                        break;
                    case R.id.action_liste_arbres_conserves:
                        bottomNavigationView.setSelectedItemId(R.id.action_liste_arbres_marteles);
                        break;
                }
            }

            public void onSwipeLeft() {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_liste_arbres_marteles:
                        bottomNavigationView.setSelectedItemId(R.id.action_liste_arbres_conserves);
                        break;
                    case R.id.action_liste_arbres_conserves:
                        break;
                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_liste_arbres_marteles:
                        break;
                    case R.id.action_liste_arbres_conserves:
                        bottomNavigationView.setSelectedItemId(R.id.action_liste_arbres_marteles);
                        break;
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_liste_arbres_marteles:
                        bottomNavigationView.setSelectedItemId(R.id.action_liste_arbres_conserves);
                        break;
                    case R.id.action_liste_arbres_conserves:
                        break;
                }
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_liste_arbres_marteles:
                                previous.setVisibility(View.INVISIBLE);
                                next.setVisibility(View.VISIBLE);

                                textTypeListeArbres.setText(R.string.type_arbres_liste_marteles);

                                textComplementArbresMarteles.setText("Nombre d'arbres martelés : " + Integer.toString(nbArbresMarteles));

                                textCouleurArbre.setText("■ Note écologique ≥ " + Integer.toString(ChoixMartelageFragment.noteEcologiqueHaute));
                                textCouleurArbre.setTextColor(mainView.getResources().getColor(R.color.colorRed));

                                treeCardNumber.setVisibility(View.INVISIBLE);
                                textRaison.setVisibility(View.INVISIBLE);

                                listeArbresMarteles.setVisibility(View.VISIBLE);
                                listeArbresConserves.setVisibility(View.GONE);

                                break;


                            case R.id.action_liste_arbres_conserves:
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.INVISIBLE);

                                textTypeListeArbres.setText(R.string.type_arbres_liste_conserves);

                                textComplementArbresMarteles.setText("Nombre d'arbres conservés : " + Integer.toString(nbArbresConserves));

                                textCouleurArbre.setText("■ Note écologique ≥ " + Integer.toString(ChoixMartelageFragment.noteEcologiqueHaute));
                                textCouleurArbre.setTextColor(mainView.getResources().getColor(R.color.colorGreen));

                                treeCardNumber.setVisibility(View.INVISIBLE);
                                textRaison.setVisibility(View.INVISIBLE);

                                listeArbresMarteles.setVisibility(View.GONE);
                                listeArbresConserves.setVisibility(View.VISIBLE);
                                break;
                        }
                        return true;
                    }
                });


        return mainView;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    public void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.action_liste_arbres_marteles);
    }


    private void getListeArbresMarteles(){


        listeArbresMarteles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                TextView text = view.findViewById(R.id.numero_arbre_traite);

                String numero = text.getText().toString();
                cur1 = dbHelper.executeQuery("Select *"
                        + " FROM " + DatabaseHelper.RAISON_TABLE
                        + " WHERE " + DatabaseHelper.NUMERO_ARBRE_TRAITE_RAISON + " = " + numero);
                cur1.moveToFirst();

                String raison = cur1.getString(cur1.getColumnIndex(DatabaseHelper.RAISON));

                textRaison.setText(String.format("Raisons du Martelage : \n\n- %s\n", raisonToString(raison)));
                while (cur1.moveToNext()) {
                    textRaison.setText(String.format("%s\n- %s\n", textRaison.getText(),
                            raisonToString(cur1.getString(cur1.getColumnIndex(DatabaseHelper.RAISON)))));
                }

                cur1 = dbHelper.executeQuery("SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE " + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numero);
                cur1.moveToFirst();
                TextView numCard = mainView.findViewById(R.id.numero_martele_card);
                TextView detailCard = mainView.findViewById(R.id.details_martele_card);
                numCard.setText(cur1.getString(cur1.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC)));
                detailCard.setText(String.format("%s",
                        cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE))));

                textRaison.setVisibility(View.VISIBLE);
                treeCardNumber.setVisibility(View.VISIBLE);
            }
        });


        cur1 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART +
                        " AND ap." + DatabaseHelper.NOTE_ECO_ARBRE + " >= " + ChoixMartelageFragment.noteEcologiqueHaute +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");
        cur1.moveToFirst();

        cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART +
                        " AND ap." + DatabaseHelper.NOTE_ECO_ARBRE + " < " + ChoixMartelageFragment.noteEcologiqueHaute +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");
        cur2.moveToFirst();

        nbArbresMarteles = cur1.getCount() + cur2.getCount();

        ArbresMartelesAdapter arbresMartelesAdapterNoteEcoSup = new ArbresMartelesAdapter(mainView.getContext(), cur1, true);
        ArbresMartelesAdapter arbresMartelesAdapter = new ArbresMartelesAdapter(mainView.getContext(), cur2, true);

        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(arbresMartelesAdapterNoteEcoSup);
        mergeAdapter.addAdapter(arbresMartelesAdapter);
        listeArbresMarteles.setAdapter(mergeAdapter);
    }

    private void getListeArbresConserves(){

        listeArbresConserves.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                TextView text = view.findViewById(R.id.numero_arbre_traite);

                String numero = text.getText().toString();
                cur1 = dbHelper.executeQuery("Select *"
                        + " FROM " + DatabaseHelper.RAISON_TABLE
                        + " WHERE " + DatabaseHelper.NUMERO_ARBRE_TRAITE_RAISON + " = " + numero);
                cur1.moveToFirst();

                textRaison.setText(R.string.raison_conserve);

                cur1 = dbHelper.executeQuery("SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE " + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numero);
                cur1.moveToFirst();
                TextView numCard = mainView.findViewById(R.id.numero_martele_card);
                TextView detailCard = mainView.findViewById(R.id.details_martele_card);
                numCard.setText(cur1.getString(cur1.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC)));
                detailCard.setText(String.format("%s",
                        cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE))));

                textRaison.setVisibility(View.VISIBLE);
                treeCardNumber.setVisibility(View.VISIBLE);
            }
        });


        cur1 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS +
                        " AND ap." + DatabaseHelper.NOTE_ECO_ARBRE + " >= " + ChoixMartelageFragment.noteEcologiqueHaute +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");
        cur1.moveToFirst();

        cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS +
                        " AND ap." + DatabaseHelper.NOTE_ECO_ARBRE + " < " + ChoixMartelageFragment.noteEcologiqueHaute +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");
        cur2.moveToFirst();

        nbArbresConserves = cur1.getCount() + cur2.getCount();

        ArbresConservesAdapter arbresConservesAdapterNoteEcoSup = new ArbresConservesAdapter(mainView.getContext(), cur1, true);
        ArbresConservesAdapter arbresConservesAdapter = new ArbresConservesAdapter(mainView.getContext(), cur2, true);


        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(arbresConservesAdapterNoteEcoSup);
        mergeAdapter.addAdapter(arbresConservesAdapter);
        listeArbresConserves.setAdapter(mergeAdapter);
    }


    private String raisonToString(String raison){
        if(raison.equals(DatabaseHelper.ECLAIRCIE)) {
            return "Eclaircie au profit d'un arbre d'avenir";
        }
        return raison;

    }
}
