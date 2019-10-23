package lab07_pop.monitor;

import javax.swing.table.AbstractTableModel;

import lab07_pop.Info;
import lab07_pop.Ticket;

class MonitorTableModel extends AbstractTableModel {

	private static final int COLUMN_COUNT = 3;
	private final Monitor monitor;

	MonitorTableModel(Monitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Category";
		case 1:
			return "Pending appliciants";
		case 2:
			return "Tickets being served";
		default:
			return "";
		}
	}

	@Override
	public Class<? extends Object> getColumnClass(int c) {
		if (c == 0)
			return String.class;
		else
			return Integer.class;
	}

	@Override
	public int getRowCount() {
		return monitor.getInfo().length;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		Info[] info = monitor.getInfo();
		switch (arg1) {
		case 0:
			return info[arg0].category;
		case 1:
			return info[arg0].waiting;
		case 2:
			Ticket[] servingTickets = info[arg0].serving;
			if (servingTickets.length != 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("No. " + info[arg0].serving[0].number);
				for (int i = 1; i != servingTickets.length; ++i) {
					sb.append(", No. " + servingTickets[i].number);
				}
				return sb.toString();
			} else {
				return "-";
			}
		default:
			return "";
		}
	}

}
