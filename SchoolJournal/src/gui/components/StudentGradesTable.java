package gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import appLogic.Journal;
import appLogic.Student;

public class StudentGradesTable extends JTable implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 8973370668840970970L;
	
	private final Journal journal;
	private final StringBuffer currentSubject;

	public StudentGradesTable(Journal journal) {
		this.journal = journal;
		this.currentSubject = new StringBuffer();
		this.currentSubject.append(journal.getAvailableSubjects().get(0));

		this.getTableHeader().setReorderingAllowed(false);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellSelectionEnabled(true);

		this.createModel();
		this.refreshView();
	}

	private void createModel() {
		this.setModel(new StudentGradesTableModel(journal, currentSubject));

		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.getModel());
		this.setRowSorter(sorter);

		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		for (int i = 0; i < this.getColumnCount(); ++i) {
			sorter.setSortable(i, false);
		}

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		this.setDefaultRenderer(String.class, centerRenderer);
		this.setDefaultRenderer(Integer.class, centerRenderer);
		this.setDefaultRenderer(Journal.Grades.class, centerRenderer);
	}

	private void refreshView() {
		this.getColumnModel().getColumn(0).setMaxWidth(60);
		this.revalidate();
		this.repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		this.createModel();
		this.refreshView();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("choose")) {
			JComboBox<Object> source = (JComboBox<Object>) e.getSource();
			currentSubject.delete(0, currentSubject.length());
			currentSubject.append(source.getSelectedItem());
			this.createModel();
			this.refreshView();

		} else if (e.getActionCommand().equals("add")) {
			if (this.getSelectedRows().length == 0) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Select a student before performing this operation!",
						"Operation not allowed", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String[] possibleGrades = Journal.Grades.getPossibleGradesAsStringArray();
			String grade = (String) JOptionPane.showInputDialog(this.getRootPane(), "Choose the grade", "Add grade",
					JOptionPane.PLAIN_MESSAGE, null, possibleGrades, possibleGrades[0]);
			Integer journalNumber = decodeJournalNumber(this.getSelectedRow());
			journal.addGrade(journalNumber, currentSubject.toString(), Journal.Grades.fromString(grade));
			this.createModel();
			this.refreshView();

		} else if (e.getActionCommand().equals("remove")) {
			if (this.getSelectedRows().length == 0) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Select a student before performing this operation!",
						"Operation not allowed", JOptionPane.WARNING_MESSAGE);
				return;
			}

			journal.removeGrade(decodeJournalNumber(this.getSelectedRow()), currentSubject.toString(),
					this.getSelectedColumn() - 2);
			this.setModel(new StudentGradesTableModel(journal, currentSubject));
			this.createModel();
			this.refreshView();
		}
	}

	private Integer decodeJournalNumber(int selectedRow) {
		Map<Integer, Student> students = journal.getStudents();
		Iterator<Entry<Integer, Student>> it = students.entrySet().iterator();
		Integer journalNumber = it.next().getKey();
		for (int i = 0; i != selectedRow; ++i) {
			journalNumber = it.next().getKey();
		}
		return journalNumber;
	}

	private class StudentGradesTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -1307155370328154847L;
		
		private final Map<Student, List<Journal.Grades>> subjectGrades;

		public StudentGradesTableModel(Journal journal, StringBuffer currentSubject) {
			this.subjectGrades  = journal.getSubjectGrades(currentSubject.toString());
		}

		@Override
		public Class<?> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		@Override
		public String getColumnName(int col) {
			if (col == 0) {
				return "No.";
			} else if (col == 1) {
				return "Full name";
			} else {
				return Integer.toString(col - 1);
			}
		}

		@Override
		public int getColumnCount() {
			int maxColumnLength = 0;
			for (List<Journal.Grades> studentGrades : subjectGrades.values()) {
				if (studentGrades.size() > maxColumnLength) {
					maxColumnLength = studentGrades.size();
				}
			}
			return maxColumnLength + 2;
		}

		@Override
		public int getRowCount() {
			return journal.getStudents().size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			Iterator<Entry<Student, List<Journal.Grades>>> it = subjectGrades.entrySet().iterator();
			Entry<Student, List<Journal.Grades>> studentGradesEntry;
			int counter = -1;
			do {
				studentGradesEntry = it.next();
				++counter;
			} while (counter != arg0);

			if (arg1 == 0) {
				return studentGradesEntry.getKey().getJournalNumber();
			} else if (arg1 == 1) {
				return studentGradesEntry.getKey().toString();
			} else if (arg1 < studentGradesEntry.getValue().size() + 2) {
				return studentGradesEntry.getValue().get(arg1 - 2);
			} else
				return "";
		}
	}
}
