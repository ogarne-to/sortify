package com.example.jedrzej.sortify;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jedrz on 02.07.2016.
 */

public class Utils {

    private Utils() {}

    public static final int FORMAT_VISIBLE_ID = 0;
    public static final int FORMAT_DATE_TIME = 1;

    public static String getTime(long createdAt, int format) {

        Date date = new Date();
        date.setTime(createdAt);

        if (format == FORMAT_VISIBLE_ID) {
            //returns zetanumber
            SimpleDateFormat sdf = new SimpleDateFormat("yyMdHms", Locale.US);
            String ymdhms = sdf.format(date);
            Log.d("UTILS", ymdhms);
            return getZeta(Long.parseLong(ymdhms));
        } else if (format == FORMAT_DATE_TIME) {
            //returns DD-MM-YY HH:MM:SS
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            return sdf.format(date);
        } else {
            return null;
        }

    }

    public static long getNow() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getNowString() {
        return getTime(getNow(), FORMAT_DATE_TIME);
    }

    private static String getZeta(long input) {

        // For storing remainder
        int reminder;

        // For storing result
        String result = "";

        // Digits in zeta-decimal number system
        char zeta[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        while (input > 0) {
            reminder = (int)(input % 36);
            result = zeta[reminder] + result;
            input = input / 36;
        }
        Log.d("UTILS", result);
        return result;

    }

    public static void log(Class what, String message) {
        Log.d(what.getSimpleName(), message);
    }

}
