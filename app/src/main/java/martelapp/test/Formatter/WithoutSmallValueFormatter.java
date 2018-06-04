package martelapp.test.Formatter;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Formatter utilisé pour le PieChart pour n'afficher que les valeurs supérieurs ou égales à 5.
 */

public class WithoutSmallValueFormatter implements IValueFormatter{

    protected DecimalFormat mFormat;

    public WithoutSmallValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0");
    }

    /**
     * Allow a custom decimalformat
     *
     * @param format
     */
    public WithoutSmallValueFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    // IValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if(value < 5f){
            return "";
        }
        return mFormat.format(value);
    }
}