package martelapp.test.Class;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import martelapp.test.R;

/**
 * Created by cimin on 24/04/2018.
 */

public class ChartHelper {

    public static TextView createTextView(boolean endline, boolean endcolumn, View view){
        TextView text = new TextView(view.getContext(), null, R.style.frag3HeaderCol);
        int bottom = endline ? 1 : 0;
        int right = endcolumn ? 1 : 0;

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.3f);
        params.setMargins(1, 1, right, bottom);
        text.setLayoutParams(params);
        text.setPadding(4, 4, 10, 4);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(view.getResources().getColor(R.color.colorBlack));
        text.setBackgroundColor(Color.WHITE);
        text.setTextSize(20f);
        return text;
    }
}
