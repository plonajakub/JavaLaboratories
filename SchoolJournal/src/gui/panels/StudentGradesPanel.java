package gui.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import appLogic.Journal;
import gui.components.StudentGradesTable;

public class StudentGradesPanel extends JPanel {
	private static final long serialVersionUID = -3015562346174412355L;

	private final JButton addGradeButton;
	private final JButton removeGradeButton;
	private final JComboBox<Object> subjectComboBox;
	private final StudentGradesTable gradesTable;

	StudentGradesPanel(Journal journal) {
		this.addGradeButton = new JButton("Enter grade");
		this.removeGradeButton = new JButton("Remove grade");
		this.subjectComboBox = new JComboBox<>(journal.getAvailableSubjects().toArray());
		this.gradesTable = new StudentGradesTable(journal);

		this.setLayout(new BorderLayout());
		prepareComponents();
	}

	private void prepareComponents() {
		addGradeButton.setToolTipText("Insert a new grade");
		addGradeButton.setMnemonic(KeyEvent.VK_E);
		addGradeButton.setActionCommand("add");
		addGradeButton.addActionListener(gradesTable);

		removeGradeButton.setToolTipText("Remove existing grade");
		removeGradeButton.setMnemonic(KeyEvent.VK_R);
		removeGradeButton.setActionCommand("remove");
		removeGradeButton.addActionListener(gradesTable);

		subjectComboBox.setToolTipText("Choose a subject");
		subjectComboBox.setActionCommand("choose");
		subjectComboBox.addActionListener(gradesTable);

		JScrollPane scrollPane = new JScrollPane(gradesTable);

		JPanel buttonPanel = new JPanel();
		FlowLayout buttonLayout = new FlowLayout(FlowLayout.CENTER, 100, 50);
		buttonPanel.setLayout(buttonLayout);

		buttonPanel.add(subjectComboBox);
		buttonPanel.add(addGradeButton);
		buttonPanel.add(removeGradeButton);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(550);

		splitPane.add(scrollPane);
		splitPane.add(buttonPanel);

		this.add(splitPane);
	}

	public StudentGradesTable getGradesTable() {
		return gradesTable;
	}

}
