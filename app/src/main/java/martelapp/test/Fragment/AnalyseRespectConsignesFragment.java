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
    TextView tvPrelevementVolumeR, tvGrosDiametreR, tvEcoR;
    ImageView ivPrelevementVolumeR, ivGrosDiametreR, ivEcoR;

    DecimalFormat df;

    int     prelevementMin = 0,
            prelevementMax = 0;

    float volumeTotalBoisMartele;

    float diametre;
    int nbArbresDiamSup50conserve = 0;

    float noteEcologique;
    int nbArbreEcoConserves =0;

    double volumeBoisTotalParcelle = 0f;
    double volumeBoisTotalParcelleHa = 0f;
    double  surfaceParcelle = 0f;

    float volumeMartelePourcent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_respect_consignes, null);

        dbHelper = new DatabaseHelper(view.getContext());

        tvPrelevementVolumeR = view.findViewById(R.id.tvPrelevementVolumeR);
        tvGrosDiametreR = view.findViewById(R.id.tvGrosDiametreR);
        tvEcoR = view.findViewById(R.id.tvEcoR);

        ivPrelevementVolumeR = view.findViewById(R.id.ivPrelevementVolumeR);
        ivGrosDiametreR = view.findViewById(R.id.ivGrosDiametreR);
        ivEcoR = view.findViewById(R.id.ivEcoR);

        // Récupération du prélevement min et max sur la parcelle en %
        cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
        cur.moveToFirst();
        prelevementMin = (int) cur.getFloat(cur.getColumnIndex(DatabaseHelper.PRELEVEMENT_VOLUME_MIN));
        prelevementMax = (int) cur.getFloat(cur.getColumnIndex(DatabaseHelper.PRELEVEMENT_VOLUME_MAX));


        surfaceParcelle = cur.getDouble(cur.getColumnIndex(DatabaseHelper.SURFACE_PARCELLE));


        df = new DecimalFormat("#0.00");


        // !!!!!!!!!!!!!! A RETIRER QUAND SURFACE SERA EN HA !!!!!!!!!!!!!!!!!!!

        surfaceParcelle = surfaceParcelle / 1000;

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


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
        tvPrelevementVolumeR.setText(String.format("Prélever entre %s et %s du volume de bois de la parcelle.\nVolume prélevé: %sm3 (%s)",
                prelevementMin +"%" , prelevementMax +"%" ,df.format(volumeTotalBoisMartele),df.format(volumeMartelePourcent)+"%"));
        if (prelevementMin >= volumeMartelePourcent || volumeMartelePourcent >= prelevementMax) {
            tvPrelevementVolumeR.setTextColor(getResources().getColor(R.color.colorRed));
            ivPrelevementVolumeR.setColorFilter(getResources().getColor(R.color.colorRed));
            ivPrelevementVolumeR.setImageResource(R.drawable.cross);
        }

        /*
            CONSIGNE 1 *********************************************
        */





        /*
            CONSIGNE 2:
            Avoir conservé au moins 3 arbres de gros diamètre par hectare
        */


        // Nombre d'abres à diametre > 50 Conservé
        cur = dbHelper.getAllDataFromTableWithCondition(
                 DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac"
                , "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS);

        while (cur.moveToNext()) {
            diametre = cur.getFloat(cur.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE));
            if (diametre >= 50) {
                nbArbresDiamSup50conserve++;
            }

        }


        tvGrosDiametreR.setText(String.format("Conserver au moins 3 arbres de gros diamètre par hectare.\nArbres conservés: %s",
                (nbArbresDiamSup50conserve)));
        // Moins de 3 * surfaceParcelle arbres de diamètre > 50 à la fin de l'exercice
        if (nbArbresDiamSup50conserve < (3*surfaceParcelle)) {
            tvGrosDiametreR.setTextColor(getResources().getColor(R.color.colorRed));
            ivGrosDiametreR.setColorFilter(getResources().getColor(R.color.colorRed));
            ivGrosDiametreR.setImageResource(R.drawable.cross);
        }

        /*
            CONSIGNE 2
        */



        /*
            CONSIGNE 3:
            Avoir conservé au moins 2 arbres porteurs de micros habitats
        */

        // Nombre d'abres à NoteEco > 6
        cur = dbHelper.getAllDataFromTableWithCondition(
                DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS);

        while (cur.moveToNext()) {
            noteEcologique = cur.getFloat(cur.getColumnIndex(DatabaseHelper.NOTE_ECO_ARBRE));
            if (noteEcologique >= 6) {
                nbArbreEcoConserves++;
            }
        }
        tvEcoR.setText(String.format("Conserver au moins 2 arbres porteurs de micros-habitats par hectare.\nArbres conservés: %s",
                (nbArbreEcoConserves)));
        // Moins de 2 * surfaceParcelle arbres ECO à la fin de l'exercice
        if (nbArbreEcoConserves < (2*surfaceParcelle)) {
            tvEcoR.setTextColor(getResources().getColor(R.color.colorRed));
            ivEcoR.setColorFilter(getResources().getColor(R.color.colorRed));
            ivEcoR.setImageResource(R.drawable.cross);
        }


        /*
            CONSIGNE 3
        */

        return view;
    }
}
