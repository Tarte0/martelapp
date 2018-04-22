package martelapp.test.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import martelapp.test.Activity.RechercheActivity;
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
    ImageButton martelerButtonTreeCard;

    ImageView dejaMarteleImage;

    TextView mTextView,
            tvNum,
            tvEssence, tvEtat;

    DatabaseHelper dbHelper;
    Cursor cur;
    String query;

    String numArbreCourant,
            nomEquipe;

    Snackbar errorsSnack;

    LinearLayout treeCardNumber;

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
        martelerButtonTreeCard = (ImageButton) view.findViewById(R.id.martelerButtonTreeCard);
        tvNum = (TextView) view.findViewById(R.id.numero_tree_card);
        tvEssence = (TextView) view.findViewById(R.id.essence_tree_card);
        tvEtat = (TextView) view.findViewById(R.id.etat_tree_card);

        mEditText = (EditText) view.findViewById(R.id.editText);

        dejaMarteleImage = (ImageView) view.findViewById(R.id.dejaMarteleImage);

        treeCardNumber = view.findViewById(R.id.arbreLayout);

        Bundle extras = getActivity().getIntent().getExtras();

        dbHelper = new DatabaseHelper(view.getContext());

        if (extras != null) {
            nomEquipe = extras.getString("nomEquipe");
        }

        // Bouton 0
        mButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(String.format("%s0", mEditText.getText()));
            }
        });

        // Bouton 1
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(String.format("%s1", mEditText.getText()));
            }
        });

        // Bouton 2
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(String.format("%s2", mEditText.getText()));
            }
        });

        // Bouton 3
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(String.format("%s3", mEditText.getText()));
            }
        });

        // Bouton 4
        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(String.format("%s4", mEditText.getText()));
            }
        });

        // Bouton 5
        mButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(String.format("%s5", mEditText.getText()));
            }
        });

        // Bouton 6
        mButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(String.format("%s6", mEditText.getText()));
            }
        });

        // Bouton 7
        mButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(String.format("%s7", mEditText.getText()));
            }
        });

        // Bouton 8
        mButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(String.format("%s8", mEditText.getText()));
            }
        });

        // Bouton 9
        mButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText(String.format("%s9", mEditText.getText()));
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
                 */
                    query = "SELECT * FROM " + DatabaseHelper.ARBRES_MARTELES_TABLE + " WHERE " +
                            DatabaseHelper.NUMERO_ARBRE_MART + " = " + numEntree;
                    cur = dbHelper.executeQuery(query);
                    if (cur.moveToFirst()) {
                        dejaMarteleImage.setVisibility(View.VISIBLE);
                        dejaMarteleImage.bringToFront();
                        cleanCard();
                        showSnackbar(view, "Arbre n°" + numEntree + " deja martelé");
                    } else {
                        dejaMarteleImage.setVisibility(View.INVISIBLE);
                        treeCardNumber.setVisibility(View.VISIBLE);
                        // Requete de recherche d'arbre par le numéro "numEntree"
                        query = "SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE +
                                " WHERE " + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numEntree;
                        cur = dbHelper.executeQuery(query);

                        // Si le numéro entrée correspond à un arbre existant dans la parcelle
                        if (cur.moveToFirst()) {
                            numArbreCourant = numEntree;
                            //on met a jour l'affichage de l'arbre
                            tvEssence.setText(cur.getString(cur.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
                            tvEtat.setText(etatToString(cur.getString(cur.getColumnIndex(DatabaseHelper.ETAT_ARBRE))));
                            tvNum.setText(String.format("Arbre n°%s", cur.getString(cur.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC))));
                        }

                        // Si l'arbre cherché n'existe pas, un message d'erreur est affiché
                        else {
                            cleanCard();
                            showSnackbar(view, "L'arbre n°" + numEntree + " n'existe pas");
                        }
                    }
                    mEditText.setText("");
                    cur.close();
                    numEntree = "";
                }
            }
        });


        martelerButtonTreeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!numArbreCourant.equals("")) {
                    ((RechercheActivity) getActivity()).openMartelagePopup(numArbreCourant);
                    cleanCard();
                    numArbreCourant = "";
                }

            }
        });

        treeCardNumber.setVisibility(View.INVISIBLE);

        return view;
    }

    private String etatToString(String etat) {
        switch (etat) {
            case "v":
                return "Vivant";
            case "mp":
                return "Mort sur pied";
            case "ms":
                return "Mort sur sol";
            default:
                return "";
        }
    }

    private void showSnackbar(View view, String text) {
        errorsSnack = Snackbar.make(view, text, Snackbar.LENGTH_LONG);

        errorsSnack.show();
    }

    public void cleanCard() {
        treeCardNumber.setVisibility(View.INVISIBLE);
        tvEssence.setText("");
        tvEtat.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        cleanCard();
    }
}
