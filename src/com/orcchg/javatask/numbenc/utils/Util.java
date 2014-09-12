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
}
