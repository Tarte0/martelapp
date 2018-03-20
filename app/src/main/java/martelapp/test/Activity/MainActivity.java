package martelapp.test.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    EditText editTextTeamName;

    Button buttonStart;

    String nomEquipe;

    DatabaseReference mDatabase;

    DatabaseHelper dbHelper;

    static {
        //Rendre bdd dispo quand offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(getApplicationContext());
        dbHelper.clean();

        editTextTeamName = (EditText) findViewById(R.id.editTextTeamName);
        buttonStart = (Button) findViewById(R.id.buttonStart);


//on choppe la reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //on cree un listener pour tout récuperer
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // je prend toute la parcelle dans une classe parcelle
                Parcelle parcelle = dataSnapshot.getValue(Parcelle.class);
                if(parcelle != null){
                    HashMap<String, Tree> arbres_parcelle = parcelle.arbres;

                    if(!arbres_parcelle.isEmpty()){


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
                        Toast toast = Toast.makeText(getApplicationContext(), "oui", Toast.LENGTH_LONG);
                        toast.show();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        //on attache le listener sur la parcelle test (en gros on recupere la parcelle "test" sou forme d'ojet)
        mDatabase.child("parcelles").child("parcelleMartelapp").addListenerForSingleValueEvent(postListener);

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
