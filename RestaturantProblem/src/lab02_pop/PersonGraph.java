package lab02_pop;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class PersonGraph {

	private final Map<String, Person> persons = new HashMap<>();

	public PersonGraph(String path) {
		loadPersonsFromFile(path);
	}

	private void loadPersonsFromFile(String path) {
		File f = new File(path);
		List<String> personDescriptions = new LinkedList<>();
		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNext() && !sc.nextLine().matches("^\\s*\\bOsoba\\b\\s+\\bZnajomi\\b\\s*$"))
				;// Null statement
			String temp;
			while (sc.hasNext()) {
				temp = sc.nextLine();
				if (temp.equals("\n") || temp.isEmpty())
					break;
				personDescriptions.add(temp);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		String regex = "[\\s,]+";
		List<String> splitedInfo;
		for (String personDescription : personDescriptions) {
			splitedInfo = new ArrayList<>(Arrays.asList(personDescription.split(regex)));
			if (splitedInfo.get(0).matches("[\\s,]*")) {
				splitedInfo.remove(0);
			}
			if (splitedInfo.get(splitedInfo.size() - 1).matches("[\\s,]*")) {
				splitedInfo.remove(splitedInfo.size() - 1);
			}
			persons.putIfAbsent(splitedInfo.get(0), new Person(splitedInfo.get(0)));
			for (int i = 1; i < splitedInfo.size(); ++i) {
				persons.putIfAbsent(splitedInfo.get(i), new Person(splitedInfo.get(i)));
				persons.get(splitedInfo.get(0)).addPreference(persons.get(splitedInfo.get(i)));
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Person> entry : persons.entrySet()) {
			sb.append(entry.getKey() + ": " + entry.getValue().getPreferences() + "\n");
		}
		return sb.toString();
	}

	public Person getPerson(String name) {
		return persons.get(name);
	}

	public Set<String> getPersonNames() {
		return persons.keySet();
	}

	public int getPersonCount() {
		return persons.size();
	}
}