package martelapp.test.Adapter;

import android.content.Context;
import android.database.Cursor;
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

    public ArbreMartelesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.arbres_marteles_row_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView numeroArbreMartele = (TextView) view.findViewById(R.id.numero_arbre_martele);
        TextView essenceArbreMartele = (TextView) view.findViewById(R.id.essence_arbre_martele);
        TextView diameteArbreMartele = (TextView) view.findViewById(R.id.diametre_arbre_martele);

        numeroArbreMartele.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC)));
        essenceArbreMartele.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ESSENCE_ARBRE)));
        diameteArbreMartele.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DIAMETRE_ARBRE))));

        showRaisons(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMERO_ARBRE_PARC)), context, view);
    }

    //on affiche les icones associees aux raisons du martelage
    private void showRaisons(String numeroArbre, Context context, View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        Cursor raisonsCur = dbHelper.executeQuery("Select *"
                + " FROM " + DatabaseHelper.RAISON_TABLE
                + " WHERE " + DatabaseHelper.NUMERO_ARBRE_MARTELE_RAISON + " = " + numeroArbre);

        ImageView arbreMur = (ImageView) view.findViewById(R.id.arbreMurIcon);
        arbreMur.setVisibility(View.INVISIBLE);
        ImageView eclaircie = (ImageView) view.findViewById(R.id.eclaircieIcon);
        eclaircie.setVisibility(View.INVISIBLE);
        ImageView sanitaire = (ImageView) view.findViewById(R.id.sanitaireIcon);
        sanitaire.setVisibility(View.INVISIBLE);
        ImageView regeneration = (ImageView) view.findViewById(R.id.regenerationIcon);
        regeneration.setVisibility(View.INVISIBLE);
        ImageView exploitation = (ImageView) view.findViewById(R.id.exploitationIcon);
        exploitation.setVisibility(View.INVISIBLE);
        ImageView stabilite = (ImageView) view.findViewById(R.id.stabiliteIcon);
        stabilite.setVisibility(View.INVISIBLE);

        if (raisonsCur.moveToFirst()) {
            do {
                switch (raisonsCur.getString(raisonsCur.getColumnIndex(DatabaseHelper.RAISON))) {
                    case DatabaseHelper.ARBRE_MUR:
                        arbreMur.setVisibility(View.VISIBLE);
                        break;
                    case DatabaseHelper.ECLAIRCIE:
                        eclaircie.setVisibility(View.VISIBLE);
                        break;
                    case DatabaseHelper.SANITAIRE:
                        sanitaire.setVisibility(View.VISIBLE);
                        break;
                    case DatabaseHelper.REGENERATION:
                        regeneration.setVisibility(View.VISIBLE);
                        break;
                    case DatabaseHelper.EXPLOITATION:
                        exploitation.setVisibility(View.VISIBLE);
                        break;
                    case DatabaseHelper.STABILITE:
                        stabilite.setVisibility(View.VISIBLE);
                        break;
                }
            } while (raisonsCur.moveToNext());
        }

    }
}
