package martelapp.test.Fragment.Exercice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import martelapp.test.Activity.ExerciceActivity;
import martelapp.test.Adapter.ArbresConservesAdapter;
import martelapp.test.Adapter.ArbresMartelesAdapter;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;


/**
 * Arbres sélectionnés est un Fragment contenant la Liste
 * des arbres martelés ou conservés par l'utilisateur.
 */
public class ArbresSelectionnesFragment extends Fragment {
    ListView listeArbresMarteles;

    ViewPager viewPager;

    View mainView;
    DatabaseHelper dbHelper;
    Cursor cur, cur1, cur2;
    LinearLayout treeCardNumber;

    Button finishButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_arbresmarteles, null);

            finishButton = mainView.findViewById(R.id.finish);
            treeCardNumber = mainView.findViewById(R.id.arbreLayout);
            listeArbresMarteles = mainView.findViewById(R.id.liste_arbres_marteles);


            /*
             * Cliquer sur un arbre de la liste affiche dans la Card les
             * raisons de sa conservation ou de son martelage.
             */
            listeArbresMarteles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    TextView text = view.findViewById(R.id.numero_arbre_traite);

                    dbHelper = new DatabaseHelper(mainView.getContext());

                    String numero = text.getText().toString();
                    // On récupère les raisons de la conservation ou du martelage dans la base de données
                    cur = dbHelper.executeQuery("Select *"
                            + " FROM " + DatabaseHelper.RAISON_TABLE
                            + " WHERE " + DatabaseHelper.NUMERO_ARBRE_TRAITE_RAISON + " = " + numero);
                    cur.moveToFirst();
                    TextView textRaison = mainView.findViewById(R.id.raison_martele_card);

                    String raison = cur.getString(cur.getColumnIndex(DatabaseHelper.RAISON));

                    // Affichage des raisons du martelage ou de la conservation dans la Card
                    if (raison.equals(DatabaseHelper.BIODIVERSITE)) {
                        textRaison.setText(R.string.raison_conserve);
                    } else {
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

                    treeCardNumber.setVisibility(View.VISIBLE);

                    cur.close();
                    dbHelper.close();
                }
            });



            /*###################################
             *###  Bouton "Fin de l'exercice" ###
             *###################################
             *
             * Met fin à l'exercice de Martelage et mène
             * à AnalyseActivity.
             * Un popUp est affiché pour que l'utilisateur
             * confirme son choix.
             * On ne peut mettre fin à l'exercice que si au moins
             * un arbre a été martelé.
             */
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    dbHelper = new DatabaseHelper(mainView.getContext());

                    cur = dbHelper.getAllDataFromTable(DatabaseHelper.ARBRES_MARTELES_TABLE);

                    // Affichage du popUp permettant de confirmer la fin de l'exercice
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertDialogCustom));
                    builder.setTitle("Êtes-vous sûr de vouloir terminer l'exercice ?");
                    builder.setMessage("Vous ne pourrez plus revenir en arrière");

                    builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            cur1.close();
                            cur2.close();
                            Intent intent = new Intent(view.getContext(), AnalyseActivity.class);
                            startActivity(intent);
                            ((ExerciceActivity) getActivity()).finish();
                        }
                    });

                    builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    if (cur1.moveToFirst()) {
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        Snackbar.make(view, "Mais vous n'avez rien martelé !", Snackbar.LENGTH_LONG).show();
                    }

                    dbHelper.close();
                }
            });

            reload();
        }

        treeCardNumber.setVisibility(View.INVISIBLE);
        return mainView;
    }


    private String raisonToString(String raison){
        if(raison.equals(DatabaseHelper.ECLAIRCIE)) {
            return "Eclaircie au profit d'un arbre d'avenir";
        }
        return raison;

    }


    public void setVp(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    /**
     * Méthode réactualisant la listView des arbres martelés et conservés
     */
    public void reload() {
        dbHelper = new DatabaseHelper(mainView.getContext());

        cur1 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " as INTEGER)");

        cur1.moveToFirst();


        cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " as INTEGER)");

        cur2.moveToFirst();
        ArbresMartelesAdapter arbresMartelesAdapter = new ArbresMartelesAdapter(mainView.getContext(), cur1, false);
        ArbresConservesAdapter arbresConservesAdapter = new ArbresConservesAdapter(mainView.getContext(), cur2, false);

        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(arbresMartelesAdapter);
        mergeAdapter.addAdapter(arbresConservesAdapter);
        listeArbresMarteles.setAdapter(mergeAdapter);

        dbHelper.close();
    }
}
