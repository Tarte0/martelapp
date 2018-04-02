package martelapp.test.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import martelapp.test.R;

public class MainActivity extends AppCompatActivity {

    DatabaseReference firebaseDatabase;
    DatabaseReference connectedRef;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(getApplicationContext());


        /*
         *  Bouton "Nouvel Exercice"
         *
         *  Efface toutes les données qu'il y a dans les tables
         *  <arbres_marteles_table> et <raison_table> et lance
         *  l'activité du choix du nom de l'équipe <NomEquipeActivity>
         *  pour commencer un exercice de martelage de zero
         */
        Button buttonNouvelExercice = findViewById(R.id.nouvel_exercice);
        buttonNouvelExercice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.clearTableExercice();
                Intent intent = new Intent(getApplicationContext(), NomEquipeActivity.class);
                startActivity(intent);
            }
        });


        /*
         *  Bouton "Continuer Exercice"
         *
         *  Renvoie directement à l'activité de recherche d'arbre
         *  <RechercheActivity>.
         *  Sécurité en cas de mauvaise manipulation de l'utilisateur
         *  ou de crash de l'application pour que l'utilisateur puisse
         *  retrouver sa progression de l'exercice.
         */
        Button buttonContinuerExercice = findViewById(R.id.continuer_exercice);
        buttonContinuerExercice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RechercheActivity.class);
                startActivity(intent);
            }
        });


        /*
         *  Bouton "Test MAJ"
         *
         *  Met à jour les données de <arbres_parcelle_table>
         *  depuis une base de données Firebase en testant
         *  premièrement si il y a une connexion à internet
         */
        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        Button buttonTestMAJ = findViewById(R.id.test_maj_bdd);
        buttonTestMAJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                 *  Test d'une connexion internet à Firebase
                 *  Si il y connexion, <arbres_parcelle_table>
                 *  est mise à jour
                 */
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            miseAJourArbreParcelleTable();
                            Toast toast = Toast.makeText(getApplicationContext(), "Connexion à la base de données Firebase reussi, mise à jour des données", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Echec de connexion à la base de données Firebase, impossible de mettre à jour", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Erreur: échec test connexion", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        });
    }


    public void miseAJourArbreParcelleTable(){

        // Suppression des données de la table <arbres_parcelles_table>
        dbHelper.clearTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);

        // Récupération de la base de données firebase
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        /*
         *  Listener qui récupère toute la parcelle à partir de la base de données firebase
         *  et enregistre tous les arbres de la parcelle dans la table arbres_parcelle_table
         */
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Récupération de la parcelle de la base de données firebase dans la classe Parcelle
                Parcelle parcelle = dataSnapshot.getValue(Parcelle.class);

                // Test de la récupération de la parcelle
                if(parcelle != null){

                    // Tous les arbres de la parcelle son enregistrer dans une Hashmap
                    HashMap<String, Tree> arbres_parcelle = parcelle.arbres;

                    // Test si il y a bien des arbres dans la parcelle
                    if(!arbres_parcelle.isEmpty()){

                        /*
                         *  Parcours de la Hashmap<String, Tree> contenant tous les arbres
                         *  et les enregistres dans la table arbres_parcelle_table
                         */
                        Iterator it = arbres_parcelle.entrySet().iterator();
                        while (it.hasNext()) {
                            HashMap.Entry pair = (HashMap.Entry)it.next();
                            Tree arbre = (Tree) pair.getValue();
                            dbHelper.insertArbreParcelle(   arbre.numero,
                                    arbre.essence,
                                    Integer.parseInt(arbre.diametre),
                                    Integer.parseInt(arbre.noteEcologique),
                                    arbre.etat,
                                    arbre.coord.x,
                                    arbre.coord.y,
                                    arbre.utilisationBois.chauffage.equals("")? 0.0 : Double.parseDouble(arbre.utilisationBois.chauffage),
                                    arbre.utilisationBois.industrie.equals("")? 0.0 : Double.parseDouble(arbre.utilisationBois.industrie),
                                    arbre.utilisationBois.oeuvre.equals("")? 0.0 : Double.parseDouble(arbre.utilisationBois.oeuvre));

                            it.remove();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        // On assigne le listener "postListener" à notre parcelle Martelapp de la base de données firebase
        firebaseDatabase.child("parcelles").child("parcelleMartelapp").addListenerForSingleValueEvent(postListener);
    }
}
