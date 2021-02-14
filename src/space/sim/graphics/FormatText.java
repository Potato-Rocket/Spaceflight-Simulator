package space.sim.graphics;

import java.awt.*;

public class FormatText {

  public static void drawText(Graphics2D g, String[] text, int x, int y, double lineSpacing) {
    for (int l = 0; l < text.length; l++) {
      g.drawString(text[l], x, y + 9 + (int) (lineSpacing * 12 * l));
    }
  }

  public static String formatTime(long millis) {
    double seconds = (double) millis / 1000;
    String s = String.valueOf(Math.round((seconds % 60) * 100) / 100.0);
    if (seconds % 60 < 10) {
      s = "0" + s;
    }
    int m = (int) (seconds / 60) % 60;
    int h = (int) (seconds / 3600) % 24;
    int d = (int) (seconds / 86400) % 365;
    int y = (int) (seconds / 31536000);
    return fixLength(y) + "y:" + fixLength(d) + "d:" + fixLength(h) + "h:" + fixLength(m) + "m:" + s + "s";
  }

  public static String formatNum(double num, String unit, String kiloUnit) {
    String suffix = unit;
    if (num >= 1000) {
      num /= 1000;
      suffix = kiloUnit;
    }
    String str = String.valueOf((long) num);
    if (str.length() > 3) {
      StringBuilder grouped = new StringBuilder();
      for (int i = str.length() - 1; i >= 0; i--) {
        grouped.insert(0, str.charAt(i));
        if (i > 0 && (str.length() - i) % 3 == 0) {
          grouped.insert(0, ",");
        }
      }
      str = grouped.toString();
    }
    return str + suffix;
  }

  private static String fixLength(int num) {
    String str = String.valueOf(num);
    if (str.length() < 2) {
      str = "0" + str;
    }
    return str;
  }

}
