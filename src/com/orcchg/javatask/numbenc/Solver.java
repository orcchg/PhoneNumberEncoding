package com.orcchg.javatask.numbenc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.orcchg.javatask.numbenc.struct.Automaton;
import com.orcchg.javatask.numbenc.struct.AutomatonNode;
import com.orcchg.javatask.numbenc.struct.LookupTable;
import com.orcchg.javatask.numbenc.utils.Util;

public class Solver {
  private final Map<Character, Automaton> mDictionary;
  
  public Solver() {
    mDictionary = new HashMap<Character, Automaton>(26);
  }
  
  public Automaton addEmptyAutomaton(char label) {
    label = Character.toLowerCase(label);
    Automaton automaton = new Automaton(label);
    mDictionary.put(label, automaton);
    return automaton;
  }
  
  public Automaton getAutomaton(char label) {
    label = Character.toLowerCase(label);
    return mDictionary.get(label);
  }
  
  public void solve(final String number) {
    String digital_number = Util.remainDigitsOnly(number);
    processNumber(digital_number);
  }
  
  public List<String> processNumber(final String digital_number) {
    List<String> answer = new ArrayList<>(3000);
    
    char first_digit = digital_number.charAt(0);
    List<Automaton> accept_automata = getAllSuitableAutomata(first_digit);
    if (accept_automata.isEmpty()) {
      // no word representation for such number
      return answer;
    }

    Map<Character, List<Integer>> node_index = new HashMap<>(3);
    for (Automaton automaton : accept_automata) {
      AutomatonNode node = automaton.getNode(0);
      List<Integer> list = new ArrayList<>();
      list.add(node.getIndex());
      node_index.put(automaton.getID(), list);
    }
    
    // walk through each automaton obtaining terminal states - words from dictionary
    for (Automaton automaton : accept_automata) {
      List<Integer> queue = node_index.get(automaton.getID());
      StringBuilder preword = new StringBuilder().append(automaton.getID());  // start word with root character
      
      for (int i = 1; i < digital_number.length(); ++i) {
        char next_digit = digital_number.charAt(i);
        int next_value = Character.getNumericValue(next_digit);
        
          int null_counter = 0;
          for (char label : LookupTable.map[next_value]) {
            
            
              AutomatonNode node = automaton.makeTransition(index, label);
              if (node != null) {
                preword.append(label);
                queue.add(node.getIndex());  // next node to be visited
                if (node.isTerminal()) {
                  String word = preword.toString();
                  answer.add(word);
                  // to next prefix or continue
                }
              } else {
                ++null_counter;
              }
            
          }
          
          if (null_counter == LookupTable.map[next_value].length) {
            // no transitions from current node (index) for given digit (label)
            
          }
          
        }
    }

    return answer;
  }
  
  /* Private methods */
  // --------------------------------------------------------------------------
  private List<String> getAllWords(final Automaton automaton, final String digital_number) {
    List<String> answer = new ArrayList<>(3000);
    StringBuilder prefix = new StringBuilder().append(automaton.getID());  // start word with root character
    
    List<AutomatonNode> terminal_nodes = new ArrayList<>(3000);
    
    Queue<Integer> track = new LinkedList<>();
    Queue<Integer> buffer = new LinkedList<>();
    track.add(automaton.getNode(0).getIndex());  // start walking from root node
    buffer.addAll(track);
    
    int suffix_index = 0;
    int i = 1;
    char next_digit = digital_number.charAt(i);
    int next_value = Character.getNumericValue(next_digit);
    
    while (!track.isEmpty()) {
      for (char label : LookupTable.map[next_value]) {
        int index = buffer.peek();
        AutomatonNode node = automaton.makeTransition(index, label);
        if (node != null) {
          track.add(node.getIndex());
          if (node.isTerminal()) {
            terminal_nodes.add(node);
          }
        }
      }
      buffer.poll();
      track.poll();
      if (buffer.isEmpty() && !track.isEmpty()) {
        buffer.addAll(track);
        ++i;
        next_digit = digital_number.charAt(i);
        next_value = Character.getNumericValue(next_digit);
      }
    }
    // prefix reached end of automaton
        
        
        
//        if (node != null) {
//          index = node.getIndex();
//          track.add(index);
//          if (node.isUpperCase()) {
//            prefix.append(Character.toUpperCase(label));
//          } else {
//            prefix.append(label);
//          }
//          if (node.hasUmlaut()) {
//            prefix.append('"');
//          }
//          if (node.isTerminal()) {
//            answer.add(prefix.toString());
//          }
//        } else {
//          // no transition from node by label
//          suffix_index = i;
//          break;
//        }
        

    
    String digital_suffix = digital_number.substring(suffix_index);
    char first_digit = digital_suffix.charAt(0);
    List<Automaton> accept_automata = getAllSuitableAutomata(first_digit);
    for (Automaton subautomaton : accept_automata) {
      List<String> subanswer = getAllWords(subautomaton, digital_suffix);
      String delimeter = " ### ";
      answer.add(delimeter);
      answer.addAll(subanswer);
    }
    
    return answer;
  }
  
  private List<Automaton> getAllSuitableAutomata(char digit) {
    int value = Character.getNumericValue(digit);
    List<Automaton> accept_automata = new ArrayList<Automaton>();
    for (char label : LookupTable.map[value]) {
      Automaton automaton = mDictionary.get(label);
      if (automaton != null) {
        accept_automata.add(automaton);
      }
    }
    return accept_automata;
  }
}
