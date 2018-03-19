package martelapp.test;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    EditText editTextTeamName;

    Button buttonStart;

    String nomEquipe;

    //Get Database Reference
    DatabaseReference mRefArbre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRefArbre = FirebaseDatabase.getInstance().getReference();
        //Rendre bdd dispo quand offline
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        editTextTeamName = (EditText)findViewById(R.id.editTextTeamName);
        buttonStart = (Button)findViewById(R.id.buttonStart);



        nomEquipe = editTextTeamName.getText().toString();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Recherche.class);
                startActivity(intent);
            }
        });

    }

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


    public void dataBuilder(){
        DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Parcelle parcelle = dataSnapshot.getValue(Parcelle.class);
                if(parcelle != null){

                    Hashmap<String, Tree> arbres_parcelle = parcelle.getArbresParcelle();
                    Iterator it = arbres_parcelle.entrySet().iterator();
                    (while it.hasNext()){

                    }

                    /*

                    tu itere dans les arbres et tu rempli la bdd sql

                     */
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // errerur a gérer, je ferais quand j'aurais mis un pantalon

            }
        };
    }

}
