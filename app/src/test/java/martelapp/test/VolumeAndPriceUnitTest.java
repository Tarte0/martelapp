package martelapp.test;

import org.junit.Test;

import java.util.HashMap;

import martelapp.test.Class.Tree;
import martelapp.test.Class.VolumeCalculator;

import static org.junit.Assert.assertEquals;

/**
 * Created by cimin on 18/04/2018.
 */

public class VolumeAndPriceUnitTest {

    //constantes pate a code
    private double hauteurMoyennePetitBois = 15d;
    private double hauteurMoyenneFeuillu = 10d;
    private double hauteurMoyenneResineux = 20d;
    private double prixBoisChauffageFeuillu = 15d;
    private double prixBoisChauffageResineux = 15d;
    private double prixBoisIndustrieFeuillu = 5d;
    private double prixBoisIndustrieResineux = 5d;
    private double prixBoisOeuvreEpicea = 45d;
    private double prixBoisOeuvreFeuillu = 40d;
    private double prixBoisOeuvreResineux = 40d;
    private double prixBoisOeuvreSapin = 40d;
    private double volumeCommercialFeuillu = 0.55;
    private double volumeCommercialResineux = 0.42;

    Tree tree;

    @Test
    public void volumeAndPricesVariousTrees() throws Exception {
        //initialisation *************************************
        HashMap<String, Double> constants = new HashMap<>();
        constants.put("hauteurMoyennePetitBois", hauteurMoyennePetitBois);
        constants.put("hauteurMoyennefeuillu", hauteurMoyenneFeuillu);
        constants.put("hauteurMoyennerésineux", hauteurMoyenneResineux);
        constants.put("prixBoischauffagefeuillu", prixBoisChauffageFeuillu);
        constants.put("prixBoischauffagerésineux", prixBoisChauffageResineux);
        constants.put("prixBoisindustriefeuillu", prixBoisIndustrieFeuillu);
        constants.put("prixBoisindustrierésineux", prixBoisIndustrieResineux);
        constants.put("prixBoisoeuvreepicéa", prixBoisOeuvreEpicea);
        constants.put("prixBoisoeuvrefeuillu", prixBoisOeuvreFeuillu);
        constants.put("prixBoisoeuvrerésineux", prixBoisOeuvreResineux);
        constants.put("prixBoisoeuvresapin", prixBoisOeuvreSapin);
        constants.put("volumeCommercialfeuillu", volumeCommercialFeuillu);
        constants.put("volumeCommercialrésineux", volumeCommercialResineux);

        HashMap<String, String> essences = new HashMap<>();
        essences.put("alisier blanc", "feuillu");
        essences.put("aulne", "feuillu");
        essences.put("chataignier", "feuillu");
        essences.put("chêne", "feuillu");
        essences.put("epicéa", "résineux");
        essences.put("frêne", "feuillu");
        essences.put("hêtre", "feuillu");
        essences.put("if", "résineux");
        essences.put("merisier", "feuillu");
        essences.put("sapin", "résineux");
        essences.put("tilleul", "feuillu");

        //fin initialisation **********************************

        //tests
        VolumeCalculator vc = new VolumeCalculator(constants, essences);

        Tree t146 = new Tree("146", "60", "4", "epicéa", "v",
                "0", "0", "100", 80.15, 66.59);

        Tree t14 = new Tree("14", "65", "0", "sapin", "v",
                "0", "34", "66", 60.86, 17.83);

        Tree t62 = new Tree("62", "25", "2", "hêtre", "mp",
                "100", "0", "0", 56.58, 44.58);

        Tree t352 = new Tree("352", "35", "0", "frêne", "v",
                "100", "0", "0", 43.44, 11.49);

        Tree t270 = new Tree("270", "15", "0", "epicéa", "v",
                "0", "100", "0", 41.89, 41.58);

        //le volume commercial est testé à 10cm^3 près
        //la valeur economique est testée à 5€ près

        //epicéa
        assertEquals(3.02, vc.getVolumeCommercial(t146), 0.1);
        assertEquals(136.08, vc.getValeurEco(t146), 5);

        //sapin
        assertEquals(3.55, vc.getVolumeCommercial(t14), 0.1);
        assertEquals(99.75, vc.getValeurEco(t14), 5);

        //mp
        assertEquals(0.0, vc.getVolumeCommercial(t62), 0);
        assertEquals(0.0, vc.getValeurEco(t62), 0);

        //standard
        assertEquals(0.67, vc.getVolumeCommercial(t352), 0.1);
        assertEquals(10.05, vc.getValeurEco(t352), 5);

        //petit bois
        assertEquals(0.14, vc.getVolumeCommercial(t270), 0.1);
        assertEquals(0.7, vc.getValeurEco(t270), 5);
    }
}
