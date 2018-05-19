package martelapp.test.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import martelapp.test.Fragment.Analyse.AnalyseListeArbresConservesFragment;
import martelapp.test.Fragment.Analyse.AnalyseListeArbresMartelesFragment;
import martelapp.test.Fragment.Analyse.AnalyseParcelleFragment;
import martelapp.test.Fragment.Analyse.AnalyseRaisonsFragment;
import martelapp.test.Fragment.Analyse.AnalyseRespectConsignesFragment;
import martelapp.test.Fragment.Analyse.AnalyseSyntheseFragment;
import martelapp.test.Fragment.Analyse.AnalyseTigesDiametreFragment;
import martelapp.test.Fragment.Analyse.AnalyseTigesEcoFragment;
import martelapp.test.Fragment.Analyse.AnalyseTigesEssencesFragment;
import martelapp.test.Fragment.Analyse.AnalyseVolumeCategorieFragment;
import martelapp.test.Fragment.Analyse.AnalyseVolumeDiametreFragment;
import martelapp.test.Fragment.Analyse.GrapheFragment;
import martelapp.test.R;


public class AnalyseActivity extends AppCompatActivity {
    /**
     * Viewpager de l'activité utilisé pour creer plusieurs fragments
     */
    ViewPager viewPager;



    GrapheFragment gf;

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
        gf = new GrapheFragment();
        gf.setVp(viewPager);
        adapter.addFragment(new AnalyseRespectConsignesFragment(), "Consignes");
        adapter.addFragment(new AnalyseSyntheseFragment(), "Synthèse");
        adapter.addFragment(new AnalyseListeArbresMartelesFragment(), "Arbres martelés");
        adapter.addFragment(new AnalyseListeArbresConservesFragment(), "Arbres conservés");
        adapter.addFragment(gf, "Graphe wesh");
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

