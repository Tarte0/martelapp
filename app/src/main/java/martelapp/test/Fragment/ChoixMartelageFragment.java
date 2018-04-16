package martelapp.test.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import martelapp.test.Activity.MessageErreurArbreMarteleActivity;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by cimin on 10/04/2018.
 */

public class ChoixMartelageFragment extends DialogFragment {
    CheckBox arbreMur, eclaircie, sanitaire, regeneration, exploitation, stabilite;
    Button boutonMarteler,
            buttonCancel;
    DatabaseHelper dbHelper;
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

                    // Si le numéro entrée correspond à un arbre existant dans la parcelle
                    if (cur.moveToFirst()) {
                        dbHelper.insertArbreMarteles(numeroArbre);
                        insertRaisonFromCheckBoxes();

                        if (cur.getInt(cur.getColumnIndex(DatabaseHelper.NOTE_ECO_ARBRE)) > noteEcologiqueHaute) {
                            Intent intent = new Intent(view.getContext(), MessageErreurArbreMarteleActivity.class);
                            startActivity(intent);
                        }

                    }

                    dismiss();

                }else if(!possedeRaison()){
                    Snackbar errorsSnack = Snackbar.make(view,"Il faut choisir au moins une raison",Snackbar.LENGTH_LONG);

                    errorsSnack.show();
                }
            }
        });
        return view;
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
