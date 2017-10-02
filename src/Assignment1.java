
/**
 * Class to implement Stable Matching algorithms
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.PagesPerMinute;
import javax.xml.ws.Holder;

import org.omg.CORBA.PUBLIC_MEMBER;

public class Assignment1 {

	// Part1: Implement a Brute Force Solution
	public static ArrayList<Integer> stableMatchBruteForce(Preferences preferences) {
		ArrayList<Integer> pairing = new ArrayList<Integer>();

		// taken = true
		// also used to gauge number of students and professors
		boolean[] students = new boolean[preferences.getNumberOfStudents()];
		boolean[] professors = new boolean[preferences.getNumberOfProfessors()];

		// ArrayList of professor preferences
		ArrayList<ArrayList<Integer>> professorList = preferences.getProfessors_preference();
		ArrayList<ArrayList<Integer>> studentList = preferences.getStudents_preference();

		// get every permutation of professors
		ArrayList<Integer> pList = new ArrayList<Integer>();
		for (int i = 0; i < professors.length; i++) {
			pList.add(i + 1);
		}
		ArrayList<ArrayList<Integer>> professorPermutations = new ArrayList<ArrayList<Integer>>();
		permuteHeap(professors.length, professorPermutations, pList.toArray(new Integer[pList.size()]));
		// get every permutation of students
		ArrayList<Integer> sList = new ArrayList<Integer>();
		for (int i = 0; i < students.length; i++) {
			sList.add(i + 1);
		}
		ArrayList<ArrayList<Integer>> studentPermutations = new ArrayList<ArrayList<Integer>>();
		permuteHeap(professors.length, studentPermutations, sList.toArray(new Integer[sList.size()]));

		// iterate through permutations, checking each pairing for stability
		for (int i = 0; i < professorPermutations.size(); i++) {
			for (int j = 0; j < studentPermutations.size(); j++) {

				ArrayList<Integer> currentProfessors = professorPermutations.get(i);
				ArrayList<Integer> currentStudents = studentPermutations.get(j);

				for (int k = 0; k < currentProfessors.size(); k++) {
					for (int l = 0; l < currentStudents.size(); l++) {
						pairing.set(currentProfessors.get(k - 1), currentStudents.get(l));
					}
				}
				// check pairing, if stable return
				if (checkPairing(pairing, preferences)) {
					return pairing;
				}
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
	 * This method checks an ArrayList of matches for stability based on a
	 * Preferences object
	 * 
	 * @param pairings
	 * @param p
	 * @return true if the pairing is stable, false if not
	 */
	public static boolean checkPairing(ArrayList<Integer> pairings, Preferences p) {

		for (int i = 0; i < pairings.size(); i++) {
			// our questionably faithful pair
			int professor = i + 1;
			int student = pairings.get(i);

			for (int j = 0; j < pairings.size(); j++) {
				// potential trouble couple
				int professorX = j + 1;
				int studentX = pairings.get(j);

				// get each member's preference list
				ArrayList<Integer> pPreference = p.getProfessors_preference().get(professor - 1);
				ArrayList<Integer> sPreference = p.getStudents_preference().get(student - 1);
				ArrayList<Integer> pXPreference = p.getProfessors_preference().get(professorX - 1);
				ArrayList<Integer> sXPreference = p.getStudents_preference().get(studentX - 1);

				// check if p prefers sX to s
				for (int k = 0; k < pPreference.size(); k++) {
					// if s is earlier on preferences, break
					if (pPreference.get(k) == student) {
						break;
					} else if (pPreference.get(k) == studentX) { // else sX is earlier on the preference list
						// if pX is first on sX's preference list, break
						for (int l = 0; l < sXPreference.size(); l++) {
							if (sXPreference.get(l) == professorX) { // if the infidelity is not mutual
								break;
							} else if (sXPreference.get(l) == professor) { // if the infidelity is mutual, return false
																			// for instability
								return false;
							}
						}
					}
				}

				// check if s prefers pX to p
				for (int k = 0; k < sPreference.size(); k++) {
					// if p is earlier on preferences, break
					if (sPreference.get(k) == professor) {
						break;
					} else if (sPreference.get(k) == professorX) { // else pX is earlier on the preference list
						for (int l = 0; l < pXPreference.size(); l++) {
							if (pXPreference.get(l) == studentX) { // if the infidelity is not mutual
								break;
							} else if (pXPreference.get(l) == student) { // if the infidelity is mutual, return false
																			// for instability
								return false;
							}
						}
					}
				}

			}

		}
		return true;
	}

	public static void permuteHeap(int size, ArrayList<ArrayList<Integer>> aL, Integer[] a) {

		if (size == 1) {
			ArrayList<Integer> toAdd = new ArrayList<Integer>();
			for (int i = 0; i < a.length; i++) {
				toAdd.add(a[i]);
			}
			aL.add(toAdd);
		} else {
			for (int i = 0; i < size - 1; i++) {
				permuteHeap(size - 1, aL, a);
				if (size % 2 == 0) {
					int temp = a[i];
					a[i] = a[size - 1];
					a[size - 1] = temp;
				} else {
					int temp = a[size - 1];
					a[size - 1] = a[i];
					a[i] = temp;
				}
			}
			permuteHeap(size - 1, aL, a);
		}
	}
}
