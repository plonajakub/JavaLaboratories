package lab02_pop;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.stream.Collectors;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ProblemSolver {

	final List<Integer> tableCapacity = new ArrayList<>();
	final List<Double> userCriteriaCoefficients = new ArrayList<>();

	public ProblemSolver(String path) {
		loadTablesFromFile(path);
		loadUserCriteriaCoefficientsFromFile(path);
	}

	public void solveByComparingAllCombinations(PersonGraph pg) {
		List<List<Integer>> possibleDistributionPatterns = getDistributionPatterns(pg.getPersonCount());
		List<String> availablePersons = new ArrayList<>(pg.getPersonNames());
		List<List<String>> allDistributions = new LinkedList<>();
		Iterator<List<Integer>> it = possibleDistributionPatterns.iterator();
		while (it.hasNext()) {
			allDistributions.addAll(designateCombinationsWithDistribution(availablePersons, it.next(), 0));
		}
		List<List<List<String>>> allDistributions3d = convertTo3dData(allDistributions);
		List<List<Double>> criteriaData = getCriteriaData(pg, allDistributions3d);
		List<Integer> indecies = getBestDistributionsIndecies(criteriaData);
		int indeciesSize = indecies.size();
		for (int i = 0; i < indeciesSize; ++i) {
			System.out.println("Solution No. " + (i + 1));
			printDistribution(allDistributions3d.get(indecies.get(i)));
			System.out.println();
		}
	}

	public static void printDistribution(List<List<String>> distribution) {
		int counter = 0;
		for (List<String> table : distribution) {
			System.out.print("Table No. " + ++counter + ": ");
			for (String person : table) {
				System.out.print(person + " ");
			}
			System.out.println();
		}
	}

	private List<List<Integer>> getDistributionPatterns(int personsCount) {
		CombinationGenerator cg = new CombinationGenerator(personsCount + tableCapacity.size() - 1,
				tableCapacity.size() - 1);
		List<List<Integer>> allDelimeterPositions = new ArrayList<>();
		while (cg.hasMore()) {
			allDelimeterPositions.add(Arrays.stream(cg.getNext()).boxed().collect(Collectors.toList()));
		}
		List<List<Integer>> personDistributions = new ArrayList<>();
		List<Integer> personDistribution = new ArrayList<>();
		for (List<Integer> delimeterPositions : allDelimeterPositions) {
			personDistribution.add(delimeterPositions.get(0));
			int delimeterPositionSize = delimeterPositions.size();
			for (int i = 1; i < delimeterPositionSize; ++i) {
				personDistribution.add(delimeterPositions.get(i) - delimeterPositions.get(i - 1) - 1);
			}
			Integer seatedPersonsTotal = 0;
			for (Integer seatedPersons : personDistribution) {
				seatedPersonsTotal += seatedPersons;
			}
			personDistribution.add(personsCount - seatedPersonsTotal);
			personDistributions.add(personDistribution);
			personDistribution = new ArrayList<>();
		}
		int tableCapacitySize = tableCapacity.size();
		for (int i = 0; i < personDistributions.size(); ++i) {
			for (int j = 0; j < tableCapacitySize; ++j) {
				if (personDistributions.get(i).get(j) > tableCapacity.get(j)) {
					personDistributions.remove(i);
					--i;
					break;
				}
			}
		}
		return personDistributions;
	}

	private List<List<String>> designateCombinationsWithDistribution(List<String> availablePersons,
			List<Integer> distribution, int tableIndex) {
		if (tableIndex > distribution.size() - 1) {
			return null;
		}
		List<List<String>> distributionPatterns = new LinkedList<>();
		if (availablePersons.size() == 0) {
			List<String> emptyTable = new LinkedList<>();
			emptyTable.add("|");
			distributionPatterns.addAll(cartesianProduct(emptyTable,
					designateCombinationsWithDistribution(availablePersons, distribution, tableIndex + 1)));
			return distributionPatterns;
		}
		List<String> availablePersonsAfterMatching = new ArrayList<>();
		CombinationGenerator cg = new CombinationGenerator(availablePersons.size(),
				distribution.get(tableIndex).intValue());
		List<List<Integer>> combinationIndecies = new LinkedList<>();
		while (cg.hasMore()) {
			combinationIndecies.add(Arrays.stream(cg.getNext()).boxed().collect(Collectors.toList()));
		}
		List<String> distributionPattern = new LinkedList<>();
		for (List<Integer> indecies : combinationIndecies) {
			for (Integer index : indecies) {
				distributionPattern.add(availablePersons.get(index.intValue()));
			}
			int availablePersonsSize = availablePersons.size();
			for (int i = 0; i < availablePersonsSize; ++i) {
				if (indecies.contains(i)) {
					continue;
				}
				availablePersonsAfterMatching.add(availablePersons.get(i));
			}
			distributionPattern.add("|");
			distributionPatterns.addAll(cartesianProduct(distributionPattern, designateCombinationsWithDistribution(
					availablePersonsAfterMatching, distribution, tableIndex + 1)));
			distributionPattern = new LinkedList<>();
			availablePersonsAfterMatching = new ArrayList<>();
		}
		return distributionPatterns;
	}

	private List<List<Double>> getCriteriaData(PersonGraph pg, List<List<List<String>>> allDistributions3d) {
		List<List<Double>> criteriaData = new LinkedList<>();
		double happyCount = 0d, unhappyCount = 0d, emptyTablesCount = 0d;
		double happyWeight = 1d;
		int tableIdx = 0;
		List<Double> dataHandle = new ArrayList<>();
		for (List<List<String>> distribution : allDistributions3d) {
			for (List<String> table : distribution) {
				for (int i = 0; i < table.size(); ++i) {
					for (int j = 0; j < table.size(); ++j) {
						if (i == j) {
							continue;
						}
						if (pg.getPerson(table.get(i)).getPreferences().contains(table.get(j))) {
							happyCount += happyWeight;
							happyWeight /= 2d;
						}
					}
					if (happyWeight == 1d && !pg.getPerson(table.get(i)).getPreferences().isEmpty()) {
						--unhappyCount;
					}
					happyWeight = 1d;
				}
				if (table.isEmpty()) {
					emptyTablesCount += tableCapacity.get(tableIdx) / 2;
				}
				++tableIdx;
			}
			dataHandle.add(happyCount);
			dataHandle.add(unhappyCount);
			dataHandle.add(emptyTablesCount);
			criteriaData.add(dataHandle);
			happyCount = 0d;
			unhappyCount = 0d;
			emptyTablesCount = 0d;
			tableIdx = 0;
			dataHandle = new ArrayList<>();
		}
		return criteriaData;
	}

	private List<Integer> getBestDistributionsIndecies(List<List<Double>> criteriaData) {
		List<Double> scores = new ArrayList<>();
		double tScore = 0d;
		for (List<Double> criteria : criteriaData) {
			tScore = userCriteriaCoefficients.get(0) * criteria.get(0)
					+ userCriteriaCoefficients.get(1) * criteria.get(1)
					+ userCriteriaCoefficients.get(2) * criteria.get(2);
			scores.add(tScore);
			tScore = 0;
		}
		Double bestScore = scores.get(0);
		for (Double score : scores) {
			if (bestScore < score) {
				bestScore = score;
			}
		}
		List<Integer> solutionIndecies = new LinkedList<>();
		int scoresSize = scores.size();
		for (int i = 0; i < scoresSize; ++i) {
			if (scores.get(i).equals(bestScore.doubleValue())) {
				solutionIndecies.add(i);
			}
		}
		System.out.println("Best solution score: " + scores.get(solutionIndecies.get(0)) + "\n");
		return solutionIndecies;
	}

	/**
	 * 
	 * Utilities
	 */
	private void loadTablesFromFile(String path) {
		File f = new File(path);
		List<String> tableDescriptions = new LinkedList<>();
		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNext() && !sc.nextLine().matches("^\\s*\\bStolik\\b\\s+\\bMiejsc\\b\\s*$"))
				; // Null statement
			String temp;
			while (sc.hasNext()) {
				temp = sc.nextLine();
				if (temp.equals("\n") || temp.isEmpty())
					break;
				tableDescriptions.add(temp);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		String regex = "^\\s*(\\d)+[\\s,]+(\\d)+\\s*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match;
		for (String tableDescription : tableDescriptions) {
			match = pattern.matcher(tableDescription);
			if (match.matches()) {
				tableCapacity.add(new Integer(Integer.parseInt(match.group(2))));
			}
		}
	}

	private void loadUserCriteriaCoefficientsFromFile(String path) {
		File f = new File(path);
		List<String> criteriaDescriptions = new LinkedList<>();
		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNext() && !sc.nextLine().matches("^\\s*\\bKryterium\\b\\s+\\bWaga\\b\\s*$"))
				; // Null statement
			String temp;
			while (sc.hasNext()) {
				temp = sc.nextLine();
				if (temp.equals("\n") || temp.isEmpty())
					break;
				criteriaDescriptions.add(temp);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		String regex = "^\\s*([\\w-]+\\s+)+(-?\\d+(.\\d+)?)\\s*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match;
		for (String criteriaDescription : criteriaDescriptions) {
			match = pattern.matcher(criteriaDescription);
			if (match.matches()) {
				userCriteriaCoefficients.add(new Double(Double.parseDouble(match.group(2))));
			}
		}
	}

	private List<List<List<String>>> convertTo3dData(List<List<String>> allDistributions) {
		List<List<List<String>>> allDistributions3d = new ArrayList<>();
		List<List<String>> distribution;
		List<String> tableDistribution;
		for (List<String> dist : allDistributions) {
			distribution = new ArrayList<>();
			tableDistribution = new ArrayList<>();
			for (String tDist : dist) {
				if (tDist.equals("|")) {
					distribution.add(tableDistribution);
					tableDistribution = new ArrayList<>();
					continue;
				}
				tableDistribution.add(tDist);
			}
			allDistributions3d.add(distribution);
		}
		return allDistributions3d;
	}

	private List<List<String>> cartesianProduct(List<String> x, List<List<String>> y) {
		List<List<String>> product = new LinkedList<>();
		if (y == null) {
			product.add(x);
			return product;
		}
		if (x.isEmpty()) {
			product.addAll(y);
			return product;
		}
		List<String> temp;
		for (List<String> yList : y) {
			temp = new LinkedList<>(x);
			temp.addAll(yList);
			product.add(temp);
		}
		return product;
	}
}
