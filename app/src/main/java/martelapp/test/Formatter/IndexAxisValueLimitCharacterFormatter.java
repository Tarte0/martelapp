package martelapp.test.Formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Collection;

/**
 * Formatter utilisé pour les graphes affichant des données sur les axes avec une option de limiter
 * les caractères
 */

public class IndexAxisValueLimitCharacterFormatter implements IAxisValueFormatter {

        private String[] mValues = new String[] {};
        private int mValueCount = 0;
        private int limitChar;

        /**
         * An empty constructor.
         * Use `setValues` to set the axis labels.
         */
    public IndexAxisValueLimitCharacterFormatter() {
    }

        /**
         * Constructor that specifies axis labels.
         *
         * @param values The values string array
         */
    public IndexAxisValueLimitCharacterFormatter(String[] values, int limitChar) {
        if (values != null)
            setValues(values);
        this.limitChar = limitChar;
    }

        /**
         * Constructor that specifies axis labels.
         *
         * @param values The values string array
         */
    public IndexAxisValueLimitCharacterFormatter(Collection<String> values, int limitChar) {
        if (values != null)
            setValues(values.toArray(new String[values.size()]));

        this.limitChar = limitChar;
    }

    public String getFormattedValue(float value, AxisBase axis) {
        int index = Math.round(value);

        if (index < 0 || index >= mValueCount || index != (int)value)
            return "";

        if(mValues[index].length() > limitChar) {
            return mValues[index].substring(0,limitChar) + "...";
        }
        return mValues[index];
    }

    public String[] getValues()
    {
        return mValues;
    }

    public void setValues(String[] values)
    {
        if (values == null)
            values = new String[] {};

        this.mValues = values;
        this.mValueCount = values.length;
    }
}

