import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
	
	static int proffessors;
	static int students;
	
	/**
	 * @param args[0] contains name of first text file
	 * @param args[1] contains name of second text file
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		File dataOne = new File(args[0]);
		File dataTwo = new File(args[1]);
		
		Preferences p1 = preferencesFromFile(dataOne);
		Preferences p2 = preferencesFromFile(dataTwo);
		
		Assignment1.stableMatchBruteForce(p1);
		
		Assignment1.stableMatchGaleShapley(p1);
		Assignment1.stableMatchGaleShapley(p2);
		
		//Assignment1.stableMatchCosts(p1);
	}
	
	/**
	 * Populates an ArrayList (passed by reference) with preferences listed in an input file
	 */
	public static void populateLists(ArrayList<ArrayList<Integer>> aL, Scanner sc, int lengthOne, int lengthTwo) {
		//import actor one's preferences
		for (int i = 0; i < lengthOne; i++) {
			
			//add another actor to the array
			aL.add(new ArrayList<Integer>());
			
			//put actor two's preferences into an array
			String s = sc.nextLine();
			String [] nums = s.split("[ /]+");
			
			//import preferences into ArrayList
			for (int j = 0; j < lengthTwo; j++) {
				aL.get(i).add(Integer.parseInt(nums[j]));
			}
			
		}
	}
	
	/**
	 * Returns Preference object based on data from an input file
	 * @param f file with professor and student preferences
	 * @return Preference object
	 */
	public static Preferences preferencesFromFile(File f) throws FileNotFoundException{ 
		
		Scanner sc = new Scanner(f);
		
		//get the number of professors and the number of students
		String s = sc.nextLine();
		String[] nums = s.split("[ /]+");
		
		int professors = Integer.parseInt(nums[0]);
		ArrayList<ArrayList<Integer>> professorList = new ArrayList<ArrayList<Integer>>();
		
		int students = Integer.parseInt(nums[1]);
		ArrayList<ArrayList<Integer>> studentList = new ArrayList<ArrayList<Integer>>();
		
		populateLists(professorList, sc, professors, students);
		populateLists(studentList, sc, students, professors);
		
		Preferences p = new Preferences(professors, students, professorList, studentList);
		p.setNumberOfProfessors(professors);
		p.setNumberOfStudents(students);
		
		sc.close();
		
		return p;
	}

}
