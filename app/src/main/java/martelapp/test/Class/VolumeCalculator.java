package martelapp.test.Class;

import java.text.Normalizer;
import java.util.HashMap;

/**
 * Created by cimin on 18/04/2018.
 */

/*
 * classe permettant de calculer les differentes valeurs liées au volume et au prix d'un arbre.
 * on l'initialise avec les constantes nécéssaires sous forme de HashMap ou de tableau.
 */
public class VolumeCalculator {

    private final String ETAT_VIVANT = "v";
    private final String ETAT_MORT_PIED = "mp";
    private final String ETAT_MORT_SOL = "ms";
    private final String OEUVRE = "oeuvre";
    private final String CHAUFFAGE = "chauffage";
    private final String INDUSTRIE = "industrie";

    private HashMap<String, Double> prixBoixChauffage = new HashMap<>();
    private HashMap<String, Double> prixBoixIndustrie = new HashMap<>();
    private HashMap<String, Double> prixBoixOeuvre = new HashMap<>();

    private HashMap<String, String> essences = new HashMap<>();

    String nomTarifResineux;
    int versionTarifResineux;

    String nomTarifFeuillus;
    int versionTarifFeuillus;

    public VolumeCalculator(HashMap<String, Double> prixBoixChauffage, HashMap<String, Double> prixBoixIndustrie, HashMap<String, Double> prixBoixOeuvre, HashMap<String, String> essences,
                            String nomTarifResineux, int versionTarifResineux, String nomTarifFeuillus, int versionTarifFeuillus) {
        this.prixBoixChauffage = prixBoixChauffage;
        this.prixBoixIndustrie = prixBoixIndustrie;
        this.prixBoixOeuvre = prixBoixOeuvre;
        this.essences = essences;
        this.nomTarifResineux = nomTarifResineux;
        this.versionTarifResineux = versionTarifResineux;
        this.nomTarifFeuillus = nomTarifFeuillus;
        this.versionTarifFeuillus = versionTarifFeuillus;
    }

    public VolumeCalculator(String[] constantsKeys, double[] constantsValues,
                            String[] essencesKeys, String[] essencesValues) {
        instantiateConstantsMap(constantsKeys, constantsValues);
        instantiateEssencesMap(essencesKeys, essencesValues);
    }

    /*
     * initialise la map des constantes
     * tableau de clés (String)
     * tableau de valeurs (double)
     * la valeur de rang i est associée a la clé de rang i
     */
    public void instantiateConstantsMap(String[] keys, double[] values) {
        for (int i = 0; i < Math.min(keys.length, values.length); i++) {
            constants.put(keys[i], values[i]);
        }
    }

    /*
     * initialise la map des essences
     * tableau de clés pour les essences (String)
     * tableau de valeurs pour les types (String)
     * la valeur de rang i est associée a la clé de rang i
     */
    public void instantiateEssencesMap(String[] keys, String[] values) {
        for (int i = 0; i < Math.min(keys.length, values.length); i++) {
            essences.put(keys[i], values[i]);
        }
    }

    public HashMap<String, Double> getConstants() {
        return constants;
    }

    public HashMap<String, String> getEssencess() {
        return essences;
    }

    public String getType(Tree tree) {
        return Normalizer.normalize(essences.get((tree.getEssence()).toLowerCase()), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public double getHauteurDecoupe(Tree tree) {
        //si l'arbre est un petit bois on renvoie simplement sa constante
        if (tree.getDiametre() <= 30) {
            //return constants.get(DatabaseHelper.HAUTEUR_MOYENNE_PETIT_BOIS);
        }
        //sinon on va chercher la constante correspondant au type de son essence
        return constants.get(("HAUTEUR_MOYENNE_".concat(getType(tree))).toUpperCase());
    }

    public double getVolumeCommercial(Tree tree) {
        if (tree.getEtat().equals(ETAT_VIVANT)) {
            return round(constants.get(("VOLUME_COMMERCIAL_".concat(getType(tree))).toUpperCase()) *
                    Math.pow((float)(tree.getDiametre()) / 100, 2) * getHauteurDecoupe(tree),2);
        }
        //un arbre mort n'a pas de volume commercialisable
        return 0d;
    }

    //map avec 3 valeurs pour oeuvre, chauffage et industrie
    public HashMap<String, Double> getVolumeCommercialBois(Tree tree) {
        HashMap<String, Double> volComBois = new HashMap<>();
        double volumeCom = getVolumeCommercial(tree);
        HashMap<String, Double> utilisationBois = tree.getUtilBoisAsMap();
        volComBois.put(OEUVRE, volumeCom * utilisationBois.get(OEUVRE) / 100);
        volComBois.put(CHAUFFAGE, volumeCom * utilisationBois.get(CHAUFFAGE) / 100);
        volComBois.put(INDUSTRIE, volumeCom * utilisationBois.get(INDUSTRIE) / 100);
        return volComBois;
    }

    //renvoie le prix pour un des attribut oeuvre, chauffage ou industrie
    private double getPrixBoisFromAttribute(Tree tree, String utilisationBoisAttribute) {
        Double prixBoisAttribute = constants.get(("PRIX_BOIS_".concat(utilisationBoisAttribute).concat("_").concat(tree.getEssence())).toUpperCase());
        //si il n'y a pas de constante propre a l'essence, on fait selon le type
        if (prixBoisAttribute == null) {
            prixBoisAttribute = constants.get(("PRIX_BOIS_".concat(utilisationBoisAttribute).concat("_").concat(getType(tree))).toUpperCase());
        }
        return prixBoisAttribute;
    }

    //map avec 3 valeurs pour oeuvre, chauffage et industrie
    public HashMap<String, Double> getPrixBois(Tree tree) {
        HashMap<String, Double> prixBois = new HashMap<>();
        prixBois.put(OEUVRE, getPrixBoisFromAttribute(tree, OEUVRE));
        prixBois.put(CHAUFFAGE, getPrixBoisFromAttribute(tree, CHAUFFAGE));
        prixBois.put(INDUSTRIE, getPrixBoisFromAttribute(tree, INDUSTRIE));
        return prixBois;
    }

    //map avec 3 valeurs pour oeuvre, chauffage et industrie
    public HashMap<String, Double> getValeurEcoBois(Tree tree){
        HashMap<String, Double> valeurEcoBois = new HashMap<>();
        HashMap<String, Double> volComBois = getVolumeCommercialBois(tree);
        HashMap<String, Double> prixBois = getPrixBois(tree);
        valeurEcoBois.put(OEUVRE, volComBois.get(OEUVRE) * prixBois.get(OEUVRE));
        valeurEcoBois.put(CHAUFFAGE, volComBois.get(CHAUFFAGE) * prixBois.get(CHAUFFAGE));
        valeurEcoBois.put(INDUSTRIE, volComBois.get(INDUSTRIE) * prixBois.get(INDUSTRIE));
        return valeurEcoBois;
    }

    public double getValeurEco(Tree tree){
        HashMap<String, Double> valeurEcoBois = getValeurEcoBois(tree);
        return round(valeurEcoBois.get(OEUVRE) + valeurEcoBois.get(CHAUFFAGE) + valeurEcoBois.get(INDUSTRIE), 2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}