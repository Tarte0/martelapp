package martelapp.test.Class;

import java.util.HashMap;

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

    public String getDiametre() {
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
        public String chauffage = "", industrie = "", oeuvre = "";

        public UtilisationBois() {
        }

        public UtilisationBois(String chauffage, String industrie, String oeuvre) {
            this.chauffage = chauffage;
            this.industrie = industrie;
            this.oeuvre = oeuvre;
        }
    }

    public Tree() {
    }

    public Tree(String numero, String diametre, String noteEcologique, String essence, String etat) {
        this.numero = numero;
        this.diametre = diametre;
        this.noteEcologique = noteEcologique;
        this.essence = essence;
        this.etat = etat;
    }

    public Tree(String numero, String diametre, String noteEcologique, String essence, String etat,
                String chauffage, String industrie, String oeuvre, double x, double y) {
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

    public String getNoteEcologique() {
        return noteEcologique;
    }

    public HashMap<String, Double> getUtilBoisAsMap() {
        HashMap<String, Double> util = new HashMap<>();
        util.put("oeuvre", utilisationBois.oeuvre.equals("")? 0.0 : Double.parseDouble(utilisationBois.oeuvre));
        util.put("chauffage", utilisationBois.chauffage.equals("")? 0.0 : Double.parseDouble(utilisationBois.chauffage));
        util.put("industrie", utilisationBois.industrie.equals("")? 0.0 : Double.parseDouble(utilisationBois.industrie));
        return util;
    }
}