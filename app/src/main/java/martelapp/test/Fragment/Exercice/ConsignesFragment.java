package martelapp.test.Fragment.Exercice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuItemImpl;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
 * Created by cimin on 04/04/2018.
 */

public class ConsignesFragment extends Fragment {

    View view;

    ViewPager viewPager;

    BottomNavigationView bottomNavigationView;
    TextView textViewConsignes, textViewTitleConsignes;
    ImageButton previous, next;
    Button buttonGoToInfos;

    DecimalFormat df;

    DatabaseHelper dbHelper;
    Cursor cur;

    int     prelevementMin = 0,
            prelevementMax = 0;

    int rotationMin = 0,
        rotationMax = 0;

    double  volumeBoisTotalParcelleHa = 0f;
    double volumeBoisTotalParcelle= 0f;
    double  surfaceParcelle = 0f;

    Spanned objectifs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.view_page_consignes, null);

            dbHelper = new DatabaseHelper(view.getContext());

            textViewConsignes = (TextView) view.findViewById(R.id.textViewConsignes);
            textViewConsignes.setTextSize(22f);
            textViewTitleConsignes = (TextView) view.findViewById(R.id.titleConsignes);
            bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation_consignes);
            previous = (ImageButton) view.findViewById(R.id.previousConsignes);
            next = (ImageButton) view.findViewById(R.id.nextConsignes);
            buttonGoToInfos = (Button) view.findViewById(R.id.button_go_to_info);

            cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
            cur.moveToFirst();

            prelevementMin = (int) cur.getFloat(cur.getColumnIndex(DatabaseHelper.PRELEVEMENT_VOLUME_MIN));
            prelevementMax = (int) cur.getFloat(cur.getColumnIndex(DatabaseHelper.PRELEVEMENT_VOLUME_MAX));
            rotationMin = cur.getInt(cur.getColumnIndex(DatabaseHelper.ROTATION_MIN));
            rotationMax = cur.getInt(cur.getColumnIndex(DatabaseHelper.ROTATION_MAX));

            surfaceParcelle = cur.getDouble(cur.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));

            df = new DecimalFormat("#0.00");


            cur = dbHelper.getDataFromTable("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")",
                    DatabaseHelper.ARBRES_PARCELLE_TABLE);
            cur.moveToFirst();
            volumeBoisTotalParcelle = cur.getDouble(0);
            volumeBoisTotalParcelleHa = volumeBoisTotalParcelle / surfaceParcelle;

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
                            "<br>- Épicéa : de 50 à 70 cm" +
                            "<br>- Sapin pectiné : de 45 à 50 cm" +
                            "<br>- Hêtre : jusqu'à 50 cm" +
                            "<br>- Frêne, Érables : à partir de 50 cm</i>" +
                            "<br><br><b>Biodiversité</b>" +
                            "<br>• Conservez sciemment au moins 3 arbres de gros diamètre par hectare." +
                            "<br>• Conservez sciemment au moins 2 arbres porteurs de micros-habitats par hectare.");

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
                                    textViewConsignes.setTextSize(21);
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
