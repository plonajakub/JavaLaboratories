package lab02_pop;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class Person {

	private final String name;
	private final Map<String, Person> preferences = new HashMap<>();

	public Person(String name) {
		this.name = name;
	}

	void addPreference(Person p) {
		preferences.putIfAbsent(p.getName(), p);
	}

	public Set<String> getPreferences() {
		return preferences.keySet();
	}

	public String getName() {
		return name;
	}
}