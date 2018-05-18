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

import martelapp.test.Adapter.ArbresMartelesAdapter;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Fragment.Exercice.ChoixMartelageFragment;
import martelapp.test.R;

public class AnalyseListeArbresMartelesFragment extends Fragment {

    ListView listeArbresMartelesAnalyse;

    Cursor cur1, cur2;
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
                cur1 = dbHelper.executeQuery("Select *"
                        + " FROM " + DatabaseHelper.RAISON_TABLE
                        + " WHERE " + DatabaseHelper.NUMERO_ARBRE_TRAITE_RAISON + " = " + numero);
                cur1.moveToFirst();
                TextView textRaison = mainView.findViewById(R.id.raison_martele_card);
                String raison = cur1.getString(cur1.getColumnIndex(DatabaseHelper.RAISON));

                textRaison.setText(String.format("Raisons du Martelage : \n\n- %s\n", raisonToString(raison)));
                while (cur1.moveToNext()) {
                    textRaison.setText(String.format("%s\n- %s\n", textRaison.getText(),
                            raisonToString(cur1.getString(cur1.getColumnIndex(DatabaseHelper.RAISON)))));
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
                        " AND ap." + DatabaseHelper.NOTE_ECO_ARBRE + " >= " + ChoixMartelageFragment.noteEcologiqueHaute +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");
        cur1.moveToFirst();

        cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_PARCELLE_TABLE + " ap," + DatabaseHelper.ARBRES_MARTELES_TABLE + " am",
                "ap." + DatabaseHelper.NUMERO_ARBRE_PARC + " = am." + DatabaseHelper.NUMERO_ARBRE_MART +
                        " AND ap." + DatabaseHelper.NOTE_ECO_ARBRE + " < " + ChoixMartelageFragment.noteEcologiqueHaute +
                        " ORDER BY CAST(ap." + DatabaseHelper.NUMERO_ARBRE_PARC +" as INTEGER)");
        cur2.moveToFirst();

        nbArbresMarteles = cur1.getCount() + cur2.getCount();

        ArbresMartelesAdapter arbresMartelesAdapterNoteEcoSup = new ArbresMartelesAdapter(mainView.getContext(), cur1, true);
        ArbresMartelesAdapter arbresMartelesAdapter = new ArbresMartelesAdapter(mainView.getContext(), cur2, true);

        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(arbresMartelesAdapterNoteEcoSup);
        mergeAdapter.addAdapter(arbresMartelesAdapter);
        listeArbresMartelesAnalyse.setAdapter(mergeAdapter);

        TextView textComplementArbresMarteles = mainView.findViewById(R.id.text_complement_arbres);
        TextView textCouleurArbre = mainView.findViewById(R.id.text_couleur_arbre);

        textComplementArbresMarteles.setText("Nombre d'arbres martelés : " + Integer.toString(nbArbresMarteles));

        textCouleurArbre.setText("■ Note écologique ≥ " + Integer.toString(ChoixMartelageFragment.noteEcologiqueHaute));
        textCouleurArbre.setTextColor(mainView.getResources().getColor(R.color.colorRed));

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
