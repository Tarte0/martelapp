package martelapp.test.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by Baptiste on 09/05/2018.
 */

public class ArbresConservesAdapter extends CursorAdapter {

    public ArbresConservesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.arbres_conserves_row_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView numeroArbreMartele =  view.findViewById(R.id.numero_arbre_conserve);
        TextView essenceArbreMartele =  view.findViewById(R.id.essence_arbre_conserve);
        TextView diameteArbreMartele =  view.findViewById(R.id.diametre_arbre_conserve);

        numeroArbreMartele.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC)));
        essenceArbreMartele.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
        diameteArbreMartele.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE))));
    }
}

