package martelapp.test.Class;

import java.util.HashMap;
import java.util.Iterator;
/**
 * Created by Sancho on 20/03/2018.
 */

public class Parcelle {

    public double altitude = 0;
    public HashMap<String, Tree> arbres;
    public String habitat;
    public String nom = "";
    public String lieu ="";
    public double surface = 0f;

    public Parcelle(){};

    public Parcelle(String name, String place, double surface){
        this.nom = name;
        this.lieu = place;
        this.surface = surface;
    }

    public Parcelle(double altitude, String habitat, String nom, String place, double surface, HashMap<String, Tree> trees){
        this.altitude = altitude;
        this.habitat = habitat;
        this.nom = nom;
        this.lieu = place;
        this.surface = surface;
        this.arbres = trees;
    }

    public String toString(){
        String s = nom + ", "+ lieu + ", " + surface +"\n";
        if(!arbres.isEmpty()){
            s+= "arbres : \n";
            Iterator it = arbres.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry)it.next();
                s+="\t-" + pair.getValue() + "\n";
                it.remove();
            }
        }
        return s;
    }
}
