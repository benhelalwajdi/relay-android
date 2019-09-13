package app.com.relay.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormat {

    public static String format(String price) {

        NumberFormat numberFormat = new DecimalFormat("#,###");
        numberFormat.setMaximumFractionDigits(3);
        return numberFormat.format(Float.valueOf(price)) + " DT";
    }
}
