package martelapp.test.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import martelapp.test.Fragment.ArbresMartelesFragment;
import martelapp.test.Fragment.ChoixMartelageFragment;
import martelapp.test.Fragment.ConsignesFragment;
import martelapp.test.Fragment.ParcelleFragment;
import martelapp.test.Fragment.RechercheFragment;
import martelapp.test.R;

public class RechercheActivity extends AppCompatActivity {
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        //On configure notre ViewPager pour chaque onglet
        //Un fragment pour chaque onglet
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //on associe nos onglets avec le viewpager
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    // ajout et associations des Fragments aux onglets
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new RechercheFragment(), "Recherche");
        adapter.addFragment(new ConsignesFragment(), "Consignes");
        adapter.addFragment(new ParcelleFragment(), "Parcelle");
        adapter.addFragment(new ArbresMartelesFragment(), "Arbres martelés");
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

    public void openMartelagePopup(String numeroArbre){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChoixMartelageFragment newFragment = new ChoixMartelageFragment();
        newFragment.setNumeroArbre(numeroArbre);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(android.R.id.content, newFragment);
        transaction.addToBackStack(null).commit();
    }
}

