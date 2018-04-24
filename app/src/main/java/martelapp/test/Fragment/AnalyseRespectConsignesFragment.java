package martelapp.test.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;


/**
 * Created by cimin on 24/04/2018.
 */

public class AnalyseRespectConsignesFragment extends Fragment {
    Cursor cur;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_respect_consignes, null);

        dbHelper = new DatabaseHelper(view.getContext());

        cur = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap, " + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART);


        /**************
         *  CONSIGNE PRELEVEMENT VOLUME
         *************/


        float volumeTotalBoisMartele, gainTotal;


        cur = dbHelper.executeQuery("SELECT SUM(" + DatabaseHelper.VOLUME_COMMERCIAL + ")"
                + " FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am"
                + " WHERE ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART);
        cur.moveToFirst();
        volumeTotalBoisMartele = cur.getFloat(0);


        // Recup SURFACE_PARCELLE
        cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
        cur.moveToFirst();
        final float surfaceParcelle = cur.getFloat(cur.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));

        final float minPrelevement = (float) DatabaseHelper.MIN_PRELEVEMENT;
        final float maxPrelevement = (float) DatabaseHelper.MAX_PRELEVEMENT;

        float limiteInf = surfaceParcelle * minPrelevement;
        float limiteSup = surfaceParcelle * maxPrelevement;

        // Consigne Volume prélevement respectée**********************************************
        if (limiteInf < volumeTotalBoisMartele && volumeTotalBoisMartele < limiteSup) {

        }

        /*Toast.makeText(view.getContext(),
                Float.toString(volumeTotalBoisMartele), Toast.LENGTH_LONG).show();*/


        /**************
         *  CONSIGNE PRELEVEMENT VOLUME
         *************/


        /*******************
         * CONSIGNE il doit rester au moins 3 Arbres a diam > 50/ha
         */

        float diametre;

        int nbArbresDiamSup50Base = 0;


        // Nombre d'abres à diametre > 50 dans la Base
        cur = dbHelper.executeQuery("SELECT(" + DatabaseHelper.DIAMETRE_ARBRE + ")"
                + " FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE);

        while (cur.moveToNext()) {
            diametre = cur.getFloat(0);
            if (diametre >= 50) {
                nbArbresDiamSup50Base++;
            }

        }

        int nbArbresDiamSup50Marteles = 0;

        // Nombre d'abres à diametre > 50 Marteles
        cur = dbHelper.executeQuery("SELECT(" + DatabaseHelper.DIAMETRE_ARBRE + ")"
                + " FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am"
                + " WHERE ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART);

        while (cur.moveToNext()) {
            diametre = cur.getFloat(0);
            if (diametre >= 50) {
                nbArbresDiamSup50Marteles++;
            }

        }

        int nbArbreDiamHectare = (int) (nbArbresDiamSup50Base - nbArbresDiamSup50Marteles)/(1+(int)surfaceParcelle);
        // Moins de 3 * surfaceParcelle arbres de diamètre > 50 à la fin de l'exercice
        if(nbArbreDiamHectare<3){
            //CONSIGNE PAS RESPECTEE ***********************************************************************************
        }

        /*******************
         * CONSIGNE il doit rester au moins 3 Arbres a diam > 50/ha
         */



        /*******************
         * CONSIGNE il doit rester au moins  Arbres a noteEco > 6 /ha
         */


        float noteEcologique;

        int nbArbreEcoBase = 0;


        // Nombre d'abres à diametre > 50 dans la Base
        cur = dbHelper.executeQuery("SELECT(" + DatabaseHelper.NOTE_ECO_ARBRE + ")"
                + " FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE);

        while (cur.moveToNext()) {
            noteEcologique = cur.getFloat(0);
            if (noteEcologique >= 6) {
                nbArbreEcoBase++;
            }

        }

        int nbArbreEcoMarteles = 0;

        // Nombre d'abres à diametre > 50 Marteles
        cur = dbHelper.executeQuery("SELECT(" + DatabaseHelper.NOTE_ECO_ARBRE + ")"
                + " FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am"
                + " WHERE ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART);

        while (cur.moveToNext()) {
            noteEcologique = cur.getFloat(0);
            if (noteEcologique >= 6) {
                nbArbreEcoMarteles++;
            }

        }




        int nbArbreEcoHectare = (int) (nbArbreEcoBase - nbArbreEcoMarteles)/(1+(int)surfaceParcelle);
        // Moins de 2 * surfaceParcelle arbres ECO à la fin de l'exercice
        if(nbArbreEcoHectare<2){
            //CONSIGNE PAS RESPECTEE ***********************************************************************************
        }


        /*******************
         * CONSIGNE il doit rester au moins  Arbres a noteEco > 6 /ha
         */





        /*
         * GAIN de tous les arbres
         */

        cur = dbHelper.executeQuery("SELECT SUM(" + DatabaseHelper.VALEUR_ECONOMIQUE + ")"
                + " FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am"
                + " WHERE ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART);
        cur.moveToFirst();
        gainTotal = cur.getFloat(0);


        // AFFICHER GAIN TOTAL

        /*
         * GAIN de tous les arbres
         */


        return view;
    }
}
