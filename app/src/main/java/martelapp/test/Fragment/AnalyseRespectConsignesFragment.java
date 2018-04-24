package martelapp.test.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;

import java.text.DecimalFormat;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;


/**
 * Created by cimin on 24/04/2018.
 */

public class AnalyseRespectConsignesFragment extends Fragment {
    Cursor cur;
    DatabaseHelper dbHelper;
    TextView tvPrelevementVolumeR, tvGrosDiametreR, tvEcoR, tvGainR;
    ImageView ivPrelevementVolumeR, ivGrosDiametreR, ivEcoR;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_respect_consignes, null);

        dbHelper = new DatabaseHelper(view.getContext());

        tvPrelevementVolumeR = view.findViewById(R.id.tvPrelevementVolumeR);
        tvGrosDiametreR = view.findViewById(R.id.tvGrosDiametreR);
        tvEcoR = view.findViewById(R.id.tvEcoR);
        tvGainR = view.findViewById(R.id.tvGainR);

        ivPrelevementVolumeR = view.findViewById(R.id.ivPrelevementVolumeR);
        ivGrosDiametreR = view.findViewById(R.id.ivGrosDiametreR);
        ivEcoR = view.findViewById(R.id.ivEcoR);

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
        if (limiteInf >= volumeTotalBoisMartele || volumeTotalBoisMartele >= limiteSup) {
            tvPrelevementVolumeR.setTextColor(getResources().getColor(R.color.colorRed));
            ivPrelevementVolumeR.setColorFilter(getResources().getColor(R.color.colorRed));
            ivPrelevementVolumeR.setImageResource(R.drawable.cross);
            if(volumeTotalBoisMartele < limiteInf){
                tvPrelevementVolumeR.setText(String.format("%s : %s < %s", tvPrelevementVolumeR.getText(), volumeTotalBoisMartele, Math.floor(limiteInf)));
            }else{
                tvPrelevementVolumeR.setText(String.format("%s : %s > %s", tvPrelevementVolumeR.getText(), volumeTotalBoisMartele, Math.floor(limiteSup)));
            }

        }else {
            tvPrelevementVolumeR.setText(String.format("%s : %s > %s > %s", tvPrelevementVolumeR.getText(), Math.floor(limiteInf), volumeTotalBoisMartele, Math.floor(limiteSup)));
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

        int nbArbreDiamHectare = (int) (nbArbresDiamSup50Base - nbArbresDiamSup50Marteles) / (1 + (int) surfaceParcelle);
        tvGrosDiametreR.setText(String.format("%s : %s / %s", tvGrosDiametreR.getText(), (nbArbresDiamSup50Base - nbArbresDiamSup50Marteles), (int)Math.round(surfaceParcelle*3)));
        // Moins de 3 * surfaceParcelle arbres de diamètre > 50 à la fin de l'exercice
        if (nbArbreDiamHectare < 3) {
            tvGrosDiametreR.setTextColor(getResources().getColor(R.color.colorRed));
            ivGrosDiametreR.setColorFilter(getResources().getColor(R.color.colorRed));
            ivGrosDiametreR.setImageResource(R.drawable.cross);
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


        int nbArbreEcoHectare = (int) (nbArbreEcoBase - nbArbreEcoMarteles) / (1 + (int) surfaceParcelle);
        tvEcoR.setText(String.format("%s : %s / %s", tvEcoR.getText(), (nbArbreEcoBase - nbArbreEcoMarteles), (int)Math.floor(surfaceParcelle*2)));
        // Moins de 2 * surfaceParcelle arbres ECO à la fin de l'exercice
        if (nbArbreEcoHectare < 2) {
            tvEcoR.setTextColor(getResources().getColor(R.color.colorRed));
            ivEcoR.setColorFilter(getResources().getColor(R.color.colorRed));
            ivEcoR.setImageResource(R.drawable.cross);
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
        DecimalFormat df = new DecimalFormat("#0.00");
        tvGainR.setText("Gains total du martelage : " + df.format(gainTotal) + "€");


        return view;
    }
}
