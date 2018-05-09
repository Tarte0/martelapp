package martelapp.test.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import martelapp.test.Class.DatabaseHelper;
import martelapp.test.R;

/**
 * Created by Baptiste on 25/03/2018.
 */

public class ArbreMartelesAdapter extends CursorAdapter {
    DatabaseHelper dbHelper;
    Cursor cur;


    public ArbreMartelesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.arbres_marteles_row_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView numeroArbreMartele =  view.findViewById(R.id.numero_arbre_martele);
        TextView essenceArbreMartele =  view.findViewById(R.id.essence_arbre_martele);
        TextView diameteArbreMartele =  view.findViewById(R.id.diametre_arbre_martele);

        numeroArbreMartele.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC)));
        essenceArbreMartele.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
        diameteArbreMartele.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE))));




        dbHelper = new DatabaseHelper(view.getContext());


        String numero = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC));
        cur = dbHelper.executeQuery("Select *"
                + " FROM " + DatabaseHelper.RAISON_TABLE
                + " WHERE " + DatabaseHelper.NUMERO_ARBRE_MARTELE_RAISON + " = " + numero);
        cur.moveToFirst();


        String raison = cur.getString(cur.getColumnIndex(DatabaseHelper.RAISON));

        if(raison.equals(DatabaseHelper.BIODIVERSITE)){
            numeroArbreMartele.setTextColor(Color.GREEN);
            essenceArbreMartele.setTextColor(Color.GREEN);
            diameteArbreMartele.setTextColor(Color.GREEN);
        }
        else {
            numeroArbreMartele.setTextColor(Color.parseColor("#FFA500"));
            essenceArbreMartele.setTextColor(Color.parseColor("#FFA500"));
            diameteArbreMartele.setTextColor(Color.parseColor("#FFA500"));
        }
    }
}