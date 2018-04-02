package martelapp.test.Activity;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import martelapp.test.Adapter.ArbreMartelesAdapter;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

public class ArbresMartelesActivity extends AppCompatActivity {

    ListView listeArbresMarteles;

    DatabaseHelper dbHelper;
    Cursor cur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arbres_marteles);

        dbHelper = new DatabaseHelper(getApplicationContext());


        listeArbresMarteles = findViewById(R.id.liste_arbres_marteles);
        listeArbresMarteles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                TextView text = view.findViewById(R.id.numero_arbre_martele);
                String numero = text.getText().toString();
                cur = dbHelper.executeQuery("Select *"
                                            + " FROM " + DatabaseHelper.RAISON_TABLE
                                            + " WHERE " + DatabaseHelper.NUMERO_ARBRE_MARTELE_RAISON + " = " + numero );
                cur.moveToFirst();
                TextView textRaison = findViewById(R.id.text_raison);
                textRaison.setText("Raison : " + cur.getString(cur.getColumnIndex(DatabaseHelper.RAISON)));
                while(cur.moveToNext()){
                    textRaison.setText(textRaison.getText() + " | " + cur.getString(cur.getColumnIndex(DatabaseHelper.RAISON)));
                }
            }
        });

        cur = dbHelper.executeQuery("SELECT *"
                                    + " FROM " + dbHelper.ARBRES_PARCELLE_TABLE + " ap," + dbHelper.ARBRES_MARTELES_TABLE + " am"
                                    + " WHERE ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                                    + " ORDER BY ap." + dbHelper.NUMERO_ARBRE_PARC + " ASC");
        cur.moveToFirst();


        ArbreMartelesAdapter arbreMartelesAdapter = new ArbreMartelesAdapter(ArbresMartelesActivity.this, cur);
        listeArbresMarteles.setAdapter(arbreMartelesAdapter);
    }
}
