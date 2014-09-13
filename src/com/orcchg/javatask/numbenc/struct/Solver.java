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
    mDictionary = new HashMap<Character, Automaton>(26);  // one automaton per letter
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
  
  public List<String> solve(final String number) {
    String digital_number = Util.remainDigitsOnly(number);
    List<String> answer = processNumber(digital_number);
    List<String> formatted_answer = new ArrayList<>(3000);
    String prefix = new StringBuilder().append(number).append(": ").toString();
    for (String line : answer) {
      formatted_answer.add(prefix + line);
    }
    return formatted_answer;
  }
  
  public List<String> processNumber(final String digital_number) {
    List<String> answer = new ArrayList<>(3000);
    
    char first_digit = digital_number.charAt(0);
    List<Automaton> accept_automata = getAllSuitableAutomata(first_digit);
    if (accept_automata.isEmpty()) {
      StringBuilder single_digit_prefix = new StringBuilder().append(first_digit).append(" ");
      List<String> subanswer = processNumber(digital_number.substring(1));
      for (String word : subanswer) {
        answer.add(single_digit_prefix + word);
      }
      return answer;
    }
    for (Automaton automaton : accept_automata) {
      List<String> subanswer = getAllWords(automaton, digital_number);
      answer.addAll(subanswer);
    }

    answer = cullInconsistentByLength(digital_number, answer);
    return culling(answer);
  }
  
  @Override
  public String toString() {
    StringBuilder representation = new StringBuilder();
    for (Map.Entry<Character, Automaton> entry : mDictionary.entrySet()) {
      representation.append(entry.getValue()).append("\n\n");
    }
    return representation.toString();
  }
  
  /* Private methods */
  // --------------------------------------------------------------------------
  private List<String> getAllWords(final Automaton automaton, final String digital_number) {
    List<String> answer = new ArrayList<>(3000);

    if (digital_number.length() == 1) {
      answer.add(digital_number);  // digit is encoded by itself
      return answer;
    }
    
    Map<Integer, List<AutomatonNode>> prefix_representation = new HashMap<>();
    
    Queue<Integer> track = new LinkedList<>();   // explored, but not visited nodes
    Queue<Integer> buffer = new LinkedList<>();  // previous iteration backup
    track.addAll(automaton.getNodesFromRoot());  // start walking at nodes origin from root
    buffer.addAll(track);
    
    int prefix_last_index = 1;
    char next_digit = digital_number.charAt(prefix_last_index);
    int next_value = Character.getNumericValue(next_digit);
    
    while (!track.isEmpty()) {
      for (char label : LookupTable.map[next_value]) {
        int index = buffer.peek();
        AutomatonNode node = automaton.makeTransition(index, label);
        if (node != null) {
          track.add(node.getIndex());
          if (node.isTerminal()) {
            if (prefix_representation.get(prefix_last_index) == null) {
              prefix_representation.put(prefix_last_index, new ArrayList<AutomatonNode>());
            }
            prefix_representation.get(prefix_last_index).add(node);
          }
        }
      }
      
      buffer.poll();
      track.poll();
      
      if (buffer.isEmpty() && !track.isEmpty()) {
        buffer.addAll(track);
        ++prefix_last_index;
        if (prefix_last_index >= digital_number.length()) {
          break;
        }
        
        next_digit = digital_number.charAt(prefix_last_index);
        next_value = Character.getNumericValue(next_digit);
      }
    }
    
    if (prefix_last_index == 1) {
      // digit is encoded by itself
      --prefix_last_index;
      next_digit = digital_number.charAt(prefix_last_index);
      next_value = Character.getNumericValue(next_digit);
      
      if (prefix_representation.get(prefix_last_index) == null) {
        prefix_representation.put(prefix_last_index, new ArrayList<AutomatonNode>());
      }
      AutomatonNode node = new AutomatonNode.Builder()
                              .setParentNodeIndex(0)
                              .setLabelFromParent(next_digit)
                              .build();
      prefix_representation.get(prefix_last_index).add(node);
    }
    
    for (Map.Entry<Integer, List<AutomatonNode>> entry : prefix_representation.entrySet()) {
      List<StringBuilder> answer_ctor = new ArrayList<>(3000);
      List<AutomatonNode> terminal_nodes = entry.getValue();
      List<String> prefix_words = gatherWords(automaton, terminal_nodes);
      for (String word : prefix_words) {
        answer_ctor.add(new StringBuilder().append(word));
      }
      
      String digital_suffix = digital_number.substring(entry.getKey() + 1);
      if (!digital_suffix.isEmpty()) {
        char first_digit = digital_suffix.charAt(0);
        List<Automaton> accept_automata = getAllSuitableAutomata(first_digit);
        for (Automaton subautomaton : accept_automata) {
          List<String> subanswer = getAllWords(subautomaton, digital_suffix);
          for (StringBuilder preword : answer_ctor) {
            for (String subword : subanswer) {
              answer.add(preword.toString() + " " + subword);
            }
          }
        }
      } else {
        for (StringBuilder preword : answer_ctor) {
          String word = preword.toString();
          String alpha_word = Util.remainLettersOnly(word);
          if (alpha_word.length() == digital_number.length()) {
            answer.add(word);
          }
        }
      }
    }

    return answer;
  }
  
  private List<String> gatherWords(final Automaton automaton, final List<AutomatonNode> terminal_nodes) {
    List<String> answer = new ArrayList<>(terminal_nodes.size());
    for (AutomatonNode node : terminal_nodes) {
      StringBuilder reverted_word = new StringBuilder();
      while (node.getParentNodeIndex() != AutomatonNode.EDGE_IS_ABSENT) {
        char label = node.getLabelFromParent();
        if (Util.isUmlaut(label)) {
          char converted = Util.convertFromUmlaut(label);
          reverted_word.append('"').append(converted);
        } else if (node.isUpperCase()) {
          reverted_word.append(Character.toUpperCase(label));
        } else {
          reverted_word.append(label);
        }
        node = automaton.getNode(node.getParentNodeIndex());
      }
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
  
  private List<String> cullInconsistentByLength(final String digital_number, final List<String> answer_with_mistakes) {
    List<String> answer = new ArrayList<>(3000);
    for (String word : answer_with_mistakes) {
      String alpha_word = Util.remainLettersOnly(word);
      if (alpha_word.length() == digital_number.length()) {
        answer.add(word);
      }
    }
    return answer;
  }
  
  private List<String> culling(final List<String> answer_with_mistakes) {
    List<String> no_duplicates = Util.removeDuplicates(answer_with_mistakes);
    List<String> answer = new ArrayList<>(3000);
    for (String word : no_duplicates) {
      if (!Util.hasAdjacentDigits(word)) {
        answer.add(word);
      }
    }
    return answer;
  }
}
