package martelapp.test.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

public class ChoixConserverFragment extends DialogFragment {

    Button boutonConserver,
            boutonCancel;

    TextView textViewConserver,
            textViewNum,
            textViewEssence,
            textViewEtat,
            textViewDiametre;

    DatabaseHelper dbHelper;
    Cursor cur1;

    private String numeroArbre;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_page_choixconserver, null);
        final View rechercheFragmentView = inflater.inflate(R.layout.view_page_recherche, null);

        dbHelper = new DatabaseHelper(view.getContext());

        boutonConserver = view.findViewById(R.id.boutonConserver);
        boutonCancel = view.findViewById(R.id.cancel);

        textViewConserver = view.findViewById(R.id.textViewConserver);
        textViewNum = view.findViewById(R.id.textViewNum);
        textViewEssence = view.findViewById(R.id.textViewEssence);
        textViewEtat = view.findViewById(R.id.textViewEtat);
        textViewDiametre = view.findViewById(R.id.textViewDiametre);


        String queryCaractArbre = "SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE "
                + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numeroArbre;

        cur1 = dbHelper.executeQuery(queryCaractArbre);
        cur1.moveToFirst();


        textViewNum.setText(String.format("Arbre n°%s", cur1.getString(cur1.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC))));
        textViewEssence.setText(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
        textViewEtat.setText(etatToString(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ETAT_ARBRE))));
        textViewDiametre.setText(String.format("Diametre : %s cm", cur1.getString(cur1.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE))));

        boutonConserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.insertArbreConserve(numeroArbre);
                dbHelper.insertRaison(numeroArbre, DatabaseHelper.BIODIVERSITE);
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
