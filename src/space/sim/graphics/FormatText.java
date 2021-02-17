package space.sim.graphics;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class to store methods dealing with text and strings to be displayed on screen.
 */
public class FormatText {

  /**
   * Prints an array of lines of text onto the screen. Each <code>String</code> in the array is
   * treated as a line, and the positioning is determined by the line spacing and base <b>x</b>
   * and <b>y</b> positions.
   *  @param g current <code>Graphics2D</code> object
   * @param text array of lines of text to print
   * @param x <b>x</b> position of upper-left corner
   * @param y <b>y</b> position of upper-left corner
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
   * Formats a time duration into a readable string. Formats milliseconds into years, days,
   * hours, minutes, seconds, and the remainder milliseconds.
   *
   * @param millis millisecond duration
   * @return Returns the formatted string.
   */
  public static String formatTime(long millis) {
    double seconds = (double) millis / 1000;
    int ms = (int) (double) millis % 1000;
    int s = (int) (seconds % 60);
    int m = (int) (seconds / 60) % 60;
    int h = (int) (seconds / 3600) % 24;
    int d = (int) (seconds / 86400) % 365;
    int y = (int) (seconds / 31536000);
    return fixLength(y) + "y:" + fixLength(d) + "d:" + fixLength(h) + "h:" + fixLength(m) + "m:" + fixLength(s) + "s." + ms + "ms";
  }

  /**
   * Formats a large number to be more readable. Groups the zeroes if possible, and appends the
   * correct unit. If value is over 1000, will use the kilo unit and divide the value by 1000.
   * (eg. 10000000 -> 10,000km)
   *
   * @param num number to format
   * @param unit base unit
   * @param kiloUnit unit 1000x base unit
   * @return Returns the formatted string.
   */
  public static String formatNum(double num, String unit, String kiloUnit) {
    String suffix = unit;
    if (num >= 1000) {
      num /= 1000;
      suffix = kiloUnit;
    }
    String str;
    if (num < 1000) {
      str = String.valueOf((double) Math.round(num * 100) / 100);
    } else if (num <= Long.MAX_VALUE){
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

  /**
   * Fixes a number strings length to two digits. If the length is less than two, it inserts a
   * zero at the front of the <code>String</code>.
   *
   * @param num number to format
   * @return Returns the formatted string.
   */
  private static String fixLength(int num) {
    String str = String.valueOf(num);
    if (str.length() < 2) {
      str = "0" + str;
    }
    return str;
  }

}
