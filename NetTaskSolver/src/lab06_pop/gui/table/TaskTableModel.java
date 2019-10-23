package lab06_pop.gui.table;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import lab06_pop.tasks.TaskManager;

public class TaskTableModel extends AbstractTableModel {

	private static int COLUMN_COUNT = 7;
	private Map<Integer, Map<String, String>> taskData = TaskManager.getInstance().getTasksPrintableRepresentation();

	@Override
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "ID";
		case 1:
			return "Type";
		case 2:
			return "Arguments";
		case 3:
			return "Priority";
		case 4:
			return "Category";
		case 5:
			return "State";
		case 6:
			return "Result";
		default:
			return "";
		}
	}

	@Override
	public Class<? extends Object> getColumnClass(int c) {
		if (c == 0 || c == 6)
			return Integer.class;
		else
			return String.class;
	}

	@Override
	public int getRowCount() {
		return taskData.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		synchronized (TaskManager.getInstance()) {
			Map<String, String> entry = new ArrayList<Entry<Integer, Map<String, String>>>(taskData.entrySet())
					.get(arg0).getValue();
			switch (arg1) {
			case 0:
				return entry.get("ID");
			case 1:
				return entry.get("Type");
			case 2:
				return entry.get("Arguments");
			case 3:
				return entry.get("Priority");
			case 4:
				return entry.get("Category");
			case 5:
				return entry.get("State");
			case 6:
				return entry.get("Result");
			default:
				return "";
			}
		}
	}

}
