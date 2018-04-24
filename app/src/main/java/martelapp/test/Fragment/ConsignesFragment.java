package martelapp.test.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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


    BottomNavigationView bottomNavigationView;
    TextView textViewConsignes, textViewTitleConsignes;
    ImageButton previous, next;

    DatabaseHelper dbHelper;
    Cursor cur;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_consignes, null);


        dbHelper = new DatabaseHelper(view.getContext());

        DecimalFormat df = new DecimalFormat("#0.00");

        textViewConsignes = (TextView) view.findViewById(R.id.textViewConsignes);
        textViewTitleConsignes = (TextView) view.findViewById(R.id.titleConsignes);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation_consignes);
        previous = (ImageButton) view.findViewById(R.id.previousConsignes);
        next = (ImageButton) view.findViewById(R.id.nextConsignes);

        cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
        cur.moveToFirst();
        final float surfaceParcelle = cur.getFloat(cur.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));

        //on gere le swipe gauche et droite (un peu brute)
        view.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
            public void onSwipeRight() {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_general:
                        break;
                    case R.id.action_volume:
                        bottomNavigationView.setSelectedItemId(R.id.action_general);
                        break;
                    case R.id.action_biodiversite:
                        bottomNavigationView.setSelectedItemId(R.id.action_volume);
                        break;
                    case R.id.action_exploitabilite:
                        bottomNavigationView.setSelectedItemId(R.id.action_biodiversite);
                        break;
                    case R.id.action_rotation:
                        bottomNavigationView.setSelectedItemId(R.id.action_exploitabilite);
                        break;
                }

            }
            public void onSwipeLeft() {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_general:
                        bottomNavigationView.setSelectedItemId(R.id.action_volume);
                        break;
                    case R.id.action_volume:
                        bottomNavigationView.setSelectedItemId(R.id.action_biodiversite);
                        break;
                    case R.id.action_biodiversite:
                        bottomNavigationView.setSelectedItemId(R.id.action_exploitabilite);
                        break;
                    case R.id.action_exploitabilite:
                        bottomNavigationView.setSelectedItemId(R.id.action_rotation);
                        break;
                    case R.id.action_rotation:
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
                    case R.id.action_volume:
                        bottomNavigationView.setSelectedItemId(R.id.action_general);
                        break;
                    case R.id.action_biodiversite:
                        bottomNavigationView.setSelectedItemId(R.id.action_volume);
                        break;
                    case R.id.action_exploitabilite:
                        bottomNavigationView.setSelectedItemId(R.id.action_biodiversite);
                        break;
                    case R.id.action_rotation:
                        bottomNavigationView.setSelectedItemId(R.id.action_exploitabilite);
                        break;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_general:
                        bottomNavigationView.setSelectedItemId(R.id.action_volume);
                        break;
                    case R.id.action_volume:
                        bottomNavigationView.setSelectedItemId(R.id.action_biodiversite);
                        break;
                    case R.id.action_biodiversite:
                        bottomNavigationView.setSelectedItemId(R.id.action_exploitabilite);
                        break;
                    case R.id.action_exploitabilite:
                        bottomNavigationView.setSelectedItemId(R.id.action_rotation);
                        break;
                    case R.id.action_rotation:
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
                                break;
                            case R.id.action_volume:
                                textViewConsignes.setText(R.string.consignes_volume);
                                textViewConsignes.setText(textViewConsignes.getText() +
                                                "Notre parcelle fait " + surfaceParcelle + " ha. Il faudra alors prélever un volume entre "
                                                + (int)(DatabaseHelper.MIN_PRELEVEMENT * surfaceParcelle) + " et "
                                                + (int)(DatabaseHelper.MAX_PRELEVEMENT * surfaceParcelle) + " m3.\n\n\n");
                                textViewTitleConsignes.setText(R.string.volume_caps);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_biodiversite:
                                textViewConsignes.setText(R.string.consignes_biodiversite);
                                textViewTitleConsignes.setText(R.string.biodiversite_caps);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_exploitabilite:
                                textViewConsignes.setText(R.string.consignes_exploitabilite);
                                textViewTitleConsignes.setText(R.string.exploitabilite_caps);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_rotation:
                                textViewConsignes.setText(R.string.consignes_rotations);
                                textViewTitleConsignes.setText(R.string.rotations_caps);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.INVISIBLE);
                                break;
                        }
                        return true;
                    }
                });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.action_general);
    }
}
