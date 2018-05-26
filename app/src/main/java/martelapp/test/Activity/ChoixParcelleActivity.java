package martelapp.test.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

/**
 * ChoixParcelleActivity est l'activité permettant à l'utilisateur de changer la parcelle
 * sur laquelle il souhaite réaliser l'exercice.
 *
 * Dans cette activité est récupérée la base de données firebase de la parcelle
 * pour l'enregistrer localement dans l'appareil avec SQLITE.
 *
 */
public class ChoixParcelleActivity extends AppCompatActivity {

    /**
     * Champ de la base de données firebase "metadata" où se trouvent les champs
     * "constantes" et "essences".
     */
    public static final String CHAMP_METADATA           = "metadata";

    /**
     * Champ de la base de données firebase "parcelles" où se trouvent toutes
     * les parcelles enregistrées.
     *
     * C'est dans ce champ que se trouvent les noms de parcelle à choisir pour changer
     * la variable NOM_PARCELLE_FIREBASE.
     */
    public static final String CHAMP_PARCELLES          = "parcelles";


    /**
     * Champ de la base de données firebase "constantes" où se trouvent les champs
     * "hauteurMoyenne", "prix" et "volume".
     *
     */
    public static final String CHAMP_CONSTANTES         = "constantes";

    /**
     * Champ de la base de données firebase "exploitation"
     */
    public static final String CHAMP_EXPLOITATION       = "exploitation";

    /**
     * Champ de la base de données firebase "tarifs"
     */
    public static final String CHAMP_TARIFS             = "tarifs";

    /**
     * Champ de la base de données firebase "essences"
     */
    public static final String CHAMP_ESSENCES           = "essences";

    /**
     * Champ de la base de données firebase "prelevement"
     */
    public static final String CHAMP_PRELEVEMENT        = "prelevement";

    /**
     * Champ de la base de données firebase "rotation"
     */
    public static final String CHAMP_ROTATION        = "rotation";

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

    /**
     * Liste des parcelles disponibles
     */
    Spinner spinnerParcelles;

    /**
     * ProgressBar qui s'affiche pendant l'actualisation de la liste des parcelles
     */
    ProgressBar progressBarListe;

    /**
     * Bouton pour mettre la base de donnée à jour
     */
    Button buttonMajBdd;

    /**
     * Bouton pour actualiser la liste des parcelles
     */
    ImageButton buttonRefreshList;

    /**
     * Bouton pour retourner dans MainActivity
     */
    Button buttonRetour;

    TextView textTemoin;


    /**
     * Référence de la base de données firebase pour récupérer les données
     */
    private DatabaseReference firebaseDatabase;

    // HashMap contenant le nom des parcelles disponibles dans la base de données Firebase
    HashMap<String, String> idNomParcelle;

    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_parcelle);


        // Récupération de la base de données firebase
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        spinnerParcelles = findViewById(R.id.spinner_parcelle_firebase);
        textTemoin = findViewById(R.id.text_temoin);
        progressBarListe = findViewById(R.id.progressBar_liste);

        // La progressBar n'est affichée que lorsqu'on actualise la liste des parcelles
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
        buttonMajBdd = findViewById(R.id.button_maj_bdd);
        buttonMajBdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinnerParcelles.getSelectedItem() != null) {
                    if (DatabaseHelper.isNetworkAvailable(getApplicationContext())) {
                        textTemoin.setText(R.string.maj_en_cours);
                        miseAJourConstantesTable();
                        miseAJourParcelle(idNomParcelle.get(spinnerParcelles.getSelectedItem().toString()));
                    } else {
                        textTemoin.setText(R.string.pas_de_co_maj);
                    }
                }
            }
        });


        /*##########################
         *##  Bouton "Rafraîchir" ##
         *##########################
         *
         * Met à jour la liste des parcelles disponnible depuis une
         * base de données Firebase en testant premièrement
         * s'il y a une connexion à internet.
         */
        buttonRefreshList = findViewById(R.id.button_refresh_liste);
        buttonRefreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DatabaseHelper.isNetworkAvailable(getApplicationContext())) {
                    progressBarListe.setVisibility(View.VISIBLE);
                    getAllParcelle();
                    textTemoin.setText("");
                } else {
                    textTemoin.setText(R.string.pas_de_co);
                }
            }
        });

        /*########################
         *###  Bouton "Retour" ###
         *########################
         *
         * Permet de retourner à MainActivity
         */
        buttonRetour = findViewById(R.id.button_retour);
        buttonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    /**
     * Récupère la liste des noms des parcelles présentes dans la base de données Firebase.
     *
     * <p>
     * On commence par récupérer les ID et les noms de chaque parcelles disponibles dans notre base Firebase
     * </p>
     *
     * <p>
     * Le spinner contenant la liste des parcelles est alors créé
     * </p>
     */
    public void getAllParcelle(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> listeParcelle = new ArrayList<>();
                idNomParcelle = new HashMap<>();

                // On récupère pour chaque parcelle son ID et son nom et on les ajoute à l'ArrayList liste des parcelles
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String idParcelle = child.getKey();
                    String nomParcelle = child.child("nom").getValue(String.class);

                    idNomParcelle.put(nomParcelle, idParcelle);
                    listeParcelle.add(nomParcelle);
                }

                // On crée notre spinner contenant la liste des parcelles disponibles
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, listeParcelle);
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



    /**
     * Met à jour ARBRES_PARCELLE_TABLE avec la base de données firebase.
     *
     * <p>
     * La méthode commence par supprimer toutes les données que contient ARBRES_PARCELLE_TABLE.
     * </p>
     *
     * <p>
     * La table est mise à jour à partir de la référence firebaseDatabase
     * dans le CHAMP_PARCELLE et récupère toutes les informations
     * de la parcelle NOM_PARCELLE_FIREBASE dans une instance de la
     * classe Parcelle.
     * </p>
     *
     * <p>
     * La méthode récupère également des informations depuis CONSTANTES_TABLE
     * et TYPE_ARBRE_TABLE pour calculer le volume et la valeur économique d'un
     * arbre à l'aide de VolumeCalculator.
     * </p>
     *
     * <p>
     * Une fois les informations d'un arbre récupérées, elles sont insérées dans la table.
     * </p>
     *
     * @see DatabaseHelper#ARBRES_PARCELLE_TABLE
     * @see DatabaseHelper#CONSTANTES_TABLE
     * @see DatabaseHelper#TYPE_ARBRE_TABLE
     * @see DatabaseHelper#clearTable(String)
     * @see DatabaseHelper#insertArbreParcelle(String, String, int, int, String , double, double, double, double, double,,double, double)
     *
     * @see ChoixParcelleActivity#firebaseDatabase
     * @see ChoixParcelleActivity#CHAMP_PARCELLES
     *
     * @see Parcelle
     *
     * @see VolumeCalculator
     *
     */
    public void miseAJourParcelle(String parcelle){



        /*
         * Listener qui récupère toute la parcelle à partir de la base de données firebase
         * et enregistre tous les arbres de la parcelle dans ARBRES_PARCELLE_TABLE.
         */
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dbHelper = new DatabaseHelper(getApplicationContext());

                // Suppression des données de ARBRES_PARCELLE_TABLE
                dbHelper.clearTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);
                dbHelper.clearTable(DatabaseHelper.DIAMETRE_EXPLOIT_TABLE);
                dbHelper.clearTable(DatabaseHelper.TARIF_VOLUME_TABLE);
                dbHelper.clearTable(DatabaseHelper.CONSTANTES_TABLE);
                dbHelper.clearTable(DatabaseHelper.PRIX_BOIS_CHAUFFAGE_TABLE);
                dbHelper.clearTable(DatabaseHelper.PRIX_BOIS_INDUSTRIE_TABLE);
                dbHelper.clearTable(DatabaseHelper.PRIX_BOIS_OEUVRE_TABLE);


                // Nouvelle instance de VolumeCalculator avec les constantes "constants" et les types de l'arbre selon l'essence "essence_type"
                //VolumeCalculator volumeCalculator = new VolumeCalculator(constants, essence_type);


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
                    double altitude = parcelle.altitude;
                    String habitat = parcelle.habitat;
                    double surface = parcelle.surface;
                    String lieu = parcelle.lieu;
                    String nom = parcelle.nom;

                    double prelevementMin = dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_PRELEVEMENT).child(CHAMP_MIN).getValue(Double.class);
                    double prelevementMax = dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_PRELEVEMENT).child(CHAMP_MAX).getValue(Double.class);

                    int rotationMin = dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_ROTATION).child(CHAMP_MIN).getValue(Integer.class);
                    int rotationMax = dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_ROTATION).child(CHAMP_MAX).getValue(Integer.class);

                    dbHelper.insertConstante(nom, lieu, altitude, habitat, surface, prelevementMin, prelevementMax, rotationMin, rotationMax);


                    DataSnapshot dataPrixBois = dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_PRIX).child(CHAMP_BOIS);

                    for( DataSnapshot child : dataPrixBois.child(CHAMP_CHAUFFAGE).getChildren()){
                        String typeOuEssence = child.getKey();
                        Double prix = child.getValue(Double.class);

                        dbHelper.insertPrixBoisChauffage(typeOuEssence, prix);
                    }

                    for( DataSnapshot child : dataPrixBois.child(CHAMP_INDUSTRIE).getChildren()){
                        String typeOuEssence = child.getKey();
                        Double prix = child.getValue(Double.class);

                        dbHelper.insertPrixBoisIndustrie(typeOuEssence, prix);
                    }

                    for( DataSnapshot child : dataPrixBois.child(CHAMP_OEUVRE).getChildren()){
                        String typeOuEssence = child.getKey();
                        Double prix = child.getValue(Double.class);

                        dbHelper.insertPrixBoisOeuvre(typeOuEssence, prix);
                    }



                    for( DataSnapshot child : dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_EXPLOITATION).getChildren()){
                        String essence = child.getKey();
                        Integer diametre = child.getValue(Integer.class);

                        System.out.println("Essence : " + essence + " - diametre exploit : " + Integer.toString(diametre));

                        dbHelper.insertDiametreExploitabilite(essence, diametre);
                    }

                    for( DataSnapshot child : dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_TARIFS).child("feuillus").getChildren()){
                        String nomTarif = child.getKey();
                        Integer versionTarif = child.getValue(Integer.class);

                        System.out.println("Feuillu : nomTarif : " + nomTarif + " - version : " + Integer.toString(versionTarif));

                        dbHelper.insertTarifVolume(CHAMP_FEUILLU, nomTarif, versionTarif);
                    }


                    for( DataSnapshot child : dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_TARIFS).child("resineux").getChildren()){
                        String nomTarif = child.getKey();
                        Integer versionTarif = child.getValue(Integer.class);

                        System.out.println("Resineux : nomTarif : " + nomTarif + " - version : " + Integer.toString(versionTarif));

                        dbHelper.insertTarifVolume(CHAMP_RESINEUX, nomTarif, versionTarif);
                    }



                    // Récupération des informations des types de l'arbre selon l'essence depuis TYPE_ARBRE_TABLE
                    HashMap<String, String> essence_type = new HashMap<>();
                    Cursor cur = dbHelper.getAllDataFromTable(DatabaseHelper.TYPE_ARBRE_TABLE);
                    while(cur.moveToNext()){
                        essence_type.put(cur.getString(cur.getColumnIndex(DatabaseHelper.ESSENCE_TYPE)), cur.getString(cur.getColumnIndex(DatabaseHelper.TYPE_ARBRE)));
                    }

                    HashMap<String, Double> prixBoisChauffage = new HashMap<>();
                    cur = dbHelper.getAllDataFromTable(DatabaseHelper.PRIX_BOIS_CHAUFFAGE_TABLE);
                    while(cur.moveToNext()){
                        prixBoisChauffage.put(cur.getString(cur.getColumnIndex(DatabaseHelper.TYPE_ESSENCE_ARBRE_CHAUFFAGE)), cur.getDouble(cur.getColumnIndex(DatabaseHelper.PRIX_CHAUFFAGE)));
                    }

                    HashMap<String, Double> prixBoisIndustrie = new HashMap<>();
                    cur = dbHelper.getAllDataFromTable(DatabaseHelper.PRIX_BOIS_INDUSTRIE_TABLE);
                    while(cur.moveToNext()){
                        prixBoisIndustrie.put(cur.getString(cur.getColumnIndex(DatabaseHelper.TYPE_ESSENCE_ARBRE_INDUSTRIE)), cur.getDouble(cur.getColumnIndex(DatabaseHelper.PRIX_INDUSTRIE)));
                    }

                    HashMap<String, Double> prixBoisOeuvre = new HashMap<>();
                    cur = dbHelper.getAllDataFromTable(DatabaseHelper.PRIX_BOIS_OEUVRE_TABLE);
                    while(cur.moveToNext()){
                        prixBoisOeuvre.put(cur.getString(cur.getColumnIndex(DatabaseHelper.TYPE_ESSENCE_ARBRE_OEUVRE)), cur.getDouble(cur.getColumnIndex(DatabaseHelper.PRIX_OEUVRE)));
                    }


                    cur = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.TARIF_VOLUME_TABLE,
                            DatabaseHelper.TYPE_ARBRE_TARIF + " = '" + CHAMP_RESINEUX + "'");
                    cur.moveToFirst();
                    String nomTarifResineux = cur.getString(cur.getColumnIndex(DatabaseHelper.NOM_TARIF));
                    int versionTarifResineux = cur.getInt(cur.getColumnIndex(DatabaseHelper.VERSION_TARIF));


                    cur = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.TARIF_VOLUME_TABLE,
                            DatabaseHelper.TYPE_ARBRE_TARIF + " = '" + CHAMP_FEUILLU + "'");
                    cur.moveToFirst();
                    String nomTarifFeuillus = cur.getString(cur.getColumnIndex(DatabaseHelper.NOM_TARIF));
                    int versionTarifFeuillus = cur.getInt(cur.getColumnIndex(DatabaseHelper.VERSION_TARIF));


                    cur.close();

                    VolumeCalculator volumeCalculator = new VolumeCalculator(prixBoisChauffage, prixBoisIndustrie, prixBoisOeuvre, essence_type, nomTarifResineux, versionTarifResineux, nomTarifFeuillus, versionTarifFeuillus);

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

                            System.out.println("Arbre n°" + arbre.getNumero() + " Volume : " + Double.toString(volumeCom) + " - Valeur Eco : " + Double.toString(valeurEco));

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

                        textTemoin.setText("La base de données de l'application a été mise à jour avec la parcelle : " + spinnerParcelles.getSelectedItem().toString());
                    }
                }
                dbHelper.close();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        // On assigne le listener "postListener" à notre parcelle NOM_PARCELLE_FIREBASE de la référence firebaseDatabase
        firebaseDatabase.child(CHAMP_PARCELLES).child(parcelle).addListenerForSingleValueEvent(postListener);
    }

    /**
     * Met à jour CONSTANTES_TABLE et TYPE_ARBRE_TABLE avec la base de données firebase.
     *
     * <p>
     * La méthode commence par supprimer toutes les données que contient CONSTANTES_TABLE et TYPE_ARBRE_TABLE.
     * </p>
     *
     * <p>
     * La table est mise à jour à partir de la référence firebaseDatabase
     * dans le CHAMP_METADATA et récupère toutes les constantes du CHAMP_CONSTANTES
     * et le type des arbres selon l'essence du CHAMP_ESSENCES.
     * </p>
     *
     * <p>
     * Les informations sont ensuite insérées dans CONSTANTES_TABLE et TYPE_ARBRE_TABLE.
     * </p>
     *
     * @see DatabaseHelper#CONSTANTES_TABLE
     * @see DatabaseHelper#TYPE_ARBRE_TABLE
     * @see DatabaseHelper#clearTable(String)
     * @see DatabaseHelper#insertTypeArbre(String, String)
     *
     */
    public void miseAJourConstantesTable(){


        /*
         * Listener qui récupère toutes les constantes à partir de la base de données firebase
         * et les enregistrent dans CONSTANTES_TABLE et TYPE_ARBRE_TABLE.
         */
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dbHelper = new DatabaseHelper(getApplicationContext());

                // Suppression des données de la table CONSTANTES_TABLE et TYPE_ARBRE_TABLE
                dbHelper.clearTable(DatabaseHelper.TYPE_ARBRE_TABLE);


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

                dbHelper.close();
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

        if (DatabaseHelper.isNetworkAvailable(getApplicationContext())) {
            progressBarListe.setVisibility(View.VISIBLE);
            getAllParcelle();
            textTemoin.setText("");
        } else {
            textTemoin.setText(R.string.pas_de_co);
        }
    }
}


