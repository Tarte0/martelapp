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
import martelapp.test.Fragment.ChoixConserverFragment;
import martelapp.test.Fragment.ChoixMartelageFragment;
import martelapp.test.Fragment.ConsignesFragment;
import martelapp.test.Fragment.InfosFragment;
import martelapp.test.Fragment.ParcelleFragment;
import martelapp.test.Fragment.RechercheFragment;
import martelapp.test.R;

public class RechercheActivity extends AppCompatActivity {
    ViewPager viewPager;
    InfosFragment infosf;
    ConsignesFragment cf;
    ArbresMartelesFragment af;


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


        viewPager.setCurrentItem(0);

    }

    // ajout et associations des Fragments aux onglets
    private void setupViewPager(ViewPager viewPager) {
        cf = new ConsignesFragment();
        cf.setVp(viewPager);
        infosf = new InfosFragment();
        infosf.setVp(viewPager);
        af = new ArbresMartelesFragment();
        af.setVp(viewPager);
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(cf, "Consignes");
        adapter.addFragment(infosf, "Info");
        adapter.addFragment(new ParcelleFragment(), "Carte");
        adapter.addFragment(new RechercheFragment(), "Sélection");
        adapter.addFragment(new ArbresMartelesFragment(), "Arbres sélectionnés");
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

    public void openConserverPopup(String numeroArbre){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChoixConserverFragment newFragment = new ChoixConserverFragment();
        newFragment.setNumeroArbre(numeroArbre);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(android.R.id.content, newFragment);
        transaction.addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {

    }
}

