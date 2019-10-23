package gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import appLogic.Journal;
import appLogic.Student;

public class StudentDataTable extends JTable implements ActionListener {
	private static final long serialVersionUID = -4506888084344862526L;
	
	private final Journal journal;

	public StudentDataTable(Journal journal) {
		this.journal = journal;
		
		this.setCellSelectionEnabled(false);
		this.getTableHeader().setReorderingAllowed(false);
		this.createModel();
		this.getColumnModel().getColumn(0).setMaxWidth(60);
	}
	
	private void createModel() {
		this.setModel(new StudentDataTableModel(journal));

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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("add")) {
			String studentData = JOptionPane.showInputDialog(this.getRootPane(), "Enter student's full name:",
					"Add student", JOptionPane.PLAIN_MESSAGE);
			String[] studentFullName = studentData.split("[^\\w]+");
			journal.addStudent(studentFullName[0], studentFullName[1]);

		} else if (e.getActionCommand().equals("remove")) {
			Collection<Integer> journalNumbers = journal.getStudents().keySet();
			Set<Integer> sortedJournalNumbers = new TreeSet<>(journalNumbers);
			Object[] journalNumbersArray = sortedJournalNumbers.toArray();
			Integer userChoice = (Integer) JOptionPane.showInputDialog(this.getRootPane(),
					"Choose a student by the journal number:", "Remove student", JOptionPane.PLAIN_MESSAGE, null,
					journalNumbersArray, journalNumbersArray[0]);
			journal.removeStudent(userChoice);
		}
		this.createModel();
		this.refreshView();
	}

	private void refreshView() {
		this.getColumnModel().getColumn(0).setMaxWidth(60);
		this.revalidate();
		this.repaint();
	}

	private class StudentDataTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -1307155370328154847L;
		
		private static final int STUDENT_DATA_COUNT = 3;
		private final Map<Integer, Student> students;

		public StudentDataTableModel(Journal journal) {
			this.students = journal.getStudents();
		}

		@Override
		public String getColumnName(int col) {
			switch (col) {
			case 0:
				return "No.";
			case 1:
				return "First name";
			case 2:
				return "Last name";
			default:
				return "";
			}
		}
		
		@Override
		public Class<?> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		@Override
		public int getColumnCount() {
			return STUDENT_DATA_COUNT;
		}

		@Override
		public int getRowCount() {
			return students.size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			Student student;
			Iterator<Entry<Integer, Student>> it = students.entrySet().iterator();
			int counter = -1;
			do {
				student = it.next().getValue();
				++counter;
			} while (counter != arg0);

			switch (arg1) {
			case 0:
				return student.getJournalNumber();
			case 1:
				return student.getFirstName();
			case 2:
				return student.getLastName();
			default:
				throw new IllegalArgumentException();
			}
		}
	}
}
