package martelapp.test.Class;

import java.util.HashMap;
import java.util.Iterator;
/**
 * Created by Sancho on 20/03/2018.
 */

public class Parcelle {

    public HashMap<String, Tree> arbres;
    public String nom = "";
    public String lieu ="";
    public float surface = 0f;

    public Parcelle(){};

    public Parcelle(String name, String place, float surface){
        this.nom = name;
        this.lieu = place;
        this.surface = surface;
    }

    public Parcelle(String name, String place, float surface, HashMap<String, Tree> trees){
        this.nom = name;
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
