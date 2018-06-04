package martelapp.test.Formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Formatter utilisé pour les PieChart pour affiché les valeurs en pourcentage qui sont supérieur à
 * 5%
 */

public class PercentWithoutSmallValueFormatter implements IValueFormatter, IAxisValueFormatter
{

    protected DecimalFormat mFormat;

    public PercentWithoutSmallValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }

    /**
     * Allow a custom decimalformat
     *
     * @param format
     */
    public PercentWithoutSmallValueFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    // IValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if(value < 5f){
            return "";
        }
        return mFormat.format(value) + " %";
    }

    // IAxisValueFormatter
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(value < 5f){
            return "";
        }
        return mFormat.format(value) + " %";
    }

    public int getDecimalDigits() {
        return 1;
    }
}