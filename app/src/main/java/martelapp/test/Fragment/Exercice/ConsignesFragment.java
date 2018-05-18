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

    ViewPager viewPager;


    BottomNavigationView bottomNavigationView;
    TextView textViewConsignes, textViewTitleConsignes;
    ImageButton previous, next;
    Button buttonGoToInfos;

    DatabaseHelper dbHelper;
    Cursor cur;

    int     prelevementMin = 0,
            prelevementMax = 0;

    double  volumeBoisTotalParcelleHa = 0f;
    double  surfaceParcelle = 0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_page_consignes, null);

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

        surfaceParcelle = cur.getDouble(cur.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));




        cur = dbHelper.getDataFromTable("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL +")",
                                        DatabaseHelper.ARBRES_PARCELLE_TABLE);
        cur.moveToFirst();
        volumeBoisTotalParcelleHa = cur.getDouble(0);
        volumeBoisTotalParcelleHa = volumeBoisTotalParcelleHa / surfaceParcelle;

        dbHelper.close();
        cur.close();

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
                                textViewTitleConsignes.setText(R.string.general_caps);
                                previous.setVisibility(View.INVISIBLE);
                                next.setVisibility(View.VISIBLE);
                                buttonGoToInfos.setVisibility((View.INVISIBLE));
                                break;
                            case R.id.action_objectif:
                                textViewConsignes.setText(Html.fromHtml("<b>Volume</b>" +
                                        "<br><br>• Prélever entre " + Integer.toString(prelevementMin) + " % et " + Integer.toString(prelevementMax) + " % du volume de bois de la parcelle" +
                                        " soit entre " + Integer.toString((int)(volumeBoisTotalParcelleHa * prelevementMin / 100)) + " et " + Integer.toString((int)(volumeBoisTotalParcelleHa * prelevementMax / 100)) + " m3/ha" +
                                        "<br><br><br><b>Biodiversité</b>" +
                                        "<br>" +
                                        "<br>• Conservez au moins 3 arbres de gros diamètre par hectare." +
                                        "<br>" +
                                        "<br>• Conservez au moins 2 arbres porteurs de micros-habitats par hectare."));
                                textViewTitleConsignes.setText(R.string.objectif_caps);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.INVISIBLE);
                                buttonGoToInfos.setVisibility((View.VISIBLE));

                                buttonGoToInfos.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);

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

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.action_general);
    }
}
