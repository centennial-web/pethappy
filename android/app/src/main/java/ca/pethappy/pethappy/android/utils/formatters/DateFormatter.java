package ca.pethappy.pethappy.android.utils.formatters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.US);
    private static final DateFormatter ourInstance = new DateFormatter();

    public static DateFormatter getInstance() {
        return ourInstance;
    }

    private DateFormatter() {
    }

    public String formatDateTimeShort(Date dateTime) {
        return sdf.format(dateTime);
    }

    public String formatDateTimeMid(Date dateTime) {
        return sdf2.format(dateTime);
    }
}
