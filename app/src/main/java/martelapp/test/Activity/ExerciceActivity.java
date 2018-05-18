package martelapp.test.Activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import martelapp.test.Fragment.Exercice.ArbresSelectionnesFragment;
import martelapp.test.Fragment.Exercice.ChoixConserverFragment;
import martelapp.test.Fragment.Exercice.ChoixMartelageFragment;
import martelapp.test.Fragment.Exercice.ConsignesFragment;
import martelapp.test.Fragment.Exercice.InfosFragment;
import martelapp.test.Fragment.Exercice.CarteParcelleFragment;
import martelapp.test.Fragment.Exercice.SelectionArbreFragment;
import martelapp.test.R;

public class ExerciceActivity extends AppCompatActivity {
    ViewPager viewPager;
    InfosFragment infosf;
    ConsignesFragment cf;
    ArbresSelectionnesFragment af;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice);

        //On configure notre ViewPager pour chaque onglet
        //Un fragment pour chaque onglet
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        //on associe nos onglets avec le viewpager
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //on ajoute un listener pour changer la couleur des icones dans les tabs
        tabs.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int white = ContextCompat.getColor(getApplicationContext(), R.color.colorWhite);
                        tab.getIcon().setColorFilter(white, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

        int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

        //on associe les icones aux tabs et on met en blanc le premier
        tabs.getTabAt(0).setIcon(R.drawable.consignes);
        int white = ContextCompat.getColor(getApplicationContext(), R.color.colorWhite);
        tabs.getTabAt(0).getIcon().setColorFilter(white, PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(1).setIcon(R.drawable.info);
        tabs.getTabAt(1).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(2).setIcon(R.drawable.carte);
        tabs.getTabAt(2).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(3).setIcon(R.drawable.marteau_tab);
        tabs.getTabAt(3).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(4).setIcon(R.drawable.arbre_selec);
        tabs.getTabAt(4).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        viewPager.setCurrentItem(0);//on selectionne le premier

    }

    // ajout et associations des Fragments aux onglets
    private void setupViewPager(ViewPager viewPager) {
        cf = new ConsignesFragment();
        cf.setVp(viewPager);
        infosf = new InfosFragment();
        infosf.setVp(viewPager);
        af = new ArbresSelectionnesFragment();
        af.setVp(viewPager);
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(cf, "Consignes");
        adapter.addFragment(infosf, "Info");
        adapter.addFragment(new CarteParcelleFragment(), "Carte");
        adapter.addFragment(new SelectionArbreFragment(), "Sélection");
        adapter.addFragment(af, "Arbres sélectionnés");
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

    public void reloadArbreMartelesFragment()
    {
        if(af != null){
            af.reload();
        }
    }
}

