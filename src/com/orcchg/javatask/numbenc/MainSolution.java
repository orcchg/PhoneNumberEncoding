package com.orcchg.javatask.numbenc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.orcchg.javatask.numbenc.struct.Automaton;
import com.orcchg.javatask.numbenc.struct.Solver;

public class MainSolution {
  private Solver mSolver;
  
  public MainSolution() {
    mSolver = new Solver();
  }
  
  public static void main(String... args) {  // Usage: <main> input.txt dictionary.txt
    MainSolution instance = new MainSolution();
    instance.readDictionary(args[1]);
    instance.readNumbers(args[0]);  // read numbers and solve
  }
  
  /* Private methods */
  // --------------------------------------------------------------------------
  private void readNumbers(String filename) {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line = null;
      while ((line = br.readLine()) != null) {
        List<String> answer = mSolver.solve(line);
        for (String word : answer) {
          System.out.println(word);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void readDictionary(String filename) {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
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
