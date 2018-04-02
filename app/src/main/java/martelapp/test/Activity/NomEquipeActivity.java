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

public class NomEquipeActivity extends AppCompatActivity {

    EditText editTextTeamName;

    String nomEquipe;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nom_equipe);

        dbHelper = new DatabaseHelper(getApplicationContext());

        editTextTeamName = findViewById(R.id.editTextTeamName);

        Button buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(editTextTeamName.getText().length()>0) {
                    nomEquipe = editTextTeamName.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), RechercheActivity.class);
                    intent.putExtra("nomEquipe", nomEquipe);
                    startActivity(intent);

            }
        });
    }
}
