package martelapp.test.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import martelapp.test.Class.Arbre;
import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.Parcelle;
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
     * Constantes des noms des champs de la base de données Firebase
     */
    public static final String CHAMP_METADATA           = "metadata";
    public static final String CHAMP_PARCELLES          = "parcelles";
    public static final String CHAMP_CONSTANTES         = "constantes";
    public static final String CHAMP_EXPLOITATION       = "exploitation";
    public static final String CHAMP_TARIFS             = "tarifs";
    public static final String CHAMP_ESSENCES           = "essences";
    public static final String CHAMP_PRELEVEMENT        = "prelevement";
    public static final String CHAMP_ROTATION           = "rotation";
    public static final String CHAMP_PRIX               = "prix";
    public static final String CHAMP_BOIS               = "bois";
    public static final String CHAMP_CHAUFFAGE          = "chauffage";
    public static final String CHAMP_INDUSTRIE          = "industrie";
    public static final String CHAMP_OEUVRE             = "oeuvre";
    public static final String CHAMP_FEUILLU            = "feuillu";
    public static final String CHAMP_RESINEUX           = "résineux";
    public static final String CHAMP_MAX                = "max";
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

    /**
     * Texte indiquant l'état de mise à jour de la base de données
     */
    TextView textTemoin;


    /**
     * Référence de la base de données firebase pour récupérer les données
     */
    private DatabaseReference firebaseDatabase;

    /**
     * HashMap contenant le nom des parcelles disponibles dans la base de données Firebase
     */
    HashMap<String, String> idNomParcelle;

    /**
     * La base de données SQLITE locale
     */
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
                        miseAJourMetadata();
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
     * On commence par récupérer les ID et les noms de chaque parcelles disponibles dans notre base
     * Firebase.
     *
     * Le spinner contenant la liste des parcelles est alors créé.
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
     * La méthode commence par supprimer toutes les données de la parcelle actuelle.
     *
     * La table est mise à jour à partir de la référence firebaseDatabase
     * dans le CHAMP_PARCELLE et récupère toutes les informations
     * de la parcelle "parcelle" dans une instance de la
     * classe Parcelle.
     *
     * La méthode récupère également toutes les constantes nécessaire
     * pour calculer le volume et la valeur économique d'un
     * arbre à l'aide de VolumeCalculator.
     *
     * Une fois les informations d'un arbre récupérées, elles sont insérées dans la table.
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

                // Suppression des données de la parcelle actuelle
                dbHelper.clearTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);
                dbHelper.clearTable(DatabaseHelper.DIAMETRE_EXPLOIT_TABLE);
                dbHelper.clearTable(DatabaseHelper.TARIF_VOLUME_TABLE);
                dbHelper.clearTable(DatabaseHelper.CONSTANTES_TABLE);
                dbHelper.clearTable(DatabaseHelper.PRIX_BOIS_CHAUFFAGE_TABLE);
                dbHelper.clearTable(DatabaseHelper.PRIX_BOIS_INDUSTRIE_TABLE);
                dbHelper.clearTable(DatabaseHelper.PRIX_BOIS_OEUVRE_TABLE);


                // Récupération de la parcelle de la base de données firebase dans la classe Parcelle
                Parcelle parcelle = dataSnapshot.getValue(Parcelle.class);

                // Test de la récupération de la parcelle
                if(parcelle != null){





                    /*
                     *##############################################################################
                     *################## MISE A JOUR DES CONSTANTES DE LA PARCELLE #################
                     *##############################################################################
                     */

                    // CONSTANTES GENERALES DE LA PARCELLE

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


                    // CONSTANTES PRIX BOIS CHAUFFAGE DE LA PARCELLE

                    DataSnapshot dataPrixBois = dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_PRIX).child(CHAMP_BOIS);

                    for( DataSnapshot child : dataPrixBois.child(CHAMP_CHAUFFAGE).getChildren()){
                        String typeOuEssence = child.getKey();
                        Double prix = child.getValue(Double.class);

                        dbHelper.insertPrixBoisChauffage(typeOuEssence, prix);
                    }



                    // CONSTANTES PRIX BOIS INDUSTRIE DE LA PARCELLE

                    for( DataSnapshot child : dataPrixBois.child(CHAMP_INDUSTRIE).getChildren()){
                        String typeOuEssence = child.getKey();
                        Double prix = child.getValue(Double.class);

                        dbHelper.insertPrixBoisIndustrie(typeOuEssence, prix);
                    }



                    // CONSTANTES PRIX BOIS OEUVRE DE LA PARCELLE

                    for( DataSnapshot child : dataPrixBois.child(CHAMP_OEUVRE).getChildren()){
                        String typeOuEssence = child.getKey();
                        Double prix = child.getValue(Double.class);

                        dbHelper.insertPrixBoisOeuvre(typeOuEssence, prix);
                    }



                    // CONSTANTES DIAMETRE D'EXPLOITATBILITE DE LA PARCELLE

                    for( DataSnapshot child : dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_EXPLOITATION).getChildren()){
                        String essence = child.getKey();
                        Integer diametre = child.getValue(Integer.class);

                        System.out.println("Essence : " + essence + " - diametre exploit : " + Integer.toString(diametre));

                        dbHelper.insertDiametreExploitabilite(essence, diametre);
                    }



                    // CONSTANTES TARIF VOLUME FEUILLU DE LA PARCELLE

                    for( DataSnapshot child : dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_TARIFS).child("feuillus").getChildren()){
                        String nomTarif = child.getKey();
                        Integer versionTarif = child.getValue(Integer.class);

                        System.out.println("Feuillu : nomTarif : " + nomTarif + " - version : " + Integer.toString(versionTarif));

                        dbHelper.insertTarifVolume(CHAMP_FEUILLU, nomTarif, versionTarif);
                    }



                    // CONSTANTES TARIF VOLUME RESINEUX DE LA PARCELLE

                    for( DataSnapshot child : dataSnapshot.child(CHAMP_CONSTANTES).child(CHAMP_TARIFS).child("resineux").getChildren()){
                        String nomTarif = child.getKey();
                        Integer versionTarif = child.getValue(Integer.class);

                        System.out.println("Resineux : nomTarif : " + nomTarif + " - version : " + Integer.toString(versionTarif));

                        dbHelper.insertTarifVolume(CHAMP_RESINEUX, nomTarif, versionTarif);
                    }








                    /*
                     *##############################################################################
                     *######## RECUPERATION DES CONSTANTES POUR CALCUL DU VOLUME ET DU PRIX ########
                     *##############################################################################
                     */

                    // Récupération des types d'arbres en fonction de l'essence
                    HashMap<String, String> essence_type = new HashMap<>();
                    Cursor cur = dbHelper.getAllDataFromTable(DatabaseHelper.TYPE_ARBRE_TABLE);
                    while(cur.moveToNext()){
                        essence_type.put(cur.getString(cur.getColumnIndex(DatabaseHelper.ESSENCE_TYPE)), cur.getString(cur.getColumnIndex(DatabaseHelper.TYPE_ARBRE)));
                    }

                    // Récupération du prix du bois pour l'utilisation "chauffage" en fonction des essences
                    HashMap<String, Double> prixBoisChauffage = new HashMap<>();
                    cur = dbHelper.getAllDataFromTable(DatabaseHelper.PRIX_BOIS_CHAUFFAGE_TABLE);
                    while(cur.moveToNext()){
                        prixBoisChauffage.put(cur.getString(cur.getColumnIndex(DatabaseHelper.TYPE_ESSENCE_ARBRE_CHAUFFAGE)), cur.getDouble(cur.getColumnIndex(DatabaseHelper.PRIX_CHAUFFAGE)));
                    }

                    // Récupération du prix du bois pour l'utilisation "industrie" en fonction des essences
                    HashMap<String, Double> prixBoisIndustrie = new HashMap<>();
                    cur = dbHelper.getAllDataFromTable(DatabaseHelper.PRIX_BOIS_INDUSTRIE_TABLE);
                    while(cur.moveToNext()){
                        prixBoisIndustrie.put(cur.getString(cur.getColumnIndex(DatabaseHelper.TYPE_ESSENCE_ARBRE_INDUSTRIE)), cur.getDouble(cur.getColumnIndex(DatabaseHelper.PRIX_INDUSTRIE)));
                    }

                    // Récupération du prix du bois pour l'utilisation "oeuvre" en fonction des essences
                    HashMap<String, Double> prixBoisOeuvre = new HashMap<>();
                    cur = dbHelper.getAllDataFromTable(DatabaseHelper.PRIX_BOIS_OEUVRE_TABLE);
                    while(cur.moveToNext()){
                        prixBoisOeuvre.put(cur.getString(cur.getColumnIndex(DatabaseHelper.TYPE_ESSENCE_ARBRE_OEUVRE)), cur.getDouble(cur.getColumnIndex(DatabaseHelper.PRIX_OEUVRE)));
                    }

                    // Récupération du nom et de la version du tarif pour le type "resineux"
                    cur = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.TARIF_VOLUME_TABLE,
                            DatabaseHelper.TYPE_ARBRE_TARIF + " = '" + CHAMP_RESINEUX + "'");
                    cur.moveToFirst();
                    String nomTarifResineux = cur.getString(cur.getColumnIndex(DatabaseHelper.NOM_TARIF));
                    int versionTarifResineux = cur.getInt(cur.getColumnIndex(DatabaseHelper.VERSION_TARIF));

                    // Récupération du nom et de la version du tarif pour le type "feuillu"
                    cur = dbHelper.getAllDataFromTableWithCondition(DatabaseHelper.TARIF_VOLUME_TABLE,
                            DatabaseHelper.TYPE_ARBRE_TARIF + " = '" + CHAMP_FEUILLU + "'");
                    cur.moveToFirst();
                    String nomTarifFeuillus = cur.getString(cur.getColumnIndex(DatabaseHelper.NOM_TARIF));
                    int versionTarifFeuillus = cur.getInt(cur.getColumnIndex(DatabaseHelper.VERSION_TARIF));

                    cur.close();

                    // Création d'une instance VolumeCalculator avec toutes les constantes récupérées dans des HashMap
                    VolumeCalculator volumeCalculator = new VolumeCalculator(prixBoisChauffage, prixBoisIndustrie, prixBoisOeuvre, essence_type, nomTarifResineux, versionTarifResineux, nomTarifFeuillus, versionTarifFeuillus);

                    /*
                     * Récupération des arbres de la parcelle dans une Hashmap.
                     * Les arbres sont de type Arbre.
                     */
                    HashMap<String, Arbre> arbres_parcelle = parcelle.arbres;

                    // Test si il y a bien des arbres dans la parcelle
                    if(!arbres_parcelle.isEmpty()){

                        /*
                         * Parcours de la Hashmap<String, Arbre> contenant tous les arbres,
                         * calcul leur volume et leur valeur économique et insère toutes
                         * les informations dans ARBRES_PARCELLE_TABLE.
                         */
                        Iterator it = arbres_parcelle.entrySet().iterator();
                        while (it.hasNext()) {
                            HashMap.Entry pair = (HashMap.Entry)it.next();
                            Arbre arbre = (Arbre) pair.getValue();

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
     * Met à jour les metadata avec la base de données firebase.
     *
     * La table est mise à jour à partir de la référence firebaseDatabase
     * dans le CHAMP_METADATA et récupère le type des arbres selon l'essence du CHAMP_ESSENCES.
     * Les informations sont ensuite insérées dans TYPE_ARBRE_TABLE.
     *
     *
     */
    public void miseAJourMetadata(){


        /*
         * Listener qui récupère toutes les metadata à partir de la base de données firebase
         * et les enregistrent dans TYPE_ARBRE_TABLE.
         */
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dbHelper = new DatabaseHelper(getApplicationContext());

                // Suppression des données de la table TYPE_ARBRE_TABLE
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


