package martelapp.test.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import martelapp.test.Activity.AnalyseActivity;
import martelapp.test.Activity.NomEquipeActivity;
import martelapp.test.Adapter.ArbreMartelesAdapter;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by cimin on 04/04/2018.
 */

public class ArbresMartelesFragment extends Fragment {
    ListView listeArbresMarteles;

    DatabaseHelper dbHelper;
    Cursor cur;

    Button finishButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mainView = inflater.inflate(R.layout.view_page_arbresmarteles, null);
        dbHelper = new DatabaseHelper(mainView.getContext());
        finishButton = (Button) mainView.findViewById(R.id.finish);

        listeArbresMarteles = mainView.findViewById(R.id.liste_arbres_marteles);
        listeArbresMarteles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                TextView text = view.findViewById(R.id.numero_arbre_martele);
                String numero = text.getText().toString();
                cur = dbHelper.executeQuery("Select *"
                        + " FROM " + DatabaseHelper.RAISON_TABLE
                        + " WHERE " + DatabaseHelper.NUMERO_ARBRE_MARTELE_RAISON + " = " + numero);
                cur.moveToFirst();
                TextView textRaison = mainView.findViewById(R.id.raison_martele_card);

                String raison = cur.getString(cur.getColumnIndex(DatabaseHelper.RAISON));

                if(raison.equals(DatabaseHelper.BIODIVERSITE)){
                    textRaison.setText("Arbre conservé pour la Biodiversité");
                }
                else {
                    textRaison.setText(String.format("Raisons du Martelage : \n\n- %s\n", raisonToString(raison)));
                    while (cur.moveToNext()) {
                        textRaison.setText(String.format("%s\n- %s\n", textRaison.getText(),
                                raisonToString(cur.getString(cur.getColumnIndex(DatabaseHelper.RAISON)))));
                    }
                }
                cur = dbHelper.executeQuery("SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE " + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numero);
                cur.moveToFirst();
                TextView numCard = mainView.findViewById(R.id.numero_martele_card);
                TextView detailCard = mainView.findViewById(R.id.details_martele_card);
                numCard.setText(cur.getString(cur.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC)));
                detailCard.setText(String.format("%s",
                        cur.getString(cur.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE))));
            }
        });

        cur = dbHelper.executeQuery("SELECT *"
                + " FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am"
                + " WHERE ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART
                + " UNION"
                + " SELECT *"
                + " FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac"
                + " WHERE ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS);
        cur.moveToFirst();


        ArbreMartelesAdapter arbreMartelesAdapter = new ArbreMartelesAdapter(mainView.getContext(), cur);
        listeArbresMarteles.setAdapter(arbreMartelesAdapter);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertDialogCustom));
                builder.setTitle("Êtes vous sur de vouloir terminer l'exercice ?");
                builder.setMessage("Vous ne pourrez plus revenir en arrière");

                builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(view.getContext(), AnalyseActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return mainView;
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

    private String raisonToString(String raison){
        if(raison.equals(DatabaseHelper.ECLAIRCIE)) {
            return "Eclaircie au profit d'un arbre d'avenir";
        }
        return raison;

    }
}
