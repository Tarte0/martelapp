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
 * ChoixMartelerFragment est le fragment dans lequel l'utilisateur peut choisir de marteler un arbre.
 */
public class ChoixMartelageFragment extends DialogFragment {
    // CheckBox contenant les raisons de martelage d'un arbre
    CheckBox arbreMur, eclaircie, sanitaire, regeneration, exploitation;

    // Bouton permettant de marteler un arbre
    Button boutonMarteler;

    // Bouton permettant d'annuler le martelage d'un arbre et menant à SelectionArbreFragment
    Button buttonCancel;

    TextView tvNum,
            tvEssence,
            tvEtat,
            tvDiametre;

    // Card contenant les caractéristiques d'un arbre
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

        arbreMur = view.findViewById(R.id.arbreMur);
        eclaircie = view.findViewById(R.id.eclaircie);
        sanitaire = view.findViewById(R.id.sanitaire);
        regeneration = view.findViewById(R.id.regeneration);
        exploitation = view.findViewById(R.id.exploitation);

        boutonMarteler = view.findViewById(R.id.boutonMarteler);
        buttonCancel = view.findViewById(R.id.cancel);

        tvNum = view.findViewById(R.id.numero_tree_card);
        tvEssence = view.findViewById(R.id.essence_tree_card);
        tvEtat = view.findViewById(R.id.etat_tree_card);
        tvDiametre = view.findViewById(R.id.diametre_tree_card);


        treeCardNumber = view.findViewById(R.id.arbreLayout);

        // Requete pour récupérer les caractéristiques d'un arbre depuis la base de données locale
        String queryCaractArbre = "SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE "
                + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numeroArbre;

        cur1 = dbHelper.executeQuery(queryCaractArbre);
        cur1.moveToFirst();

        // Affichage des caractéristiques de l'arbre dans les textView correspondants
        tvEssence.setText(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
        tvEtat.setText(etatToString(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ETAT_ARBRE))));
        tvDiametre.setText(String.format("Diamètre : %s cm", cur1.getString(cur1.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE))));
        tvNum.setText(String.format("Arbre n°%s", cur1.getString(cur1.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC))));

        treeCardNumber.setVisibility(View.GONE);

        /*###########################
         *###  Bouton "Annuler" ###
         *###########################
         *
         * Annule la conservation d'un arbre et mène à
         * ArbreSelectionFragment
         */
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



        /*###########################
         *###  Bouton "Marteler" ###
         *###########################
         *
         * Bouton qui permet de marteler un arbre.
         * Ajoute l'arbre à marteler dans la table des
         * arbres martelés et permet de retourner a "SelectionArbreFragment"
         * Un arbre peut être martelés seulement si au moins une raison est cochée.
         */
        boutonMarteler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (possedeRaison() && !numeroArbre.equals("")) {
                    String query = "SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE " + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numeroArbre;
                    Cursor cur = dbHelper.executeQuery(query);
                    cur.moveToFirst();
                    // Insertion de l'arbre à la table des arbres martelés
                    dbHelper.insertArbreMartele(numeroArbre);
                    // Insertion des raisons à la base de données locale
                    insertRaisonFromCheckBoxes();

                    if (rechercheFragmentView.getContext() instanceof ExerciceActivity) {
                        ((ExerciceActivity) rechercheFragmentView.getContext()).reloadArbreMartelesFragment();
                    }

                    // Si l'arbre martelé possède une note écologique > 6, mène au popUp MessageErreurArbreMarteleActivity
                    if (cur.getInt(cur.getColumnIndex(DatabaseHelper.NOTE_ECO_ARBRE)) >= noteEcologiqueHaute) {
                        Intent intent = new Intent(view.getContext(), MessageErreurArbreMarteleActivity.class);
                        startActivity(intent);
                    }


                    dismiss();

                } else if (!possedeRaison()) { // Si pas de raison cochée, affiche un texte dans un Snackbar
                    Snackbar errorsSnack = Snackbar.make(view, "Il faut choisir au moins une raison", Snackbar.LENGTH_LONG);

                    errorsSnack.show();
                }
            }
        });

        return view;
    }

    /**
     * Fonction permettant de convertir l'état d'un arbre en String
     * @param etat : Etat de l'arbre
     * @return String correspondant à l'état de l'arbre
     */
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
        if (arbreMur.isChecked() || eclaircie.isChecked() || sanitaire.isChecked() || regeneration.isChecked() || exploitation.isChecked()) {
            return true;
        }
        return false;
    }

    // Ajouter la ou les raisons à la base de données locale
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
