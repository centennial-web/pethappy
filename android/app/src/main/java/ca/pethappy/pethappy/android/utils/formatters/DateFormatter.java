package ca.pethappy.pethappy.android.utils.formatters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.CANADA);
    private static final DateFormatter ourInstance = new DateFormatter();

    public static DateFormatter getInstance() {
        return ourInstance;
    }

    private DateFormatter() {
    }

    public String formatDateTime(Date dateTime) {
        return sdf.format(dateTime);
    }
}
