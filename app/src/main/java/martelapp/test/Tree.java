package martelapp.test;

/**
 * Created by Sancho on 28/02/2018.
 */

public class Tree {

        public String numero = "",
                diametre = "",
                noteEcologique = "",
                essence = "",
                etat = "";

        public Coord coord;

        public UtilisationBois utilisationBois;

        public static class Coord{
            public float x = 0f, y=0f;

            public Coord(){}

            public Coord(float x, float y){
                this.x = x;
                this.y = y;
            }
        }

        public static class UtilisationBois{
            public String chauffage = "", industrie = "", oeuvre = "";

            public UtilisationBois(){}

            public UtilisationBois(String chauffage, String industrie, String oeuvre){
                this.chauffage = chauffage;
                this.industrie = industrie;
                this.oeuvre = oeuvre;
            }
        }

        public Tree(){
        }

        public Tree(String numero, String diametre, String noteEcologique, String essence, String etat){
            this.numero = numero;
            this.diametre = diametre;
            this.noteEcologique = noteEcologique;
            this.essence = essence;
            this.etat = etat;
        }

        public String toString(){
            return numero + ": " + diametre + ", " + noteEcologique;
        }

        public String getNumero(){
            return numero;
        }


        public String getEssence(){
            return essence;
         }

        public String getEtat(){
            return etat;
        }

        public String getNoteEcologique(){return noteEcologique;}
}
