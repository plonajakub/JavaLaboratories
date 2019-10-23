package lab06_pop.gui.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import lab06_pop.tasks.TaskInProgressException;
import lab06_pop.tasks.TaskManager;

public class TaskTable extends JTable implements ActionListener {

	public TaskTable() {
		TableModel model = new TaskTableModel();
		setModel(model);

		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.getModel());
		setRowSorter(sorter);

		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.setComparator(0, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return new Integer(o1).compareTo(new Integer(o2));
			}
		});
		sorter.sort();

		setRowSelectionAllowed(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setReorderingAllowed(false);
		for (int i = 0; i < getColumnCount(); ++i) {
			sorter.setSortable(i, false);
		}

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		this.setDefaultRenderer(String.class, centerRenderer);
		
		
		getColumnModel().getColumn(0).setMaxWidth(40);
		getColumnModel().getColumn(1).setMinWidth(120);
		getColumnModel().getColumn(2).setMaxWidth(100);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("add")) {
			String input = JOptionPane.showInputDialog(getRootPane(),
					"Enter new task descrition\nFormat: <type;arguments;priority;category>", "Add new task",
					JOptionPane.PLAIN_MESSAGE);
			try {
				List<String> splitedInput = Arrays.asList(input.split(";"));
				Map<String, String> newEntry = new HashMap<>();
				newEntry.put("type", splitedInput.get(0));
				newEntry.put("arguments", splitedInput.get(1));
				newEntry.put("priority", splitedInput.get(2));
				newEntry.put("category", splitedInput.get(3));
				TaskManager.getInstance().addTask(newEntry);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(getRootPane(), "Wrong data format!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			((AbstractTableModel) getModel()).fireTableDataChanged();
		} else if (arg0.getActionCommand().equals("remove")) {
			if (getSelectedRows().length == 0) {
				JOptionPane.showMessageDialog(getRootPane(), "Select a row before performing this operation.",
						"Empty selection", JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				TaskManager.getInstance()
						.removeTask((String) getModel().getValueAt(convertRowIndexToModel(getSelectedRow()), 0));
			} catch (TaskInProgressException e) {
				JOptionPane.showMessageDialog(getRootPane(), "Task is being executed thus it cannot be deleted.",
						"Operation forbidden", JOptionPane.ERROR_MESSAGE);
				return;
			}
			((AbstractTableModel) getModel()).fireTableDataChanged();
		}

	}
}
