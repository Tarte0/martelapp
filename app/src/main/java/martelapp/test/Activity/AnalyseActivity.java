package martelapp.test.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import martelapp.test.Class.PdfCreator;
import martelapp.test.Fragment.Analyse.AnalyseGrapheCarteFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheNombreTigesFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheRaisonsFragment;
import martelapp.test.Fragment.Analyse.AnalyseGrapheVolumeFragment;
import martelapp.test.Fragment.Analyse.AnalyseListeArbresSelectionnesFragment;
import martelapp.test.Fragment.Analyse.AnalyseResultatFragment;
import martelapp.test.Fragment.Analyse.CreationPdfFragment;
import martelapp.test.R;

/**
 * AnalyseActivity est l'activité sur laquelle l'utilisateur arrive une fois l'exercice de martelage terminé.
 *
 * Elle contient plusieurs fragments séparés en plusieurs pages :
 * - Résultats :
 *                  - Respect des consignes : L'utilisateur à-t-il respecté les consignes?
 *                  - Synthèse du martelage : Différents nombres résumant l'exercice.
 *
 * - Arbres Sélectionnés :
 *                  - Arbres Martelés : Liste des arbres martelés pendant l'exercice.
 *                  - Arbres Conservés : Liste des arbres conservés pendant l'exercice.
 *
 * - Graphe Volume : Graphique de la Répartition du prélèvement en volume par classe de diamètre
 *
 * - Graphe Nombre de tiges :
 *                  - Essence : Graphique de la Répartition du prélèvement en nombre de tiges par essence.
 *                  - Diamètre : Graphique de la Répartition du prélèvement en nombre de tiges par classe de diamètre.
 *                  - Note écologique : Graphique de la Répartition du prélèvement en nombre de tiges par note écologique.
 *                  - Classe de diamètre : Graphique de l'évolution du nombre de tiges par classe de diamètre (Graphique avant et après martelage).
 *
 * - Graphe Raisons : Graphique de la Répartition des raisons de martelage.
 *
 * - Carte Parcelle : Carte des arbres martelés et conservés pendant l'exercice.
 */

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

    PdfCreator pdfCreator;
    CreationPdfFragment creationPdfFragment;

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

        adapter.addFragment(analyseResultatFragment, "Résultats");
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

    /**
     * Ouvre un popUp CreationPdfFragment pour indiquer la "Création des pdf en cours"
     */
    public CreationPdfFragment openCreationPdfPopup(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        CreationPdfFragment newFragment = new CreationPdfFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(android.R.id.content, newFragment);
        transaction.addToBackStack(null).commit();
        return newFragment;
    }


    /**
     * Méthode parcourant les pages de l'analyse pour récupérer les Views si ce n'est pas déjà fait
     * par l'utilisateur et appel l'asyncTask pour la création des deux pdf
     */
    public void getAllViewAndCreatePdf(){

        creationPdfFragment = openCreationPdfPopup();

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


        Handler handler9 = new Handler();
        handler9.postDelayed(new Runnable() {
            @Override
            public void run() {
                pdfCreator = new PdfCreator(getApplicationContext(), creationPdfFragment);
                pdfCreator.execute(listeViewPdf);
            }
        },delay);

        delay += addDelay*12;

        Handler handler8 = new Handler();
        handler8.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                finish();
            }
        }, delay);
    }



    public void addViewPdf(View view, int index){
        listeViewPdf[index] = view;
    }
}

