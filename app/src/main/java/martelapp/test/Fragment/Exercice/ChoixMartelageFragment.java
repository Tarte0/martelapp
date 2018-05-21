package martelapp.test.Fragment.Exercice;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import martelapp.test.Activity.MessageErreurArbreMarteleActivity;
import martelapp.test.Activity.ExerciceActivity;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by cimin on 10/04/2018.
 */

public class ChoixMartelageFragment extends DialogFragment {
    CheckBox arbreMur, eclaircie, sanitaire, regeneration, exploitation, stabilite;
    Button  boutonMarteler,
            buttonCancel;

    TextView tvNum,
            tvEssence,
            tvEtat,
            tvDiametre;

    LinearLayout treeCardNumber;

    DatabaseHelper dbHelper;
    Cursor cur1;
    public static int noteEcologiqueHaute = 6;
    private String numeroArbre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_page_choixmartelage, null);
        final View rechercheFragmentView = inflater.inflate(R.layout.view_page_selection, null);

        dbHelper = new DatabaseHelper(view.getContext());

        arbreMur = (CheckBox) view.findViewById(R.id.arbreMur);
        eclaircie = (CheckBox) view.findViewById(R.id.eclaircie);
        sanitaire = (CheckBox) view.findViewById(R.id.sanitaire);
        regeneration = (CheckBox) view.findViewById(R.id.regeneration);
        exploitation = (CheckBox) view.findViewById(R.id.exploitation);
        stabilite = (CheckBox) view.findViewById(R.id.stabilite);

        boutonMarteler = (Button) view.findViewById(R.id.boutonMarteler);
        buttonCancel = (Button) view.findViewById(R.id.cancel);
        //buttonConserver = mainView.findViewById(R.id.conserver);

        tvNum = (TextView) view.findViewById(R.id.numero_tree_card);
        tvEssence = (TextView) view.findViewById(R.id.essence_tree_card);
        tvEtat = (TextView) view.findViewById(R.id.etat_tree_card);
        tvDiametre = (TextView) view.findViewById(R.id.diametre_tree_card);


        treeCardNumber = view.findViewById(R.id.arbreLayout);
        // Affichage caractéristiques de l'arbre
        String queryCaractArbre = "SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE "
                                    + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numeroArbre;

        cur1 = dbHelper.executeQuery(queryCaractArbre);
        cur1.moveToFirst();

        tvEssence.setText(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
        tvEtat.setText(etatToString(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ETAT_ARBRE))));
        tvDiametre.setText(String.format("Diametre : %s cm", cur1.getString(cur1.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE))));
        tvNum.setText(String.format("Arbre n°%s", cur1.getString(cur1.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC))));

        treeCardNumber.setVisibility(View.GONE);


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

                    if(rechercheFragmentView.getContext() instanceof ExerciceActivity){
                        ((ExerciceActivity) rechercheFragmentView.getContext()).reloadArbreMartelesFragment();
                    }


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

}
