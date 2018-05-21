package martelapp.test.Fragment.Exercice;

import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import martelapp.test.Activity.ExerciceActivity;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by cimin on 04/04/2018.
 */

public class SelectionArbreFragment extends Fragment {

    View mainView;

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



    LinearLayout conserverButtonTreeCard,
                 martelerButtonTreeCard,
                 prochaineFoisButtonTreeCard;

    ImageView dejaSelectionneImage;

    TextView tvNum,
            tvEssence,
            tvEtat,
            tvDiametre,
            tvDejaSelectionne;

    DatabaseHelper dbHelper;
    Cursor cur1, cur2;
    String query;

    String numArbreCourant;

    Snackbar errorsSnack;

    LinearLayout treeCardNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mainView == null) {
            mainView = inflater.inflate(R.layout.view_page_selection, null);

            mButton0 = (Button) mainView.findViewById(R.id.buttonZero);
            mButton1 = (Button) mainView.findViewById(R.id.buttonOne);
            mButton2 = (Button) mainView.findViewById(R.id.buttonTwo);
            mButton3 = (Button) mainView.findViewById(R.id.buttonThree);
            mButton4 = (Button) mainView.findViewById(R.id.buttonFour);
            mButton5 = (Button) mainView.findViewById(R.id.buttonFive);
            mButton6 = (Button) mainView.findViewById(R.id.buttonSix);
            mButton7 = (Button) mainView.findViewById(R.id.buttonSeven);
            mButton8 = (Button) mainView.findViewById(R.id.buttonEight);
            mButton9 = (Button) mainView.findViewById(R.id.buttonNine);
            mButtonClear = (Button) mainView.findViewById(R.id.buttonClear);
            mButtonDel = (Button) mainView.findViewById(R.id.buttonDel);
            mButtonOk = (Button) mainView.findViewById(R.id.buttonOk);
            martelerButtonTreeCard = mainView.findViewById(R.id.imgMarteler);
            conserverButtonTreeCard = mainView.findViewById(R.id.imgConserver);
            prochaineFoisButtonTreeCard = mainView.findViewById(R.id.imgProchaineFois);
            tvNum = (TextView) mainView.findViewById(R.id.numero_tree_card);
            tvEssence = (TextView) mainView.findViewById(R.id.essence_tree_card);
            tvEtat = (TextView) mainView.findViewById(R.id.etat_tree_card);
            tvDiametre = (TextView) mainView.findViewById(R.id.diametre_tree_card);
            tvDejaSelectionne = mainView.findViewById(R.id.Deja_selectionne);

            mEditText = (EditText) mainView.findViewById(R.id.editText);

            dejaSelectionneImage = (ImageView) mainView.findViewById(R.id.dejaSelectionneImage);
            //dejaConserveImage = (ImageView) mainView.findViewById(R.id.dejaConserveImage);


            treeCardNumber = mainView.findViewById(R.id.arbreLayout);


            mEditText.setHint("Saisie n° d'arbre");

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
                        dbHelper = new DatabaseHelper(mainView.getContext());

                        query = "SELECT * FROM " + DatabaseHelper.ARBRES_MARTELES_TABLE + " WHERE " +
                                DatabaseHelper.NUMERO_ARBRE_MART + " = " + numEntree;
                        cur1 = dbHelper.executeQuery(query);

                        cur2 = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.ARBRES_CONSERVES_TABLE, DatabaseHelper.NUMERO_ARBRE_CONS + " = " + numEntree);

                        if (cur1.moveToFirst()) {
                            dejaSelectionneImage.bringToFront();
                            dejaSelectionneImage.setVisibility(View.VISIBLE);
                            dejaSelectionneImage.setImageResource(R.drawable.marteau_martele);
                            dejaSelectionneImage.setColorFilter(dejaSelectionneImage.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

                            tvDejaSelectionne.setVisibility(View.VISIBLE);
                            tvDejaSelectionne.setText("Cet arbre a déjà été martelé !");
                            cleanCard();
                        } else if (cur2.moveToFirst()) {
                            dejaSelectionneImage.setVisibility(View.VISIBLE);
                            dejaSelectionneImage.setImageResource(R.drawable.conserver);
                            dejaSelectionneImage.setColorFilter(dejaSelectionneImage.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

                            tvDejaSelectionne.setVisibility(View.VISIBLE);
                            tvDejaSelectionne.setText("Cet arbre a déjà été conservé !");

                            cleanCard();
                        } else {
                            dejaSelectionneImage.setVisibility(View.INVISIBLE);
                            treeCardNumber.setVisibility(View.VISIBLE);
                            tvDejaSelectionne.setVisibility(View.GONE);

                            // Requete de recherche d'arbre par le numéro "numEntree"
                            query = "SELECT * FROM " + DatabaseHelper.ARBRES_PARCELLE_TABLE +
                                    " WHERE " + DatabaseHelper.NUMERO_ARBRE_PARC + " = " + numEntree;
                            cur1 = dbHelper.executeQuery(query);

                            // Si le numéro entrée correspond à un arbre existant dans la parcelle
                            if (cur1.moveToFirst()) {
                                numArbreCourant = numEntree;
                                //on met a jour l'affichage de l'arbre
                                tvEssence.setText(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
                                tvEtat.setText(etatToString(cur1.getString(cur1.getColumnIndex(DatabaseHelper.ETAT_ARBRE))));
                                tvDiametre.setText(String.format("Diametre : %s cm", cur1.getString(cur1.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE))));
                                tvNum.setText(String.format("Arbre n°%s", cur1.getString(cur1.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC))));

                            }

                            // Si l'arbre cherché n'existe pas, un message d'erreur est affiché
                            else {
                                tvDejaSelectionne.setVisibility(View.VISIBLE);
                                tvDejaSelectionne.setText("Cet arbre n'existe pas !");

                                dejaSelectionneImage.setVisibility(View.VISIBLE);
                                dejaSelectionneImage.setImageResource(R.drawable.alerte);
                                dejaSelectionneImage.setColorFilter(dejaSelectionneImage.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);


                                cleanCard();
                            }
                        }
                        mEditText.setText("");
                        cur1.close();
                        cur2.close();
                        dbHelper.close();
                    }
                }
            });


            martelerButtonTreeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!numArbreCourant.equals("")) {
                        ((ExerciceActivity) getActivity()).openMartelagePopup(numArbreCourant);
                        cleanCard();
                        numArbreCourant = "";
                    }

                }
            });

            conserverButtonTreeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!numArbreCourant.equals("")) {
                        ((ExerciceActivity) getActivity()).openConserverPopup(numArbreCourant);
                        cleanCard();
                        numArbreCourant = "";
                    }

                }
            });

            prochaineFoisButtonTreeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!numArbreCourant.equals("")) {
                        cleanCard();
                        numArbreCourant = "";
                    }

                }
            });
        }

        cleanCard();

        return mainView;
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
        tvDiametre.setText("");
        tvNum.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        cleanCard();
    }
}
