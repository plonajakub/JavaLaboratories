package gui.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
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

public class StudentMeanGradesTable extends JTable implements ChangeListener {
	private static final long serialVersionUID = -288543990954304805L;
	
	private final Journal journal;
	private final JLabel classMeanLabel;

	public StudentMeanGradesTable(Journal journal, JLabel classMeanLabel) {
		this.journal = journal;
		this.classMeanLabel = classMeanLabel;

		this.getTableHeader().setReorderingAllowed(false);
		this.setCellSelectionEnabled(false);

		this.createModel();
		this.refreshView();
	}

	private void createModel() {
		this.setModel(new StudentMeanGradesTableModel(journal));

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
		this.setDefaultRenderer(Double.class, centerRenderer);
	}

	private void refreshView() {
		this.getColumnModel().getColumn(0).setMaxWidth(60);
		this.revalidate();
		this.repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		this.classMeanLabel.setText(journal.getClassMean().toString());
		this.createModel();
		this.refreshView();
	}

	private class StudentMeanGradesTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 2121726306462991382L;
		
		private final Journal journal;
		
		private final Map<Student, Map<String, Double>> studentSubjectMeans;
		private final Map<Student, Double> studentMeans;

		public StudentMeanGradesTableModel(Journal journal) {
			this.journal = journal;
			this.studentSubjectMeans = journal.getStudentsSubjectMean();
			this.studentMeans = journal.getStudentsMean();
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
			} else if (col < journal.getAvailableSubjects().size() + 2) {
				return journal.getAvailableSubjects().get(col - 2);
			} else {
				return "Avarage";
			}
		}

		@Override
		public int getColumnCount() {
			return journal.getAvailableSubjects().size() + 3;
		}

		@Override
		public int getRowCount() {
			return journal.getStudents().size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			Iterator<Map.Entry<Student, Map<String, Double>>> subjectMeanIt = studentSubjectMeans.entrySet().iterator();
			Iterator<Map.Entry<Student, Double>> studentMeanIt = studentMeans.entrySet().iterator();
			Map.Entry<Student, Map<String, Double>> subjectMeanEntry;
			Map.Entry<Student, Double> studentMeanEntry;
			int counter = -1;
			do {
				subjectMeanEntry = subjectMeanIt.next();
				studentMeanEntry = studentMeanIt.next();
				++counter;
			} while (counter != arg0);

			if (arg1 == 0) {
				return subjectMeanEntry.getKey().getJournalNumber();
			} else if (arg1 == 1) {
				return subjectMeanEntry.getKey().toString();
			} else if (arg1 > 1 && arg1 < subjectMeanEntry.getValue().size() + 2) {
				return subjectMeanEntry.getValue().get(journal.getAvailableSubjects().get(arg1 - 2));
			} else {
				return studentMeanEntry.getValue();
			}
		}
	}
}
