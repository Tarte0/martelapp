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

public class ChoixConserverFragment extends DialogFragment {

    Button boutonConserver,
            boutonCancel;

    TextView textViewConserver,
            tvNum,
            tvEssence,
            tvEtat,
            tvDiametre;

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

        tvNum = (TextView) view.findViewById(R.id.numero_tree_card);
        tvEssence = (TextView) view.findViewById(R.id.essence_tree_card);
        tvEtat = (TextView) view.findViewById(R.id.etat_tree_card);
        tvDiametre = (TextView) view.findViewById(R.id.diametre_tree_card);

        treeCardNumber = view.findViewById(R.id.arbreLayout);

        String queryCaractArbre = "SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE "
                + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numeroArbre;

        cur1 = dbHelper.executeQuery(queryCaractArbre);
        cur1.moveToFirst();

        tvEssence.setText(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
        tvEtat.setText(etatToString(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ETAT_ARBRE))));
        tvDiametre.setText(String.format("Diamètre : %s cm", cur1.getString(cur1.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE))));
        tvNum.setText(String.format("Arbre n°%s", cur1.getString(cur1.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC))));

        treeCardNumber.setVisibility(View.GONE);

        boutonConserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.insertArbreConserve(numeroArbre);
                dbHelper.insertRaison(numeroArbre, DatabaseHelper.BIODIVERSITE);

                if(rechercheFragmentView.getContext() instanceof ExerciceActivity){
                    ((ExerciceActivity) rechercheFragmentView.getContext()).reloadArbreMartelesFragment();
                }

                dismiss();
            }
        });

        boutonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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

    public void setNumeroArbre(String numeroArbre) {
        this.numeroArbre = numeroArbre;
    }


}
