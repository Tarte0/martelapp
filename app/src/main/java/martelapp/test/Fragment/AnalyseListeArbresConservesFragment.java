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

import martelapp.test.Adapter.ArbresConservesAdapter;
import martelapp.test.Adapter.ArbresMartelesAdapter;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

public class AnalyseListeArbresConservesFragment extends Fragment {

    ListView listeArbresMartelesAnalyse;

    Cursor cur;
    DatabaseHelper dbHelper;
    LinearLayout treeCardNumber;

    int nbArbresConserves = 0;

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

                textRaison.setText("Arbre conservé pour la Biodiversité");

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


        cur = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");
        cur.moveToFirst();
        nbArbresConserves = cur.getCount();

        ArbresConservesAdapter arbresConservesAdapter = new ArbresConservesAdapter(mainView.getContext(), cur, true);

        listeArbresMartelesAnalyse.setAdapter(arbresConservesAdapter);

        TextView textComplementArbresMarteles = mainView.findViewById(R.id.text_complement_arbres);

        textComplementArbresMarteles.setText("Nombre d'arbres conservés : " + Integer.toString(nbArbresConserves));

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