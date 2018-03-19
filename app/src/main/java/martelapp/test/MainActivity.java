package martelapp.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText editTextTeamName;

    Button buttonStart;

    String nomEquipe;

    static {
        //Rendre bdd dispo quand offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextTeamName = (EditText) findViewById(R.id.editTextTeamName);
        buttonStart = (Button) findViewById(R.id.buttonStart);




        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomEquipe = editTextTeamName.getText().toString();
                Intent intent = new Intent(getApplicationContext(), Recherche.class);
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
