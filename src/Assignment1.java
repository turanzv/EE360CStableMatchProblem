/**
 * Class to implement Stable Matching algorithms
 */

import java.util.ArrayList;

public class Assignment1 {

    // Part1: Implement a Brute Force Solution
    public static ArrayList<Integer> stableMatchBruteForce(Preferences preferences) {
    		
    		return new ArrayList<Integer>();
    }

    // Part2: Implement Gale-Shapley Algorithm
    public static ArrayList<Integer> stableMatchGaleShapley(Preferences preferences) {
    		
    		//ArrayList<Integer> to be returned
    		ArrayList<Integer> pairing = new ArrayList<Integer>();
    	
    		//taken = true
    		boolean [] professors = new boolean[preferences.getNumberOfProfessors()];
    		boolean [] students = new boolean[preferences.getNumberOfStudents()];
    		
    		ArrayList<ArrayList<Integer>> professorList = preferences.getProfessors_preference();
    		ArrayList<ArrayList<Integer>> studentList = preferences.getStudents_preference();
    		
    		//for each professor
    		for(int i = 0; i < professors.length; i++) {
    			ArrayList<Integer> professorPreference = professorList.get(i);
    			
    			//traverse preferences first through last
    			for(int j = 0; j < professorPreference.size(); j++) {
    				//if student is not taken, take student
    				if(!students[professorPreference.get(j)-1]) {
    					//pair student with professor, mark the student taken
    					pairing.add(professorPreference.get(j));
    					students[professorPreference.get(j)-1] = true;
    					break;
    				}
    			}
    			
    		}
    		
    		return pairing;
    }

    // Part3: Matching with Costs
    public static ArrayList<Cost> stableMatchCosts(Preferences preferences) {
    		
    		return new ArrayList<Cost>();
    }
}
