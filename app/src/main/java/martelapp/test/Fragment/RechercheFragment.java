package martelapp.test.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import martelapp.test.Activity.AfficherArbreActivity;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by cimin on 04/04/2018.
 */

public class RechercheFragment extends Fragment {

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
            mButtonOk;

    TextView mTextView,
            tvNum,
            tvEssence, tvEtat;

    DatabaseHelper dbHelper;
    Cursor cur;
    String query;

    String numero,
            essence,
            etat,
            nomEquipe;
    int noteEcologique;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_page_recherche, null);

        mButton0 = (Button) view.findViewById(R.id.buttonZero);
        mButton1 = (Button) view.findViewById(R.id.buttonOne);
        mButton2 = (Button) view.findViewById(R.id.buttonTwo);
        mButton3 = (Button) view.findViewById(R.id.buttonThree);
        mButton4 = (Button) view.findViewById(R.id.buttonFour);
        mButton5 = (Button) view.findViewById(R.id.buttonFive);
        mButton6 = (Button) view.findViewById(R.id.buttonSix);
        mButton7 = (Button) view.findViewById(R.id.buttonSeven);
        mButton8 = (Button) view.findViewById(R.id.buttonEight);
        mButton9 = (Button) view.findViewById(R.id.buttonNine);
        mButtonClear = (Button) view.findViewById(R.id.buttonClear);
        mButtonDel = (Button) view.findViewById(R.id.buttonDel);
        mButtonOk = (Button) view.findViewById(R.id.buttonOk);
        tvNum = (TextView) view.findViewById(R.id.numero_tree_card);
        tvEssence = (TextView) view.findViewById(R.id.essence_tree_card);
        tvEtat = (TextView) view.findViewById(R.id.etat_tree_card);

        mEditText = (EditText) view.findViewById(R.id.editText);

        mTextView = (TextView) view.findViewById(R.id.textView);

        Bundle extras = getActivity().getIntent().getExtras();

        dbHelper = new DatabaseHelper(view.getContext());

        if (extras != null) {
            nomEquipe = extras.getString("nomEquipe");

        }

        // Bouton 0
        mButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText() + "0");
            }
        });

        // Bouton 1
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText() + "1");
            }
        });

        // Bouton 2
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText() + "2");
            }
        });

        // Bouton 3
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText() + "3");
            }
        });

        // Bouton 4
        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText() + "4");
            }
        });

        // Bouton 5
        mButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText() + "5");
            }
        });

        // Bouton 6
        mButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText() + "6");
            }
        });

        // Bouton 7
        mButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText() + "7");
            }
        });

        // Bouton 8
        mButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText() + "8");
            }
        });

        // Bouton 9
        mButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(mEditText.getText() + "9");
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
                if (mEditText.getText().length() > 0) {
                    mEditText.setText(mEditText.getText().subSequence(0, mEditText.getText().length() - 1));
                }
            }
        });

        // Bouton Ok
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEditText.getText().length() > 0) {
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

                            //on met a jour l'affichage de l'arbre
                            tvEssence.setText(cur.getString(cur.getColumnIndex(dbHelper.ESSENCE_ARBRE)));
                            tvEtat.setText(cur.getString(cur.getColumnIndex(dbHelper.ETAT_ARBRE)));
                            tvNum.setText(cur.getString(cur.getColumnIndex(dbHelper.NUMERO_ARBRE_PARC)));
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
        /*mButtonListeArbre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ArbresMartelesActivity.class);
                startActivity(i);
            }
        });*/

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


        return view;
    }


}
