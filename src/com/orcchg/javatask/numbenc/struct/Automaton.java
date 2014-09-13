package com.orcchg.javatask.numbenc.struct;

import java.util.ArrayList;
import java.util.List;

import com.orcchg.javatask.numbenc.utils.Util;

public class Automaton {
  protected static final String authorTag = "Written by Maxim Alov <m.alov@samsung.com>, <orcchg@yandex.ru> aka Orcchg (Moscow, Russia);  github.com/orcchg".intern();
  private static final int APPROXIMATE_TOTAL_WORDS = 3_150;
  
  private Character mID;
  private int mSizeOfAutomaton;  // total nodes in automaton
  private List<AutomatonNode> mNodes;
  private List<Integer> mNodesFromRoot;
  
  public Automaton(char id) {
    mID = id;
    mNodes = new ArrayList<AutomatonNode>(APPROXIMATE_TOTAL_WORDS);
    mNodes.add(new AutomatonNode.Builder().build());  // add root node
    mNodesFromRoot = new ArrayList<>(LookupTable.TABLE_SIZE);
    mSizeOfAutomaton = 1;
  }
  
  public Character getID() {
    return mID;
  }
  
  public AutomatonNode getNode(int index) {
    return mNodes.get(index);
  }
  
  public List<Integer> getNodesFromRoot() {
    return mNodesFromRoot;
  }
  
  public void addWord(final String word) {
    int node_index = 0;
    for (int i = 0; i < word.length(); ++i) {
      char label = word.charAt(i);
      if (Util.isQuote(label)) {
        continue;
      }
      if ((i + 1 < word.length()) && Util.isQuote(word.charAt(i + 1))) {
        label = Util.convertToUmlaut(label);
      }
      
      boolean upper_case = Character.isUpperCase(label);
      //label = Character.toLowerCase(label);
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
        if (node.getParentNodeIndex() == AutomatonNode.INDEX_OF_ROOT) {
          mNodesFromRoot.add(node.getIndex());
        }
        ++mSizeOfAutomaton;
      }
      node_index = mNodes.get(node_index).getTransitions().get(char_index);
    }
    mNodes.get(node_index).setTerminal(true);  // entire word corresponds to terminal state
  }
  
  public AutomatonNode makeTransition(int from_node, char label) {
    //label = Character.toLowerCase(label);
    int char_index = Util.getCharShift(label);
    int to_node = mNodes.get(from_node).getTransitions().get(char_index);
    if (to_node != AutomatonNode.EDGE_IS_ABSENT) {
      return mNodes.get(to_node);
    } else {
      return null;
    }
  }
  
  @Override
  public String toString() {
    StringBuilder representation = new StringBuilder()
                                      .append("[")
                                      .append(mID)
                                      .append("]: ");
    for (AutomatonNode node : mNodes) {
      representation.append(node.toString());
    }
    return representation.toString();
  }
  
  /* Private methods */
  // --------------------------------------------------------------------------
  protected void printAuthorTag() {
    System.out.println(authorTag);
  }
  
  private boolean isEdgeAbsent(int char_index, int node_index) {
    return mNodes.get(node_index).getTransitions().get(char_index) == AutomatonNode.EDGE_IS_ABSENT;
  }
}
