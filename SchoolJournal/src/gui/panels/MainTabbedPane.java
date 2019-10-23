package gui.panels;

import java.awt.event.KeyEvent;

import javax.swing.JTabbedPane;

import appLogic.Journal;

public class MainTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = -5829173485087965060L;

	public MainTabbedPane(Journal journal) {
		StudentDataPanel dataPanel = new StudentDataPanel(journal);
		this.addTab("Personal data", dataPanel);
		this.setMnemonicAt(0, KeyEvent.VK_1);

		StudentGradesPanel gradesPanel = new StudentGradesPanel(journal);
		this.addTab("Grades", gradesPanel);
		this.addChangeListener(gradesPanel.getGradesTable());
		setMnemonicAt(1, KeyEvent.VK_2);

		StudentPresencePanel presencePanel = new StudentPresencePanel(journal);
		this.addTab("Presence", presencePanel);
		this.addChangeListener(presencePanel.getPresenceTable());
		setMnemonicAt(2, KeyEvent.VK_3);

		StudentMeanGradesPanel meanGradesPanel = new StudentMeanGradesPanel(journal);
		this.addTab("Statistics", meanGradesPanel);
		this.addChangeListener(meanGradesPanel.getMeanGradesTable());
		setMnemonicAt(3, KeyEvent.VK_4);

	}
}
