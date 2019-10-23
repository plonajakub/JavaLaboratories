package appLogic;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Student implements Serializable {
	private static final long serialVersionUID = -8030483768123388159L;

	private String firstName;
	private String lastName;
	private final Integer journalNumber;

	/**
	 * The key holds a subject's name, the value is self-explanatory
	 */
	private final Map<String, List<Journal.Grades>> grades = new LinkedHashMap<>();

	/**
	 * The dimensions are (from left): subject, month, date, and presence
	 */
	private final Map<String, Map<Month, Map<LocalDate, Journal.Presence>>> presence = new LinkedHashMap<>();

	public Student() {
		this.firstName = "<first name>";
		this.lastName = "<last name>";
		this.journalNumber = 0;
	}

	public Student(String firstName, String lasteName, Set<Integer> occupiedJournalNumbers) {
		this.firstName = firstName;
		this.lastName = lasteName;
		this.journalNumber = IdGenerator.getNext(occupiedJournalNumbers);
	}

	void initializeSubjects(List<String> subjects) {
		if (subjects == null || subjects.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for (String subject : subjects) {
			grades.put(subject, new ArrayList<>());
		}
	}

	void initializeSchoolYearDates(String startYear, String endYear, List<String> availableSubjects) {
		for (String subject : availableSubjects) {
			Map<Month, Map<LocalDate, Journal.Presence>> months = new LinkedHashMap<>();

			for (Month m = Month.SEPTEMBER; m != Month.JULY; m = m.plus(1)) {
				Map<LocalDate, Journal.Presence> dayPresence = new LinkedHashMap<>();
				if (m.getValue() >= 9 && m.getValue() <= 12) {
					for (LocalDate beg = LocalDate.of(Integer.parseInt(startYear), m, 1); !beg
							.isAfter(LocalDate.of(Integer.parseInt(startYear), m,
									m.length(LocalDate.now().isLeapYear()))); beg = beg.plusDays(1)) {
						dayPresence.put(beg, Journal.Presence.PRESENT);
					}
				} else if (m.getValue() >= 1 && m.getValue() <= 6) {
					for (LocalDate beg = LocalDate.of(Integer.parseInt(endYear), m, 1); !beg
							.isAfter(LocalDate.of(Integer.parseInt(endYear), m,
									m.length(LocalDate.now().isLeapYear()))); beg = beg.plusDays(1)) {
						dayPresence.put(beg, Journal.Presence.PRESENT);
					}
				}
				months.put(m, dayPresence);
			}
			presence.put(subject, months);
		}
	}

	public void addGrade(String subject, Journal.Grades grade) {
		if (!grades.containsKey(subject)) {
			throw new IllegalArgumentException();
		}
		List<Journal.Grades> subjectGrades = grades.get(subject);
		if (subjectGrades == null) {
			grades.put(subject, new ArrayList<>());
		}
		subjectGrades.add(grade);
	}

	public void removeGrade(String subject, int index) {
		if (!grades.containsKey(subject)) {
			throw new IllegalArgumentException();
		}
		List<Journal.Grades> subjectGrades = grades.get(subject);
		if (subjectGrades == null || index >= subjectGrades.size()) {
			throw new IllegalArgumentException();
		}
		subjectGrades.remove(index);
	}

	public List<Journal.Grades> getGrades(String subject) {
		if (!grades.containsKey(subject)) {
			throw new IllegalArgumentException();
		}
		if (grades.get(subject) == null) {
			grades.put(subject, new ArrayList<>());
		}
		return grades.get(subject);
	}

	public Double getMeanGrade(String subject) {
		if (!grades.containsKey(subject)) {
			throw new IllegalArgumentException();
		}
		List<Journal.Grades> subjectGrades = grades.get(subject);
		Double total = 0d;
		for (Journal.Grades grade : subjectGrades) {
			total += grade.getValue();
		}
		if (total == 0d) {
			return 1d;
		}
		return new Double(Math.round(100 * total / subjectGrades.size()) / 100.0);
	}

	public Double getMeanGrade() {
		Double total = 0d;
		for (String subject : grades.keySet()) {
			total += getMeanGrade(subject);
		}
		if (total == 0d) {
			return 1d;
		}
		return new Double(Math.round(100 * total / grades.size()) / 100.0);
	}

	public Map<Month, Map<LocalDate, Journal.Presence>> getPresence(String subject) {
		if (!presence.containsKey(subject)) {
			throw new IllegalArgumentException();
		}
		return presence.get(subject);
	}

	public void setPresence(String subject, Month month, LocalDate date, Journal.Presence p) {
		if (!presence.containsKey(subject)) {
			throw new IllegalArgumentException();
		}
		if (!presence.get(subject).get(month).containsKey(date)) {
			throw new IllegalArgumentException();
		}
		presence.get(subject).get(month).put(date, p);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String sureName) {
		this.lastName = sureName;
	}

	public Integer getJournalNumber() {
		return journalNumber;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
