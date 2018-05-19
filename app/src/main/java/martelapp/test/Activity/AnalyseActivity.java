package martelapp.test.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import martelapp.test.Fragment.Analyse.AnalyseGrapheCarteFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheRaisonsFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheVolumeFragment;
import martelapp.test.Fragment.Analyse.AnalyseListeArbresSelectionnesFragment;
import martelapp.test.Fragment.Analyse.AnalyseResultatFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheNombreTigesFragment;
import martelapp.test.R;


public class AnalyseActivity extends AppCompatActivity {
    /**
     * Viewpager de l'activité utilisé pour creer plusieurs fragments
     */
    ViewPager viewPager;

    AnalyseResultatFragment analyseResultatFragment;
    AnalyseListeArbresSelectionnesFragment analyseListeArbresSelectionnesFragment;
    AnalyseGrapheNombreTigesFragment analyseGrapheNombreTigesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);



        // On configure notre ViewPager pour chaque onglet
        // Un fragment pour chaque onglet
        viewPager = (ViewPager) findViewById(R.id.viewpagerAnalyse);
        setupViewPager(viewPager);

        // On associe nos onglets avec le viewpager
        final TabLayout tabs = (TabLayout) findViewById(R.id.tabsAnalyse);
        tabs.setupWithViewPager(viewPager);



        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    // Ajout et associations des Fragments aux onglets
    private void setupViewPager(ViewPager viewPager) {
        ExerciceActivity.Adapter adapter = new ExerciceActivity.Adapter(getSupportFragmentManager());
        analyseResultatFragment = new AnalyseResultatFragment();
        analyseResultatFragment.setVp(viewPager);

        analyseListeArbresSelectionnesFragment = new AnalyseListeArbresSelectionnesFragment();
        analyseListeArbresSelectionnesFragment.setVp(viewPager);

        analyseGrapheNombreTigesFragment = new AnalyseGrapheNombreTigesFragment();
        analyseGrapheNombreTigesFragment.setVp(viewPager);

        adapter.addFragment(analyseResultatFragment, "Résultats");
        adapter.addFragment(analyseListeArbresSelectionnesFragment, "Arbres Sélectionnes");
        adapter.addFragment(new AnalyseGrapheVolumeFragment(), "Graphe volume");
        adapter.addFragment(analyseGrapheNombreTigesFragment, "Graphe nombre de tiges");
        adapter.addFragment(new AnalyseGrapheRaisonsFragment(), "Graphe raisons");
        adapter.addFragment(new AnalyseGrapheCarteFragment(), "Carte parcelle");
        viewPager.setAdapter(adapter);
    }


    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {

    }
}

