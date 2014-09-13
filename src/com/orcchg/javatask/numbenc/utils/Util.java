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
    return character - 'A';
  }
  
  public static String remainDigitsOnly(final String number) {
    String digital_number = number.replaceAll("-|/", "");
    return digital_number;
  }
  
  public static String remainLettersOnly(final String word) {
    String alpha_word = word.replaceAll("\"| ", "");
    return alpha_word;
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
    if (string.length() == 2 &&
        Character.isDigit(string.charAt(0)) &&
        Character.isDigit(string.charAt(1))) {
      return true;
    }
    
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
  
  public static char convertToUmlaut(char character) {
    char converted = 0;
    switch (character) {
      case 'a':
      case 'A':
        converted = '{';
        break;
      case 'o':
      case 'O':
        converted = '|';
        break;
      case 'u':
      case 'U':
        converted = '}';
        break;
      default:
        throw new IllegalArgumentException("Unable to convert [" + character + "] to umlaut.");
    }
    return converted;
  }
  
  public static boolean isUmlaut(char character) {
    if (character == '{' || character == '|' || character == '}') {
      return true;
    }
    return false;
  }
  
  public static char convertFromUmlaut(char character) {
    char converted = 0;
    switch (character) {
      case '{':
        converted = 'a';
        break;
      case '|':
        converted = 'o';
        break;
      case '}':
        converted = 'u';
        break;
      default:
        throw new IllegalArgumentException("Character [" + character + "] has no mapping on set of umlauts.");
    }
    return converted;
  }
  
  public static void printList(final String prefix, final List<String> list) {
    System.out.print(prefix + " ");
    for (String word : list) {
      System.out.print(word + " ");
    }
    System.out.println("");
  }
}
