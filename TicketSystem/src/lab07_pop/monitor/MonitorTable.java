package lab07_pop.monitor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

class MonitorTable extends JTable {

	MonitorTable(Monitor monitor) {
		TableModel model = new MonitorTableModel(monitor);
		setModel(model);

		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.getModel());
		setRowSorter(sorter);

		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.sort();

		setCellSelectionEnabled(false);
		getTableHeader().setReorderingAllowed(false);
		for (int i = 0; i < getColumnCount(); ++i) {
			sorter.setSortable(i, false);
		}

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		this.setDefaultRenderer(String.class, centerRenderer);

		getColumnModel().getColumn(0).setMinWidth(125);
		getColumnModel().getColumn(1).setMinWidth(150);
		getColumnModel().getColumn(2).setMinWidth(150);
	}
}
