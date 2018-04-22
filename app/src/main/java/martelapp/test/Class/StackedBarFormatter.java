package martelapp.test.Class;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Baptiste on 17/04/2018.
 */

public class StackedBarFormatter implements IValueFormatter {

    private DecimalFormat format;
    private String appendix;

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

        if(entry instanceof BarEntry){

            BarEntry barEntry = (BarEntry) entry;
            float[] values = barEntry.getYVals();
            StringBuffer result = new StringBuffer();
            int top = 1;

            if(values != null){

                for(; top <= values.length; top++){
                    if(values[values.length - top] != 0){
                        break;
                    }
                }

                if(values[values.length - top] == value) {
                    for (int i = 0; i < values.length; i++) {
                        result.append(format.format(values[i]));
                        if (i != values.length - 1) {
                            result.append(appendix);
                        }
                    }
                    return result.toString();
                }
            }
        }

        return "";
    }
}
