package dev.isnow.mcrekus.util;

import java.util.Formatter;
import java.util.Locale;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtil {
    public String formatElapsedTime(long elapsedMilliseconds) {
        // Break the elapsed milliseconds into seconds and milliseconds. From Android
        long seconds = 0;
        long milliseconds;
        if (elapsedMilliseconds >= 1000) {
            seconds = elapsedMilliseconds / 1000;
            elapsedMilliseconds -= seconds * 1000;
        }
        milliseconds = elapsedMilliseconds;
        final StringBuilder sb = new StringBuilder(8);

        final Formatter f = new Formatter(sb, Locale.getDefault());

        return f.format("%d.%03d", seconds, milliseconds).toString();
    }

    public String formatNewTime(final long elapsedMilliseconds) {
        long seconds = elapsedMilliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        hours %= 24;
        minutes %= 60;

        final StringBuilder timeString = new StringBuilder();
        if (days > 0) {
            timeString.append(days).append("d ");
        }
        timeString.append(hours).append("h ");
        timeString.append(minutes).append("m");

        RekusLogger.debug("Time: " + timeString.toString().trim() + " | Elapsed: " + elapsedMilliseconds);
        return timeString.toString().trim();
    }
}