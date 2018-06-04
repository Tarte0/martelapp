package martelapp.test.Formatter;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Formatter utilisé pour les BarChart avec plusieurs valeurs sur une bar. Affiche toutes les valeurs
 * des différents éléments d'une bar au dessus de la bar avec un séparateur "appendix"
 */

public class StackedBarFormatter implements IValueFormatter {

    private DecimalFormat format;
    private String appendix;

    private int indexValue = 0;
    private int top = 1;
    private boolean topValue = false;
    private boolean bottomValue = false;

    public StackedBarFormatter(String appendix, int decimals){
        this.appendix = appendix;

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < decimals; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }

        this.format = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        if(entry instanceof BarEntry) {


            BarEntry barEntry = (BarEntry) entry;

            float[] values = barEntry.getYVals();
            StringBuffer result = new StringBuffer();

            if (values != null) {

                if(!bottomValue){
                    for(int i = 0; i < values.length; i++){
                        if(values[i] != 0){
                            bottomValue = true;
                            indexValue = i;
                            break;
                        }
                    }
                }

                if(!topValue) {
                    for (; top <= values.length; top++) {
                        if (values[values.length - top] != 0) {
                            topValue = true;
                            break;
                        }
                    }
                }

                if (indexValue < values.length - top) {
                    indexValue++;
                    return "";
                }

                indexValue = 0;
                top = 1;
                topValue = false;
                bottomValue = false;

                for (int i = 0; i < values.length; i++) {
                    result.append(format.format(values[i]));
                    if (i != values.length - 1) {
                        result.append(appendix);
                    }
                }
                return result.toString();
            }
        }

        return "";
    }
}
