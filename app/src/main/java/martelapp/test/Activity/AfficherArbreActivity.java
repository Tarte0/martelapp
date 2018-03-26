package martelapp.test.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;


import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

public class AfficherArbreActivity extends AppCompatActivity {

    TextView mTextView;
    Button mButtonMarteler;
    ToggleButton mToggleButton;
    RadioGroup mRadioGroup;

    int noteEcologiqueMax = 6;

    int noteEcologique;

    String numero,
            essence,
            etat;

    DatabaseHelper dbHelper;
    String query;
    Cursor cur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_arbre);

        dbHelper = new DatabaseHelper(getApplicationContext());

        // Bouton retour sur Barre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextView = (TextView) findViewById(R.id.textView);

        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        mRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        mButtonMarteler = (Button) findViewById(R.id.buttonMarteler);

        mRadioGroup.setVisibility(View.INVISIBLE);
        mButtonMarteler.setVisibility(View.INVISIBLE);


       Bundle extras = getIntent().getExtras();

        if (extras != null) {

            numero = extras.getString("numero");

            query = "SELECT * FROM " + dbHelper.ARBRES_PARCELLE_TABLE + " WHERE " + dbHelper.NUMERO_ARBRE_PARC + " = " + numero;
            cur = dbHelper.executeQuery(query);
            cur.moveToFirst();

            essence = cur.getString(cur.getColumnIndex(dbHelper.ESSENCE_ARBRE));
            etat = cur.getString(cur.getColumnIndex(dbHelper.ETAT_ARBRE));
            noteEcologique = cur.getInt(cur.getColumnIndex(dbHelper.NOTE_ECO_ARBRE));


            // Affiche le numéro de l'arbre dans la barre supérieure
            getSupportActionBar().setTitle("Arbre n°" + numero);

        }


        mTextView.setText("Numéro : " + numero + " ,\nEssence : " + essence + " ,\nEtat : " + etat + " ,\nNote Ecologique : " + noteEcologique);

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mRadioGroup.setVisibility(View.VISIBLE);
                    mButtonMarteler.setVisibility(View.VISIBLE);
                } else {
                    mRadioGroup.setVisibility(View.INVISIBLE);
                    mButtonMarteler.setVisibility(View.INVISIBLE);
                }
            }
        });


        mButtonMarteler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.insertArbreMarteles(numero);
                //insertRaisonFromCheckBoxes();

                if (noteEcologique > noteEcologiqueMax) {
                    Intent intent = new Intent(getApplicationContext(), MessageErreurArbreMarteleActivity.class);
                    startActivity(intent);
                }
                AfficherArbreActivity.this.finish();
            }
        });

    }

    public void insertRaisonFromCheckBoxes(){
        /*if(checkboxRaison1.isChecked()){
            dbHelper.insertRaison(numero, "raison1");
        }*/
    }


    /*public String onRadioButtonClicked(RadioGroup rb) {
        // Is the button now checked?
        int buttonChecked = rb.getCheckedRadioButtonId();

        // Check which radio button was clicked
        switch (buttonChecked) {
            case R.id.arbreMur:
                return "M";

            case R.id.Eclaircie:
                return "E";

            case R.id.sanitaire:
                return "S";

            case R.id.Régénération:
                return "R";

            case R.id.Exploitation:
                return "EX";

            case R.id.Stabilité:
                return "ST";

        }
        return "";
    }*/
}
