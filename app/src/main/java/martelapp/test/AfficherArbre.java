package martelapp.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AfficherArbre extends AppCompatActivity {

    TextView mTextView;

    String  numero,
            essence,
            etat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_arbre);

        mTextView = (TextView)findViewById(R.id.textView);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            numero = extras.getString("numero");
            essence = extras.getString("essence");
            etat = extras.getString("etat");

        }
        /*
        numero = (String) getIntent().getExtras().getString("numero");

        essence = (String) getIntent().getExtras().getString("essence");

        etat = (String) getIntent().getExtras().getString("etat");
    */


        mTextView.setText("Numéro : " + numero + " , Essence : " + essence + " , Etat : " + etat);


    }
}
