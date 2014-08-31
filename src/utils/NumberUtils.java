package utils;

public final class NumberUtils {

  private NumberUtils() { throw new RuntimeException("Should not be instanciated."); }

  public static String toString(int num, int digits) {
    String ns = String.valueOf(num);
    while (ns.length() < digits)
      ns = '0' + ns;
    return ns;
  }

  public static String toString(int num) {
    return toString(num, 2);
  }
}