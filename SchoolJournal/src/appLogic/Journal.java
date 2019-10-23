package appLogic;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Journal implements Serializable {
	private static final long serialVersionUID = 7728965201222683352L;
	
	private String startYear;
	private String endYear;

	private final List<String> availableSubjects = new LinkedList<>();
	private final Map<Integer, Student> students = new TreeMap<>();

	public enum State {
		VALID, INVALID
	}
	private State journalState;

	public enum Presence {
		PRESENT(""), ABSENT("A"), JUSTIFIED("J");

		private String presence;

		Presence(String presence) {
			this.presence = presence;
		}

		public String getPresence() {
			return presence;
		}

		public static String[] getPossiblePresenceStates() {
			return new String[] { "Present", "Absent", "Justified" };
		}

		public static Presence fromString(String s) {
			if (s.equals("Present"))
				return PRESENT;
			else if (s.equals("Absent"))
				return ABSENT;
			else
				return JUSTIFIED;
		}
	}

	public enum Grades {
		F(1), E(2), D(3), C(4), B(5), A(6);

		private int grade;

		Grades(int i) {
			grade = i;
		}

		int getValue() {
			return grade;
		}

		@Override
		public String toString() {
			switch (grade) {
			case 1:
				return "F";
			case 2:
				return "E";
			case 3:
				return "D";
			case 4:
				return "C";
			case 5:
				return "B";
			case 6:
				return "A";
			default:
				return "";
			}
		}

		public static Grades fromString(String s) {
			if (s.equals("A"))
				return A;
			else if (s.equals("B"))
				return B;
			else if (s.equals("C"))
				return C;
			else if (s.equals("D"))
				return D;
			else if (s.equals("E"))
				return E;
			else if (s.equals("F"))
				return F;
			else
				return null;

		}

		public static String[] getPossibleGradesAsStringArray() {
			return new String[] { "F", "E", "D", "C", "B", "A" };
		}
	}

	/**
	 * Year and subjects must be provided before using an instance of Journal object,
	 * also at least one student must be completely initialized
	 */
	public Journal() {
		this.journalState = State.INVALID;
	}

	public Journal(String startYear, String endYear, List<String> subjects) {
		this.startYear = startYear;
		this.endYear = endYear;
		this.initializeSubjects(subjects);
		this.journalState = State.VALID;
	}

	public void initializeSubjects(List<String> subjects) {
		if (subjects == null || subjects.isEmpty()) {
			throw new IllegalArgumentException();
		}
		availableSubjects.addAll(subjects);
	}

	public void addStudent(String firstName, String sureName) {
		Student s = new Student(firstName, sureName, students.keySet());
		s.initializeSchoolYearDates(startYear, endYear, availableSubjects);
		s.initializeSubjects(availableSubjects);
		students.put(s.getJournalNumber(), s);
	}

	public void removeStudent(Integer journalNumber) {
		if (!students.containsKey(journalNumber)) {
			throw new IllegalArgumentException();
		}
		students.remove(journalNumber);
	}

	public void addGrade(Integer journalNumber, String subject, Grades grade) {
		if (!students.containsKey(journalNumber)) {
			throw new IllegalArgumentException();
		}
		if (!availableSubjects.contains(subject)) {
			throw new IllegalArgumentException();
		}
		students.get(journalNumber).addGrade(subject, grade);
	}

	public void removeGrade(Integer journalNumber, String subject, Integer index) {
		if (!students.containsKey(journalNumber)) {
			throw new IllegalArgumentException();
		}
		if (!availableSubjects.contains(subject)) {
			throw new IllegalArgumentException();
		}
		students.get(journalNumber).removeGrade(subject, index);
	}

	public Map<Student, List<Grades>> getSubjectGrades(String subject) {
		if (!availableSubjects.contains(subject)) {
			throw new IllegalArgumentException();
		}
		Map<Student, List<Grades>> subjectGrades = new HashMap<>();
		for (Student s : students.values()) {
			subjectGrades.put(s, s.getGrades(subject));
		}
		return subjectGrades;
	}

	public void setPresence(Integer journalNumber, String subject, Month m, LocalDate date, Presence presence) {
		if (!students.containsKey(journalNumber)) {
			throw new IllegalArgumentException();
		}
		if (!availableSubjects.contains(subject)) {
			throw new IllegalArgumentException();
		}
		students.get(journalNumber).setPresence(subject, m, date, presence);
	}

	public Map<Student, Map<Month, Map<LocalDate, Presence>>> getSubjectPresence(String subject) {
		if (!availableSubjects.contains(subject)) {
			throw new IllegalArgumentException();
		}
		Map<Student, Map<Month, Map<LocalDate, Presence>>> subjectPresence = new LinkedHashMap<>();
		for (Student student : students.values()) {
			subjectPresence.put(student, student.getPresence(subject));
		}
		return subjectPresence;
	}

	public Double calculateStudentSubjectMean(Integer journalNumber, String subject) {
		if (!students.containsKey(journalNumber)) {
			throw new IllegalArgumentException();
		}
		if (!availableSubjects.contains(subject)) {
			throw new IllegalArgumentException();
		}
		return students.get(journalNumber).getMeanGrade(subject);
	}

	public Map<Student, Map<String, Double>> getStudentsSubjectMean() {
		Map<Student, Map<String, Double>> studentsMeans = new LinkedHashMap<>();
		for (Student student : students.values()) {
			Map<String, Double> means = new LinkedHashMap<>();
			for (String subject : availableSubjects) {
				means.put(subject, calculateStudentSubjectMean(student.getJournalNumber(), subject));
			}
			studentsMeans.put(student, means);
		}
		return studentsMeans;
	}

	public Double calculateStudentMean(Integer journalNumber) {
		if (!students.containsKey(journalNumber)) {
			throw new IllegalArgumentException();
		}
		return students.get(journalNumber).getMeanGrade();
	}

	public Map<Student, Double> getStudentsMean() {
		Map<Student, Double> means = new LinkedHashMap<>();
		for (Student student : students.values()) {
			means.put(student, student.getMeanGrade());
		}
		return means;
	}

	public Double getClassMean() {
		Double total = 0d;
		for (Student s : students.values()) {
			total += s.getMeanGrade();
		}
		return new Double(Math.round(100 * total / (students.size())) / 100.0);
	}

	/**
	 * 
	 * @return true if operation completed successfully, false otherwise
	 * 
	 */
	public Boolean makeJournalValid() {
		if (startYear != null && !startYear.isEmpty() && endYear != null && !endYear.isEmpty()
				&& !availableSubjects.isEmpty()) {
			journalState = State.VALID;
			return true;
		}
		return false;
	}

	public State getJournalState() {
		return journalState;
	}

	public List<String> getAvailableSubjects() {
		return availableSubjects;
	}

	public Map<Integer, Student> getStudents() {
		return students;
	}

	public Month[] getAvailableMonthsEnum() {
		Month[] months = new Month[10];
		int index = 0;
		for (Month m = Month.SEPTEMBER; !m.equals(Month.JUNE); m = m.plus(1)) {
			months[index] = m;
			++index;
		}
		return months;
	}

	public String[] getAvailableMonthsString() {
		String[] months = new String[10];
		int index = 0;
		for (Month m = Month.SEPTEMBER; !m.equals(Month.JULY); m = m.plus(1)) {
			months[index] = m.toString();
			++index;
		}
		return months;
	}

	public String getStartYear() {
		return startYear;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}
}
