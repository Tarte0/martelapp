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
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(editTextTeamName.getText().length()>0) {
                    dbHelper.clearTableExercice();
                    nomEquipe = editTextTeamName.getText().toString();
                    if (nomEquipe.length() >0) {
                        dbHelper.updateNomEquipeConstante(nomEquipe);
                        dbHelper.close();
                        Intent intent = new Intent(getApplicationContext(), ExerciceActivity.class);
                        startActivity(intent);
                        finish();
                   }
                    else if(nomEquipe.length() == 0){
                        tvErreurNom.setText(R.string.saisir_nom);
                    }

            }
        });
    }
}
