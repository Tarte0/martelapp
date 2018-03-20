package martelapp.test.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.Tree;
import martelapp.test.R;

public class RechercheActivity extends AppCompatActivity {

    EditText mEditText;
    Button mButton0,
            mButton1,
            mButton2,
            mButton3,
            mButton4,
            mButton5,
            mButton6,
            mButton7,
            mButton8,
            mButton9,
            mButtonClear,
            mButtonDel,
            mButtonOk,
            mButtonConsignes,
            mButtonListeArbre,
            mButtonTerminer;

    TextView mTextView;

    boolean estTrouve;

    //Get Database Reference
    DatabaseReference mRefArbre;
    DatabaseHelper dbHelper;

    Tree arbre;
    String  numero,
            essence,
            etat,
            noteEcologique,
            nomEquipe;

    Map<String, String> arbresMarteles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        dbHelper = new DatabaseHelper(getApplicationContext());


        // Bouton retour sur Barre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Reference des arbres
        mRefArbre = FirebaseDatabase.getInstance().getReference().child("parcelles/parcelleMartelapp/arbres");

        // pour que la bdd soit dispo en hors ligne
        mRefArbre.keepSynced(true);

        mButton0 = (Button)findViewById(R.id.buttonZero);
        mButton1 = (Button)findViewById(R.id.buttonOne);
        mButton2 = (Button)findViewById(R.id.buttonTwo);
        mButton3 = (Button)findViewById(R.id.buttonThree);
        mButton4 = (Button)findViewById(R.id.buttonFour);
        mButton5 = (Button)findViewById(R.id.buttonFive);
        mButton6 = (Button)findViewById(R.id.buttonSix);
        mButton7 = (Button)findViewById(R.id.buttonSeven);
        mButton8 = (Button)findViewById(R.id.buttonEight);
        mButton9 = (Button)findViewById(R.id.buttonNine);
        mButtonClear = (Button)findViewById(R.id.buttonClear);
        mButtonDel = (Button)findViewById(R.id.buttonDel);
        mButtonOk = (Button)findViewById(R.id.buttonOk);
        mButtonConsignes = (Button)findViewById(R.id.buttonConsignes);
        mButtonListeArbre = (Button)findViewById(R.id.buttonListeArbre);
        mButtonTerminer = (Button)findViewById(R.id.buttonTerminer);

        mEditText = (EditText)findViewById(R.id.editText);

        mTextView = (TextView) findViewById(R.id.textView);
        //mTextView.setVisibility(View.INVISIBLE);

        if(dbHelper.isEmpty(DatabaseHelper.ARBRES_PARCELLE_TABLE)){
            Toast toast = Toast.makeText(getApplicationContext(), "La table est vide", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Y'a un truc dans la table", Toast.LENGTH_LONG);
            toast.show();
        }


        Cursor cur = dbHelper.getAllDataFromTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);
        cur.moveToFirst();
        String n = cur.getString(cur.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC));
        mTextView.setText(n);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            nomEquipe = extras.getString("nomEquipe");


            // Affiche le numéro de l'arbre dans la barre supérieure
            getSupportActionBar().setTitle("MartelApp : " + nomEquipe);

        }



        // Bouton 0
        mButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText()+ "0");
            }
        });

        // Bouton 1
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText()+ "1");
            }
        });

        // Bouton 2
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText()+ "2");
            }
        });

        // Bouton 3
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText()+ "3");
            }
        });

        // Bouton 4
        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText()+ "4");
            }
        });

        // Bouton 5
        mButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText()+ "5");
            }
        });

        // Bouton 6
        mButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText()+ "6");
            }
        });

        // Bouton 7
        mButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText()+ "7");
            }
        });

        // Bouton 8
        mButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText()+ "8");
            }
        });

        // Bouton 9
        mButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText()+ "9");
            }
        });

        // Bouton Clear
        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
            }
        });

        // Bouton Del
        mButtonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText().subSequence(0,mEditText.getText().length()-1));
            }
        });

        // Bouton Ok
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numEntree = mEditText.getText().toString();



                // Traiter cas où pas trouvé !!! + boutons retour + demander raison modifiable? sinon bloquer creer boutons consignes/consulter arbre marteles/...



                mRefArbre.orderByChild("numero").equalTo(numEntree).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        arbre = dataSnapshot.getValue(Tree.class);
                        numero = arbre.getNumero();
                        essence = arbre.getEssence();
                        etat = arbre.getEtat();
                        noteEcologique = arbre.getNoteEcologique();

                        // Pour donner les string a afficher Arbre
                        Intent intent = new Intent(getApplicationContext(),AfficherArbreActivity.class);
                        intent.putExtra("numero", numero);
                        intent.putExtra("essence", essence);
                        intent.putExtra("etat", etat);
                        intent.putExtra("noteEcologique", noteEcologique);
                        startActivity(intent);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                /* Si un arbre n'est pas trouvé, on rend visiblele textView et on affiche un message d'erreur
                if(!estTrouve){
                    mTextView.setVisibility(View.VISIBLE);
                    mTextView.setText("L'arbre n°" + mEditText.getText().toString() +" n'est pas présent dans la parcelle.");
                }
                */

            }
        });








    }

    protected void onPause() {
        super.onPause();
        mTextView.setText("");

    }

}
