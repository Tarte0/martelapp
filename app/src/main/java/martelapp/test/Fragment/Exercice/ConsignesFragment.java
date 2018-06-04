package martelapp.test.Fragment.Exercice;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.OnSwipeTouchListener;
import martelapp.test.R;

/**
 * ConsignesFragment est un framgment contenant 2 pages :
 *              - Général : Consignes générales de l'exercice de martelage.
 *              - Objectifs : Objectifs de l'exercice de martelage.
 */

public class ConsignesFragment extends Fragment {

    public static final int LIMIT_DIAMETRE_EXPLOIT_AFFICHE = 4;

    View view;

    ViewPager viewPager;

    // Barre de navigation entre les pages
    BottomNavigationView bottomNavigationView;
    TextView textViewConsignes, textViewTitleConsignes;
    // Boutons latéraux permettant de naviguer entre les pages
    ImageButton previous, next;
    // Bouton permettant d'aller vers le fragment InfosFragment
    Button buttonGoToInfos;

    DecimalFormat df;

    DatabaseHelper dbHelper;
    Cursor cur, cur2;

    int     prelevementMin = 0,
            prelevementMax = 0;

    int rotationMin = 0,
        rotationMax = 0;

    double volumeBoisTotalParcelleHa = 0f;
    double volumeBoisTotalParcelle= 0f;
    double surfaceParcelle = 0f;

    StringBuffer diametreExploitabilite;
    Spanned objectifs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.view_page_consignes, null);

            dbHelper = new DatabaseHelper(view.getContext());

            df = new DecimalFormat("#0.00");

            textViewConsignes = view.findViewById(R.id.textViewConsignes);
            textViewConsignes.setTextSize(22f);
            textViewTitleConsignes = view.findViewById(R.id.titleConsignes);

            bottomNavigationView = view.findViewById(R.id.bottom_navigation_consignes);

            previous = view.findViewById(R.id.previousConsignes);
            next = view.findViewById(R.id.nextConsignes);

            buttonGoToInfos = view.findViewById(R.id.button_go_to_info);

            cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
            cur.moveToFirst();



            // Récupération du prélèvement minimum et maximum de la base de données locale
            prelevementMin = (int) cur.getFloat(cur.getColumnIndex(DatabaseHelper.PRELEVEMENT_VOLUME_MIN));
            prelevementMax = (int) cur.getFloat(cur.getColumnIndex(DatabaseHelper.PRELEVEMENT_VOLUME_MAX));


            // Récupération de la rotation minimum et maximum de la base de données locale
            rotationMin = cur.getInt(cur.getColumnIndex(DatabaseHelper.ROTATION_MIN));
            rotationMax = cur.getInt(cur.getColumnIndex(DatabaseHelper.ROTATION_MAX));


            // Récupération de la surface de la parcelle
            surfaceParcelle = cur.getDouble(cur.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));


            // Récupération du volume de bois dans la parcelle
            cur = dbHelper.getDataFromTable("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")",
                    DatabaseHelper.ARBRES_PARCELLE_TABLE);
            cur.moveToFirst();
            volumeBoisTotalParcelle = cur.getDouble(0);

            // Volume de bois ramené à l'hectare
            if(surfaceParcelle != 0f) {
                volumeBoisTotalParcelleHa = volumeBoisTotalParcelle / surfaceParcelle;
            }


            // Récupération et enregistrement dans une String le diamètre d'exploitabilité des essences
            diametreExploitabilite = new StringBuffer();

            cur = dbHelper.getDataFromTableWithCondition("COUNT(*) AS nb_tiges_par_essence, ap." + DatabaseHelper.ESSENCE_ARBRE,
                    DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.DIAMETRE_EXPLOIT_TABLE + " de",
                            "ap." + DatabaseHelper.ESSENCE_ARBRE + " = de." + DatabaseHelper.ESSENCE_DIAM_EXPLOIT +
                            " GROUP BY ap." + DatabaseHelper.ESSENCE_ARBRE +
                            " ORDER BY nb_tiges_par_essence DESC");
            int maxAffichees = 0;
            while(cur.moveToNext() && maxAffichees < LIMIT_DIAMETRE_EXPLOIT_AFFICHE){
                System.out.println("Essence : " + cur.getString(cur.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)) + " nb tiges : " + Integer.toString(cur.getInt(0)));
                cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.DIAMETRE_EXPLOIT_TABLE,
                        DatabaseHelper.ESSENCE_DIAM_EXPLOIT + " = '" + cur.getString(cur.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)) + "'");
                cur2.moveToFirst();

                String essence = cur2.getString(cur2.getColumnIndex(DatabaseHelper.ESSENCE_DIAM_EXPLOIT));
                int diametre = cur2.getInt(cur2.getColumnIndex(DatabaseHelper.DIAMETRE_EXPLOITABILITE));

                if(diametre != 0) {
                    diametreExploitabilite.append("<br>" + essence + " : à partir de " + Integer.toString(diametre) + " cm");
                    maxAffichees++;
                }

                cur2.close();
            }

            if(diametreExploitabilite.length() <= 0){
                diametreExploitabilite.append("<br>Inconnu");
            }

            dbHelper.close();
            cur.close();

            objectifs = Html.fromHtml(
                    "Compte tenu d'une période de rotation fixée entre " + Integer.toString(rotationMin) + " et " + Integer.toString(rotationMax) + " ans et des diamètres d'exploitabilité(*) sur cette parcelle :" +
                            "<br><br><b>Volume</b>" +
                            "<br>• Prélever entre " + Integer.toString(prelevementMin) + " % et " + Integer.toString(prelevementMax) + " % du volume de bois de la parcelle" +
                            " soit entre " + Integer.toString((int) (volumeBoisTotalParcelleHa * prelevementMin / 100)) + " et " + Integer.toString((int) (volumeBoisTotalParcelleHa * prelevementMax / 100)) + " m3/ha." +
                            "<br>Pour notre parcelle de " + df.format(surfaceParcelle) + " ha, il faut donc prélever entre " + Integer.toString((int) (volumeBoisTotalParcelle * prelevementMin / 100)) + " et " + Integer.toString((int) (volumeBoisTotalParcelle * prelevementMax / 100)) + " m3." +
                            "<br>" +
                            "<br><i>(*)Pour les principales essences : " +
                            diametreExploitabilite + "</i>" +
                            "<br><br><b>Biodiversité</b>" +
                            "<br>• Conserver sciemment au moins 3 arbres de gros diamètre par hectare." +
                            "<br>• Conserver sciemment au moins 2 arbres porteurs de micros-habitats par hectare." +
                            "<br><br>Les arbres ainsi conservés seront immobilisés sur plusieurs dizaines d'années pour permettre la réalisation du stade final du cycle naturel de la forêt, particulièrement favorable à la biodiversité. " +
                            "Il s'agit de les laisser vieillir jusqu'à leurs écroulements. ");

            //on gere le swipe gauche et droite (un peu brute)
            view.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
                public void onSwipeRight() {
                    switch (bottomNavigationView.getSelectedItemId()) {
                        case R.id.action_general:
                            break;
                        case R.id.action_objectif:
                            bottomNavigationView.setSelectedItemId(R.id.action_general);
                            break;
                    }

                }

                public void onSwipeLeft() {
                    switch (bottomNavigationView.getSelectedItemId()) {
                        case R.id.action_general:
                            bottomNavigationView.setSelectedItemId(R.id.action_objectif);
                            break;
                        case R.id.action_objectif:
                            break;
                    }
                }
            });

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (bottomNavigationView.getSelectedItemId()) {
                        case R.id.action_general:
                            break;
                        case R.id.action_objectif:
                            bottomNavigationView.setSelectedItemId(R.id.action_general);
                            break;
                    }
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (bottomNavigationView.getSelectedItemId()) {
                        case R.id.action_general:
                            bottomNavigationView.setSelectedItemId(R.id.action_objectif);
                            break;
                        case R.id.action_objectif:
                            break;
                    }
                }
            });

            //on gere les selection des items du bnv
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_general:
                                    textViewConsignes.setText(R.string.consignes_general);
                                    textViewConsignes.setTextSize(22);
                                    textViewTitleConsignes.setText(R.string.general_caps);
                                    previous.setVisibility(View.INVISIBLE);
                                    next.setVisibility(View.VISIBLE);
                                    buttonGoToInfos.setVisibility((View.INVISIBLE));
                                    break;
                                case R.id.action_objectif:
                                    textViewTitleConsignes.setText(R.string.objectif_caps);
                                    textViewConsignes.setText(objectifs);
                                    textViewConsignes.setTextSize(18);
                                    previous.setVisibility(View.VISIBLE);
                                    next.setVisibility(View.INVISIBLE);
                                    buttonGoToInfos.setVisibility((View.VISIBLE));

                                    buttonGoToInfos.setOnClickListener(new View.OnClickListener() {
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
        }
        return view;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.action_general);
    }
}
