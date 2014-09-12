package com.orcchg.javatask.numbenc.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
  
  @SuppressWarnings("unchecked")
  public static List<String> removeDuplicates(List<String> list) {
    Set<String> set = new TreeSet<>();
    set.addAll(list);
    List<Object> result = Arrays.asList(set.toArray());
    return (List<String>)(List<?>) result;
  }
  
  public static boolean hasAdjacentDigits(final String input_string) {
    // remove spaces
    String string = input_string.replaceAll(" ", "");
    for (int i = 1; i + 1 < string.length(); /* no-op */) {
      if (Character.isDigit(string.charAt(i))) {
        if (Character.isDigit(string.charAt(i - 1)) ||
            Character.isDigit(string.charAt(i + 1))) {
          return true;
        } else {
          i += 3;
          continue;
        }
      } else {
        i += 2;
        continue;
      }
    }
    return false;
  }
}
