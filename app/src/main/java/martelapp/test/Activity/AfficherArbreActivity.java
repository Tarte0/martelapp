package martelapp.test.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    CheckBox arbreMur, eclaircie, sanitaire, regeneration, exploitation, stabilité;

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

        mTextView = (TextView) findViewById(R.id.textView);

        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        mButtonMarteler = (Button) findViewById(R.id.buttonMarteler);

        arbreMur = (CheckBox) findViewById(R.id.arbreMur);
        eclaircie = (CheckBox) findViewById(R.id.Eclaircie);
        sanitaire = (CheckBox) findViewById(R.id.sanitaire);
        regeneration = (CheckBox) findViewById(R.id.Régénération);
        exploitation = (CheckBox) findViewById(R.id.Exploitation);
        stabilité = (CheckBox) findViewById(R.id.Stabilité);


        arbreMur.setVisibility(View.INVISIBLE);
        eclaircie.setVisibility(View.INVISIBLE);
        sanitaire.setVisibility(View.INVISIBLE);
        regeneration.setVisibility(View.INVISIBLE);
        exploitation.setVisibility(View.INVISIBLE);
        stabilité.setVisibility(View.INVISIBLE);

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

        }


        mTextView.setText("Numéro : " + numero + " ,\nEssence : " + essence + " ,\nEtat : " + etat + " ,\nNote Ecologique : " + noteEcologique);

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    arbreMur.setVisibility(View.VISIBLE);
                    eclaircie.setVisibility(View.VISIBLE);
                    sanitaire.setVisibility(View.VISIBLE);
                    regeneration.setVisibility(View.VISIBLE);
                    exploitation.setVisibility(View.VISIBLE);
                    stabilité.setVisibility(View.VISIBLE);
                    mButtonMarteler.setVisibility(View.VISIBLE);
                } else {
                    arbreMur.setVisibility(View.INVISIBLE);
                    eclaircie.setVisibility(View.INVISIBLE);
                    sanitaire.setVisibility(View.INVISIBLE);
                    regeneration.setVisibility(View.INVISIBLE);
                    exploitation.setVisibility(View.INVISIBLE);
                    stabilité.setVisibility(View.INVISIBLE);
                    mButtonMarteler.setVisibility(View.INVISIBLE);
                }
            }
        });


        mButtonMarteler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (possedeRaison()) {
                    dbHelper.insertArbreMarteles(numero);
                    insertRaisonFromCheckBoxes();

                    if (noteEcologique > noteEcologiqueMax) {
                        Intent intent = new Intent(getApplicationContext(), MessageErreurArbreMarteleActivity.class);
                        startActivity(intent);
                    }
                    AfficherArbreActivity.this.finish();

                }
            }
        });

    }


    // Savoir si une raison est bien cochée au minimum
    public boolean possedeRaison(){
        if (arbreMur.isChecked() | eclaircie.isChecked() | sanitaire.isChecked() | regeneration.isChecked() | exploitation.isChecked() | stabilité.isChecked()) {
           return true;
        }
        return false;
    }


    // Ajouter raison a BDD
    public void insertRaisonFromCheckBoxes() {
        if (arbreMur.isChecked()) {
            dbHelper.insertRaison(numero, "Arbre Mûr");
        }
        if (eclaircie.isChecked()) {
            dbHelper.insertRaison(numero, "Eclaircie au profit d'un arbre d'avenir");
        }
        if (sanitaire.isChecked()) {
            dbHelper.insertRaison(numero, "Sanitaire");
        }
        if (regeneration.isChecked()) {
            dbHelper.insertRaison(numero, "Pour la Régénération");
        }
        if (exploitation.isChecked()) {
            dbHelper.insertRaison(numero, "Pour l'Exploitation");
        }
        if (stabilité.isChecked()) {
            dbHelper.insertRaison(numero, "Pour la stabilité");
        }
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
