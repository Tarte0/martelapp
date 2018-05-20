package martelapp.test.Activity;

import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import martelapp.test.Class.PdfCreator;
import martelapp.test.Fragment.Analyse.AnalyseGrapheCarteFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheRaisonsFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheVolumeFragment;
import martelapp.test.Fragment.Analyse.AnalyseListeArbresSelectionnesFragment;
import martelapp.test.Fragment.Analyse.AnalyseResultatFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheNombreTigesFragment;
import martelapp.test.Fragment.Exercice.ChoixConserverFragment;
import martelapp.test.R;


public class AnalyseActivity extends AppCompatActivity {
    /**
     * Viewpager de l'activité utilisé pour creer plusieurs fragments
     */
    ViewPager viewPager;

    AnalyseResultatFragment analyseResultatFragment;
    AnalyseListeArbresSelectionnesFragment analyseListeArbresSelectionnesFragment;
    AnalyseGrapheVolumeFragment analyseGrapheVolumeFragment;
    AnalyseGrapheNombreTigesFragment analyseGrapheNombreTigesFragment;
    AnalyseGrapheRaisonsFragment analyseGrapheRaisonsFragment;
    AnalyseGrapheCarteFragment analyseGrapheCarteFragment;

    View listeViewPdf[];

    BottomNavigationView btv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);



        // On configure notre ViewPager pour chaque onglet
        // Un fragment pour chaque onglet
        viewPager = findViewById(R.id.viewpagerAnalyse);
        setupViewPager(viewPager);

        // On associe nos onglets avec le viewpager
        final TabLayout tabs = (TabLayout) findViewById(R.id.tabsAnalyse);
        tabs.setupWithViewPager(viewPager);

        listeViewPdf = new View[9];

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

        analyseGrapheVolumeFragment = new AnalyseGrapheVolumeFragment();

        analyseGrapheNombreTigesFragment = new AnalyseGrapheNombreTigesFragment();
        analyseGrapheNombreTigesFragment.setVp(viewPager);

        analyseGrapheRaisonsFragment = new AnalyseGrapheRaisonsFragment();

        analyseGrapheCarteFragment = new AnalyseGrapheCarteFragment();

        adapter.addFragment(analyseResultatFragment, "Résumé");
        adapter.addFragment(analyseListeArbresSelectionnesFragment, "Arbres Sélectionnes");
        adapter.addFragment(analyseGrapheVolumeFragment, "Graphe volume");
        adapter.addFragment(analyseGrapheNombreTigesFragment, "Graphe nombre de tiges");
        adapter.addFragment(analyseGrapheRaisonsFragment, "Graphe raisons");
        adapter.addFragment(analyseGrapheCarteFragment, "Carte parcelle");
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

    public void openCreationPdfPopup(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChoixConserverFragment newFragment = new ChoixConserverFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(android.R.id.content, newFragment);
        transaction.addToBackStack(null).commit();
    }

    public void getAllViewAndCreatePdf(){

        int delay = 0;
        int addDelay = 500;

        /*
         ******************
         ***** PAGE 0 *****
         ******************
         */
        if(!analyseResultatFragment.getRespectConsigneViewAdded()){
            viewPager.setCurrentItem(0);
            btv = analyseResultatFragment.getBottomNavigationView();
            btv.setSelectedItemId(R.id.action_respect_consigne_analyse);

            delay += addDelay;
        }

        if(!analyseResultatFragment.getSynthesePictoViewAdded()){
            Handler handler0 = new Handler();
            handler0.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(0);
                    btv = analyseResultatFragment.getBottomNavigationView();
                    btv.setSelectedItemId(R.id.action_synthese_picto_analyse);
                }
            },delay);

            delay+= addDelay;
        }

        /*
         ******************
         ***** PAGE 2 *****
         ******************
         */

        if(!analyseGrapheVolumeFragment.getViewBarChartVolumeAdded()){
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(2);
                }
            },delay);

            delay+= addDelay;
        }

        /*
         ******************
         ***** PAGE 3 *****
         ******************
         */
        if(!analyseGrapheNombreTigesFragment.getViewBarChartEssenceAdded()){
            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(3);
                    btv = analyseGrapheNombreTigesFragment.getBottomNavigationView();
                    btv.setSelectedItemId(R.id.action_graphe_essence_analyse);
                }
            },delay);

            delay+= addDelay;
        }

        if(!analyseGrapheNombreTigesFragment.getViewBarChartDiametreAdded()){
            Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(3);
                    btv = analyseGrapheNombreTigesFragment.getBottomNavigationView();
                    btv.setSelectedItemId(R.id.action_graphe_diametre_analyse);
                }
            },delay);

            delay+= addDelay;
        }

        if(!analyseGrapheNombreTigesFragment.getViewBarChartNoteEcologiqueAdded()){
            Handler handler4 = new Handler();
            handler4.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(3);
                    btv = analyseGrapheNombreTigesFragment.getBottomNavigationView();
                    btv.setSelectedItemId(R.id.action_graphe_note_eco_analyse);
                }
            },delay);

            delay+= addDelay;
        }

        if(!analyseGrapheNombreTigesFragment.getViewPieChartEvolutionAdded()){
            Handler handler5 = new Handler();
            handler5.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(3);
                    btv = analyseGrapheNombreTigesFragment.getBottomNavigationView();
                    btv.setSelectedItemId(R.id.action_graphe_evolution_nb_tiges);
                }
            },delay);

            delay+= addDelay;
        }

        /*
         ******************
         ***** PAGE 4 *****
         ******************
         */

        if(!analyseGrapheRaisonsFragment.getViewBarChartRaisonsAdded()){
            Handler handler6 = new Handler();
            handler6.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(4);
                }
            },delay);

            delay+= addDelay;
        }

        /*
         ******************
         ***** PAGE 5 *****
         ******************
         */

        if(!analyseGrapheCarteFragment.getViewScatterChartCarteAdeed()){
            Handler handler7 = new Handler();
            handler7.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(5);
                }
            },delay);

            delay+= addDelay;
        }

        Handler handler8 = new Handler();
        handler8.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(5);
            }
        }, delay);

        delay+= addDelay;

        Handler handler9 = new Handler();
        handler9.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("Je fait le pdf !!!!!!!!!!!!!!!!!!!");
                PdfCreator pdfCreator = new PdfCreator(getApplicationContext());
                pdfCreator.execute(listeViewPdf);
            }
        },delay);
    }



    public void addViewPdf(View view, int index){
        listeViewPdf[index] = view;
    }
}

