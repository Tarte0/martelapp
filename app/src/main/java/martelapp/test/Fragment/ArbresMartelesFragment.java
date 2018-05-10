package martelapp.test.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;

import martelapp.test.Activity.AnalyseActivity;
import martelapp.test.Adapter.ArbresConservesAdapter;
import martelapp.test.Adapter.ArbresMartelesAdapter;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by cimin on 04/04/2018.
 */

public class ArbresMartelesFragment extends Fragment {
    ListView listeArbresMarteles;

    DatabaseHelper dbHelper;
    Cursor cur1, cur2;
    LinearLayout treeCardNumber;

    Button finishButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mainView = inflater.inflate(R.layout.view_page_arbresmarteles, null);
        dbHelper = new DatabaseHelper(mainView.getContext());
        finishButton = (Button) mainView.findViewById(R.id.finish);

        treeCardNumber = (LinearLayout) mainView.findViewById(R.id.arbreLayout);
        listeArbresMarteles = mainView.findViewById(R.id.liste_arbres_marteles);

        listeArbresMarteles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                TextView text = view.findViewById(R.id.numero_arbre_martele);
                if(text == null) text = view.findViewById(R.id.numero_arbre_conserve);

                String numero = text.getText().toString();
                cur1 = dbHelper.executeQuery("Select *"
                        + " FROM " + DatabaseHelper.RAISON_TABLE
                        + " WHERE " + DatabaseHelper.NUMERO_ARBRE_TRAITE_RAISON + " = " + numero);
                cur1.moveToFirst();
                TextView textRaison = mainView.findViewById(R.id.raison_martele_card);

                String raison = cur1.getString(cur1.getColumnIndex(DatabaseHelper.RAISON));

                LinearLayout arbreLayout = mainView.findViewById(R.id.arbreLayout);
                if (raison.equals(DatabaseHelper.BIODIVERSITE)) {
                    textRaison.setText("Arbre conservé pour la Biodiversité");
                    arbreLayout.setBackgroundColor(getResources().getColor(R.color.colorBarOrange));
                } else {
                    textRaison.setText(String.format("Raisons du Martelage : \n\n- %s\n", raisonToString(raison)));
                    while (cur1.moveToNext()) {
                        textRaison.setText(String.format("%s\n- %s\n", textRaison.getText(),
                                raisonToString(cur1.getString(cur1.getColumnIndex(DatabaseHelper.RAISON)))));
                    }
                    arbreLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                cur1 = dbHelper.executeQuery("SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE " + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numero);
                cur1.moveToFirst();
                TextView numCard = mainView.findViewById(R.id.numero_martele_card);
                TextView detailCard = mainView.findViewById(R.id.details_martele_card);
                numCard.setText(cur1.getString(cur1.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC)));
                detailCard.setText(String.format("%s",
                        cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE))));
                treeCardNumber.setVisibility(View.VISIBLE);
            }
        });

        cur1 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");

        cur1.moveToFirst();


        cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac",
                                                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS +
                                                          " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");

        cur2.moveToFirst();
        ArbresMartelesAdapter arbresMartelesAdapter = new ArbresMartelesAdapter(mainView.getContext(), cur1);
        ArbresConservesAdapter arbresConservesAdapter = new ArbresConservesAdapter(mainView.getContext(), cur2);

        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(arbresMartelesAdapter);
        mergeAdapter.addAdapter(arbresConservesAdapter);
        listeArbresMarteles.setAdapter(mergeAdapter);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                cur1 = dbHelper.getAllDataFromTable(DatabaseHelper.ARBRES_MARTELES_TABLE);

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertDialogCustom));
                builder.setTitle("Êtes vous sur de vouloir terminer l'exercice ?");
                builder.setMessage("Vous ne pourrez plus revenir en arrière");

                builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.close();
                        cur1.close();
                        Intent intent = new Intent(view.getContext(), AnalyseActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                if(cur1.moveToFirst()) {
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else{
                    Snackbar.make(view, "Mais vous n'avez rien martelé !", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        treeCardNumber.setVisibility(View.INVISIBLE);
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
