package martelapp.test.Fragment;

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
import android.widget.TextView;

import martelapp.test.Class.OnSwipeTouchListener;
import martelapp.test.R;

/**
 * Created by cimin on 04/04/2018.
 */

public class ConsignesFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    TextView textViewConsignes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_consignes, null);

        textViewConsignes = (TextView) view.findViewById(R.id.textViewConsignes);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation_consignes);

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

        //on gere les selection des items du bnv
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_general:
                                textViewConsignes.setText(R.string.consignes_general);
                                break;
                            case R.id.action_volume:
                                textViewConsignes.setText(R.string.consignes_volume);
                                break;
                            case R.id.action_biodiversite:
                                textViewConsignes.setText(R.string.consignes_biodiversite);
                                break;
                            case R.id.action_exploitabilite:
                                textViewConsignes.setText(R.string.consignes_exploitabilite);
                                break;
                            case R.id.action_rotation:
                                textViewConsignes.setText(R.string.consignes_rotations);
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
