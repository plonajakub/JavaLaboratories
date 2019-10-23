package appLogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import appLogic.Journal.Grades;

public class JournalDataManager {

	private final Journal journal;
	private final JFrame mainFrame;

	public JournalDataManager(JFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.journal = loadJournalData();
		configureJournalIfNeeded(mainFrame);
	}

	public Journal getJournalInstance() {
		return journal;
	}

	public void saveJournalState() {
		File f = new File("journal.ser");
		if (!f.exists()) {
			JOptionPane.showMessageDialog(mainFrame, "Data file not found. New save file will be created.",
					"Save file not found", JOptionPane.WARNING_MESSAGE);
			try {
				f.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(mainFrame, "I/O error occured. Program is going to be terminated.",
						"I/O error", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
		}
		try {
			FileOutputStream fos = new FileOutputStream("journal.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(journal);
			oos.close();
			fos.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(mainFrame, "I/O error occured. Program is going to be terminated.",
					"I/O error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

	private Journal loadJournalData() {
		Journal j = null;
		File f = new File("journal.ser");
		if (!f.exists()) {
			JOptionPane.showMessageDialog(mainFrame, "Data file not found. New save file will be created.",
					"Save file not found", JOptionPane.WARNING_MESSAGE);
			try {
				f.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(mainFrame, "I/O error occured. Program is going to be terminated.",
						"I/O error", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
			j = new Journal();
			return j;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("journal.ser"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if (br.readLine() == null) {
				JOptionPane.showMessageDialog(mainFrame, "Data file corrupted. New save file will be created.",
						"Data corrupted", JOptionPane.ERROR_MESSAGE);
				br.close();
				f.delete();
				f.createNewFile();
				j = new Journal();
				return j;
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(mainFrame, "I/O error occured. Program is going to be terminated.",
					"I/O error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		try {
			FileInputStream fis = new FileInputStream("journal.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			j = (Journal) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(mainFrame, "I/O error occured. Program is going to be terminated.",
					"I/O error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(mainFrame, "Vital files not found. Program is going to be terminated.",
					"Unknown error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		return j;
	}

	private void configureJournalIfNeeded(JFrame frame) {
		if (journal.getJournalState() == Journal.State.VALID) {
			return;
		}

		String journalYear = JOptionPane.showInputDialog(frame, "Enter a school year for the journal:",
				"First configuration", JOptionPane.PLAIN_MESSAGE);
		String[] years = journalYear.split("\\s*/\\s*");
		journal.setStartYear(years[0]);
		journal.setEndYear(years[1]);

		String subjects = JOptionPane.showInputDialog(frame, "Enter subjects for the journal:", "First configuration",
				JOptionPane.PLAIN_MESSAGE);
		String[] splitedSubjects = subjects.split("[^\\w]+");
		List<String> formattedSubjects = new ArrayList<String>();
		for (int i = 0; i < splitedSubjects.length; ++i) {
			formattedSubjects.add(splitedSubjects[i].substring(0, 1).toUpperCase()
					+ splitedSubjects[i].substring(1, splitedSubjects[i].length()).toLowerCase());
		}
		journal.initializeSubjects(formattedSubjects);
		
		Student blankEntry = new Student();
		blankEntry.initializeSchoolYearDates(years[0], years[1], formattedSubjects);
		blankEntry.initializeSubjects(formattedSubjects);
		blankEntry.addGrade(formattedSubjects.get(0), Grades.F);
		journal.getStudents().put(blankEntry.getJournalNumber(), blankEntry);
		
		if (!journal.makeJournalValid()) {
			JOptionPane.showMessageDialog(frame, "Coniguration failed, program is going to be terminated...");
			System.exit(-1);
		}
	}
}
