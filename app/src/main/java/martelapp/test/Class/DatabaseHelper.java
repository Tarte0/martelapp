package martelapp.test.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Baptiste on 11/03/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper{


    public static final String DATABASE_NAME = "martelapp.db";
    public static final int DATABASE_VERSION = 4;

    /*
     * Table "arbres_parcelle"
     *
     * Utilisée pour stocker tous les arbres d'une parcelle
     */
    public static final String ARBRES_PARCELLE_TABLE    = "arbres_parcelle_table";
    public static final String ID_ARBRE_PARC            = "_id";
    public static final String NUMERO_ARBRE_PARC        = "NUMERO";
    public static final String ESSENCE_ARBRE            = "ESSENCE";
    public static final String DIAMETRE_ARBRE           = "DIAMETRE";
    public static final String NOTE_ECO_ARBRE           = "NOTE_ECOLOGIQUE";
    public static final String ETAT_ARBRE               = "ETAT";
    public static final String COORD_X_ARBRE            = "COORDONNEE_X";
    public static final String COORD_Y_ARBRE            = "COORDONNEE_Y";
    public static final String UTIL_BOIS_CHAUFFAGE      = "UTILISATION_BOIS_CHAUFFAGE";
    public static final String UTIL_BOIS_INDUSTRIE      = "UTILISATION_BOIS_INDUSTRIE";
    public static final String UTIL_BOIS_OEUVRE         = "UTILISATION_BOIS_OEUVRE";
    public static final String VOLUME_COMMERCIAL        = "VOLUME_COMMERCIAL";
    public static final String VALEUR_ECONOMIQUE        = "VALEUR_ECONOMIQUE";

    /*
     * Table "arbres_marteles"
     *
     * Utilisée pour stocker tous les arbres martelés d'un exercice
     */
    public static final String ARBRES_MARTELES_TABLE    = "arbres_marteles_table";
    public static final String ID_ARBRE_MART            = "_id";
    public static final String NUMERO_ARBRE_MART        = "NUMERO";

    /*
     * Table "arbres_conserves"
     *
     * Utilisée pour stocker tous les arbres conservés d'un exercice
     */
    public static final String ARBRES_CONSERVES_TABLE   = "arbres_conserves_table";
    public static final String ID_ARBRE_CONS            = "_id";
    public static final String NUMERO_ARBRE_CONS        = "NUMERO";

    /*
     * Table "raison"
     *
     * Utilisée pour stocker les raisons d'un martelage ou d'une conservation d'un arbre
     */
    public static final String RAISON_TABLE                 = "raison_table";
    public static final String ID_RAISON                    = "_id";
    public static final String NUMERO_ARBRE_TRAITE_RAISON   = "NUMERO_ARBRE_TRAITE_RAISON";
    public static final String RAISON                       = "RAISON";

    /*
     * Table "constantes"
     *
     * Utilisée pour stocker les constantes pour un exercice
     */
    public static final String CONSTANTES_TABLE             = "constantes_table";
    public static final String ID_CONSTANTE                 = "_id";
    public static final String NOM_EQUIPE                   = "NOM_EQUIPE";
    public static final String NOM_PARCELLE                 = "NOM_PARCELLE";
    public static final String LIEU_PARCELLE                = "LIEU_PARCELLE";
    public static final String SURFACE_PARCELLE             = "SURFACE_PARCELLE";
    public static final String ALTITUDE_PARCELLE            = "ALTITUDE_PARCELLE";
    public static final String HABITAT_PARCELLE             = "HABITAT_PARCELLE";
    public static final String PRELEVEMENT_VOLUME_MIN       = "PRELEVEMENT_VOLUME_MIN";
    public static final String PRELEVEMENT_VOLUME_MAX       = "PRELEVEMENT_VOLUME_MAX";
    public static final String ROTATION_MIN                 = "ROTATION_MIN";
    public static final String ROTATION_MAX                 = "ROTATION_MAX";

    /*
     * Table "prix_bois_chauffage"
     *
     * Utilisée pour stocker le prix du bois pour l'utilisation "Chauffage" en fonction du type
     * ou de l'essence d'un arbre
     */
    public static final String PRIX_BOIS_CHAUFFAGE_TABLE    = "prix_bois_chauffage_table";
    public static final String ID_PRIX_BOIS_CHAUFFAGE       = "_id";
    public static final String TYPE_ESSENCE_ARBRE_CHAUFFAGE = "TYPE_ESSENCE_ARBRE_CHAUFFAGE";
    public static final String PRIX_CHAUFFAGE               = "PRIX_CHAUFFAGE";

    /*
     * Table "prix_bois_industrie"
     *
     * Utilisée pour stocker le prix du bois pour l'utilisation "Industrie" en fonction du type
     * ou de l'essence d'un arbre
     */
    public static final String PRIX_BOIS_INDUSTRIE_TABLE    = "prix_bois_industrie_table";
    public static final String ID_PRIX_BOIS_INDUSTRIE       = "_id";
    public static final String TYPE_ESSENCE_ARBRE_INDUSTRIE = "TYPE_ESSENCE_ARBRE_INDUSTRIE";
    public static final String PRIX_INDUSTRIE               = "PRIX_INDUSTRIE";

    /*
     * Table "prix_bois_oeuvre"
     *
     * Utilisée pour stocker le prix du bois pour l'utilisation "Oeuvre" en fonction du type
     * ou de l'essence d'un arbre
     */

    public static final String PRIX_BOIS_OEUVRE_TABLE       = "prix_bois_oeuvre_table";
    public static final String ID_PRIX_BOIS_OEUVRE          = "_id";
    public static final String TYPE_ESSENCE_ARBRE_OEUVRE    = "TYPE_ESSENCE_ARBRE_OEUVRE";
    public static final String PRIX_OEUVRE                  = "PRIX_OEUVRE";

    /*
     * Table "tarif_volume"
     *
     * Utilisée pour stocker le nom et la version du tarif pour un type d'arbre donné
     */
    public static final String TARIF_VOLUME_TABLE       = "tarif_volume_table";
    public static final String ID_TARIF                 = "_id";
    public static final String TYPE_ARBRE_TARIF         = "TYPE_ARBRE";
    public static final String NOM_TARIF                = "NOM_TARIF";
    public static final String VERSION_TARIF            = "VERSION_TARIF";


    /*
     * Table "type_arbre"
     *
     * Utilisée pour associer un type d'arbre à chaque essence
     */
    public static final String TYPE_ARBRE_TABLE         = "type_arbre_table";
    public static final String ID_TYPE                  = "_id";
    public static final String ESSENCE_TYPE             = "ESSENCE_TYPE";
    public static final String TYPE_ARBRE               = "TYPE_ARBRE";

    /*
     * Table "diametre_exploit"
     *
     * Utilisée pour associer un diamètre d'exploitabilité à chaque essence
     */
    public static final String DIAMETRE_EXPLOIT_TABLE   = "diametre_exploitabilite_table";
    public static final String ID_DIAMETRE_EXPLOIT      = "_id";
    public static final String ESSENCE_DIAM_EXPLOIT     = "ESSENCE_DIAM_EXPLOIT";
    public static final String DIAMETRE_EXPLOITABILITE  = "DIAMETRE_EXPLOITABILITE";


    // Constantes des raisons de martelage et de conservation
    public static final String ARBRE_MUR        = "Arbre Mûr";
    public static final String ECLAIRCIE        = "Eclaircie";
    public static final String SANITAIRE        = "Sanitaire";
    public static final String REGENERATION     = "Régénération";
    public static final String EXPLOITATION     = "Exploitation";
    public static final String BIODIVERSITE     = "Biodiversité";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*
         *  Création de la table "arbres_parcelle"
         *  PRIMARY KEY : ID_ARBRE_PARC
         */
        db.execSQL("CREATE TABLE " + ARBRES_PARCELLE_TABLE + "("
                + ID_ARBRE_PARC         + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NUMERO_ARBRE_PARC     + " TEXT, "
                + ESSENCE_ARBRE         + " TEXT, "
                + DIAMETRE_ARBRE        + " INTEGER, "
                + NOTE_ECO_ARBRE        + " INTEGER, "
                + ETAT_ARBRE            + " TEXT, "
                + COORD_X_ARBRE         + " REAL, "
                + COORD_Y_ARBRE         + " REAL, "
                + UTIL_BOIS_CHAUFFAGE   + " REAL, "
                + UTIL_BOIS_INDUSTRIE   + " REAL, "
                + UTIL_BOIS_OEUVRE      + " REAL, "
                + VOLUME_COMMERCIAL     + " REAL, "
                + VALEUR_ECONOMIQUE     + " REAL, "
                + "CONSTRAINT numero_arbre_parc_constraint UNIQUE (" + NUMERO_ARBRE_PARC + "))"
        );


        /*
         *  Création de la table "arbres_marteles"
         *  PRIMARY KEY : ID_ARBRE_MART
         *  FOREIGN KEY : NUMERO_ARBRE_MART sur le NUMERO_ARBRE_PARC de arbres_parcelle
         */
        db.execSQL("CREATE TABLE " + ARBRES_MARTELES_TABLE + "("
                + ID_ARBRE_MART     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NUMERO_ARBRE_MART + " TEXT, "
                + "FOREIGN KEY(" + NUMERO_ARBRE_MART + ") REFERENCES " + ARBRES_PARCELLE_TABLE + "(" + NUMERO_ARBRE_PARC + "))"
        );


        /*
         *  Création de la table "arbres_conserves"
         *  PRIMARY KEY : ID_ARBRE_CONS
         *  FOREIGN KEY : NUMERO_ARBRE_CONS sur le NUMERO_ARBRE_PARC de arbres_parcelle
         */
        db.execSQL("CREATE TABLE " + ARBRES_CONSERVES_TABLE + "("
                + ID_ARBRE_CONS     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NUMERO_ARBRE_CONS + " TEXT, "
                + "FOREIGN KEY(" + NUMERO_ARBRE_CONS + ") REFERENCES " + ARBRES_PARCELLE_TABLE + "(" + NUMERO_ARBRE_PARC + "))"
        );


        /*
         *  Création de la table "raison"
         *  PRIMARY KEY : ID_RAISON
         *  FOREIGN KEY : NUMERO_ARBRE_MARTELE_RAISON sur le NUMERO_ARBRE_MART de arbres_marteles_table
         */
        db.execSQL("CREATE TABLE " + RAISON_TABLE + "("
                + ID_RAISON                     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NUMERO_ARBRE_TRAITE_RAISON    + " TEXT, "
                + RAISON                        + " TEXT, "
                + "FOREIGN KEY(" + NUMERO_ARBRE_TRAITE_RAISON + ") REFERENCES " + ARBRES_MARTELES_TABLE + "(" + NUMERO_ARBRE_MART + "))"
        );


        /*
         *  Création de la table "constantes"
         *  PRIMARY KEY : ID_CONSTANTE
         */
        db.execSQL("CREATE TABLE " + CONSTANTES_TABLE + "("
                + ID_CONSTANTE                  + " INTEGER PRIMARY KEY, "
                + NOM_EQUIPE                    + " TEXT, "
                + NOM_PARCELLE                  + " TEXT, "
                + LIEU_PARCELLE                 + " TEXT, "
                + SURFACE_PARCELLE              + " REAL, "
                + ALTITUDE_PARCELLE             + " REAL, "
                + HABITAT_PARCELLE              + " TEXT, "
                + PRELEVEMENT_VOLUME_MIN        + " REAL, "
                + PRELEVEMENT_VOLUME_MAX        + " REAL, "
                + ROTATION_MIN                  + " INTEGER, "
                + ROTATION_MAX                  + " INTEGER)"
        );

        /*
         *  Création de la table "tarif_volume"
         *  PRIMARY KEY : ID_TARIF
         *  FOREIGN KEY : TYPE_ARBRE_TARIF sur TYPE_ARBRE de la table type_arbre_table
         */
        db.execSQL("CREATE TABLE " + TARIF_VOLUME_TABLE + "("
                + ID_TARIF          + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TYPE_ARBRE_TARIF  + " TEXT, "
                + NOM_TARIF         + " TEXT, "
                + VERSION_TARIF     + " INTEGER, "
                + "FOREIGN KEY(" + TYPE_ARBRE_TARIF + ") REFERENCES " + TYPE_ARBRE_TABLE + "(" + TYPE_ARBRE + "))"
        );

        /*
         *  Création de la table "prix_bois_chauffage"
         *  PRIMARY KEY : ID_PRIX_BOIS_CHAUFFAGE
         *  FOREIGN KEY : TYPE_ESSENCE_ARBRE_CHAUFFAGE sur TYPE_ARBRE de la table type_arbre_table
         */
        db.execSQL("CREATE TABLE " + PRIX_BOIS_CHAUFFAGE_TABLE + "("
                + ID_PRIX_BOIS_CHAUFFAGE        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TYPE_ESSENCE_ARBRE_CHAUFFAGE  + " TEXT, "
                + PRIX_CHAUFFAGE                + " REAL, "
                + "FOREIGN KEY(" + TYPE_ESSENCE_ARBRE_CHAUFFAGE + ") REFERENCES " + TYPE_ARBRE_TABLE + "(" + TYPE_ARBRE + "))"
        );

        /*
         *  Création de la table "prix_bois_industrie"
         *  PRIMARY KEY : ID_PRIX_BOIS_INDUSTRIE
         *  FOREIGN KEY : TYPE_ESSENCE_ARBRE_INDUSTRIE sur TYPE_ARBRE de la table type_arbre_table
         */
        db.execSQL("CREATE TABLE " + PRIX_BOIS_INDUSTRIE_TABLE + "("
                + ID_PRIX_BOIS_INDUSTRIE        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TYPE_ESSENCE_ARBRE_INDUSTRIE  + " TEXT, "
                + PRIX_INDUSTRIE                + " REAL, "
                + "FOREIGN KEY(" + TYPE_ESSENCE_ARBRE_INDUSTRIE + ") REFERENCES " + TYPE_ARBRE_TABLE + "(" + TYPE_ARBRE + "))"
        );

        /*
         *  Création de la table "prix_bois_oeuvre"
         *  PRIMARY KEY : ID_PRIX_BOIS_OEUVRE
         *  FOREIGN KEY : TYPE_ESSENCE_ARBRE_OEUVRE sur TYPE_ARBRE de la table type_arbre_table
         */
        db.execSQL("CREATE TABLE " + PRIX_BOIS_OEUVRE_TABLE + "("
                + ID_PRIX_BOIS_OEUVRE        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TYPE_ESSENCE_ARBRE_OEUVRE  + " TEXT, "
                + PRIX_OEUVRE                + " REAL, "
                + "FOREIGN KEY(" + TYPE_ESSENCE_ARBRE_OEUVRE + ") REFERENCES " + TYPE_ARBRE_TABLE + "(" + TYPE_ARBRE + "))"
        );

        /*
         *  Création de la table "type_arbre"
         *  PRIMARY KEY : ID_TYPE
         *  FOREIGN KEY : ESSENCE_TYPE sur ESSENCE_ARBRE de la table arbres_parcelle
         */
        db.execSQL("CREATE TABLE " + TYPE_ARBRE_TABLE + "("
                + ID_TYPE           + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ESSENCE_TYPE      + " TEXT, "
                + TYPE_ARBRE        + " TEXT, "
                + "FOREIGN KEY(" + ESSENCE_TYPE + ") REFERENCES " + ARBRES_PARCELLE_TABLE + "(" + ESSENCE_ARBRE + "))"
        );

        /*
         *  Création de la table "diametre_exploitabilite"
         *  PRIMARY KEY : ID_TYPE
         *  FOREIGN KEY : ESSENCE_DIAM_EXPLOIT sur ESSENCE_ARBRE de la table arbres_parcelle
         */
        db.execSQL("CREATE TABLE " + DIAMETRE_EXPLOIT_TABLE + "("
                + ID_DIAMETRE_EXPLOIT       + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ESSENCE_DIAM_EXPLOIT      + " TEXT, "
                + DIAMETRE_EXPLOITABILITE   + " INTEGER, "
                + "FOREIGN KEY(" + ESSENCE_DIAM_EXPLOIT + ") REFERENCES " + ARBRES_PARCELLE_TABLE + "(" + ESSENCE_ARBRE + "))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ARBRES_PARCELLE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ARBRES_MARTELES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ARBRES_CONSERVES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + RAISON_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CONSTANTES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TARIF_VOLUME_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PRIX_BOIS_CHAUFFAGE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PRIX_BOIS_INDUSTRIE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PRIX_BOIS_OEUVRE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TYPE_ARBRE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DIAMETRE_EXPLOIT_TABLE);
            onCreate(db);
        }
    }


    /*
     ***********************************************************************************************
     ************************************** METHODE INSERTION **************************************
     ***********************************************************************************************
     */


    public boolean insertArbreParcelle(String numero, String essence, int diametre,
                                       int note_eco, String etat, double x, double y,
                                       double util_chauffage, double util_industrie, double util_oeuvre,
                                       double volumeCommercial, double valeurEconomique){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NUMERO_ARBRE_PARC,    numero);
        contentValues.put(ESSENCE_ARBRE,        essence);
        contentValues.put(DIAMETRE_ARBRE,       diametre);
        contentValues.put(NOTE_ECO_ARBRE,       note_eco);
        contentValues.put(ETAT_ARBRE,           etat);
        contentValues.put(COORD_X_ARBRE,        x);
        contentValues.put(COORD_Y_ARBRE,        y);
        contentValues.put(UTIL_BOIS_CHAUFFAGE,  util_chauffage);
        contentValues.put(UTIL_BOIS_INDUSTRIE,  util_industrie);
        contentValues.put(UTIL_BOIS_OEUVRE,     util_oeuvre);
        contentValues.put(VOLUME_COMMERCIAL,    volumeCommercial);
        contentValues.put(VALEUR_ECONOMIQUE,    valeurEconomique);

        long res = db.insertWithOnConflict(ARBRES_PARCELLE_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        return res != -1;
    }

    public boolean insertArbreMartele(String numero){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NUMERO_ARBRE_MART, numero);

        long res = db.insert(ARBRES_MARTELES_TABLE, null, contentValues);

        return res != -1;
    }

    public boolean insertArbreConserve(String numero){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NUMERO_ARBRE_CONS, numero);

        long res = db.insert(ARBRES_CONSERVES_TABLE, null, contentValues);

        return res != -1;
    }

    public boolean insertRaison(String numero, String raison){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NUMERO_ARBRE_TRAITE_RAISON, numero);
        contentValues.put(RAISON, raison);

        long res = db.insert(RAISON_TABLE, null, contentValues);
        return res != -1;
    }

    public boolean insertConstante(String nomParcelle, String lieuParcelle, double altitude, String habitat, double surfaceParcelle,
                                   double prelevementMin, double prelevementMax, int rotationMin, int rotationMax){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID_CONSTANTE, 0);
        contentValues.put(NOM_PARCELLE, nomParcelle);
        contentValues.put(LIEU_PARCELLE, lieuParcelle);
        contentValues.put(SURFACE_PARCELLE, surfaceParcelle);
        contentValues.put(ALTITUDE_PARCELLE, altitude);
        contentValues.put(HABITAT_PARCELLE, habitat);
        contentValues.put(PRELEVEMENT_VOLUME_MIN, prelevementMin);
        contentValues.put(PRELEVEMENT_VOLUME_MAX, prelevementMax);
        contentValues.put(ROTATION_MIN, rotationMin);
        contentValues.put(ROTATION_MAX, rotationMax);


        long res = db.insert(CONSTANTES_TABLE, null, contentValues);

        return res != -1;
    }

    public boolean insertTarifVolume(String typeArbre, String nomTarif, int versionTarif){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TYPE_ARBRE_TARIF, typeArbre);
        contentValues.put(NOM_TARIF, nomTarif);
        contentValues.put(VERSION_TARIF, versionTarif);

        long res = db.insert(TARIF_VOLUME_TABLE, null, contentValues);

        return res != -1;
    }

    public boolean insertPrixBoisChauffage(String typeOuEssence, double prixChauffage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TYPE_ESSENCE_ARBRE_CHAUFFAGE, typeOuEssence);
        contentValues.put(PRIX_CHAUFFAGE, prixChauffage);

        long res = db.insert(PRIX_BOIS_CHAUFFAGE_TABLE, null, contentValues);

        return res != -1;
    }

    public boolean insertPrixBoisIndustrie(String typeOuEssence, double prixIndustrie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TYPE_ESSENCE_ARBRE_INDUSTRIE, typeOuEssence);
        contentValues.put(PRIX_INDUSTRIE, prixIndustrie);

        long res = db.insert(PRIX_BOIS_INDUSTRIE_TABLE, null, contentValues);

        return res != -1;
    }

    public boolean insertPrixBoisOeuvre(String typeOuEssence, double prixOeuvre){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TYPE_ESSENCE_ARBRE_OEUVRE, typeOuEssence);
        contentValues.put(PRIX_OEUVRE, prixOeuvre);

        long res = db.insert(PRIX_BOIS_OEUVRE_TABLE, null, contentValues);

        return res != -1;
    }

    public boolean insertTypeArbre(String essence, String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ESSENCE_TYPE, essence);
        contentValues.put(TYPE_ARBRE, type);

        long res = db.insert(TYPE_ARBRE_TABLE, null, contentValues);

        return res != -1;
    }

    public boolean insertDiametreExploitabilite(String essence, int diametre){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ESSENCE_DIAM_EXPLOIT, essence);
        contentValues.put(DIAMETRE_EXPLOITABILITE, diametre);

        long res = db.insert(DIAMETRE_EXPLOIT_TABLE, null, contentValues);

        return res != -1;
    }

    public void updateNomEquipeConstante(String nomEquipe){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + CONSTANTES_TABLE
                + " SET " + NOM_EQUIPE + " = '" + nomEquipe + "'"
                + " WHERE " + ID_CONSTANTE + " = 0";

        db.execSQL(query);
    }


    /*
     ***********************************************************************************************
     ************************************** METHODE GET DATA ***************************************
     ***********************************************************************************************
     */

    public Cursor executeQuery(String query){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getDataFromTable(String selection, String table){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + selection
                + " FROM " + table;
        Cursor cur = db.rawQuery(query, null);

        return cur;
    }

    public Cursor getAllDataFromTable(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM "+ table, null);

        return cur;
    }

    public Cursor getDataFromTableWithCondition(String selection, String table, String condition){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + selection
                + " FROM " + table
                + " WHERE " + condition;
        Cursor cur = db.rawQuery(query, null);

        return cur;
    }

    public Cursor getAllDataFromTableWithCondition(String table, String condition){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT *"
                + " FROM " + table
                + " WHERE " + condition;
        Cursor cur = db.rawQuery(query, null);

        return cur;
    }

    /*
     ***********************************************************************************************
     ************************************ METHODE CLEAN TABLE **************************************
     ***********************************************************************************************
     */

    public void clearTableExercice(){
        clearTable(ARBRES_MARTELES_TABLE);
        clearTable(ARBRES_CONSERVES_TABLE);
        clearTable(RAISON_TABLE);
    }

    public void clearTable(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+table;
        db.execSQL(clearDBQuery);
    }


    // Vérifie qu'il y a une connexion à internet
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
