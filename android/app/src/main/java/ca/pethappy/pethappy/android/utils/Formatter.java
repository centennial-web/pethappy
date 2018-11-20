package ca.pethappy.pethappy.android.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {
    private static final DecimalFormat df2 = (DecimalFormat) NumberFormat.getNumberInstance(Locale.CANADA);

    private static Formatter ourInstance = new Formatter();

    static {
        // Formatter
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
    }

    public static Formatter getInstance() {
        return ourInstance;
    }

    private Formatter() {
    }

    public String formatNumber2(BigDecimal n) {
        return df2.format(n.setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }
}
