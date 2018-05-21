package martelapp.test.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.Parcelle;
import martelapp.test.Class.Tree;
import martelapp.test.Class.VolumeCalculator;
import martelapp.test.R;


/**
 * <b>MainActivity est la première activité lors du lancement de l'application où
 * l'utilisateur peut choisir de commencer un nouvel exercice ou de continuer un
 * exercice en cours </b>
 *
 * <p>
 * Dans cette activité est récupérée la base de données firebase de la parcelle
 * pour l'enregistrer localement dans l'appareil avec SQLITE.
 * </p>
 *
 * <p>
 * Deux boutons sont présent dans cette activité :
 * <ul>
 * <li>Nouvel exercice : Supprime les données SQLITE utilisées pour un exercice et
 * lance NomEquipeActivity pour démarrer un nouvel exercice.
 * Les tables concernées sont ARBRES_MARTELES_TABLE, ARBRES_CONSERVES_TABLE et RAISON_TABLE.</li>
 * <li>Continuer exercice : Lance ExerciceActivity. Ce bouton est une sécurité en cas de crash de
 * l'application ou d'une mauvaise manipulation de l'utilisateur pour qu'il puisse retrouver son
 * exercice en cours.</li>
 * </p>
 *
 * @see DatabaseHelper
 *
 * @author Baptiste
 * @version 1.0
 *
 */
public class MainActivity extends AppCompatActivity {

    /**
     * La base de données SQLITE locale
     *
     * @see DatabaseHelper
     */
    private DatabaseHelper dbHelper;

    private Cursor cur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getApplicationContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);

        /*#################################
         *###  Bouton "Nouvel Exercice" ###
         *#################################
         *
         * Efface toutes les données qu'il y a dans les tables
         * ARBRES_MARTELES_TABLE, ARBRES_CONSERVES_TABLE et RAISON_TABLE
         * et lance l'activité du choix du nom de l'équipe NomEquipeActivity
         * pour commencer un nouvel exercice de martelage.
         */
        Button buttonNouvelExercice = findViewById(R.id.nouvel_exercice);
        buttonNouvelExercice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkDatabase()) {
                    Intent intent = new Intent(getApplicationContext(), NomEquipeActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Mettez à jour la base de données", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*####################################
         *###  Bouton "Continuer Exercice" ###
         *####################################
         *
         * Lance l'activité de l'exercice de martelage ExerciceActivity.
         * Sécurité en cas de mauvaise manipulation de l'utilisateur
         * ou de crash de l'application pour que l'utilisateur puisse
         * retrouver sa progression de l'exercice.
         */

        Button buttonContinuerExercice = findViewById(R.id.continuer_exercice);

        if(checkDatabase()) {
            if (checkAncienExercice()) {
                buttonContinuerExercice.setVisibility(View.VISIBLE);
            } else {
                buttonContinuerExercice.setVisibility(View.GONE);
            }

        } else {
            buttonContinuerExercice.setVisibility(View.GONE);
        }

        buttonContinuerExercice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkDatabase()) {
                    if(checkAncienExercice()) {
                        Intent intent = new Intent(getApplicationContext(), ExerciceActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Aucune sauvegarde d'un exercice de martelage est disponible", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Mettez à jour la base de données", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ImageButton buttonTestMAJ = findViewById(R.id.test_maj_bdd);
        buttonTestMAJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DatabaseHelper.isNetworkAvailable(getApplicationContext())) {
                    Intent intent = new Intent(getApplicationContext(), ChoixParcelleActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Aucune connexion à internet disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean checkDatabase(){
        dbHelper = new DatabaseHelper(getApplicationContext());


        boolean res;

        cur = dbHelper.getAllDataFromTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);
        res = cur.moveToFirst();

        cur.close();
        dbHelper.close();

        return res;
    }

    private boolean checkAncienExercice(){
        dbHelper = new DatabaseHelper(getApplicationContext());

        boolean res;

        cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
        cur.moveToFirst();
        res = cur.getString(cur.getColumnIndex(DatabaseHelper.NOM_EQUIPE)) != null;

        cur.close();
        dbHelper.close();

        return res;
    }
}