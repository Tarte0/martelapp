package martelapp.test.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * NomEquipeActivity est la première activité sur laquelle l'utilisateur arrive lors d'un nouvel exercice.
 * Il doit alors saisir le nom de son équipe.
 */
public class NomEquipeActivity extends AppCompatActivity {

    EditText editTextTeamName;

    TextView tvErreurNom;

    String nomEquipe;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nom_equipe);

        dbHelper = new DatabaseHelper(getApplicationContext());

        editTextTeamName = findViewById(R.id.editTextTeamName);
        tvErreurNom = findViewById(R.id.text_erreur_nom);
        Button buttonStart = findViewById(R.id.buttonStart);


        /*##############################
         *###  Bouton "Commencer"    ###
         *##############################
         *
         * Si un nom d'équipe à été saisit, et que ce dernier ne contient
         * pas de caractères spéciaux, cliquer sur ce bouton mène à
         * ExerciceActivity, activité dans laquelle l'utilisateur va
         * réaliser l'exercice de martelage.
         */
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHelper.clearTableExercice();
                nomEquipe = editTextTeamName.getText().toString();

                // On vérifie qu'un nom a bien été saisit par l'utilisateur.
                if (nomEquipe.length() > 0) {
                    // Pas de caractères spéciaux de le nom, ceci peut pourrait provoquer des erreurs dans la requete MYSQL
                    if (nomEquipe.contains("'") || nomEquipe.contains("\"") || nomEquipe.contains(",") || nomEquipe.contains(";") || nomEquipe.contains("?") || nomEquipe.contains("!")
                            || nomEquipe.contains(":") || nomEquipe.contains("/") || nomEquipe.contains("~")) {
                        tvErreurNom.setText(R.string.saisir_pas_de_caracteres_speciaux);
                    } else {
                        dbHelper.updateNomEquipeConstante(nomEquipe);
                        dbHelper.close();
                        Intent intent = new Intent(getApplicationContext(), ExerciceActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (nomEquipe.length() == 0) {
                    tvErreurNom.setText(R.string.saisir_nom);
                }

            }
        });
    }
}
