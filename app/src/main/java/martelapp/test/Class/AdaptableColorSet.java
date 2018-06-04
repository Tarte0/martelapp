package martelapp.test.Class;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Classe utilisée pour créer des couleurs différentes en fonction du nombre donné en entrée
 */

public class AdaptableColorSet {

    public static ArrayList<Integer> createColorSet(int entryNb){
        if(entryNb<=0){
            return null;
        }
        ArrayList<Integer> colors = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < 360; i += 360 / entryNb) {

            float hue = i;
            float saturation = 1f;
            float lightness = 0.75f;

            colors.add(Color.HSVToColor(new float[]{hue, saturation, lightness}));
        }
        return colors;
    }

    public static int[] listToArray(ArrayList<Integer> list)
    {
        int[] res = new int[list.size()];
        for (int i=0; i < res.length; i++)
        {
            res[i] = list.get(i);
        }
        return res;
    }

}
