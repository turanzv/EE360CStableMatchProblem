
/**
 * Class to implement Stable Matching algorithms
 */

import java.lang.reflect.Array;
import java.security.AlgorithmConstraints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.PagesPerMinute;
import javax.swing.text.StyleConstants.ParagraphConstants;
import javax.xml.ws.Holder;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

public class Assignment1 {

	// Part1: Implement a Brute Force Solution
	public static ArrayList<Integer> stableMatchBruteForce(Preferences preferences) {
		ArrayList<Integer> pairing = new ArrayList<Integer>(
				Collections.nCopies(preferences.getNumberOfProfessors(), -1));

		// get every permutation of students
		ArrayList<Integer> sList = new ArrayList<Integer>();
		for (int i = 0; i < preferences.getNumberOfStudents(); i++) {
			sList.add(i + 1);
		}
		ArrayList<ArrayList<Integer>> studentPermutations = permute(sList);

		// iterate through permutations, checking each pairing for stability
		// for (int i = 0; i < professorPermutations.size(); i++) {
		for (int j = 0; j < studentPermutations.size(); j++) {

			// ArrayList<Integer> currentProfessors = professorPermutations.get(i);
			ArrayList<Integer> currentStudents = studentPermutations.get(j);

			for (int k = 0; k < currentStudents.size(); k++) {
				// for (int l = 0; l < currentStudents.size(); l++) {
				pairing.set(k, currentStudents.get(k));
				// }
			}
			// check pairing, if stable return
			if (checkStableSet(pairing, preferences)) {
				return pairing;
			}
		}
		return new ArrayList<Integer>();

	}

	// Part2: Implement Gale-Shapely Algorithm
	public static ArrayList<Integer> stableMatchGaleShapley(Preferences preferences) {

		// ArrayList<Integer> to be returned
		ArrayList<Integer> pairing = new ArrayList<Integer>(
				Collections.nCopies(preferences.getNumberOfProfessors(), -1));

		ArrayList<ArrayList<Integer>> professorList = preferences.getProfessors_preference();
		ArrayList<ArrayList<Integer>> studentList = preferences.getStudents_preference();

		// for each professor, while there is a professor not matched
		while (pairing.contains(-1)) {
			PROFS: for (int i = 0; i < preferences.getNumberOfProfessors(); i++) {
				if (pairing.get(i) != -1) {
					continue;
				}
				int professor = i + 1;
				ArrayList<Integer> professorPreference = professorList.get(professor - 1);

				// traverse preferences first through last
				PROFPREF: for (int j = 0; j < professorPreference.size(); j++) {
					int student = professorPreference.get(j);

					// if student is not taken, take student
					if (!pairing.contains(student)) {
						// pair student with professor, mark the student taken
						pairing.set(professor - 1, student);
						break;

					} else { // if student is taken, check if student would rather be with p than pX

						// find which professor is paired with the student
						int pX = arrayListIndexOf(pairing, student) + 1;

						// iterate through student's preferenceList
						ArrayList<Integer> studentPreference = studentList.get(student - 1);
						for (int k = 0; k < studentPreference.size(); k++) {
							// if pX comes first, leave student pair
							if (studentPreference.get(k) == pX) {
								break;
							} else if (studentPreference.get(k) == professor) { // if p comes first, swap student
								pairing.set(professor - 1, student); // put student to professor
								pairing.set(pX - 1, -1); // make pX not taken
								break PROFPREF;
							}
						}
					}
				}

			}
		}

		return pairing;
	}

	// Part3: Matching with Costs
	public static ArrayList<Cost> stableMatchCosts(Preferences preferences) {

		// get professor's optimal pairing
		ArrayList<Integer> profOpt = stableMatchGaleShapley(preferences);

		// get student's optimal pairing
		ArrayList<Integer> stuOpt = studentGaleShapely(preferences);

		// combine all pairings in one list
		ArrayList<Integer> pairing = new ArrayList<Integer>();
		pairing.addAll(profOpt);
		pairing.addAll(stuOpt);

		ArrayList<Cost> costs = new ArrayList<Cost>();

		// for each pairing
		for (int professor = 0; professor < pairing.size(); professor++) {
			int student = pairing.get(professor);

			// get professor preferences
			ArrayList<Integer> pPref = preferences.getProfessors_preference().get(professor);

			// get professor cost
			int pCost = 0;
			for (; pCost < pPref.size(); pCost++) {
				if (pPref.get(pCost) == student) {
					break;
				}
			}

			// get student preferences
			ArrayList<Integer> sPref = preferences.getStudents_preference().get(student);

			// get the student cost
			int sCost = 0;
			for (; sCost < sPref.size(); sCost++) {
				if (sPref.get(sCost) == professor) {
					break;
				}
			}

			costs.add(new Cost(professor, student, pCost, sCost));
		}

		return costs;
	}

	public static ArrayList<Integer> studentGaleShapely(Preferences preferences) {

		// ArrayList<Integer> to be returned
		ArrayList<Integer> pairing = new ArrayList<Integer>(
				Collections.nCopies(preferences.getNumberOfProfessors(), -1));

		ArrayList<ArrayList<Integer>> professorList = preferences.getProfessors_preference();
		ArrayList<ArrayList<Integer>> studentList = preferences.getStudents_preference();

		// for each student, while there is a student not matched
		while (pairing.contains(-1)) {

			// iterate through each student
			for (int i = 0; i < preferences.getNumberOfStudents(); i++) {
				// of the student is in the array, skip loop
				if (arrayListIndexOf(pairing, i + 1) != -1) {
					continue;
				}

				int student = i + 1;
				ArrayList<Integer> studentPreference = studentList.get(student - 1);

				// traverse preferences first through last
				STUPREF: for (int j = 0; j < studentPreference.size(); j++) {
					// if professor is not taken, take the professor
					int professor = studentPreference.get(j);
					if (pairing.get(professor - 1) == -1) {
						pairing.set(professor - 1, student);
						break;
					} else { // if professor is taken, check if professor would rather be with s than with sX

						// find which student is paired with the professor
						int sX = pairing.get(professor - 1);

						// iterate through professor's preferenceList
						ArrayList<Integer> professorPreference = professorList.get(professor - 1);
						for (int k = 0; k < professorPreference.size(); k++) {
							// if sX comes first, leave professor pair
							if (professorPreference.get(k) == sX) {
								break;
							} else if (professorPreference.get(k) == student) { // if s comes first, swap professor
								pairing.set(professor, student); // put student to current professor
								break STUPREF;
							}
						}
					}
				}
			}

		}

		return pairing;

	}

	/**
	 * This method checks an ArrayList of matches for stability based on a
	 * Preferences object
	 * 
	 * @param pairings
	 * @param p
	 * @return true if the pairing is stable, false if not
	 */
	public static boolean checkStableSet(ArrayList<Integer> pairings, Preferences p) {

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

				// don't check a pair against itself
				if (professor == professorX && student == studentX) {
					continue;
				}

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

	public static ArrayList<ArrayList<Integer>> permute(ArrayList<Integer> a) {
		// if recursion has distilled a to a null list, start recursing back and pushing
		// elements on
		if (a.size() == 0) {
			ArrayList<ArrayList<Integer>> r = new ArrayList<ArrayList<Integer>>();
			r.add(new ArrayList<Integer>());
			return r;
		}

		// pop bottom element from a
		int poppedFromA = a.remove(0);
		// ArrayList to be returned; built from popped elements
		ArrayList<ArrayList<Integer>> r = new ArrayList<ArrayList<Integer>>();
		// recursive call
		ArrayList<ArrayList<Integer>> p = permute(a);
		// for (ArrayList<Integer> smallerPermutated : p) {
		for (int i = 0; i < p.size(); i++) {
			ArrayList<Integer> pSlice = p.get(i);
			for (int j = 0; j <= pSlice.size(); j++) {
				ArrayList<Integer> t = new ArrayList<Integer>(pSlice);
				t.add(j, poppedFromA);
				r.add(t);
			}
		}
		return r;
	}

	/**
	 * Looks up the index of v in aL
	 * 
	 * @param aL
	 * @param v
	 * @return index of v in aL, else -1
	 */
	public static int arrayListIndexOf(ArrayList<Integer> aL, int v) {
		int i;
		for (i = 0; i < aL.size(); i++) {
			if (aL.get(i) == v) {
				return i;
			}
		}
		return -1;
	}
}
