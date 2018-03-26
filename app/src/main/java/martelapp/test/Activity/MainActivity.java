package martelapp.test.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    EditText editTextTeamName;

    Button buttonStart;

    String nomEquipe;


    DatabaseReference firebaseDatabase;
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(getApplicationContext());
        dbHelper.clean();

        // Récupération de la base de données firebase
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        /*
         * Listener qui récupère toute la parcelle à partir de la base de données firebase
         * et enregistre tous les arbres de la parcelle dans la table arbres_parcelle_table
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
                         * Parcours de la Hashmap<String, Tree> contenant tous les arbres
                         * et les enregistres dans la table arbres_parcelle_table
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


        editTextTeamName = (EditText) findViewById(R.id.editTextTeamName);
        buttonStart = (Button) findViewById(R.id.buttonStart);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomEquipe = editTextTeamName.getText().toString();
                Intent intent = new Intent(getApplicationContext(), RechercheActivity.class);
                intent.putExtra("nomEquipe", nomEquipe);
                startActivity(intent);
            }
        });

    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
        editTextTeamName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editTextTeamName.setText("");
            }
        });
    }
    */

}
