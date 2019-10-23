package lab07_pop.monitor;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

class MonitorMainPanel extends JPanel {

	private final Monitor monitor;
	private final MonitorTable monitorTable;

	MonitorMainPanel() {
		monitor = new Monitor(this);
		monitorTable = new MonitorTable(monitor);

		BorderLayout mainLayout = new BorderLayout();
		setLayout(mainLayout);

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(new JScrollPane(monitorTable), BorderLayout.CENTER);
	}

	MonitorTable getMonitorTable() {
		return monitorTable;
	}

}
