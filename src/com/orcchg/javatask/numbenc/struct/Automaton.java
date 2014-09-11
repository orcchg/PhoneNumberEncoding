package com.orcchg.javatask.numbenc.struct;

import java.util.ArrayList;

import com.orcchg.javatask.numbenc.utils.Util;

public class Automaton {
  private static final String authorTag = "Written by Maxim Alov <m.alov@samsung.com>, <orcchg@yandex.ru> aka Orcchg;  github.com/orcchg".intern();
  private static final int APPROXIMATE_TOTAL_WORDS = 3_150;
  
  private Character mID;
  private int mSizeOfAutomaton;  // total nodes in automaton
  private ArrayList<AutomatonNode> mNodes;
  
  public Automaton(char id) {
    mID = id;
    mNodes = new ArrayList<AutomatonNode>(APPROXIMATE_TOTAL_WORDS);
    mNodes.add(new AutomatonNode.Builder().build());
    mSizeOfAutomaton = 1;
  }
  
  public Character getID() {
    return mID;
  }
  
  public AutomatonNode getNode(int index) {
    return mNodes.get(index);
  }
  
  public void addWord(final String word) {
    int node_index = 0;
    for (char label : word.toCharArray()) {
      if (Util.isQuote(label)) {
        mNodes.get(node_index).setUmlaut(true);
        continue;
      }
      boolean upper_case = Character.isUpperCase(label);
      label = Character.toLowerCase(label);
      int char_index = Util.getCharShift(label);
      if (isEdgeAbsent(char_index, node_index)) {
        AutomatonNode node = new AutomatonNode.Builder()
                                .setIndex(mSizeOfAutomaton)
                                .setParentNodeIndex(node_index)
                                .setLabelFromParent(label)
                                .setUpperCase(upper_case)
                                .build();
        mNodes.add(node);
        mNodes.get(node_index).setTransition(char_index, mSizeOfAutomaton);
        ++mSizeOfAutomaton;
      }
      node_index = mNodes.get(node_index).getTransitions().get(label);
    }
    mNodes.get(node_index).setTerminal(true);  // entire word corresponds to terminal state
  }
  
  public AutomatonNode makeTransition(int from_node, char label) {
    label = Character.toLowerCase(label);
    int to_node = mNodes.get(from_node).getTransitions().get(label);
    if (to_node != AutomatonNode.EDJE_IS_ABSENT) {
      return mNodes.get(to_node);
    } else {
      return null;
    }
  }
  
  /* Private methods */
  // --------------------------------------------------------------------------
  private boolean isEdgeAbsent(int char_index, int node_index) {
    return mNodes.get(node_index).getTransitions().get(char_index) == AutomatonNode.EDJE_IS_ABSENT;
  }
}
