package martelapp.test.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Class.Parcelle;
import martelapp.test.Class.Tree;
import martelapp.test.Class.VolumeCalculator;
import martelapp.test.R;

public class MainActivity extends AppCompatActivity {

    DatabaseReference firebaseDatabase;
    DatabaseReference connectedRef;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(getApplicationContext());

        //getApplicationContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);

        // Récupération de la base de données firebase
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        /*
         *  Bouton "Nouvel Exercice"
         *
         *  Efface toutes les données qu'il y a dans les tables
         *  <arbres_marteles_table> et <raison_table> et lance
         *  l'activité du choix du nom de l'équipe <NomEquipeActivity>
         *  pour commencer un exercice de martelage de zero
         */
        Button buttonNouvelExercice = findViewById(R.id.nouvel_exercice);
        buttonNouvelExercice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.clearTableExercice();
                Intent intent = new Intent(getApplicationContext(), NomEquipeActivity.class);
                startActivity(intent);
            }
        });


        /*
         *  Bouton "Continuer Exercice"
         *
         *  Renvoie directement à l'activité de recherche d'arbre
         *  <RechercheActivity>.
         *  Sécurité en cas de mauvaise manipulation de l'utilisateur
         *  ou de crash de l'application pour que l'utilisateur puisse
         *  retrouver sa progression de l'exercice.
         */
        Button buttonContinuerExercice = findViewById(R.id.continuer_exercice);
        buttonContinuerExercice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RechercheActivity.class);
                startActivity(intent);
            }
        });


        /*
         *  Bouton "Test MAJ"
         *
         *  Met à jour les données de <arbres_parcelle_table>
         *  depuis une base de données Firebase en testant
         *  premièrement si il y a une connexion à internet
         */
        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        Button buttonTestMAJ = findViewById(R.id.test_maj_bdd);
        buttonTestMAJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                 *  Test d'une connexion internet à Firebase
                 *  Si il y connexion, <arbres_parcelle_table>
                 *  est mise à jour
                 */
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            miseAJourConstanteTable();
                            miseAJourArbreParcelleTable();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "echec connexion", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Erreur: échec test connexion", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        });
    }


    public void miseAJourArbreParcelleTable(){

        // Suppression des données de la table <arbres_parcelles_table>
        dbHelper.clearTable(DatabaseHelper.ARBRES_PARCELLE_TABLE);

        /*
         *  Listener qui récupère toute la parcelle à partir de la base de données firebase
         *  et enregistre tous les arbres de la parcelle dans la table arbres_parcelle_table
         */
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Double surfaceParcelle = dataSnapshot.child("surface").getValue(Double.class);
                dbHelper.updateSurfaceParcelleConstante(surfaceParcelle);

                HashMap<String, Double> constants = new HashMap<>();
                Cursor cur = dbHelper.getAllDataFromTable(DatabaseHelper.CONSTANTES_TABLE);
                cur.moveToFirst();
                for(int i = 3; i < cur.getColumnCount(); i++){
                    constants.put(cur.getColumnName(i), cur.getDouble(i));
                }

                HashMap<String, String> essence_type = new HashMap<>();
                cur = dbHelper.getAllDataFromTable(DatabaseHelper.TYPE_ARBRE_TABLE);
                while(cur.moveToNext()){
                    essence_type.put(cur.getString(cur.getColumnIndex(DatabaseHelper.ESSENCE_TYPE)), cur.getString(cur.getColumnIndex(DatabaseHelper.TYPE_ARBRE)));
                }

                VolumeCalculator volumeCalculator = new VolumeCalculator(constants, essence_type);

                // Récupération de la parcelle de la base de données firebase dans la classe Parcelle
                Parcelle parcelle = dataSnapshot.getValue(Parcelle.class);

                // Test de la récupération de la parcelle
                if(parcelle != null){

                    // Tous les arbres de la parcelle son enregistrer dans une Hashmap
                    HashMap<String, Tree> arbres_parcelle = parcelle.arbres;

                    // Test si il y a bien des arbres dans la parcelle
                    if(!arbres_parcelle.isEmpty()){

                        /*
                         *  Parcours de la Hashmap<String, Tree> contenant tous les arbres
                         *  et les enregistres dans la table arbres_parcelle_table
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
                                    Integer.parseInt(arbre.diametre),
                                    Integer.parseInt(arbre.noteEcologique),
                                    arbre.etat,
                                    arbre.coord.x,
                                    arbre.coord.y,
                                    arbre.utilisationBois.chauffage.equals("")? 0.0 : Double.parseDouble(arbre.utilisationBois.chauffage),
                                    arbre.utilisationBois.industrie.equals("")? 0.0 : Double.parseDouble(arbre.utilisationBois.industrie),
                                    arbre.utilisationBois.oeuvre.equals("")? 0.0 : Double.parseDouble(arbre.utilisationBois.oeuvre),
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

        // On assigne le listener "postListener" à notre parcelle Martelapp de la base de données firebase
        firebaseDatabase.child("parcelles").child("parcelleMartelapp").addListenerForSingleValueEvent(postListener);
    }


    public void miseAJourConstanteTable(){

        // Suppression des données de la table <arbres_parcelles_table>
        dbHelper.clearTable(DatabaseHelper.CONSTANTES_TABLE);
        dbHelper.clearTable(DatabaseHelper.TYPE_ARBRE_TABLE);

        /*
         *  Listener qui récupère toute la parcelle à partir de la base de données firebase
         *  et enregistre tous les arbres de la parcelle dans la table arbres_parcelle_table
         */
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataConstantes = dataSnapshot.child("constantes");
                Double hauteurMoyenneFeuillu = dataConstantes.child("hauteurMoyenne").child("feuillu").getValue(Double.class);
                Double hauteurMoyennePetitBois = dataConstantes.child("hauteurMoyenne").child("petitBois").getValue(Double.class);
                Double hauteurMoyenneResineux = dataConstantes.child("hauteurMoyenne").child("résineux").getValue(Double.class);

                Double prixBoisChauffageFeuillu = dataConstantes.child("prix").child("bois").child("chauffage").child("feuillu").getValue(Double.class);
                Double prixBoisChauffageResineux = dataConstantes.child("prix").child("bois").child("chauffage").child("résineux").getValue(Double.class);

                Double prixBoisIndustrieFeuillu = dataConstantes.child("prix").child("bois").child("industrie").child("feuillu").getValue(Double.class);
                Double prixBoisIndustrieResineux = dataConstantes.child("prix").child("bois").child("industrie").child("résineux").getValue(Double.class);

                Double prixBoisOeuvreEpicea = dataConstantes.child("prix").child("bois").child("oeuvre").child("epicéa").getValue(Double.class);
                Double prixBoisOeuvreFeuillu = dataConstantes.child("prix").child("bois").child("oeuvre").child("feuillu").getValue(Double.class);
                Double prixBoisOeuvreResineux = dataConstantes.child("prix").child("bois").child("oeuvre").child("résineux").getValue(Double.class);
                Double prixBoisOeuvreSapin = dataConstantes.child("prix").child("bois").child("oeuvre").child("sapin").getValue(Double.class);

                Double volumeCommercialFeuillu = dataConstantes.child("volume").child("commercial").child("feuillu").getValue(Double.class);
                Double volumeCommercialResineux = dataConstantes.child("volume").child("commercial").child("résineux").getValue(Double.class);

                dbHelper.insertConstante(
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


                for( DataSnapshot child : dataSnapshot.child("essences").getChildren()){
                    String essence = child.getKey();
                    String type = child.getValue(String.class);

                    dbHelper.insertTypeArbre(essence, type);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        // On assigne le listener "postListener" à notre parcelle Martelapp de la base de données firebase
        firebaseDatabase.child("metadata").addListenerForSingleValueEvent(postListener);
    }
}