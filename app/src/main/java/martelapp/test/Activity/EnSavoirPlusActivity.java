package martelapp.test.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import martelapp.test.R;

/**
 * EnSavoirPlusActivity est l'activité permettant d'obtenir des informations quant au
 * contexte dans lequel à été réalisé l'application et le tableau de bord.
 */
public class EnSavoirPlusActivity extends AppCompatActivity{

    /**
     * Bouton pour retourner dans MainActivity
     */
    Button buttonRetour;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_savoir_plus);

        buttonRetour = findViewById(R.id.bouton_retour);


        /*########################
         *###  Bouton "Retour" ###
         *########################
         *
         * Permet de retourner à MainActivity
         */
        buttonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
}
