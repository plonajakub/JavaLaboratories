package gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Month;
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
import appLogic.Journal.Presence;
import appLogic.Student;

public class StudentPresenceTable extends JTable implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 8973370668840970970L;

	private final Journal journal;
	private final StringBuffer currentSubject;
	private Month currentMonth;

	public StudentPresenceTable(Journal journal) {
		this.journal = journal;
		this.currentSubject = new StringBuffer();
		this.currentSubject.append(journal.getAvailableSubjects().get(0));
		this.currentMonth = Month.SEPTEMBER;

		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.getTableHeader().setReorderingAllowed(false);
		this.setCellSelectionEnabled(true);

		this.createModel();
		this.refreshTable();
	}

	private void createModel() {
		this.setModel(new StudentPresenceTableModel(journal, currentSubject, currentMonth));

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

	private void refreshTable() {
		this.getColumnModel().getColumn(0).setMaxWidth(80);
		this.getColumnModel().getColumn(1).setMinWidth(150);
		this.revalidate();
		this.repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		this.createModel();
		this.refreshTable();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("choose_subject")) {
			JComboBox<Object> source = (JComboBox<Object>) e.getSource();
			currentSubject.delete(0, currentSubject.length());
			currentSubject.append(source.getSelectedItem());
			this.createModel();
			this.refreshTable();

		} else if (e.getActionCommand().equals("choose_month")) {
			JComboBox<Object> source = (JComboBox<Object>) e.getSource();
			currentMonth = Month.valueOf((String) source.getSelectedItem());
			this.createModel();
			this.refreshTable();

		} else if (e.getActionCommand().equals("set_presence")) {
			if (this.getSelectedRows().length == 0) {
				JOptionPane.showMessageDialog(this.getRootPane(), "Select a student before performing this operation!",
						"Operation not allowed", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String[] possiblePresenceStates = Journal.Presence.getPossiblePresenceStates();
			String presence = (String) JOptionPane.showInputDialog(this.getRootPane(), "Choose a state",
					"Add presence entry", JOptionPane.PLAIN_MESSAGE, null, possiblePresenceStates,
					possiblePresenceStates[0]);
			Integer journalNumber = decodeJournalNumber(this.getSelectedRow());
			journal.setPresence(journalNumber, currentSubject.toString(), currentMonth,
					LocalDate.of(decodeYear(currentMonth), currentMonth, getSelectedColumn() - 1),
					Presence.fromString(presence));

			this.createModel();
			this.refreshTable();
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

	private int decodeYear(Month m) {
		if (m.equals(Month.SEPTEMBER) || m.equals(Month.OCTOBER) || m.equals(Month.NOVEMBER)
				|| m.equals(Month.DECEMBER)) {
			return Integer.parseInt(journal.getStartYear());
		}
		return Integer.parseInt(journal.getEndYear());
	}

	private class StudentPresenceTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 8661523499521138129L;

		private final Journal journal;
		Map<Student, Map<Month, Map<LocalDate, Journal.Presence>>> subjectPresence;
		private final Month currentMonth;

		public StudentPresenceTableModel(Journal journal, StringBuffer currentSubject, Month month) {
			this.journal = journal;
			this.subjectPresence = journal.getSubjectPresence(currentSubject.toString());
			this.currentMonth = month;
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
			return currentMonth.length(LocalDate.now().isLeapYear()) + 2;
		}

		@Override
		public int getRowCount() {
			return journal.getStudents().size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			Iterator<Map.Entry<Student, Map<Month, Map<LocalDate, Journal.Presence>>>> it = subjectPresence.entrySet()
					.iterator();
			Map.Entry<Student, Map<Month, Map<LocalDate, Journal.Presence>>> studentPresenceEntry;
			int counter = -1;
			do {
				studentPresenceEntry = it.next();
				++counter;
			} while (counter != arg0);

			if (arg1 == 0) {
				return studentPresenceEntry.getKey().getJournalNumber();
			} else if (arg1 == 1) {
				return studentPresenceEntry.getKey().toString();
			} else if (arg1 < currentMonth.length(true) + 2) {
				Map<LocalDate, Journal.Presence> datePresence = studentPresenceEntry.getValue().get(currentMonth);
				Iterator<Entry<LocalDate, Journal.Presence>> dateIt = datePresence.entrySet().iterator();
				Entry<LocalDate, Journal.Presence> dayPresence;
				int dayCounter = 1;
				do {
					dayPresence = dateIt.next();
					++dayCounter;
				} while (dayCounter != arg1);
				return dayPresence.getValue().getPresence();
			} else
				return "";
		}
	}
}
