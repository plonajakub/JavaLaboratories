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
import gui.components.StudentPresenceTable;

public class StudentPresencePanel extends JPanel {
	private static final long serialVersionUID = 1811768765219910439L;

	private final JButton changePresenceButton;
	private final JComboBox<Object> subjectComboBox;
	private final JComboBox<Object> monthComboBox;
	private final StudentPresenceTable presenceTable;

	StudentPresencePanel(Journal journal) {
		this.changePresenceButton = new JButton("Set presence");
		this.subjectComboBox = new JComboBox<>(journal.getAvailableSubjects().toArray());
		this.monthComboBox = new JComboBox<>(journal.getAvailableMonthsString());
		this.presenceTable = new StudentPresenceTable(journal);

		this.setLayout(new BorderLayout());
		prepareComponents();
	}

	private void prepareComponents() {
		changePresenceButton.setToolTipText("Change current presence");
		changePresenceButton.setMnemonic(KeyEvent.VK_S);
		changePresenceButton.setActionCommand("set_presence");
		changePresenceButton.addActionListener(presenceTable);

		subjectComboBox.setToolTipText("Choose a subject");
		subjectComboBox.setActionCommand("choose_subject");
		subjectComboBox.addActionListener(presenceTable);

		monthComboBox.setToolTipText("Choose a month");
		monthComboBox.setActionCommand("choose_month");
		monthComboBox.addActionListener(presenceTable);

		JPanel buttonPanel = new JPanel();
		FlowLayout buttonLayout = new FlowLayout(FlowLayout.CENTER, 100, 50);
		buttonPanel.setLayout(buttonLayout);

		buttonPanel.add(subjectComboBox);
		buttonPanel.add(changePresenceButton);
		buttonPanel.add(monthComboBox);

		JScrollPane scrollPane = new JScrollPane(presenceTable);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(550);
		splitPane.add(scrollPane);
		splitPane.add(buttonPanel);

		this.add(splitPane, BorderLayout.CENTER);
	}

	public StudentPresenceTable getPresenceTable() {
		return presenceTable;
	}

}
