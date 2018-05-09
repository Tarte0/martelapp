package martelapp.test.Fragment;

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
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.OnSwipeTouchListener;
import martelapp.test.R;

public class InfosFragment extends Fragment {

    ViewPager viewPager;

    BottomNavigationView bottomNavigationView;
    TextView textViewInfos, textViewTitleInfos;
    ImageButton previous, next;
    Button buttonGoToCarte;

    DatabaseHelper dbHelper;
    Cursor cur;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_infos, null);

        textViewInfos = (TextView) view.findViewById(R.id.textViewInfo);
        textViewTitleInfos = (TextView) view.findViewById(R.id.titleInfo);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation_info);
        previous = (ImageButton) view.findViewById(R.id.previousInfo);
        next = (ImageButton) view.findViewById(R.id.nextInfo);
        buttonGoToCarte = (Button) view.findViewById((R.id.button_go_to_carte));



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
                    case R.id.action_graphe_exploitabilite_rotation:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_essence);
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
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_exploitabilite_rotation);
                        break;
                    case R.id.action_graphe_exploitabilite_rotation:
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
                    case R.id.action_graphe_exploitabilite_rotation:
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_essence);
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
                        bottomNavigationView.setSelectedItemId(R.id.action_graphe_exploitabilite_rotation);
                        break;
                    case R.id.action_graphe_exploitabilite_rotation:
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
                                textViewInfos.setText(R.string.info_carte_id);
                                textViewTitleInfos.setText(R.string.carte_id_caps);
                                textViewInfos.setVisibility(View.VISIBLE);
                                previous.setVisibility(View.INVISIBLE);
                                next.setVisibility(View.VISIBLE);
                                buttonGoToCarte.setVisibility(View.INVISIBLE);
                                break;
                            case R.id.action_graphe_diametre:
                                textViewTitleInfos.setText(R.string.graphe_diametre_caps);
                                textViewInfos.setVisibility(View.INVISIBLE);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);
                                buttonGoToCarte.setVisibility(View.INVISIBLE);
                                break;
                            case R.id.action_graphe_note_eco:
                                textViewTitleInfos.setText(R.string.graphe_note_eco_caps);
                                textViewInfos.setVisibility(View.INVISIBLE);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);
                                buttonGoToCarte.setVisibility(View.INVISIBLE);
                                break;
                            case R.id.action_graphe_essence:
                                textViewTitleInfos.setText(R.string.graphe_essence_caps);
                                textViewInfos.setVisibility(View.INVISIBLE);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);
                                buttonGoToCarte.setVisibility(View.INVISIBLE);
                                break;
                            case R.id.action_graphe_exploitabilite_rotation:
                                textViewInfos.setText(R.string.info_exploitabilite_rotation);
                                textViewTitleInfos.setText(R.string.exploitabilite_rotation_caps);
                                textViewInfos.setVisibility(View.VISIBLE);
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.INVISIBLE);
                                buttonGoToCarte.setVisibility(View.VISIBLE);

                                buttonGoToCarte.setOnClickListener(new View.OnClickListener() {
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


    public void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.action_carte_id);
    }
}
