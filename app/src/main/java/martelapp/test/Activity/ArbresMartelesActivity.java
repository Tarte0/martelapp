package martelapp.test.Activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import martelapp.test.Adapter.ArbreMartelesAdapter;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

public class ArbresMartelesActivity extends AppCompatActivity {

    ListView listeArbresMarteles;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arbres_marteles);

        dbHelper = new DatabaseHelper(getApplicationContext());

        listeArbresMarteles = (ListView) findViewById(R.id.liste_arbres_marteles);

        Cursor cur = dbHelper.executeQuery("SELECT *"
                                        + " FROM " + dbHelper.ARBRES_PARCELLE_TABLE + " ap LEFT JOIN " + dbHelper.ARBRES_MARTELES_TABLE + " am"
                                        + " WHERE ap." + dbHelper.NUMERO_ARBRE_PARC + " = am." + dbHelper.NUMERO_ARBRE_MART
                                        + " ORDER BY " + dbHelper.NUMERO_ARBRE_PARC + " ASC");
        cur.moveToFirst();

        ArbreMartelesAdapter arbreMartelesAdapter = new ArbreMartelesAdapter(ArbresMartelesActivity.this, cur);
        listeArbresMarteles.setAdapter(arbreMartelesAdapter);
    }
}
