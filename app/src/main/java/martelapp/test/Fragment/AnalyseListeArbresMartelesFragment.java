package martelapp.test.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import martelapp.test.Adapter.ArbresMartelesAdapter;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

public class AnalyseListeArbresMartelesFragment extends Fragment {

    ListView listeArbresMartelesAnalyse;

    Cursor cur;
    DatabaseHelper dbHelper;
    LinearLayout treeCardNumber;

    int nbArbresMarteles = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mainView = inflater.inflate(R.layout.view_page_arbres_traites_analyse, null);

        listeArbresMartelesAnalyse = mainView.findViewById(R.id.liste_arbres_traites_analyse);
        treeCardNumber = (LinearLayout) mainView.findViewById(R.id.arbreLayout);


        dbHelper = new DatabaseHelper(mainView.getContext());


        listeArbresMartelesAnalyse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                TextView text = view.findViewById(R.id.numero_arbre_traite);

                String numero = text.getText().toString();
                cur = dbHelper.executeQuery("Select *"
                        + " FROM " + DatabaseHelper.RAISON_TABLE
                        + " WHERE " + DatabaseHelper.NUMERO_ARBRE_TRAITE_RAISON + " = " + numero);
                cur.moveToFirst();
                TextView textRaison = mainView.findViewById(R.id.raison_martele_card);

                String raison = cur.getString(cur.getColumnIndex(DatabaseHelper.RAISON));

                textRaison.setText(String.format("Raisons du Martelage : \n\n- %s\n", raisonToString(raison)));
                while (cur.moveToNext()) {
                    textRaison.setText(String.format("%s\n- %s\n", textRaison.getText(),
                            raisonToString(cur.getString(cur.getColumnIndex(DatabaseHelper.RAISON)))));
                }

                cur = dbHelper.executeQuery("SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE + " WHERE " + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numero);
                cur.moveToFirst();
                TextView numCard = mainView.findViewById(R.id.numero_martele_card);
                TextView detailCard = mainView.findViewById(R.id.details_martele_card);
                numCard.setText(cur.getString(cur.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC)));
                detailCard.setText(String.format("%s",
                        cur.getString(cur.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE))));
                treeCardNumber.setVisibility(View.VISIBLE);
            }
        });


        cur = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");
        cur.moveToFirst();
        nbArbresMarteles = cur.getCount();

        ArbresMartelesAdapter arbresMartelesAdapter = new ArbresMartelesAdapter(mainView.getContext(), cur, true);

        listeArbresMartelesAnalyse.setAdapter(arbresMartelesAdapter);

        TextView textComplementArbresMarteles = mainView.findViewById(R.id.text_complement_arbres);

        textComplementArbresMarteles.setText("Nombre d'arbres martelés : " + Integer.toString(nbArbresMarteles));

        treeCardNumber.setVisibility(View.INVISIBLE);

        return mainView;
    }


    private String raisonToString(String raison){
        if(raison.equals(DatabaseHelper.ECLAIRCIE)) {
            return "Eclaircie au profit d'un arbre d'avenir";
        }
        return raison;

    }
}
