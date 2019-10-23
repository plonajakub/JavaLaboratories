package lab02_pop;

public class Main {

	public static void main(String[] args) {
		PersonGraph pg = new PersonGraph("data.txt");
		ProblemSolver ps = new ProblemSolver("data.txt");
		ps.solveByComparingAllCombinations(pg);
	}
}