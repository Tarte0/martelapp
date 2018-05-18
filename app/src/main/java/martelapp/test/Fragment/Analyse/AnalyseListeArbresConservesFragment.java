package martelapp.test.Fragment.Analyse;

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

import com.commonsware.cwac.merge.MergeAdapter;

import martelapp.test.Adapter.ArbresConservesAdapter;
import martelapp.test.Adapter.ArbresMartelesAdapter;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Fragment.Exercice.ChoixMartelageFragment;
import martelapp.test.R;



public class AnalyseListeArbresConservesFragment extends Fragment {

    ListView listeArbresConservesAnalyse;

    Cursor cur1, cur2;
    DatabaseHelper dbHelper;
    LinearLayout treeCardNumber;

    int nbArbresConserves = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mainView = inflater.inflate(R.layout.view_page_arbres_traites_analyse, null);

        listeArbresConservesAnalyse = mainView.findViewById(R.id.liste_arbres_traites_analyse);
        treeCardNumber = (LinearLayout) mainView.findViewById(R.id.arbreLayout);


        dbHelper = new DatabaseHelper(mainView.getContext());


        listeArbresConservesAnalyse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                TextView text = view.findViewById(R.id.numero_arbre_traite);

                String numero = text.getText().toString();
                cur1 = dbHelper.executeQuery("Select *"
                        + " FROM " + DatabaseHelper.RAISON_TABLE
                        + " WHERE " + DatabaseHelper.NUMERO_ARBRE_TRAITE_RAISON + " = " + numero);
                cur1.moveToFirst();
                TextView textRaison = mainView.findViewById(R.id.raison_martele_card);

                textRaison.setText("Arbre conservé pour la Biodiversité");

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


        cur1 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS +
                        " AND ap." + DatabaseHelper.NOTE_ECO_ARBRE + " >= " + ChoixMartelageFragment.noteEcologiqueHaute +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");
        cur1.moveToFirst();

        cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_CONSERVES_TABLE + " ac",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = ac." + DatabaseHelper.NUMERO_ARBRE_CONS +
                        " AND ap." + DatabaseHelper.NOTE_ECO_ARBRE + " < " + ChoixMartelageFragment.noteEcologiqueHaute +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");
        cur2.moveToFirst();

        nbArbresConserves = cur1.getCount() + cur2.getCount();

        ArbresConservesAdapter arbresConservesAdapterNoteEcoSup = new ArbresConservesAdapter(mainView.getContext(), cur1, true);
        ArbresConservesAdapter arbresConservesAdapter = new ArbresConservesAdapter(mainView.getContext(), cur2, true);


        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(arbresConservesAdapterNoteEcoSup);
        mergeAdapter.addAdapter(arbresConservesAdapter);
        listeArbresConservesAnalyse.setAdapter(mergeAdapter);

        TextView textComplementArbresMarteles = mainView.findViewById(R.id.text_complement_arbres);
        TextView textCouleurArbre = mainView.findViewById(R.id.text_couleur_arbre);

        textComplementArbresMarteles.setText("Nombre d'arbres conservés : " + Integer.toString(nbArbresConserves));

        textCouleurArbre.setText("■ Note écologique ≥ " + Integer.toString(ChoixMartelageFragment.noteEcologiqueHaute));
        textCouleurArbre.setTextColor(mainView.getResources().getColor(R.color.colorGreen));

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