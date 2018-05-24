package martelapp.test.Fragment.Exercice;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import martelapp.test.Activity.ExerciceActivity;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;


/**
 * ChoixConserverFragment est le fragment dans lequel l'utilisateur peut choisir de conserver un arbre
 * pour des raisons écologiques.
 */
public class ChoixConserverFragment extends DialogFragment {

    // Bouton permettant de conserver un arbre
    Button boutonConserver;

    // Bouton permettant d'annuler la conservation d'un arbre et menant à SelectionArbreFragment
    Button boutonCancel;

    TextView textViewConserver,
            tvNum,
            tvEssence,
            tvEtat,
            tvDiametre;

    // Card contenant les caractéristiques d'un arbre
    LinearLayout treeCardNumber;

    DatabaseHelper dbHelper;
    Cursor cur1;

    private String numeroArbre;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_page_choixconserver, null);
        final View rechercheFragmentView = inflater.inflate(R.layout.view_page_selection, null);

        dbHelper = new DatabaseHelper(view.getContext());

        boutonConserver = view.findViewById(R.id.boutonConserver);
        boutonCancel = view.findViewById(R.id.cancel);

        textViewConserver = view.findViewById(R.id.textViewConserver);

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
         *###  Bouton "Conserver" ###
         *###########################
         *
         * Bouton qui permet de conserver un arbre.
         * Ajoute l'arbre à conserver dans la table des
         * arbres conservés et permet de retourner a "SelectionArbreFragment"
         */
        boutonConserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // insertion de l'arbre à la table des arbres conservés
                dbHelper.insertArbreConserve(numeroArbre);
                // insertion de la raison BIODIVERSITE
                dbHelper.insertRaison(numeroArbre, DatabaseHelper.BIODIVERSITE);

                if(rechercheFragmentView.getContext() instanceof ExerciceActivity){
                    ((ExerciceActivity) rechercheFragmentView.getContext()).reloadArbreMartelesFragment();
                }
                dismiss();
            }
        });

        /*###########################
         *###  Bouton "Annuler" ###
         *###########################
         *
         * Annule la conservation d'un arbre et mène à
         * ArbreSelectionFragment
         */
        boutonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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

    public void setNumeroArbre(String numeroArbre) {
        this.numeroArbre = numeroArbre;
    }


}
