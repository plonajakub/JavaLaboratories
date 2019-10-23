package gui.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import appLogic.Journal;
import gui.components.StudentMeanGradesTable;

public class StudentMeanGradesPanel extends JPanel {
	private static final long serialVersionUID = -1212157342214948174L;

	private final JLabel meanInfoLabel;
	private final JLabel classMeanLabel;
	private final StudentMeanGradesTable meanGradesTable;

	StudentMeanGradesPanel(Journal journal) {
		this.meanInfoLabel = new JLabel("Mean of the class:");
		this.classMeanLabel = new JLabel();
		this.meanGradesTable = new StudentMeanGradesTable(journal, classMeanLabel);

		this.setLayout(new BorderLayout());
		prepareComponents();
	}

	private void prepareComponents() {
		JPanel labelPanel = new JPanel();
		FlowLayout labelLayout = new FlowLayout(FlowLayout.CENTER, 50, 50);
		labelPanel.setLayout(labelLayout);

		labelPanel.add(meanInfoLabel);
		labelPanel.add(classMeanLabel);

		JScrollPane scrollPane = new JScrollPane(meanGradesTable);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(550);

		splitPane.add(scrollPane);
		splitPane.add(labelPanel);

		this.add(splitPane);
	}

	public StudentMeanGradesTable getMeanGradesTable() {
		return meanGradesTable;
	}

}
