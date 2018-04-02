package martelapp.test.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import martelapp.test.Class.DatabaseHelper;
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

    DatabaseHelper dbHelper;
    Cursor cur;
    String query;

    String  numero,
            essence,
            etat,
            nomEquipe;
    int noteEcologique;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        dbHelper = new DatabaseHelper(getApplicationContext());


        // Bouton retour sur Barre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                if(mEditText.getText().length()>0) {
                    mEditText.setText(mEditText.getText().subSequence(0, mEditText.getText().length() - 1));
                }
            }
        });

        // Bouton Ok
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mEditText.getText().length()>0) {
                    String numEntree = mEditText.getText().toString();

                /*
                 *  On vérifie si l'arbre selectionné a déjà été martelé
                 *  Si c'est le cas, un message d'alerte est affiché
                 */
                    query = "SELECT * FROM " + dbHelper.ARBRES_MARTELES_TABLE + " WHERE " + dbHelper.NUMERO_ARBRE_MART + " = " + numEntree;
                    cur = dbHelper.executeQuery(query);
                    if (cur.moveToFirst()) {
                        mTextView.setText("L'arbre n°" + mEditText.getText().toString() + " a déjà été martelé.");
                    } else {
                        // Requete de recherche d'arbre par le numéro "numEntree"
                        query = "SELECT * FROM " + dbHelper.ARBRES_PARCELLE_TABLE + " WHERE " + dbHelper.NUMERO_ARBRE_PARC + " = " + numEntree;
                        cur = dbHelper.executeQuery(query);

                        // Si le numéro entrée correspond à un arbre existant dans la parcelle
                        if (cur.moveToFirst()) {
                            Intent intent = new Intent(getApplicationContext(), AfficherArbreActivity.class);
                            intent.putExtra("numero", numEntree);
                            startActivity(intent);
                        }

                        // Si l'arbre cherché n'existe pas, un message d'erreur est affiché
                        else {
                            mTextView.setText("L'arbre n°" + mEditText.getText().toString() + " n'est pas présent dans la parcelle.");
                        }
                    }
                    mEditText.setText("");
                    cur.close();
                }
            }
        });


        /*
         *
         *
         *
         *       !!!!!! TEST LISTE ARBRE !!!!!!!
         *
         *
         *
         *
         *
         */
        mButtonListeArbre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ArbresMartelesActivity.class);
                startActivity(i);
            }
        });

        /*
         *
         *
         *
         *
         *         !!!!!!!!!!!! FIN TEST LISTE ARBRE !!!!!!!!!!
         *
         *
         *
         *
         */




    }

    protected void onPause() {
        mTextView.setText("");
        super.onPause();
    }

}
