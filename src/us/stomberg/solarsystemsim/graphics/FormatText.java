package us.stomberg.solarsystemsim.graphics;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;

// TODO: Replace with standard string formatting methods.

/**
 * Class to store methods dealing with text and strings to be displayed on screen.
 */
public class FormatText {

    /**
     * Prints an array of lines of text onto the screen. Each <code>String</code> in the array is treated as a line, and
     * the positioning is determined by the line spacing and base <b>x</b> and <b>y</b> positions.
     *
     * @param g           current <code>Graphics2D</code> object
     * @param text        array of lines of text to print
     * @param x           <b>x</b> position of upper-left corner
     * @param y           <b>y</b> position of upper-left corner
     * @param lineSpacing distance between lines measured in character heights
     */
    public static void drawText(Graphics2D g, ArrayList<String> text, int x, int y, double lineSpacing) {
        for (int l = 0; l < text.size(); l++) {
            g.drawString(text.get(l), x, y + 9 + (int) (lineSpacing * 12 * l));
        }
    }

    public static void drawText(Graphics2D g, String text, int x, int y) {
        g.drawString(text, x, y + 9);
    }

    /**
     * Formats a time duration into a readable string. Formats milliseconds into years, days, hours, minutes, seconds,
     * and the remainder milliseconds.
     *
     * @return Returns the formatted string.
     */
    public static String formatTime(long amount, TemporalUnit unit) {
        Duration duration = Duration.of(amount, unit);
        long years = duration.toDays() / 365;
        long days = duration.toDays() % 365;
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%dy:%03dd:%02dh:%02dm:%02ds", years, days, hours, minutes, seconds);
    }

    /**
     * Formats a large number to be more readable. Groups the zeroes if possible, and appends the correct unit. If value
     * is over 10000, will use the kilo unit and divide the value by 1000. (eg. 10000000 -> 10,000km)
     *
     * @param num      number to format
     * @param unit     base unit
     * @param kiloUnit unit 1000x base unit
     * @return Returns the formatted string.
     */
    public static String formatNum(double num, String unit, String kiloUnit) {
        String suffix = unit;
        if (num >= 10000) {
            num /= 1000;
            suffix = kiloUnit;
        }
        String str;
        if (num < 1000) {
            str = String.valueOf((double) Math.round(num * 100) / 100);
        } else if (num <= Long.MAX_VALUE) {
            str = String.valueOf((long) num);
            StringBuilder grouped = new StringBuilder();
            for (int i = str.length() - 1; i >= 0; i--) {
                grouped.insert(0, str.charAt(i));
                if (i > 0 && (str.length() - i) % 3 == 0) {
                    grouped.insert(0, ",");
                }
            }
            str = grouped.toString();
        } else {
            int exp = 1;
            while (Math.pow(10, exp + 1) < num) {
                exp++;
            }
            num /= Math.pow(10, exp);
            str = ((double) Math.round(num * 10000) / 10000) + " E" + exp + " ";
        }
        return str + suffix;
    }

}
