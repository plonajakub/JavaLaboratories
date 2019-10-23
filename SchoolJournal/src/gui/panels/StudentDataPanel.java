package gui.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import appLogic.Journal;
import gui.components.StudentDataTable;

public class StudentDataPanel extends JPanel {
	private static final long serialVersionUID = -5487060211799506289L;

	private final JButton addStudentButton;
	private final JButton removeStudentButton;
	private final StudentDataTable studentDataTable;

	public StudentDataPanel(Journal journal) {
		this.addStudentButton = new JButton("Add student");
		this.removeStudentButton = new JButton("Remove student");
		this.studentDataTable = new StudentDataTable(journal);

		this.setLayout(new BorderLayout());
		prepareComponents();
	}

	private void prepareComponents() {
		addStudentButton.setToolTipText("Add a new student to the journal");
		addStudentButton.setMnemonic(KeyEvent.VK_A);
		addStudentButton.setActionCommand("add");
		addStudentButton.addActionListener(studentDataTable);

		removeStudentButton.setToolTipText("Remove existing student from the journal");
		removeStudentButton.setMnemonic(KeyEvent.VK_R);
		removeStudentButton.setActionCommand("remove");
		removeStudentButton.addActionListener(studentDataTable);

		JScrollPane tableScrollPane = new JScrollPane(studentDataTable);

		JPanel buttonPanel = new JPanel();
		FlowLayout buttonLayout = new FlowLayout(FlowLayout.CENTER, 100, 50);
		buttonPanel.setLayout(buttonLayout);

		buttonPanel.add(addStudentButton);
		buttonPanel.add(removeStudentButton);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(550);

		splitPane.add(tableScrollPane);
		splitPane.add(buttonPanel);

		this.add(splitPane);
	}

	public StudentDataTable getStudentDataTable() {
		return studentDataTable;
	}

}