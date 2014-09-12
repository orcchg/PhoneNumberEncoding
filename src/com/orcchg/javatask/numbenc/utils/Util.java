package com.orcchg.javatask.numbenc.utils;

public class Util {
  public static boolean isQuote(char character) {
    return character == '"';
  }
  
  public static int getCharShift(char character) {
    return character - 'a';
  }
  
  public static String remainDigitsOnly(final String number) {
    String digital_number = number.replaceAll("-|/", "");
    return digital_number;
  }
  
  public static String removeFirstChar(final String string) {
    return string.substring(1);
  }
}
