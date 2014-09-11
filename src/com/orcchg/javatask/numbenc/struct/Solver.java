package com.orcchg.javatask.numbenc.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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

    return answer;
  }
  
  /* Private methods */
  // --------------------------------------------------------------------------
  private List<String> getAllWords(final Automaton automaton, final String digital_number) {
    List<String> answer = new ArrayList<>(3000);
    List<AutomatonNode> terminal_nodes = new ArrayList<>(3000);
    
    Queue<Integer> track = new LinkedList<>();  // explored, but not visited nodes
    Queue<Integer> buffer = new LinkedList<>();  // previous iteration backup
    track.add(automaton.getNode(0).getIndex());  // start walking from root node
    buffer.addAll(track);
    
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
    List<String> prefixes = gatherWords(automaton, terminal_nodes);
    
    // process suffix of digital number recursively
    String digital_suffix = digital_number.substring(i);
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
  
  private List<String> gatherWords(final Automaton automaton, final List<AutomatonNode> terminal_nodes) {
    List<String> answer = new ArrayList<>(terminal_nodes.size());
    for (AutomatonNode node : terminal_nodes) {
      StringBuilder reverted_word = new StringBuilder();
      while (node.getParentNodeIndex() != AutomatonNode.EDGE_IS_ABSENT) {
        char label = node.getLabelFromParent();
        if (node.hasUmlaut()) {
          reverted_word.append('"');
        }
        if (node.isUpperCase()) {
          reverted_word.append(Character.toUpperCase(label));
        } else {
          reverted_word.append(label);
        }
      }
      reverted_word.append(automaton.getID());
      String word = reverted_word.reverse().toString();
      answer.add(word);
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
