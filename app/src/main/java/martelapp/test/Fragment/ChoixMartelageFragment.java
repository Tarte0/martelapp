package martelapp.test.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import martelapp.test.Activity.MessageErreurArbreMarteleActivity;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by cimin on 10/04/2018.
 */

public class ChoixMartelageFragment extends DialogFragment {
    CheckBox arbreMur, eclaircie, sanitaire, regeneration, exploitation, stabilite;
    Button  boutonMarteler,
            buttonCancel,
            buttonConserver;
    TextView textViewNum,
             textViewEssence,
             textViewEtat,
             textViewDiametre;
    DatabaseHelper dbHelper;
    Cursor cur1;
    int noteEcologiqueHaute = 6;
    private String numeroArbre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_page_choixmartelage, null);
        final View rechercheFragmentView = inflater.inflate(R.layout.view_page_recherche, null);

        dbHelper = new DatabaseHelper(view.getContext());

        arbreMur = (CheckBox) view.findViewById(R.id.arbreMur);
        eclaircie = (CheckBox) view.findViewById(R.id.eclaircie);
        sanitaire = (CheckBox) view.findViewById(R.id.sanitaire);
        regeneration = (CheckBox) view.findViewById(R.id.regeneration);
        exploitation = (CheckBox) view.findViewById(R.id.exploitation);
        stabilite = (CheckBox) view.findViewById(R.id.stabilite);

        boutonMarteler = (Button) view.findViewById(R.id.boutonMarteler);
        buttonCancel = (Button) view.findViewById(R.id.cancel);
        //buttonConserver = view.findViewById(R.id.conserver);

        textViewNum = view.findViewById(R.id.textViewNum);
        textViewEssence = view.findViewById(R.id.textViewEssence);
        textViewEtat = view.findViewById(R.id.textViewEtat);
        textViewDiametre = view.findViewById(R.id.textViewDiametre);

        // Affichage caractéristiques de l'arbre
        String queryCaractArbre = "SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE "
                                    + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numeroArbre;

        cur1 = dbHelper.executeQuery(queryCaractArbre);
        cur1.moveToFirst();

        textViewNum.setText(String.format("Arbre n°%s", cur1.getString(cur1.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC))));
        textViewEssence.setText(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
        textViewEtat.setText(etatToString(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ETAT_ARBRE))));
        textViewDiametre.setText(String.format("Diametre : %s cm", cur1.getString(cur1.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE))));



        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

                boutonMarteler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (possedeRaison() && !numeroArbre.equals("")) {
                    String query = "SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE " + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numeroArbre;
                    Cursor cur = dbHelper.executeQuery(query);
                    cur.moveToFirst();
                    dbHelper.insertArbreMartele(numeroArbre);
                    insertRaisonFromCheckBoxes();

                    if (cur.getInt(cur.getColumnIndex(DatabaseHelper.NOTE_ECO_ARBRE)) > noteEcologiqueHaute) {
                        Intent intent = new Intent(view.getContext(), MessageErreurArbreMarteleActivity.class);
                        startActivity(intent);
                    }



                    dismiss();

                }else if(!possedeRaison()){
                    Snackbar errorsSnack = Snackbar.make(view,"Il faut choisir au moins une raison",Snackbar.LENGTH_LONG);

                    errorsSnack.show();
                }
            }
        });

        /*buttonConserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.insertArbreConserve(numeroArbre);
                dbHelper.insertRaison(numeroArbre, DatabaseHelper.BIODIVERSITE);
                dismiss();
            }
        });*/
        return view;
    }

    private String etatToString(String etat) {
        switch (etat) {
            case "v":
                return "Vivant";
            case "mp":
                return "Mort sur pied";
            case "ms":
                return "Mort sur sol";
            default:
                return "";
        }
    }

    // Savoir si une raison est bien cochée au minimum
    public boolean possedeRaison() {
        if (arbreMur.isChecked() || eclaircie.isChecked() || sanitaire.isChecked() || regeneration.isChecked() || exploitation.isChecked() || stabilite.isChecked()) {
            return true;
        }
        return false;
    }

    // Ajouter raison a BDD
    public void insertRaisonFromCheckBoxes() {
        if (arbreMur.isChecked()) {
            dbHelper.insertRaison(numeroArbre, DatabaseHelper.ARBRE_MUR);
        }
        if (eclaircie.isChecked()) {
            dbHelper.insertRaison(numeroArbre, DatabaseHelper.ECLAIRCIE);
        }
        if (sanitaire.isChecked()) {
            dbHelper.insertRaison(numeroArbre, DatabaseHelper.SANITAIRE);
        }
        if (regeneration.isChecked()) {
            dbHelper.insertRaison(numeroArbre, DatabaseHelper.REGENERATION);
        }
        if (exploitation.isChecked()) {
            dbHelper.insertRaison(numeroArbre, DatabaseHelper.EXPLOITATION);
        }
        if (stabilite.isChecked()) {
            dbHelper.insertRaison(numeroArbre, DatabaseHelper.STABILITE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setNumeroArbre(String numeroArbre) {
        this.numeroArbre = numeroArbre;
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_ak, menu);
    }*/
}
