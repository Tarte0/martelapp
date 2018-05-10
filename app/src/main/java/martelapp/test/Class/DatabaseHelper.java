package martelapp.test.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Baptiste on 11/03/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper{


    public static final String DATABASE_NAME = "martelapp.db";

    // Table "arbres_parcelle"
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

    // Table "arbres_marteles"
    public static final String ARBRES_MARTELES_TABLE    = "arbres_marteles_table";
    public static final String ID_ARBRE_MART            = "_id";
    public static final String NUMERO_ARBRE_MART        = "NUMERO";

    // Table "arbres_conserves"
    public static final String ARBRES_CONSERVES_TABLE   = "arbres_conserves_table";
    public static final String ID_ARBRE_CONS            = "_id";
    public static final String NUMERO_ARBRE_CONS        = "NUMERO";

    // Table "raison"
    public static final String RAISON_TABLE                 = "raison_table";
    public static final String ID_RAISON                    = "_id";
    public static final String NUMERO_ARBRE_TRAITE_RAISON   = "NUMERO_ARBRE_TRAITE_RAISON";
    public static final String RAISON                       = "RAISON";

    // Table "constantes" (Utilisée pour les calculs dans le détail de la parcelle ainsi que
    //                     l'analyse en fin d'exercice)
    public static final String CONSTANTES_TABLE             = "constantes_table";
    public static final String ID_CONSTANTE                 = "_id";
    public static final String NOM_EQUIPE                   = "NOM_EQUIPE";
    public static final String SURFACE_PARCELLE             = "SURFACE_PARCELLE";
    public static final String HAUTEUR_MOYENNE_FEUILLU      = "HAUTEUR_MOYENNE_FEUILLU";
    public static final String HAUTEUR_MOYENNE_PETIT_BOIS   = "HAUTEUR_MOYENNE_PETIT_BOIS";
    public static final String HAUTEUR_MOYENNE_RESINEUX     = "HAUTEUR_MOYENNE_RESINEUX";
    public static final String PRIX_BOIS_CHAUFFAGE_FEUILLU  = "PRIX_BOIS_CHAUFFAGE_FEUILLU";
    public static final String PRIX_BOIS_CHAUFFAGE_RESINEUX = "PRIX_BOIS_CHAUFFAGE_RESINEUX";
    public static final String PRIX_BOIS_INDUSTRIE_FEUILLU  = "PRIX_BOIS_INDUSTRIE_FEUILLU";
    public static final String PRIX_BOIS_INDUSTRIE_RESINEUX = "PRIX_BOIS_INDUSTRIE_RESINEUX";
    public static final String PRIX_BOIS_OEUVRE_EPICEA      = "PRIX_BOIS_OEUVRE_EPICEA";
    public static final String PRIX_BOIS_OEUVRE_FEUILLU     = "PRIX_BOIS_OEUVRE_FEUILLU";
    public static final String PRIX_BOIS_OEUVRE_RESINEUX    = "PRIX_BOIS_OEUVRE_RESINEUX";
    public static final String PRIX_BOIS_OEUVRE_SAPIN       = "PRIX_BOIS_OEUVRE_SAPIN";
    public static final String VOLUME_COMMERCIAL_FEUILLU    = "VOLUME_COMMERCIAL_FEUILLU";
    public static final String VOLUME_COMMERCIAL_RESINEUX   = "VOLUME_COMMERCIAL_RESINEUX";

    // Table "type_arbre", référençant chaque essence à un certain type d'arbre
    public static final String TYPE_ARBRE_TABLE         = "type_arbre_table";
    public static final String ID_TYPE                  = "_id";
    public static final String ESSENCE_TYPE             = "ESSENCE_TYPE";
    public static final String TYPE_ARBRE               = "TYPE_ARBRE";

    public static final String ARBRE_MUR        = "Arbre Mûr";
    public static final String ECLAIRCIE        = "Eclaircie";
    public static final String SANITAIRE        = "Sanitaire";
    public static final String REGENERATION     = "Régénération";
    public static final String EXPLOITATION     = "Exploitation";
    public static final String STABILITE        = "Stabilité";
    public static final String BIODIVERSITE     = "Biodiversité";

    public static final int MIN_PRELEVEMENT = 90;
    public static final int MAX_PRELEVEMENT = 140;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
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
                + NUMERO_ARBRE_TRAITE_RAISON   + " TEXT, "
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
                + SURFACE_PARCELLE              + " REAL, "
                + HAUTEUR_MOYENNE_FEUILLU       + " REAL, "
                + HAUTEUR_MOYENNE_PETIT_BOIS    + " REAL, "
                + HAUTEUR_MOYENNE_RESINEUX      + " REAL, "
                + PRIX_BOIS_CHAUFFAGE_FEUILLU   + " REAL, "
                + PRIX_BOIS_CHAUFFAGE_RESINEUX  + " REAL, "
                + PRIX_BOIS_INDUSTRIE_FEUILLU   + " REAL, "
                + PRIX_BOIS_INDUSTRIE_RESINEUX  + " REAL, "
                + PRIX_BOIS_OEUVRE_EPICEA       + " REAL, "
                + PRIX_BOIS_OEUVRE_FEUILLU      + " REAL, "
                + PRIX_BOIS_OEUVRE_RESINEUX     + " REAL, "
                + PRIX_BOIS_OEUVRE_SAPIN        + " REAL, "
                + VOLUME_COMMERCIAL_FEUILLU     + " REAL, "
                + VOLUME_COMMERCIAL_RESINEUX    + " REAL)"
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ARBRES_PARCELLE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARBRES_MARTELES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RAISON_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CONSTANTES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TYPE_ARBRE_TABLE);
        onCreate(db);
    }


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

    public boolean insertConstante(double hauteurFeuillu, double hauteurPetitBois, double hauteurResineux,
                                   double prixChauffageFeuillu, double prixChauffageResineux,
                                   double prixIndustrieFeuillu, double prixIndustrieResineux,
                                   double prixOeuvreEpicea, double prixOeuvreFeuillu, double prixOeuvreResineux, double prixOeuvreSapin,
                                   double volumeFeuillu, double volumeResineux){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID_CONSTANTE, 0);
        contentValues.put(HAUTEUR_MOYENNE_FEUILLU,      hauteurFeuillu);
        contentValues.put(HAUTEUR_MOYENNE_PETIT_BOIS,   hauteurPetitBois);
        contentValues.put(HAUTEUR_MOYENNE_RESINEUX,     hauteurResineux);
        contentValues.put(PRIX_BOIS_CHAUFFAGE_FEUILLU,  prixChauffageFeuillu);
        contentValues.put(PRIX_BOIS_CHAUFFAGE_RESINEUX, prixChauffageResineux);
        contentValues.put(PRIX_BOIS_INDUSTRIE_FEUILLU,  prixIndustrieFeuillu);
        contentValues.put(PRIX_BOIS_INDUSTRIE_RESINEUX, prixIndustrieResineux);
        contentValues.put(PRIX_BOIS_OEUVRE_EPICEA,      prixOeuvreEpicea);
        contentValues.put(PRIX_BOIS_OEUVRE_FEUILLU,     prixOeuvreFeuillu);
        contentValues.put(PRIX_BOIS_OEUVRE_RESINEUX,    prixOeuvreResineux);
        contentValues.put(PRIX_BOIS_OEUVRE_SAPIN,       prixOeuvreSapin);
        contentValues.put(VOLUME_COMMERCIAL_FEUILLU,    volumeFeuillu);
        contentValues.put(VOLUME_COMMERCIAL_RESINEUX,   volumeResineux);

        long res = db.insert(CONSTANTES_TABLE, null, contentValues);

        return res != -1;
    }

    public void updateSurfaceParcelleConstante(Double surfaceParcelle){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + CONSTANTES_TABLE
                + " SET " + SURFACE_PARCELLE + " = " + surfaceParcelle
                + " WHERE " + ID_CONSTANTE + " = 0";

        db.execSQL(query);
    }

    public boolean insertTypeArbre(String essence, String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ESSENCE_TYPE, essence);
        contentValues.put(TYPE_ARBRE, type);

        long res = db.insert(TYPE_ARBRE_TABLE, null, contentValues);

        return res != -1;
    }

    public void updateNomEquipeConstante(String nomEquipe){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + CONSTANTES_TABLE
                + " SET " + NOM_EQUIPE + " = '" + nomEquipe + "'"
                + " WHERE " + ID_CONSTANTE + " = 0";

        db.execSQL(query);
    }

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

    public boolean isEmpty(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + table, null);
        return cur.getCount() == 0;
    }

    public void cleanAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ARBRES_PARCELLE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARBRES_MARTELES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RAISON_TABLE);
        onCreate(db);
    }

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
}
