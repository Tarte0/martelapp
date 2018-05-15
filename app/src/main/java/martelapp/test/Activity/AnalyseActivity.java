package martelapp.test.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import martelapp.test.R;


public class AnalyseActivity extends AppCompatActivity {
    /**
     * Viewpager de l'activité utilisé pour creer plusieurs fragments
     */
    ViewPager viewPager;

    /**
     * Bouton permettant d'aller sur le fragment à gauche
     */
    ImageButton previous;

    /**
     * Bouton permettant d'aller sur le fragment à gauche
     */
    ImageButton next;

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

        previous = (ImageButton) findViewById(R.id.previousAnalyse);
        next = (ImageButton) findViewById(R.id.nextAnalyse);

        // Bouton fragment précédent invisible sur le premier fragment
        previous.setVisibility(View.INVISIBLE);

        // On gère l'affichage des flèches pour naviguer entre les fragments en fonction de leur position
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    previous.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                }else if(tab.getPosition() == tabs.getTabCount()-1){
                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                }else{
                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Cliquer sur le bouton suivant mène au fragment suivant
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int step = (viewPager.getCurrentItem()+1)%tabs.getTabCount();
                viewPager.setCurrentItem(step);
            }
        });

        // Cliquer sur le bouton précédent mène au fragment précédent
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(viewPager.getCurrentItem()>0){
                    int step = (viewPager.getCurrentItem()-1)%tabs.getTabCount();
                    viewPager.setCurrentItem(step);
                }
            }
        });
    }

    // Ajout et associations des Fragments aux onglets
    private void setupViewPager(ViewPager viewPager) {
        ExerciceActivity.Adapter adapter = new ExerciceActivity.Adapter(getSupportFragmentManager());
        adapter.addFragment(new AnalyseRespectConsignesFragment(), "Consignes");
        adapter.addFragment(new AnalyseSyntheseFragment(), "Synthèse");
        adapter.addFragment(new AnalyseListeArbresMartelesFragment(), "Arbres martelés");
        adapter.addFragment(new AnalyseListeArbresConservesFragment(), "Arbres conservés");
        adapter.addFragment(new AnalyseTigesEssencesFragment(), "Essences");
        adapter.addFragment(new AnalyseTigesDiametreFragment(), "Diametre");
        adapter.addFragment(new AnalyseTigesEcoFragment(), "Note écologique");
        adapter.addFragment(new AnalyseVolumeDiametreFragment(), "Volume");
        adapter.addFragment(new AnalyseVolumeCategorieFragment(), "Catégories");
        adapter.addFragment(new AnalyseRaisonsFragment(), "Raisons");
        adapter.addFragment(new AnalyseParcelleFragment(), "Parcelle");
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

