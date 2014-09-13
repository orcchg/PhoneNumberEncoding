package com.orcchg.javatask.numbenc.struct;

import java.util.ArrayList;
import java.util.Collections;

import com.orcchg.javatask.numbenc.utils.Util;

public class AutomatonNode {
  private final int mIndexInAutomaton;
  private final int mParentNodeIndex;
  private final char mLabelFromParent;
  private boolean mIsUpperCase;  // support data
  private boolean mIsTerminal;
  private final ArrayList<Integer> mTransitions;
  
  static final int ALPHABET_SIZE = 61;  // all possible transitions ('A' - 'z' - '{' - '|' - '}')
  static final int INDEX_OF_ROOT = 0;
  static final int EDGE_IS_ABSENT = -1;
  
  /* Construction */
  // --------------------------------------------------------------------------
  public AutomatonNode(final Builder builder) {
    mIndexInAutomaton = builder.mIndexInAutomaton;
    mParentNodeIndex = builder.mParentNodeIndex;
    mLabelFromParent = builder.mLabelFromParent;
    mIsUpperCase = builder.mIsUpperCase;
    mIsTerminal = builder.mIsTerminal;
    mTransitions = new ArrayList<Integer>(Collections.nCopies(ALPHABET_SIZE, EDGE_IS_ABSENT));
  }
  
  public static class Builder {
    private int mIndexInAutomaton;
    private int mParentNodeIndex;
    private char mLabelFromParent;
    private boolean mIsUpperCase;
    private boolean mIsTerminal;
    
    public Builder() {
      mIndexInAutomaton = INDEX_OF_ROOT;
      mParentNodeIndex = EDGE_IS_ABSENT;
      mLabelFromParent = '*';
      mIsTerminal = false;
    }
    
    public Builder setIndex(int index) {
      mIndexInAutomaton = index;
      return this;
    }
    
    public Builder setParentNodeIndex(int index) {
      mParentNodeIndex = index;
      return this;
    }
    
    public Builder setLabelFromParent(char label) {
      mLabelFromParent = label;
      return this;
    }
    
    public Builder setUpperCase(boolean flag) {
      mIsUpperCase = flag;
      return this;
    }
    
    public Builder setTerminal(boolean flag) {
      mIsTerminal = flag;
      return this;
    }
    
    public AutomatonNode build() {
      return new AutomatonNode(this);
    }
  }
  
  /* Data access */
  // --------------------------------------------------------------------------
  public int getIndex() {
    return mIndexInAutomaton;
  }
  
  public int getParentNodeIndex() {
    return mParentNodeIndex;
  }
  
  public char getLabelFromParent() {
    return mLabelFromParent;
  }
  
  public boolean isUpperCase() {
    return mIsUpperCase;
  }
  
  public boolean isTerminal() {
    return mIsTerminal;
  }
  
  public ArrayList<Integer> getTransitions() {
    return mTransitions;
  }
  
  public void setTerminal(boolean flag) {
    mIsTerminal = flag;
  }
  
  public void setTransition(int char_index, int transition) {
    mTransitions.set(char_index, transition);
  }
  
  @Override
  public String toString() {
    StringBuilder representation = new StringBuilder()
                                      .append(" {")
                                      .append(mIndexInAutomaton)
                                      .append(": ")
                                      .append(mParentNodeIndex)
                                      .append("<-- ");
    if (Util.isUmlaut(mLabelFromParent)) {
      representation.append(Util.convertFromUmlaut(mLabelFromParent))
                    .append('"');
    } else if (mIsUpperCase) {
      representation.append(Character.toUpperCase(mLabelFromParent));
    } else {
      representation.append(mLabelFromParent);
    }
    if (mIsTerminal) {
      representation.append("}T ");
    } else {
      representation.append("} ");
    }
    return representation.toString();
  }
}
