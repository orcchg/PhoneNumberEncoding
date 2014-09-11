package com.orcchg.javatask.numbenc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.orcchg.javatask.numbenc.struct.Automaton;
import com.orcchg.javatask.numbenc.struct.Solver;

public class MainSolution {
  private Solver mSolver;
  
  public MainSolution() {
    mSolver = new Solver();
  }
  
  public static void main(String... args) {  // Usage: <main> input.txt dictionary.txt
    MainSolution instance = new MainSolution();
    instance.readNumbers(args[0]);
    instance.readDictionary(args[1]);
  }
  
  /* Private methods */
  // --------------------------------------------------------------------------
  private void readNumbers(String filename) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line = null;
      while ((line = br.readLine()) != null) {
        //
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void readDictionary(String filename) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line = null;
      while ((line = br.readLine()) != null) {
        char label = line.charAt(0);
        Automaton automaton = mSolver.getAutomaton(label);
        if (automaton == null) {
          automaton = mSolver.addEmptyAutomaton(label);
        }
        automaton.addWord(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
