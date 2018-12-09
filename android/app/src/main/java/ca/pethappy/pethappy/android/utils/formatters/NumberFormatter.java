package ca.pethappy.pethappy.android.utils.formatters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {
    private static final DecimalFormat df2 = (DecimalFormat) NumberFormat.getNumberInstance(Locale.CANADA);
    private static final DecimalFormat dfp = (DecimalFormat) NumberFormat.getPercentInstance(Locale.CANADA);

    private static NumberFormatter ourInstance = new NumberFormatter();

    static {
        // NumberFormatter
        df2.setMinimumFractionDigits(2);
        df2.setMaximumFractionDigits(2);
        df2.setRoundingMode(RoundingMode.HALF_EVEN);
        df2.setGroupingUsed(true);

        // Symbol
        DecimalFormatSymbols symbols2 = df2.getDecimalFormatSymbols();
        symbols2.setDecimalSeparator('.');
        symbols2.setGroupingSeparator(',');

        // Set symbol to formatter
        df2.setDecimalFormatSymbols(symbols2);


        //
        // Percentage
        //
        dfp.setMinimumFractionDigits(1);
    }

    public static NumberFormatter getInstance() {
        return ourInstance;
    }

    private NumberFormatter() {
    }

    public String formatNumber2(BigDecimal n) {
        return df2.format(n.setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    public String formatPercentage(BigDecimal n) {
        return dfp.format(n.movePointLeft(2));
    }
}
