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

import martelapp.test.Fragment.AnalyseParcelleFragment;
import martelapp.test.Fragment.AnalyseRaisonsFragment;
import martelapp.test.Fragment.AnalyseRespectConsignesFragment;
import martelapp.test.Fragment.AnalyseTigesDiametreFragment;
import martelapp.test.Fragment.AnalyseTigesEcoFragment;
import martelapp.test.Fragment.AnalyseTigesEssencesFragment;
import martelapp.test.Fragment.AnalyseVolumeCategorieFragment;
import martelapp.test.Fragment.AnalyseVolumeDiametreFragment;
import martelapp.test.R;

public class AnalyseActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);

        //On configure notre ViewPager pour chaque onglet
        //Un fragment pour chaque onglet
        viewPager = (ViewPager) findViewById(R.id.viewpagerAnalyse);
        setupViewPager(viewPager);

        //on associe nos onglets avec le viewpager
        TabLayout tabs = (TabLayout) findViewById(R.id.tabsAnalyse);
        tabs.setupWithViewPager(viewPager);
    }

    // ajout et associations des Fragments aux onglets
    private void setupViewPager(ViewPager viewPager) {
        RechercheActivity.Adapter adapter = new RechercheActivity.Adapter(getSupportFragmentManager());
        adapter.addFragment(new AnalyseRespectConsignesFragment(), "Consignes");
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
}

