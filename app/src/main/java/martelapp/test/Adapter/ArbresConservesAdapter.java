package martelapp.test.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Fragment.Exercice.ChoixMartelageFragment;
import martelapp.test.R;

/**
 * Created by Baptiste on 09/05/2018.
 */

public class ArbresConservesAdapter extends CursorAdapter {

    private boolean analyse = false;

    public ArbresConservesAdapter(Context context, Cursor cursor, boolean analyse) {
        super(context, cursor, 0);
        this.analyse = analyse;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.arbres_traites_row_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        RelativeLayout layoutArbreMarteles = view.findViewById(R.id.layout_arbres_traites);

        TextView numeroArbreMartele =  view.findViewById(R.id.numero_arbre_traite);
        TextView essenceArbreMartele =  view.findViewById(R.id.essence_arbre_traite);
        TextView diameteArbreMartele =  view.findViewById(R.id.diametre_arbre_traite);

        numeroArbreMartele.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC)));
        essenceArbreMartele.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
        diameteArbreMartele.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE))));

        int noteEcologique = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.NOTE_ECO_ARBRE));

        if(analyse){
            if(noteEcologique > ChoixMartelageFragment.noteEcologiqueHaute) {
                layoutArbreMarteles.setBackgroundColor(Color.GREEN);
            }
            else{
                layoutArbreMarteles.setBackgroundColor(view.getResources().getColor(R.color.colorWhite));
            }
        }
        else{
            layoutArbreMarteles.setBackgroundColor(view.getResources().getColor(R.color.colorBarOrange));
        }
    }
}

