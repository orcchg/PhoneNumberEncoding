package com.orcchg.javatask.numbenc.struct;

public class LookupTable {
  public static final int TABLE_SIZE = 29;
  
  public static char[][] map = new char[][]{
    {'e'},                 // 0
    {'j', 'n', 'q'},       // 1
    {'r', 'w', 'x'},       // 2
    {'d', 's', 'y'},       // 3
    {'f', 't'},            // 4
    {'a', '{', 'm'},       // 5   { - a"
    {'c', 'i', 'v'},       // 6
    {'b', 'k', 'u', '}'},  // 7   } - u"
    {'l', 'o', '|', 'p'},  // 8   | - o"
    {'g', 'h', 'z'},       // 9
  };
}
