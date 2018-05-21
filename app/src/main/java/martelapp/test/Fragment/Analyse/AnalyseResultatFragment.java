package martelapp.test.Fragment.Analyse;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import martelapp.test.Activity.AnalyseActivity;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.OnSwipeTouchListener;
import martelapp.test.R;

/**
 * Created by Baptiste on 19/05/2018.
 */

public class AnalyseResultatFragment extends Fragment {

    private static int GROS_DIAMETRE = 50;
    private static int NOTE_ECO_HAUTE = 6;

    View view;

    ViewPager viewPager;

    BottomNavigationView bottomNavigationView;
    ImageButton previous, next;

    LinearLayout layoutRespectConsignes;
    LinearLayout layoutSynthesePicto;

    TextView tvPrelevementVolumeR, tvGrosDiametreR, tvEcoR;
    ImageView ivPrelevementVolumeR, ivGrosDiametreR, ivEcoR;

    TextView tvVolumePreleve, tvGainCoupe, tvNoteEco, tvTitreRespect;

    DecimalFormat df;

    int     prelevementMin = 0,
            prelevementMax = 0;

    float volumeTotalBoisMartele;

    float diametre;

    float noteEcologique;

    double volumeBoisTotalParcelle = 0f;
    double volumeBoisTotalParcelleHa = 0f;
    double  surfaceParcelle = 0f;

    float volumeMartelePourcent;

    float gainCoupe = 0f;

    int noteEcoAvant, noteEcoMartele;
    float pourcentageNoteEcoMartelage;

    private boolean respectConsigneViewAdded = false;
    private boolean synthesePictoViewAdded = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.view_page_analyse_resultat, null);


            layoutRespectConsignes = view.findViewById(R.id.layout_respect_consigne);
            layoutSynthesePicto = view.findViewById(R.id.layout_synthese_picto);

            tvPrelevementVolumeR = view.findViewById(R.id.tvPrelevementVolumeR);
            tvGrosDiametreR = view.findViewById(R.id.tvGrosDiametreR);
            tvEcoR = view.findViewById(R.id.tvEcoR);

            ivPrelevementVolumeR = view.findViewById(R.id.ivPrelevementVolumeR);
            ivGrosDiametreR = view.findViewById(R.id.ivGrosDiametreR);
            ivEcoR = view.findViewById(R.id.ivEcoR);

            tvVolumePreleve = view.findViewById(R.id.tvVolumePreleve);
            tvGainCoupe = view.findViewById(R.id.tvGainCoupe);
            tvNoteEco = view.findViewById(R.id.tvNoteEco);
            tvTitreRespect = view.findViewById(R.id.tvTitreRespect);

            bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation_resultat_analyse);
            previous = (ImageButton) view.findViewById(R.id.previousInfo);
            next = (ImageButton) view.findViewById(R.id.nextInfo);

            getValeur(view.getContext());
        }

        //on gere le swipe gauche et droite (un peu brute)
        view.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
            public void onSwipeRight() {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_respect_consigne_analyse:
                        break;
                    case R.id.action_synthese_picto_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_respect_consigne_analyse);
                        break;
                }
            }

            public void onSwipeLeft() {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_respect_consigne_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_synthese_picto_analyse);
                        break;
                    case R.id.action_synthese_picto_analyse:
                        break;
                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_respect_consigne_analyse:
                        break;
                    case R.id.action_synthese_picto_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_respect_consigne_analyse);
                        break;
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (bottomNavigationView.getSelectedItemId()) {
                    case R.id.action_respect_consigne_analyse:
                        bottomNavigationView.setSelectedItemId(R.id.action_synthese_picto_analyse);
                        break;
                    case R.id.action_synthese_picto_analyse:
                        break;
                }
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_respect_consigne_analyse:
                                previous.setVisibility(View.INVISIBLE);
                                next.setVisibility(View.VISIBLE);

                                tvTitreRespect.setText(R.string.titre_respect_consignes);
                                layoutRespectConsignes.setVisibility(View.VISIBLE);
                                layoutSynthesePicto.setVisibility(View.GONE);

                                if(!respectConsigneViewAdded){
                                    ((AnalyseActivity) getActivity()).addViewPdf(layoutRespectConsignes, 0);
                                    respectConsigneViewAdded = true;
                                }
                                break;


                            case R.id.action_synthese_picto_analyse:
                                previous.setVisibility(View.VISIBLE);
                                next.setVisibility(View.INVISIBLE);

                                tvTitreRespect.setText(R.string.titre_resultats);
                                layoutRespectConsignes.setVisibility(View.GONE);
                                layoutSynthesePicto.setVisibility(View.VISIBLE);

                                if(!synthesePictoViewAdded){
                                    ((AnalyseActivity) getActivity()).addViewPdf(layoutSynthesePicto, 1);
                                    synthesePictoViewAdded = true;
                                }
                                break;
                        }
                        return true;
                    }
                });



        return view;
    }

    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    public void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.action_respect_consigne_analyse);
    }


    private void getValeur(Context context){
        Cursor cur;
        DatabaseHelper dbHelper;

        dbHelper = new DatabaseHelper(context);

        // Récupération du prélevement min et max sur la parcelle en %
        cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
        cur.moveToFirst();
        prelevementMin = (int) cur.getFloat(cur.getColumnIndex(DatabaseHelper.PRELEVEMENT_VOLUME_MIN));
        prelevementMax = (int) cur.getFloat(cur.getColumnIndex(DatabaseHelper.PRELEVEMENT_VOLUME_MAX));


        surfaceParcelle = cur.getDouble(cur.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));

        df = new DecimalFormat("#0.00");


        // Volume de bois parcelle remis à l'hectare
        cur = dbHelper.getDataFromTable("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL +")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE);
        cur.moveToFirst();
        volumeBoisTotalParcelle = cur.getDouble(0);
        volumeBoisTotalParcelleHa = volumeBoisTotalParcelle / surfaceParcelle;

        dbHelper.close();
        cur.close();


        /*
            CONSIGNE 1:
            Prélever entre prelevementMin et prelevementMax
        */

        // Calcul volume prélevé
        cur = dbHelper.getDataFromTableWithCondition("SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART);
        cur.moveToFirst();
        volumeTotalBoisMartele = cur.getFloat(0);


        volumeMartelePourcent = (float)(volumeTotalBoisMartele/volumeBoisTotalParcelle)*100;
        /*tvPrelevementVolumeR.setText(String.format("Prélever entre %s et %s du volume de bois de la parcelle.\nVolume prélevé: %sm3 (%s)",
                prelevementMin +"%" , prelevementMax +"%" ,df.format(volumeTotalBoisMartele),df.format(volumeMartelePourcent)+"%"));*/

        if (prelevementMin >= volumeMartelePourcent || volumeMartelePourcent >= prelevementMax) {
            //tvPrelevementVolumeR.setTextColor(getResources().getColor(R.color.colorRed));
            tvPrelevementVolumeR.setText(Html.fromHtml(
                    "<i>Prélever entre " + prelevementMin + "% et " + prelevementMax + "% du volume de bois de la parcelle." +
                            "<br>(Entre " + (int)(volumeBoisTotalParcelle * prelevementMin / 100)  + " et " + (int)(volumeBoisTotalParcelle * prelevementMax / 100) + " m3 pour notre parcelle)</i>" +
                            "<br><font color='#e14b4b'>Volume prélevé : "+ df.format(volumeTotalBoisMartele) + " m3 (" + df.format(volumeMartelePourcent) + "%)</font>" ));
            ivPrelevementVolumeR.setColorFilter(getResources().getColor(R.color.colorRed));
            ivPrelevementVolumeR.setImageResource(R.drawable.cross);
        }
        else{
            tvPrelevementVolumeR.setText(Html.fromHtml(
                    "<i>Prélever entre " + prelevementMin + "% et " + prelevementMax + "% du volume de bois de la parcelle." +
                            "<br>(Entre " + (int)(volumeBoisTotalParcelle * prelevementMin / 100)  + " et " + (int)(volumeBoisTotalParcelle * prelevementMax / 100) + "m3 pour notre parcelle)</i>" +
                            "<br><font color='#32af4b'>Volume prélevé : "+ df.format(volumeTotalBoisMartele) + " m3 (" + df.format(volumeMartelePourcent) + "%)</font>"));
        }

        /*
            CONSIGNE 1 *********************************************
        */





        /*
            CONSIGNE 2:
            Avoir conservé au moins 3 arbres de gros diamètre par hectare
        */


        int nbArbresDiamSup50conserve = 0;

        // Nombre d'abres à diametre > 50 Conservé
        cur = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS +
                        " AND ap." + DatabaseHelper.DIAMETRE_ARBRE + " >= " + GROS_DIAMETRE);
        cur.moveToFirst();
        nbArbresDiamSup50conserve = cur.getCount();

        int arbreParcelleGrosDiametre = (int)Math.ceil(3*surfaceParcelle);

        // Moins de 3 * surfaceParcelle arbres de diamètre > 50 à la fin de l'exercice
        if (nbArbresDiamSup50conserve < (3*surfaceParcelle)) {
            //tvGrosDiametreR.setTextColor(getResources().getColor(R.color.colorRed));
            tvGrosDiametreR.setText(Html.fromHtml("<i>Conserver au moins 3 arbres de gros diamètre par hectare." +
                            "<br>(Soit " + arbreParcelleGrosDiametre + (arbreParcelleGrosDiametre < 2 ? " arbre" : " arbres") + " pour notre parcelle)</i>" +
                    "<br><font color='#e14b4b'>Arbres conservés : " + nbArbresDiamSup50conserve + "</font>"));
            ivGrosDiametreR.setColorFilter(getResources().getColor(R.color.colorRed));
            ivGrosDiametreR.setImageResource(R.drawable.cross);
        }
        else{
            tvGrosDiametreR.setText(Html.fromHtml("<i>Conserver au moins 3 arbres de gros diamètre par hectare." +
                    "<br>(Soit " +  arbreParcelleGrosDiametre + (arbreParcelleGrosDiametre < 2 ? " arbre" : " arbres") + " pour notre parcelle)</i>" +
                    "<br><font color='#32af4b'>Arbres conservés : " + nbArbresDiamSup50conserve + "</font>"));
        }

        /*
            CONSIGNE 2
        */



        /*
            CONSIGNE 3:
            Avoir conservé au moins 2 arbres porteurs de micros habitats
        */

        int nbArbreEcoConserves = 0;

        // Nombre d'abres à NoteEco > 6
        cur = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS +
                        " AND ap." + DatabaseHelper.NOTE_ECO_ARBRE + " >= " + NOTE_ECO_HAUTE);
        cur.moveToFirst();

        nbArbreEcoConserves = cur.getCount();


        /*tvEcoR.setText(String.format("Conserver au moins 2 arbres porteurs de micros-habitats par hectare.\nArbres conservés: %s",
                (nbArbreEcoConserves)));*/

        int arbreParcelleEco = (int)Math.ceil(2*surfaceParcelle);


        // Moins de 2 * surfaceParcelle arbres ECO à la fin de l'exercice
        if (nbArbreEcoConserves < (2*surfaceParcelle)) {
            //tvEcoR.setTextColor(getResources().getColor(R.color.colorRed));
            tvEcoR.setText(Html.fromHtml("<i>Conserver au moins 2 arbres porteurs de micros-habitats par hectare." +
                            "<br>(Soit " +  arbreParcelleEco + (arbreParcelleEco < 2 ? " arbre" : " arbres") + " pour notre parcelle)</i>" +
                    "<br><font color='#e14b4b'>Arbres conservés : " + nbArbreEcoConserves + "</font>"));
            ivEcoR.setColorFilter(getResources().getColor(R.color.colorRed));
            ivEcoR.setImageResource(R.drawable.cross);
        }
        else{
            tvEcoR.setText(Html.fromHtml("<i>Conserver au moins 2 arbres porteurs de micros-habitats par hectare." +
                    "<br>(Soit " +  arbreParcelleEco + (arbreParcelleEco < 2 ? " arbre" : " arbres") + " pour notre parcelle)</i>" +
                    "<br><font color='#32af4b'>Arbres conservés : " + nbArbreEcoConserves + "</font>"));
        }


        /*
            CONSIGNE 3
        */


        /*
         **************************
         ***** SYNTHESE PICTO *****
         **************************
         */

        tvVolumePreleve.setText(df.format(volumeTotalBoisMartele) + " m3\n" +
                                "volume de bois prélevé");

        cur = dbHelper.getDataFromTableWithCondition("SUM(ap." + DatabaseHelper.VALEUR_ECONOMIQUE + ")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART);
        cur.moveToFirst();
        gainCoupe = cur.getFloat(0);

        tvGainCoupe.setText(Integer.toString((int)gainCoupe) + " € \n" +
                "Valeur de la coupe");


        cur = dbHelper.getDataFromTable("SUM(" + DatabaseHelper.NOTE_ECO_ARBRE + ")", DatabaseHelper.ARBRES_PARCELLE_TABLE);
        cur.moveToFirst();
        noteEcoAvant = cur.getInt(0);

        cur = dbHelper.getDataFromTableWithCondition("SUM(ap." + DatabaseHelper.NOTE_ECO_ARBRE + ")",
                DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART);
        cur.moveToFirst();
        noteEcoMartele = cur.getInt(0);

        System.out.println(noteEcoAvant + " - " + noteEcoMartele);

        pourcentageNoteEcoMartelage = ((float)noteEcoMartele / (float)noteEcoAvant) * 100;

        System.out.println(noteEcoAvant + " - " + noteEcoMartele);

        tvNoteEco.setText(Integer.toString((int)pourcentageNoteEcoMartelage) + "% du total des notes écologique martelés");

        dbHelper.close();
        cur.close();
    }

    public boolean getRespectConsigneViewAdded(){
        return respectConsigneViewAdded;
    }

    public boolean getSynthesePictoViewAdded(){
        return synthesePictoViewAdded;
    }

    public BottomNavigationView getBottomNavigationView(){
        return bottomNavigationView;
    }
}
