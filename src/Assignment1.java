
/**
 * Class to implement Stable Matching algorithms
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.PagesPerMinute;

import org.omg.CORBA.PUBLIC_MEMBER;

public class Assignment1 {

	// Part1: Implement a Brute Force Solution
	public static ArrayList<Integer> stableMatchBruteForce(Preferences preferences) {
		ArrayList<Integer> pairing = new ArrayList<Integer>();

		// taken = true
		//also used to gauge number of students and professors
		boolean[] students = new boolean[preferences.getNumberOfStudents()];
		boolean[] professors = new boolean[preferences.getNumberOfProfessors()];
		
		//ArrayList of professor preferences
		ArrayList<ArrayList<Integer>> professorList = preferences.getProfessors_preference();
		ArrayList<ArrayList<Integer>> studentList = preferences.getStudents_preference();

		//get every permutation of professors
		ArrayList<Integer> pList = new ArrayList<Integer>();
		for (int i = 0; i < professors.length; i++) {pList.add(i+1);}
		ArrayList<ArrayList<Integer>> professorPermutations = new ArrayList<ArrayList<Integer>>();
		permuteHeap(professors.length, professorPermutations, pList.toArray(new Integer[pList.size()]));
		//get every permutation of students
		ArrayList<Integer> sList = new ArrayList<Integer>();
		for (int i = 0; i < students.length; i++) {sList.add(i+1);}
		ArrayList<ArrayList<Integer>> studentPermutations = new ArrayList<ArrayList<Integer>>();
		permuteHeap(professors.length, studentPermutations, sList.toArray(new Integer[sList.size()]));
		
		//iterate through permutations, checking each pairing for stability
		for(int i = 0; i < professorPermutations.size(); i++) {
			for(int j = 0; j < studentPermutations.size(); j++) {
				
				ArrayList<Integer> currentProfessors = professorPermutations.get(i);
				ArrayList<Integer> currentStudents = studentPermutations.get(j);
				
				for(int k = 0; k < currentProfessors.size(); k++) {
					for(int l = 0; l < currentStudents.size(); l++) {
						pairing.set(currentProfessors.get(k-1), currentStudents.get(l));
					}
				}
				//check pairing, if stable return
				
			}
		}

		return new ArrayList<Integer>();
	}

	// Part2: Implement Gale-Shapely Algorithm
	public static ArrayList<Integer> stableMatchGaleShapley(Preferences preferences) {

		// ArrayList<Integer> to be returned
		ArrayList<Integer> pairing = new ArrayList<Integer>();

		// taken = true
		boolean[] professors = new boolean[preferences.getNumberOfProfessors()];
		boolean[] students = new boolean[preferences.getNumberOfStudents()];

		ArrayList<ArrayList<Integer>> professorList = preferences.getProfessors_preference();
		ArrayList<ArrayList<Integer>> studentList = preferences.getStudents_preference();

		// for each professor
		for (int i = 0; i < professors.length; i++) {
			ArrayList<Integer> professorPreference = professorList.get(i);

			// traverse preferences first through last
			for (int j = 0; j < professorPreference.size(); j++) {
				// if student is not taken, take student
				if (!students[professorPreference.get(j) - 1]) {
					// pair student with professor, mark the student taken
					pairing.add(professorPreference.get(j));
					students[professorPreference.get(j) - 1] = true;
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

	/**
	 * This recursive function returns the permutation of an ArrayList
	 * 
	 * @param a
	 *            ArrayList<Integer> to be permuted
	 *
	 *            public void permute(ArrayList<Integer> a) {
	 * 
	 *            ArrayList<ArrayList<Integer>> permutations = new
	 *            ArrayList<ArrayList<Integer>>();
	 * 
	 *            for (int i = 0; i < a.size(); i++) { for(int j = 0; j < a.size();
	 *            j++) {
	 * 
	 *            } } }
	 * @return
	 */

	/*
	 * public static ArrayList<ArrayList<Integer>>
	 * permute(ArrayList<ArrayList<Integer>> aL, ArrayList<Integer> a) {
	 * ArrayList<ArrayList<Integer>> permutations = new
	 * ArrayList<ArrayList<Integer>>(); //begin with new list permutations.add(new
	 * ArrayList<Integer>());
	 * 
	 * for(int i = 0; i < a.size(); i++) { ArrayList<ArrayList<Integer>> c = new
	 * ArrayList<ArrayList<Integer>>();
	 * 
	 * for (ArrayList<Integer> l : permutations) { for (int k = 0; k < l.size()+1;
	 * k++) { l.add(k, l.get(i)); ArrayList<Integer> t = new ArrayList<Integer>(l);
	 * c.add(t);
	 * 
	 * l.remove(k); } } permutations = new ArrayList<ArrayList<Integer>>(c);
	 * 
	 * 
	 * } return permutations; }
	 */

	/**
	 * This method checks an ArrayList of matches for stability based on a
	 * Preferences object
	 * 
	 * @param pairings
	 * @param p
	 * @return true if the pairing is stable, false if not
	 */
	public static boolean checkPairing(ArrayList<Integer> pairings, Preferences p) {

		return false;
	}

	public static void permuteHeap(int size, ArrayList<ArrayList<Integer>> aL, Integer [] a) {

		if (size == 1) {
			ArrayList<Integer> toAdd = new ArrayList<Integer>();
			for(int i = 0; i < a.length; i++) {toAdd.add(a[i]);}
			aL.add(toAdd);
		} else {
			for (int i = 0; i < size - 1; i++) {
				permuteHeap(size - 1, aL, a);
				if (size % 2 == 0) {
					int temp = a[i];
					a[i] = a[size - 1];
					a[size - 1] = temp;
				} else {
					int temp = a[size-1];
					a[size-1] = a[i];
					a[i] = temp;
				}
			}
			permuteHeap(size-1, aL, a);
		}
	}
}
