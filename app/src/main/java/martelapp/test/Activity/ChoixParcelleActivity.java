package martelapp.test.Activity;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.Parcelle;
import martelapp.test.Class.Tree;
import martelapp.test.Class.VolumeCalculator;
import martelapp.test.R;


public class ChoixParcelleActivity extends AppCompatActivity {

    /**
     * Le nom de parcelle où va être récupérées toutes les informations
     * dans la base de données firebase.
     */
    public static final String NOM_PARCELLE_FIREBASE    = "2c8856a9-6d1c-4558-bfaa-8f9de8adbaab";

    /**
     * Champ de la base de données firebase où récupérer le témoin de
     * connexion à la base de données.
     */
    public static final String TEST_CONNECTION_FIREBASE = ".info/connected";

    /**
     * Champ de la base de données firebase "metadata" où se trouvent les champs
     * "constantes" et "essences".
     *
     * @see MainActivity#CHAMP_CONSTANTES
     * @see MainActivity#CHAMP_ESSENCES
     */
    public static final String CHAMP_METADATA           = "metadata";

    /**
     * Champ de la base de données firebase "parcelles" où se trouvent toutes
     * les parcelles enregistrées.
     *
     * C'est dans ce champ que se trouvent les noms de parcelle à choisir pour changer
     * la variable NOM_PARCELLE_FIREBASE.
     *
     * @see MainActivity#NOM_PARCELLE_FIREBASE
     */
    public static final String CHAMP_PARCELLES          = "parcelles";


    /**
     * Champ de la base de données firebase "constantes" où se trouvent les champs
     * "hauteurMoyenne", "prix" et "volume".
     *
     * @see MainActivity#CHAMP_HAUTEUR_MOYENNE
     * @see MainActivity#CHAMP_PRIX
     * @see MainActivity#CHAMP_VOLUME
     */
    public static final String CHAMP_CONSTANTES         = "constantes";

    /**
     * Champ de la base de données firebase "essences"
     */
    public static final String CHAMP_ESSENCES           = "essences";

    /**
     * Champ de la base de données firebase "prelevement"
     */
    public static final String CHAMP_PRELEVEMENT        = "prelevement";

    /**
     * Champ de la base de données firebase "hauteurMoyenne"
     */
    public static final String CHAMP_HAUTEUR_MOYENNE    = "hauteurMoyenne";

    /**
     * Champ de la base de données firebase "prix"
     */
    public static final String CHAMP_PRIX               = "prix";

    /**
     * Champ de la base de données firebase "volume"
     */
    public static final String CHAMP_VOLUME             = "volume";


    /**
     * Champ de la base de données firebase "bois"
     */
    public static final String CHAMP_BOIS               = "bois";

    /**
     * Champ de la base de données firebase "commercial"
     */
    public static final String CHAMP_COMMERCIAL         = "commercial";


    /**
     * Champ de la base de données firebase "chauffage"
     */
    public static final String CHAMP_CHAUFFAGE          = "chauffage";


    /**
     * Champ de la base de données firebase "industrie"
     */
    public static final String CHAMP_INDUSTRIE          = "industrie";

    /**
     * Champ de la base de données firebase "oeuvre"
     */
    public static final String CHAMP_OEUVRE             = "oeuvre";

    /**
     * Champ de la base de données firebase "feuillu"
     */
    public static final String CHAMP_FEUILLU            = "feuillu";

    /**
     * Champ de la base de données firebase "résineux"
     */
    public static final String CHAMP_RESINEUX           = "résineux";

    /**
     * Champ de la base de données firebase "petitBois"
     */
    public static final String CHAMP_PETIT_BOIS         = "petitBois";

    /**
     * Champ de la base de données firebase "epicéa"
     */
    public static final String CHAMP_EPICEA             = "epicéa";

    /**
     * Champ de la base de données firebase "sapin"
     */
    public static final String CHAMP_SAPIN              = "sapin";

    /**
     * Champ de la base de données firebase "max"
     */
    public static final String CHAMP_MAX                = "max";

    /**
     * Champ de la base de données firebase "min"
     */
    public static final String CHAMP_MIN                = "min";

    Spinner spinnerParcelles;
    TextView textTemoin;
    ProgressBar progressBarListe;
    /**
     * Référence de la base de données firebase pour récupérer les données
     */
    private DatabaseReference firebaseDatabase;


    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_parcelle);

        dbHelper = new DatabaseHelper(getApplicationContext());

        // Récupération de la base de données firebase
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        spinnerParcelles = findViewById(R.id.spinner_parcelle_firebase);
        textTemoin = findViewById(R.id.text_temoin);
        progressBarListe = findViewById(R.id.progressBar_liste);

        progressBarListe.setVisibility(View.INVISIBLE);

        /*##########################
         *###  Bouton "Test MAJ" ###
         *##########################
         *
         * Met à jour les données de ARBRES_PARCELLE_TABLE,
         * CONSTANTES_TABLE et TYPE_ARBRE_TABLE depuis une
         * base de données Firebase en testant premièrement
         * s'il y a une connexion à internet.
         */
        Button buttonMajBdd = findViewById(R.id.button_maj_bdd);
        buttonMajBdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinnerParcelles.getSelectedItem() != null) {
                    if (isNetworkAvailable(getApplicationContext())) {
                        miseAJourConstantesTable();
                        miseAJourParcelle(spinnerParcelles.getSelectedItem().toString());
                        textTemoin.setText("Mise a jour de la base de données avec la parcelle : " + spinnerParcelles.getSelectedItem().toString());
                    } else {
                        textTemoin.setText("Pas de connexion internet maj bdd");
                    }
                }
            }
        });

        Button buttonRefreshList = findViewById(R.id.button_refresh_liste);
        buttonRefreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(getApplicationContext())) {
                    progressBarListe.setVisibility(View.VISIBLE);
                    getAllParcelle();
                    textTemoin.setText("");
                } else {
                    textTemoin.setText("Pas de connexion internet get all parcelle");
                }
            }
        });
    }

    public void getAllParcelle(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> listeParcelles = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String parcelle = child.getKey();

                    listeParcelles.add(parcelle);
                }

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, listeParcelles);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

                spinnerParcelles.setAdapter(adapter);

                progressBarListe.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        firebaseDatabase.child(CHAMP_PARCELLES).addListenerForSingleValueEvent(postListener);
    }

    public void miseAJourParcelle(String parcelle){



        /*
         * Listener qui récupère toute la parcelle à partir de la base de données firebase
         * et enregistre tous les arbres de la parcelle dans ARBRES_PARCELLE_TABLE.
         */
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Suppression des données de ARBRES_PARCELLE_TABLE
                dbHelper.clearTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);

                /*
                 *----------------------------------------------------------------
                 * Récupération des informations pour les calculs de volume et de
                 * valeur économique d'un arbre.
                 *----------------------------------------------------------------
                 */

                // Récupération de toutes les constantes nécessaire aux calculs depuis CONSTANTES_TABLE
                HashMap<String, Double> constants = new HashMap<>();
                Cursor cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
                cur.moveToFirst();
                for(int i = 7; i < cur.getColumnCount(); i++){
                    constants.put(cur.getColumnName(i), cur.getDouble(i));
                }


                // Récupération des informations des types de l'arbre selon l'essence depuis TYPE_ARBRE_TABLE
                HashMap<String, String> essence_type = new HashMap<>();
                cur = dbHelper.getAllDataFromTable(DatabaseHelper.TYPE_ARBRE_TABLE);
                while(cur.moveToNext()){
                    essence_type.put(cur.getString(cur.getColumnIndex(DatabaseHelper.ESSENCE_TYPE)), cur.getString(cur.getColumnIndex(DatabaseHelper.TYPE_ARBRE)));
                }

                // Nouvelle instance de VolumeCalculator avec les constantes "constants" et les types de l'arbre selon l'essence "essence_type"
                VolumeCalculator volumeCalculator = new VolumeCalculator(constants, essence_type);


                /*
                 *----------------------------------------------------------------
                 * Récupération des arbres de la parcelle, calculs du volume et de
                 * la valeur économique et insertion dans ARBRES_PARCELLE_TABLE
                 *----------------------------------------------------------------
                 */

                // Récupération de la parcelle de la base de données firebase dans la classe Parcelle
                Parcelle parcelle = dataSnapshot.getValue(Parcelle.class);

                // Test de la récupération de la parcelle
                if(parcelle != null){



                    // Mis à jour des informations de la parcelle dans la table CONSTANTES_TABLE
                    double altitude = Double.parseDouble(parcelle.altitude);
                    String habitat = parcelle.habitat;
                    double surface = Double.parseDouble(parcelle.surface);
                    dbHelper.updateInfosParcelleConstante(altitude, habitat, surface);

                    /*
                     * Récupération des arbres de la parcelle dans une Hashmap.
                     * Les arbres sont de type Arbre.
                     */
                    HashMap<String, Tree> arbres_parcelle = parcelle.arbres;

                    // Test si il y a bien des arbres dans la parcelle
                    if(!arbres_parcelle.isEmpty()){

                        /*
                         * Parcours de la Hashmap<String, Tree> contenant tous les arbres,
                         * calcul leur volume et leur valeur économique et insère toutes
                         * les informations dans ARBRES_PARCELLE_TABLE.
                         */
                        Iterator it = arbres_parcelle.entrySet().iterator();
                        while (it.hasNext()) {
                            HashMap.Entry pair = (HashMap.Entry)it.next();
                            Tree arbre = (Tree) pair.getValue();

                            Double volumeCom = volumeCalculator.getVolumeCommercial(arbre);
                            Double valeurEco = volumeCalculator.getValeurEco(arbre);

                            dbHelper.insertArbreParcelle(
                                    arbre.numero,
                                    arbre.essence,
                                    arbre.diametre,
                                    arbre.noteEcologique,
                                    arbre.etat,
                                    arbre.coord.x,
                                    arbre.coord.y,
                                    arbre.utilisationBois.chauffage,
                                    arbre.utilisationBois.industrie,
                                    arbre.utilisationBois.oeuvre,
                                    volumeCom,
                                    valeurEco);

                            it.remove();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        // On assigne le listener "postListener" à notre parcelle NOM_PARCELLE_FIREBASE de la référence firebaseDatabase
        firebaseDatabase.child(CHAMP_PARCELLES).child(parcelle).addListenerForSingleValueEvent(postListener);
    }

    public void miseAJourConstantesTable(){


        /*
         * Listener qui récupère toutes les constantes à partir de la base de données firebase
         * et les enregistrent dans CONSTANTES_TABLE et TYPE_ARBRE_TABLE.
         */
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Suppression des données de la table CONSTANTES_TABLE et TYPE_ARBRE_TABLE
                dbHelper.clearTable(DatabaseHelper.CONSTANTES_TABLE);
                dbHelper.clearTable(DatabaseHelper.TYPE_ARBRE_TABLE);
                /*
                 *------------------------------
                 * Récupération des constantes et insertion dans CONSTANTES_TABLE
                 *------------------------------
                 */
                DataSnapshot dataConstantes = dataSnapshot.child(CHAMP_CONSTANTES);

                // Récupération des constantes "prelevement"
                DataSnapshot dataPrelevement = dataConstantes.child(CHAMP_PRELEVEMENT);
                Double prelevementMin = dataPrelevement.child(CHAMP_MIN).getValue(Double.class);
                Double prelevementMax = dataPrelevement.child(CHAMP_MAX).getValue(Double.class);

                // Récupération des constantes "hauteurMoyenne"
                DataSnapshot dataHauteurMoyenne = dataConstantes.child(CHAMP_HAUTEUR_MOYENNE);
                Double hauteurMoyenneFeuillu = dataHauteurMoyenne.child(CHAMP_FEUILLU).getValue(Double.class);
                Double hauteurMoyennePetitBois = dataHauteurMoyenne.child(CHAMP_PETIT_BOIS).getValue(Double.class);
                Double hauteurMoyenneResineux = dataHauteurMoyenne.child(CHAMP_RESINEUX).getValue(Double.class);

                // Récupération des constantes "prix"
                DataSnapshot dataPrixBois = dataConstantes.child(CHAMP_PRIX).child(CHAMP_BOIS);
                Double prixBoisChauffageFeuillu = dataPrixBois.child(CHAMP_CHAUFFAGE).child(CHAMP_FEUILLU).getValue(Double.class);
                Double prixBoisChauffageResineux = dataPrixBois.child(CHAMP_CHAUFFAGE).child(CHAMP_RESINEUX).getValue(Double.class);

                Double prixBoisIndustrieFeuillu = dataPrixBois.child(CHAMP_INDUSTRIE).child(CHAMP_FEUILLU).getValue(Double.class);
                Double prixBoisIndustrieResineux = dataPrixBois.child(CHAMP_INDUSTRIE).child(CHAMP_RESINEUX).getValue(Double.class);

                Double prixBoisOeuvreEpicea = dataPrixBois.child(CHAMP_OEUVRE).child(CHAMP_EPICEA).getValue(Double.class);
                Double prixBoisOeuvreFeuillu = dataPrixBois.child(CHAMP_OEUVRE).child(CHAMP_FEUILLU).getValue(Double.class);
                Double prixBoisOeuvreResineux = dataPrixBois.child(CHAMP_OEUVRE).child(CHAMP_RESINEUX).getValue(Double.class);
                Double prixBoisOeuvreSapin = dataPrixBois.child(CHAMP_OEUVRE).child(CHAMP_SAPIN).getValue(Double.class);

                // Récupération des constantes "volume"
                DataSnapshot dataVolumeCommercial = dataConstantes.child(CHAMP_VOLUME).child(CHAMP_COMMERCIAL);
                Double volumeCommercialFeuillu = dataVolumeCommercial.child(CHAMP_FEUILLU).getValue(Double.class);
                Double volumeCommercialResineux = dataVolumeCommercial.child(CHAMP_RESINEUX).getValue(Double.class);

                dbHelper.insertConstante(
                        prelevementMin,
                        prelevementMax,
                        hauteurMoyenneFeuillu,
                        hauteurMoyennePetitBois,
                        hauteurMoyenneResineux,
                        prixBoisChauffageFeuillu,
                        prixBoisChauffageResineux,
                        prixBoisIndustrieFeuillu,
                        prixBoisIndustrieResineux,
                        prixBoisOeuvreEpicea,
                        prixBoisOeuvreFeuillu,
                        prixBoisOeuvreResineux,
                        prixBoisOeuvreSapin,
                        volumeCommercialFeuillu,
                        volumeCommercialResineux);


                /*
                 *------------------------------
                 * Récupération des types d'arbre selon l'essence et insertion dans TYPE_ARBRE_TABLE
                 *------------------------------
                 */
                for( DataSnapshot child : dataSnapshot.child(CHAMP_ESSENCES).getChildren()){
                    String essence = child.getKey();
                    String type = child.getValue(String.class);

                    dbHelper.insertTypeArbre(essence, type);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        // On assigne le listener "postListener" au CHAMP_METADATA de la référence firebaseDatabase
        firebaseDatabase.child(CHAMP_METADATA).addListenerForSingleValueEvent(postListener);
    }

    @Override
    public void onResume(){
        super.onResume();

        if (isNetworkAvailable(getApplicationContext())) {
            progressBarListe.setVisibility(View.VISIBLE);
            getAllParcelle();
            textTemoin.setText("");
        } else {
            textTemoin.setText("Pas de connexion internet get all parcelle");
        }
    }

    public static boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}


