package martelapp.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText editTextTeamName;
    Button buttonStart;


    String nomEquipe;

    //Get Database Reference
   // DatabaseReference mRefArbre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
