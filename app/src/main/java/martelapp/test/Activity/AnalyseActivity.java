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

import martelapp.test.Fragment.AnalyseListeArbresConservesFragment;
import martelapp.test.Fragment.AnalyseListeArbresMartelesFragment;
import martelapp.test.Fragment.AnalyseParcelleFragment;
import martelapp.test.Fragment.AnalyseRaisonsFragment;
import martelapp.test.Fragment.AnalyseRespectConsignesFragment;
import martelapp.test.Fragment.AnalyseSyntheseFragment;
import martelapp.test.Fragment.AnalyseTigesDiametreFragment;
import martelapp.test.Fragment.AnalyseTigesEcoFragment;
import martelapp.test.Fragment.AnalyseTigesEssencesFragment;
import martelapp.test.Fragment.AnalyseVolumeCategorieFragment;
import martelapp.test.Fragment.AnalyseVolumeDiametreFragment;
import martelapp.test.R;

public class AnalyseActivity extends AppCompatActivity {
    ViewPager viewPager;
    ImageButton previous, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);

        //On configure notre ViewPager pour chaque onglet
        //Un fragment pour chaque onglet
        viewPager = (ViewPager) findViewById(R.id.viewpagerAnalyse);
        setupViewPager(viewPager);

        //on associe nos onglets avec le viewpager
        final TabLayout tabs = (TabLayout) findViewById(R.id.tabsAnalyse);
        tabs.setupWithViewPager(viewPager);

        previous = (ImageButton) findViewById(R.id.previousAnalyse);
        next = (ImageButton) findViewById(R.id.nextAnalyse);

        previous.setVisibility(View.INVISIBLE);

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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int step = (viewPager.getCurrentItem()+1)%tabs.getTabCount();
                viewPager.setCurrentItem(step);
            }
        });

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

    // ajout et associations des Fragments aux onglets
    private void setupViewPager(ViewPager viewPager) {
        RechercheActivity.Adapter adapter = new RechercheActivity.Adapter(getSupportFragmentManager());
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

