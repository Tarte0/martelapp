package martelapp.test.Class;

import java.util.HashMap;

/**
 * Classe utilisée pour récupérer une instance d'un arbre de firebase
 */

public class Arbre {

    public String numero = "",
            essence = "",
            etat = "";

    public int diametre = 0,
            noteEcologique = 0;
    public Coord coord;

    public UtilisationBois utilisationBois;

    public int getDiametre() {
        return diametre;
    }

    public static class Coord {
        public double x = 0d, y = 0d;

        public Coord() {
        }

        public Coord(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class UtilisationBois {
        public double chauffage = 0f, industrie = 0f, oeuvre = 0f;

        public UtilisationBois() {
        }

        public UtilisationBois(double chauffage, double industrie, double oeuvre) {
            this.chauffage = chauffage;
            this.industrie = industrie;
            this.oeuvre = oeuvre;
        }
    }

    public Arbre() {
    }

    public Arbre(String numero, int diametre, int noteEcologique, String essence, String etat) {
        this.numero = numero;
        this.diametre = diametre;
        this.noteEcologique = noteEcologique;
        this.essence = essence;
        this.etat = etat;
    }

    public Arbre(String numero, int diametre, int noteEcologique, String essence, String etat,
                 double chauffage, double industrie, double oeuvre, double x, double y) {
        this.numero = numero;
        this.diametre = diametre;
        this.noteEcologique = noteEcologique;
        this.essence = essence;
        this.etat = etat;
        this.utilisationBois = new UtilisationBois(chauffage, industrie, oeuvre);
        this.coord = new Coord(x, y);
    }

    public String toString() {
        return numero + ": " + diametre + ", " + noteEcologique;
    }

    public String getNumero() {
        return numero;
    }

    public String getEssence() {
        return essence;
    }

    public String getEtat() {
        return etat;
    }

    public int getNoteEcologique() {
        return noteEcologique;
    }

    public HashMap<String, Double> getUtilBoisAsMap() {
        HashMap<String, Double> util = new HashMap<>();
        util.put("oeuvre", utilisationBois.oeuvre);
        util.put("chauffage", utilisationBois.chauffage);
        util.put("industrie",utilisationBois.industrie);
        return util;
    }
}