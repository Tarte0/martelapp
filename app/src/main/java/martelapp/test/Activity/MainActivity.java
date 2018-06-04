package martelapp.test.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;


/**
 * MainActivity est la première activité lors du lancement de l'application.
 *
 * Plusieurs bouton sont disponibles :
 *
 * - Nouvel exercice : Commence un nouvel exercice
 *
 * - Continuer un ancien exercice : Reprend un exercice en cours
 *
 * - En savoir plus : Mène au information du contexte de la réalisation de l'application
 *
 * - Un bouton d'option avec une icone d'engrenage : Mene à la sélection d'une parcelle dans ChoixParcelleActivity
 */
public class MainActivity extends AppCompatActivity {

    /**
     * La base de données SQLITE locale
     */
    private DatabaseHelper dbHelper;

    private Cursor cur;

    /**
     * Texte indiquant la parcelle actuellement sélectionnée
     */
    TextView textParcelleEnCours;


    Button buttonContinuerExercice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textParcelleEnCours = findViewById(R.id.text_parcelle_en_cours);


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




        buttonContinuerExercice = findViewById(R.id.continuer_exercice);

        actualiseTextParcelleEnCours();


        /*####################################
         *###  Bouton "Continuer Exercice" ###
         *####################################
         *
         * Lance l'activité de l'exercice de martelage ExerciceActivity.
         * Sécurité en cas de mauvaise manipulation de l'utilisateur
         * ou de crash de l'application pour que l'utilisateur puisse
         * retrouver sa progression de l'exercice.
         */
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


        /*####################################
         *###  Bouton "boutonParametres"   ###
         *####################################
         *
         * Lance l'activité "ChoixParcelleActivity" permettant de mettre à jour ou changer
         * la parcelle sur laquelle on souhaite réaliser l'exercice.
         * On regarde si une connexion internet est disponible :
         * - Si oui, on peut accéder à l'activité et changer les paramètres de parcelle
         * - Sinon, on affiche un Toast indiquant qu'aucune connexion est disponible. On
         *   empêche l'accès aux paramètres dans ce cas pour éviter une suppression de la base.
         *   Il serait alors impossible de réaliser l'exercice.
         */
        ImageButton boutonParametres = findViewById(R.id.parametres_parcelle);
        boutonParametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DatabaseHelper.isNetworkAvailable(getApplicationContext())) {
                    Intent intent = new Intent(getApplicationContext(), ChoixParcelleActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Aucune connexion à internet disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*####################################
         *###  Bouton "En savoir Plus"     ###
         *####################################
         *
         * Lance l'activité "EnSavoirPlusActivity" dans laquelle on retrouve des informations
         * sur le contexte dans lequel a été réalisé l'application
         */
        Button enSavoirPlus = findViewById(R.id.en_savoir_plus);
        enSavoirPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EnSavoirPlusActivity.class);
                startActivity(intent);
            }
        });

    }


    /**
     * Méthode permettant de savoir si la base de données locale contient des données ou non.
     *
     * @return res : Booléen indiquant si des données sont présentes dans la base de données locale.
     */
    private boolean checkDatabase(){
        dbHelper = new DatabaseHelper(getApplicationContext());


        boolean res;

        cur = dbHelper.getAllDataFromTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);
        res = cur.moveToFirst();

        cur.close();
        dbHelper.close();

        return res;
    }


    /**
     * Méthode permettant de savoir si un exercicé à déja été réalisé ou non.
     * Pour se faire, on regarde si la base de données locale contient un nom d'équipe.
     *
     * @return : Booléen indiquant si un exercice à déja été réalisé
     */
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


    /**
     * Méthode acutalisant le texte indiquant la parcelle actuellement sélectionnée
     *
     */
    private void actualiseTextParcelleEnCours(){
        // On vérifie que la base de données locale contient des données
        if(checkDatabase()) {
            dbHelper = new DatabaseHelper(getApplicationContext());
            cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
            cur.moveToFirst();

            // Affichage du nom et du lieu de la parcelle présente sur la base de données locale
            textParcelleEnCours.setText("Parcelle actuelle :\n" + cur.getString(cur.getColumnIndex(DatabaseHelper.NOM_PARCELLE)) +
                    "\nMarteloscope de " + cur.getString(cur.getColumnIndex(DatabaseHelper.LIEU_PARCELLE)));
            cur.close();
            dbHelper.close();

            // On affiche le bouton continuer exercice uniquement si un exercice à déja été réalisé
            if (checkAncienExercice()) {
                buttonContinuerExercice.setVisibility(View.VISIBLE);
            } else {
                buttonContinuerExercice.setVisibility(View.GONE);
            }


        }
        // Si aucune parcelle n'est dans la base de données locale, on l'affiche
        else {
            textParcelleEnCours.setText(R.string.aucune_parcelle);
            buttonContinuerExercice.setVisibility(View.GONE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        actualiseTextParcelleEnCours();
    }
}