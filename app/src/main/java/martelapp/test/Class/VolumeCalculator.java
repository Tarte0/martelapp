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
    private final double[][] algan = {
            {0.05, 0.05, 0.05, 0.075, 0.075, 0.075, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.125, 0.125, 0.15, 0.15, 0.15, 0.15, 0.15, 0.2},
            {0.1, 0.1, 0.1, 0.15, 0.15, 0.15, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.25, 0.25, 0.3, 0.3, 0.3, 0.3, 0.3, 0.4},
            {0.2, 0.2, 0.2, 0.3, 0.3, 0.3, 0.4, 0.4, 0.4, 0.4, 0.4, 0.5, 0.5, 0.5, 0.6, 0.6, 0.6, 0.6, 0.6, 0.7},
            {0.3, 0.4, 0.4, 0.5, 0.5, 0.5, 0.6, 0.6, 0.6, 0.7, 0.7, 0.8, 0.8, 0.8, 0.9, 0.9, 1, 1, 1, 1.1},
            {0.5, 0.6, 0.6, 0.7, 0.7, 0.8, 0.9, 0.9, 0.9, 1, 1, 1.1, 1.2, 1.2, 1.3, 1.3, 1.4, 1.5, 1.5, 1.6},
            {0.7, 0.8, 0.8, 0.9, 1, 1.1, 1.2, 1.2, 1.3, 1.4, 1.4, 1.5, 1.6, 1.7, 1.8, 1.8, 1.9, 2, 2.1, 2.2},
            {0.9, 1, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8},
            {1.2, 1.3, 1.4, 1.6, 1.7, 1.8, 1.9, 2.1, 2.2, 2.3, 2.5, 2.6, 2.7, 2.8, 3, 3.1, 3.2, 3.3, 3.5, 3.6},
            {1.5, 1.6, 1.8, 2, 2.1, 2.3, 2.4, 2.6, 2.8, 2.9, 3.1, 3.3, 3.4, 3.6, 3.8, 3.9, 4, 4.2, 4.4, 4.5},
            {1.8, 2, 2.2, 2.4, 2.6, 2.8, 3, 3.2, 3.4, 3.6, 3.8, 4, 4.2, 4.4, 4.6, 4.8, 5, 5.2, 5.4, 5.6},
            {2.1, 2.4, 2.6, 2.9, 3.1, 3.4, 3.6, 3.8, 4.1, 4.3, 4.5, 4.8, 5, 5.3, 5.5, 5.8, 6, 6.2, 6.5, 6.7},
            {2.5, 2.8, 3.1, 3.4, 3.7, 4, 4.2, 4.5, 4.8, 5.1, 5.3, 5.6, 5.9, 6.2, 6.5, 6.8, 7.1, 7.3, 7.6, 7.9},
            {2.9, 3.2, 3.6, 3.9, 4.3, 4.6, 4.9, 5.2, 5.6, 5.9, 6.2, 6.5, 6.9, 7.2, 7.6, 7.9, 8.2, 8.5, 8.8, 9.2},
            {3.3, 3.7, 4.1, 4.5, 4.9, 5.3, 5.6, 6, 6.4, 6.7, 7.1, 7.5, 7.9, 8.3, 8.7, 9.1, 9.4, 9.8, 10.1, 10.5},
            {3.8, 4.2, 4.7, 5.1, 5.6, 6, 6.4, 7, 7.2, 7.6, 8.1, 8.5, 9, 9.4, 9.9, 10.3, 10.7, 11.1, 11.5, 12},
            {4.3, 4.8, 5.3, 5.8, 6.3, 6.8, 7.2, 7.7, 8.1, 8.6, 9.1, 9.6, 10.1, 10.6, 11.1, 11.6, 12.1, 12.5, 13, 13.5},
            {4.8, 5.4, 5.9, 6.5, 7, 7.6, 8.1, 8.6, 9.1, 9.7, 10.2, 10.8, 11.3, 11.9, 12.4, 13, 13.5, 14, 14.6, 15.1},
            {5.4, 6, 6.6, 7.2, 7.8, 8.4, 9, 9.6, 10.2, 10.8, 11.4, 12, 12.6, 13.2, 13.8, 14.4, 15, 15.6, 16.2, 16.8}
    };
    private final double[][] chaude = {
            {0.08, 0.08, 0.09, 0.1, 0.11, 0.12, 0.13, 0.13, 0.15, 0.15, 0.16, 0.17, 0.18, 0.18, 0.19, 0.2, 0.21, 0.22, 0.23, 0.23},
            {0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.5, 0.5},
            {0.3, 0.3, 0.3, 0.3, 0.4, 0.4, 0.4, 0.4, 0.5, 0.5, 0.5, 0.6, 0.6, 0.6, 0.6, 0.7, 0.7, 0.7, 0.8, 0.8},
            {0.4, 0.4, 0.5, 0.5, 0.5, 0.6, 0.6, 0.7, 0.7, 0.8, 0.8, 0.8, 0.9, 0.9, 1, 1, 1, 1.1, 1.1, 1.2},
            {0.5, 0.6, 0.6, 0.7, 0.8, 0.8, 0.9, 0.9, 1, 1.1, 1.1, 1.2, 1.2, 1.3, 1.3, 1.4, 1.5, 1.5, 1.6, 1.6},
            {0.7, 0.8, 0.9, 0.9, 1, 1.1, 1.2, 1.2, 1.3, 1.4, 1.5, 1.6, 1.6, 1.7, 1.8, 1.9, 1.9, 2, 2.1, 2.2},
            {0.9, 1, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8},
            {1.1, 1.3, 1.4, 1.5, 1.6, 1.8, 1.9, 2, 2.1, 2.3, 2.4, 2.5, 2.6, 2.8, 2.9, 3, 3.1, 3.3, 3.4, 3.5},
            {1.4, 1.5, 1.7, 1.8, 2, 2.1, 2.3, 2.4, 2.6, 2.8, 2.9, 3.1, 3.2, 3.4, 3.5, 3.7, 3.8, 4, 4.1, 4.3},
            {1.7, 1.8, 2, 2.2, 2.4, 2.6, 2.8, 2.9, 3.1, 3.3, 3.5, 3.7, 3.9, 4, 4.2, 4.4, 4.6, 4.8, 5, 5.1},
            {2, 2.2, 2.4, 2.6, 2.8, 3, 3.3, 3.5, 3.7, 3.9, 4.1, 4.3, 4.6, 4.8, 5, 5.2, 5.4, 5.6, 5.9, 6.1},
            {2.3, 2.5, 2.8, 3, 3.3, 3.5, 3.8, 4, 4.3, 4.6, 4.8, 5.1, 5.3, 5.6, 5.8, 6.1, 6.3, 6.6, 6.8, 7.1},
            {2.6, 2.9, 3.2, 3.5, 3.8, 4.1, 4.4, 4.7, 5, 5.3, 5.5, 5.8, 6.1, 6.4, 6.7, 7, 7.3, 7.6, 7.9, 8.2},
            {3, 3.3, 3.7, 4, 4.3, 4.7, 5, 5.3, 5.7, 6, 6.3, 6.7, 7, 7.3, 7.7, 8, 8.3, 8.7, 9, 9.3},
            {3.4, 3.8, 4.2, 4.5, 4.9, 5.3, 5.7, 6, 6.4, 6.8, 7.2, 7.6, 7.9, 8.3, 8.7, 9.1, 9.4, 9.8, 10.2, 10.6},
            {3.8, 4.3, 4.7, 5.1, 5.5, 6, 6.4, 6.8, 7.2, 7.7, 8.1, 8.5, 8.9, 9.4, 9.8, 10.2, 10.6, 11.1, 11.5, 11.9},
            {4.3, 4.8, 5.2, 5.7, 6.2, 6.7, 7.1, 7.6, 8.1, 8.6, 9, 9.5, 10, 10.5, 10.9, 11.4, 11.9, 12.4, 12.8, 13.3},
            {4.8, 5.3, 5.8, 6.3, 6.9, 7.4, 7.9, 8.4, 9, 9.5, 10, 10.6, 11.1, 11.6, 12.1, 12.7, 13.2, 13.7, 14.3, 14.8}
    };
    private final double[][] schaefferLent = {
            {0.08, 0.08, 0.09, 0.1, 0.11, 0.12, 0.13, 0.13, 0.15, 0.15, 0.16, 0.17, 0.18, 0.18, 0.19, 0.2, 0.21, 0.22, 0.23, 0.23},
            {0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.5, 0.5},
            {0.3, 0.3, 0.3, 0.3, 0.4, 0.4, 0.4, 0.4, 0.5, 0.5, 0.5, 0.6, 0.6, 0.6, 0.6, 0.7, 0.7, 0.7, 0.8, 0.8},
            {0.4, 0.4, 0.5, 0.5, 0.5, 0.6, 0.6, 0.7, 0.7, 0.8, 0.8, 0.8, 0.9, 0.9, 1, 1, 1, 1.1, 1.1, 1.2},
            {0.5, 0.6, 0.6, 0.7, 0.8, 0.8, 0.9, 0.9, 1, 1.1, 1.1, 1.2, 1.2, 1.3, 1.3, 1.4, 1.5, 1.5, 1.6, 1.6},
            {0.7, 0.8, 0.9, 0.9, 1, 1.1, 1.2, 1.2, 1.3, 1.4, 1.5, 1.6, 1.6, 1.7, 1.8, 1.9, 1.9, 2, 2.1, 2.2},
            {0.9, 1, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8},
            {1.1, 1.3, 1.4, 1.5, 1.6, 1.8, 1.9, 2, 2.1, 2.3, 2.4, 2.5, 2.6, 2.8, 2.9, 3, 3.1, 3.3, 3.4, 3.5},
            {1.4, 1.5, 1.7, 1.8, 2, 2.1, 2.3, 2.4, 2.6, 2.8, 2.9, 3.1, 3.2, 3.4, 3.5, 3.7, 3.8, 4, 4.1, 4.3},
            {1.7, 1.8, 2, 2.2, 2.4, 2.6, 2.8, 2.9, 3.1, 3.3, 3.5, 3.7, 3.9, 4, 4.2, 4.4, 4.6, 4.8, 5, 5.1},
            {2, 2.2, 2.4, 2.6, 2.8, 3, 3.3, 3.5, 3.7, 3.9, 4.1, 4.3, 4.6, 4.8, 5, 5.2, 5.4, 5.6, 5.9, 6.1},
            {2.3, 2.5, 2.8, 3, 3.3, 3.5, 3.8, 4, 4.3, 4.6, 4.8, 5.1, 5.3, 5.6, 5.8, 6.1, 6.3, 6.6, 6.8, 7.1},
            {2.6, 2.9, 3.2, 3.5, 3.8, 4.1, 4.4, 4.7, 5, 5.3, 5.5, 5.8, 6.1, 6.4, 6.7, 7, 7.3, 7.6, 7.9, 8.2},
            {3, 3.3, 3.7, 4, 4.3, 4.7, 5, 5.3, 5.7, 6, 6.3, 6.7, 7, 7.3, 7.7, 8, 8.3, 8.7, 9, 9.3},
            {3.4, 3.8, 4.2, 4.5, 4.9, 5.3, 5.7, 6, 6.4, 6.8, 7.2, 7.6, 7.9, 8.3, 8.7, 9.1, 9.4, 9.8, 10.2, 10.6},
            {3.8, 4.3, 4.7, 5.1, 5.5, 6, 6.4, 6.8, 7.2, 7.7, 8.1, 8.5, 8.9, 9.4, 9.8, 10.2, 10.6, 11.1, 11.5, 11.9},
            {4.3, 4.8, 5.2, 5.7, 6.2, 6.7, 7.1, 7.6, 8.1, 8.6, 9, 9.5, 10, 10.5, 10.9, 11.4, 11.9, 12.4, 12.8, 13.3},
            {4.8, 5.3, 5.8, 6.3, 6.9, 7.4, 7.9, 8.4, 9, 9.5, 10, 10.6, 11.1, 11.6, 12.1, 12.7, 13.2, 13.7, 14.3, 14.8}
    };

    private final double[][] schaefferRapide = {
            {0.03, 0.04, 0.04, 0.04, 0.05, 0.05, 0.05, 0.06, 0.06, 0.06, 0.07, 0.07, 0.08, 0.08, 0.08, 0.09, 0.09, 0.09, 0.1, 0.1},
            {0.1, 0.1, 0.1, 0.1, 0.1, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.3, 0.3, 0.3, 0.3, 0.3},
            {0.2, 0.2, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.4, 0.4, 0.4, 0.4, 0.5, 0.5, 0.5, 0.5, 0.5, 0.6, 0.6, 0.6},
            {0.3, 0.4, 0.4, 0.4, 0.5, 0.5, 0.5, 0.6, 0.6, 0.6, 0.7, 0.7, 0.8, 0.8, 0.8, 0.9, 0.9, 0.9, 1, 1},
            {0.5, 0.5, 0.6, 0.6, 0.7, 0.8, 0.8, 0.9, 0.9, 1, 1, 1.1, 1.1, 1.2, 1.2, 1.3, 1.3, 1.4, 1.4, 1.5},
            {0.7, 0.8, 0.8, 0.9, 1, 1.1, 1.1, 1.2, 1.3, 1.4, 1.4, 1.5, 1.6, 1.7, 1.7, 1.8, 1.9, 2, 2, 2.1},
            {0.9, 1, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8},
            {1.2, 1.3, 1.4, 1.5, 1.7, 1.8, 1.9, 2.1, 2.2, 2.3, 2.4, 2.6, 2.7, 2.8, 3, 3.1, 3.2, 3.3, 3.5, 3.6},
            {1.4, 1.6, 1.8, 1.9, 2.1, 2.3, 2.4, 2.6, 2.7, 2.9, 3.1, 3.2, 3.4, 3.5, 3.7, 3.9, 4, 4.2, 4.3, 4.5},
            {1.8, 2, 2.2, 2.4, 2.6, 2.8, 2.9, 3.1, 3.3, 3.5, 3.7, 3.9, 4.1, 4.3, 4.5, 4.7, 4.9, 5.1, 5.3, 5.5},
            {2.1, 2.4, 2.6, 2.8, 3.1, 3.3, 3.5, 3.8, 4, 4.2, 4.5, 4.7, 5, 5.2, 5.4, 5.7, 5.9, 6.1, 6.4, 6.6},
            {2.5, 2.8, 3.1, 3.3, 3.6, 3.9, 4.2, 4.5, 4.7, 5, 5.3, 5.6, 5.9, 6.1, 6.4, 6.7, 7, 7.2, 7.5, 7.8},
            {2.9, 3.3, 3.6, 3.9, 4.2, 4.6, 4.9, 5.2, 5.5, 5.9, 6.2, 6.5, 6.8, 7.2, 7.5, 7.8, 8.1, 8.5, 8.8, 9.1},
            {3.4, 3.8, 4.1, 4.5, 4.9, 5.3, 5.6, 6, 6.4, 6.8, 7.1, 7.5, 7.9, 8.3, 8.6, 9, 9.4, 9.8, 10.1, 10.5},
            {3.9, 4.3, 4.7, 5.1, 5.6, 6, 6.4, 6.9, 7.3, 7.7, 8.1, 8.6, 9, 9.4, 9.9, 10.3, 10.7, 11.1, 11.6, 12},
            {4.4, 4.9, 5.3, 5.8, 6.3, 6.8, 7.3, 7.8, 8.3, 8.7, 9.2, 9.7, 10.2, 10.7, 11.2, 11.7, 12.1, 12.6, 13.1, 13.6},
            {4.9, 5.5, 6, 6.6, 7.1, 7.7, 8.2, 8.7, 9.3, 9.8, 10.4, 10.9, 11.5, 12, 12.6, 13.1, 13.7, 14.2, 14.8, 15.3},
            {5.5, 6.1, 6.7, 7.3, 7.9, 8.6, 9.2, 9.8, 10.4, 11, 11.6, 12.2, 12.8, 13.4, 14, 14.7, 15.3, 15.9, 16.5, 17.1}
    };

    private HashMap<String, Double> prixBoixChauffage = new HashMap<>();
    private HashMap<String, Double> prixBoixIndustrie = new HashMap<>();
    private HashMap<String, Double> prixBoixOeuvre = new HashMap<>();

    private HashMap<String, String> essences = new HashMap<>();

    private String nomTarifResineux;
    private int versionTarifResineux;

    private String nomTarifFeuillus;
    private int versionTarifFeuillus;

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

    public int getIndexDiametreTarif(int diametre) {
        int res = diametre < 15 ? 15 : (diametre > 100 ? 100 : diametre);
        return (res - 15) / 5;
    }



    public double getVersionDiametre(double[][] tarifMat, int version, int diametre) {
        return tarifMat[getIndexDiametreTarif(diametre)][version-1];
    }


    public double getDiametreVersion(double[][] tarifMat, int diametre, int version) {
        return getVersionDiametre(tarifMat, version, diametre);
    }

    public String getType(Tree tree) {
        return essences.get(tree.getEssence()).toLowerCase();
    }

    public double getVolumeCommercial(Tree tree) {
        double[][] tarifMatFeuillus = getTarifFromName(nomTarifFeuillus);
        double[][] tarifMatResineux = getTarifFromName(nomTarifResineux);
        if (tree.getEtat().equals(ETAT_VIVANT)) {
            if (getType(tree).equals("feuillu")) {
                return getVersionDiametre(tarifMatFeuillus, versionTarifFeuillus, tree.diametre);
            } else {
                return getVersionDiametre(tarifMatResineux, versionTarifResineux, tree.diametre);
            }
        }
        //un arbre mort n'a pas de volume commercialisable
        return 0d;
    }

    private double[][] getTarifFromName(String nomTarifResineux) {
        switch (nomTarifResineux) {
            case "algan":
                return algan;
            case "chaudé":
                return chaude;
            case "schaefferLent":
                return schaefferLent;
            case "schaefferRapide":
                return schaefferRapide;
            default:
                return algan;
        }
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
        HashMap<String, Double> prixBois = null;
        switch (utilisationBoisAttribute) {
            case CHAUFFAGE:
                prixBois = prixBoixChauffage;
                break;
            case OEUVRE:
                prixBois = prixBoixOeuvre;
                break;
            case INDUSTRIE:
                prixBois = prixBoixIndustrie;
                break;
            default:
                prixBois = prixBoixIndustrie;
                break;
        }

        Double prixBoisAttribute = prixBois.get(tree.getEssence());
        //si il n'y a pas de constante propre a l'essence, on fait selon le type
        if (prixBoisAttribute == null) {
            prixBoisAttribute = prixBois.get(getType(tree));
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
    public HashMap<String, Double> getValeurEcoBois(Tree tree) {
        HashMap<String, Double> valeurEcoBois = new HashMap<>();
        HashMap<String, Double> volComBois = getVolumeCommercialBois(tree);
        HashMap<String, Double> prixBois = getPrixBois(tree);
        valeurEcoBois.put(OEUVRE, volComBois.get(OEUVRE) * prixBois.get(OEUVRE));
        valeurEcoBois.put(CHAUFFAGE, volComBois.get(CHAUFFAGE) * prixBois.get(CHAUFFAGE));
        valeurEcoBois.put(INDUSTRIE, volComBois.get(INDUSTRIE) * prixBois.get(INDUSTRIE));
        return valeurEcoBois;
    }

    public double getValeurEco(Tree tree) {
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