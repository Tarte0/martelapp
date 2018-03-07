package martelapp.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Map;

public class AfficherArbre extends AppCompatActivity {

    TextView mTextView;
    Button mButtonMarteler;
    ToggleButton mToggleButton;
    RadioGroup mRadioGroup;

    int noteEcologiqueMax = 6;

    int noteEcologique;

    String numero,
            essence,
            etat,
            getNoteEcologique,
            raisonMartelage;

    Map<String, String> arbresMarteles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_arbre);

        // Bouton retour sur Barre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mTextView = (TextView) findViewById(R.id.textView);

        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        mRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        mButtonMarteler = (Button) findViewById(R.id.buttonMarteler);

        mRadioGroup.setVisibility(View.INVISIBLE);
        mButtonMarteler.setVisibility(View.INVISIBLE);

        arbresMarteles = ((ArbresMarteles) getApplicationContext()).map;

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            numero = extras.getString("numero");
            essence = extras.getString("essence");
            etat = extras.getString("etat");
            getNoteEcologique = extras.getString("noteEcologique");

            noteEcologique = Integer.parseInt(getNoteEcologique);

            // Affiche le numéro de l'arbre dans la barre supérieure
            getSupportActionBar().setTitle("Arbre n°" + numero);

        }
        /*
        numero = (String) getIntent().getExtras().getString("numero");

        essence = (String) getIntent().getExtras().getString("essence");

        etat = (String) getIntent().getExtras().getString("etat");
    */


        mTextView.setText("Numéro : " + numero + " ,\nEssence : " + essence + " ,\nEtat : " + etat + " ,\nNote Ecologique : " + getNoteEcologique);

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

                raisonMartelage = onRadioButtonClicked(mRadioGroup);

                // Ajouter arbre + raison aux martelés
                arbresMarteles.put(numero, raisonMartelage);


                if (noteEcologique > noteEcologiqueMax) {
                    Intent intent = new Intent(getApplicationContext(), MessageErreurArbreMartele.class);
                    startActivity(intent);
                }
                AfficherArbre.this.finish();

            }
        });

    }


    public String onRadioButtonClicked(RadioGroup rb) {
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
        return null;
    }
}
