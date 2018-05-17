package martelapp.test.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.Fragment.Exercice.ChoixMartelageFragment;
import martelapp.test.R;

/**
 * Created by Baptiste on 25/03/2018.
 */

public class ArbresMartelesAdapter extends CursorAdapter {

    private boolean analyse = false;

    public ArbresMartelesAdapter(Context context, Cursor cursor, boolean analyse) {
        super(context, cursor, 0);
        this.analyse = analyse;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.arbres_traites_row_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView numeroArbreTraite =  view.findViewById(R.id.numero_arbre_traite);
        TextView essenceArbreTraite =  view.findViewById(R.id.essence_arbre_traite);
        TextView diameteArbreTraite =  view.findViewById(R.id.diametre_arbre_traite);
        TextView noteEcoArbreTraite = view.findViewById(R.id.note_eco_arbre_traite);
        ImageView imageArbreTraite = view.findViewById(R.id.image_arbre_traite);

        numeroArbreTraite.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC)));
        essenceArbreTraite.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
        diameteArbreTraite.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE))));


        imageArbreTraite.setImageResource(R.drawable.marteau_tab);

        int noteEcologique = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.NOTE_ECO_ARBRE));

        if(analyse){
            noteEcoArbreTraite.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.NOTE_ECO_ARBRE))));
            noteEcoArbreTraite.setVisibility(View.VISIBLE);
            if(noteEcologique > ChoixMartelageFragment.noteEcologiqueHaute) {
                numeroArbreTraite.setTextColor(view.getResources().getColor(R.color.colorRed));
                essenceArbreTraite.setTextColor(view.getResources().getColor(R.color.colorRed));
                diameteArbreTraite.setTextColor(view.getResources().getColor(R.color.colorRed));
                numeroArbreTraite.setTextColor(view.getResources().getColor(R.color.colorRed));
                noteEcoArbreTraite.setTextColor(view.getResources().getColor(R.color.colorRed));
                imageArbreTraite.setColorFilter(imageArbreTraite.getContext().getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
            }
        }
        else{
            noteEcoArbreTraite.setVisibility(View.GONE);
        }
    }
}