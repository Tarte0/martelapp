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
    public static final String DATABASE_NAME = "info503.db";

    // Table "arbres_parcelle"
    public static final String ARBRES_PARCELLE_TABLE    = "arbres_parcelle_table";
    public static final String ID_ARBRE_PARC            = "ID";
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

    // Table "arbres_marteles"
    public static final String ARBRES_MARTELES_TABLE    = "arbres_marteles_table";
    public static final String ID_ARBRE_MART            = "ID";
    public static final String NUMERO_ARBRE_MART        = "NUMERO";
    public static final String RAISON_MARTELAGE         = "RAISON_MARTELAGE";

    //Faire table plusieurs raison

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*
         *  Création de la table "arbres_parcelle"
         *  PRIMARY KEY : numero
         */
        db.execSQL("CREATE TABLE " + ARBRES_PARCELLE_TABLE + "("
                + ID_ARBRE_PARC + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NUMERO_ARBRE_PARC + " TEXT, "
                + ESSENCE_ARBRE + " TEXT, "
                + DIAMETRE_ARBRE + " INTEGER, "
                + NOTE_ECO_ARBRE + " INTEGER, "
                + ETAT_ARBRE + " TEXT, "
                + COORD_X_ARBRE + " REAL, "
                + COORD_Y_ARBRE + " REAL, "
                + UTIL_BOIS_CHAUFFAGE + " REAL, "
                + UTIL_BOIS_INDUSTRIE + " REAL, "
                + UTIL_BOIS_OEUVRE + " REAL)"
        );

        /*
         *  Création de la table "arbres_marteles"
         *  PRIMARY KEY : numero
         *  FOREIGN KEY : numero sur le numero de arbres_parcelle
         */
        db.execSQL("CREATE TABLE " + ARBRES_MARTELES_TABLE + "("
                + ID_ARBRE_PARC + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NUMERO_ARBRE_MART + " TEXT, "
                + RAISON_MARTELAGE + " TEXT, " +
                "FOREIGN KEY(" + NUMERO_ARBRE_MART + ") REFERENCES " + ARBRES_PARCELLE_TABLE + "(" + NUMERO_ARBRE_PARC +"))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ARBRES_PARCELLE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARBRES_MARTELES_TABLE);
        onCreate(db);
    }


    public boolean insertArbreParcelle(String numero, String essence, int diametre,
                                       int note_eco, String etat, double x, double y,
                                       double util_chauffage, double util_industrie, double util_oeuvre){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NUMERO_ARBRE_PARC, numero);
        contentValues.put(ESSENCE_ARBRE, essence);
        contentValues.put(DIAMETRE_ARBRE, diametre);
        contentValues.put(NOTE_ECO_ARBRE, note_eco);
        contentValues.put(ETAT_ARBRE, etat);
        contentValues.put(COORD_X_ARBRE, x);
        contentValues.put(COORD_Y_ARBRE, y);
        contentValues.put(UTIL_BOIS_CHAUFFAGE, util_chauffage);
        contentValues.put(UTIL_BOIS_INDUSTRIE, util_industrie);
        contentValues.put(UTIL_BOIS_OEUVRE, util_oeuvre);

        db.insert(ARBRES_PARCELLE_TABLE, null, contentValues);

        return false;
    }

    public boolean insertArbreMarteles(String numero, String raison){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NUMERO_ARBRE_MART, numero);
        contentValues.put(RAISON_MARTELAGE, raison);

        db.insert(ARBRES_MARTELES_TABLE, null, contentValues);

        return false;
    }


    public Cursor getAllData(String query){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor executeQuery(String query){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getAllDataFromTable(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM "+ table, null);
        return cur;
    }

    public boolean isEmpty(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + table, null);
        return cur.getCount() == 0;
    }

    public void clean(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ARBRES_PARCELLE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARBRES_MARTELES_TABLE);
        onCreate(db);
    }

    public void clearTable(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+table;
        db.execSQL(clearDBQuery);
    }
}
