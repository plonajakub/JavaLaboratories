package lab06_pop.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import lab06_pop.gui.table.TaskTable;
import lab06_pop.net.server.ServerManager;

public class ServerPanel extends JPanel {

	private final TaskTable taskTable;
	private final JLabel serverLabel;
	private final JLabel portLabel;
	private final JTextField portTextField;
	private final JLabel serverStateLabel;
	private final JLabel serverStateLabelInfo;
	private final JButton startServerButton, stopServerButton;
	private final JLabel tableLabel;
	private final JButton addTaskButton, removeTaskButton;

	private final ServerManager serverManager;

	ServerPanel() {
		taskTable = new TaskTable();
		serverLabel = new JLabel("--- Server panel ---", JLabel.CENTER);
		portLabel = new JLabel("Port: ");
		portTextField = new JTextField("8080");
		serverStateLabel = new JLabel("Server status:", JLabel.CENTER);
		serverStateLabelInfo = new JLabel("stopped", JLabel.CENTER);
		startServerButton = new JButton("Start server");
		stopServerButton = new JButton("Stop server");
		tableLabel = new JLabel("--- Table panel ---", JLabel.CENTER);
		addTaskButton = new JButton("Add task");
		removeTaskButton = new JButton("Remove task");

		serverManager = new ServerManager(this);

		BorderLayout mainLayout = new BorderLayout();
		setLayout(mainLayout);

		prepareComponents();
	}

	private void prepareComponents() {

		//////////////////////////////////////
		FlowLayout serverStatePanelLayout = new FlowLayout(FlowLayout.CENTER, 10, 5);
		JPanel serverStatePanel = new JPanel(serverStatePanelLayout);
		serverStatePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

		serverStatePanel.add(serverStateLabel);

		serverStateLabelInfo.setBorder(BorderFactory.createLineBorder(Color.red));
		serverStateLabelInfo.setForeground(Color.red);
		serverStateLabelInfo.setPreferredSize(new Dimension(50, 20));
		serverStatePanel.add(serverStateLabelInfo);

		//////////////////////////////////////
		FlowLayout portPanelLayout = new FlowLayout(FlowLayout.CENTER, 10, 5);
		JPanel portPanel = new JPanel(portPanelLayout);
		portPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

		portPanel.add(portLabel);

		portTextField.setPreferredSize(new Dimension(150, 20));
		portPanel.add(portTextField);

		///////////////////////////////////////
		JPanel serverSubPanel = new JPanel();
		BoxLayout serverSubPanelLayout = new BoxLayout(serverSubPanel, BoxLayout.PAGE_AXIS);
		serverSubPanel.setLayout(serverSubPanelLayout);
		serverSubPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		serverSubPanel.add(Box.createVerticalGlue());

		serverLabel.setAlignmentX(CENTER_ALIGNMENT);
		serverSubPanel.add(serverLabel);

		serverSubPanel.add(Box.createRigidArea(new Dimension(Integer.MAX_VALUE, 30)));

		serverStatePanel.setAlignmentX(CENTER_ALIGNMENT);
		serverSubPanel.add(serverStatePanel);

		serverSubPanel.add(Box.createRigidArea(new Dimension(Integer.MAX_VALUE, 10)));
		serverSubPanel.add(Box.createVerticalGlue());

		portPanel.setAlignmentX(CENTER_ALIGNMENT);
		serverSubPanel.add(portPanel);

		serverSubPanel.add(Box.createRigidArea(new Dimension(Integer.MAX_VALUE, 20)));

		startServerButton.setActionCommand("START_SERVER");
		startServerButton.addActionListener(serverManager);
		startServerButton.setAlignmentX(CENTER_ALIGNMENT);
		serverSubPanel.add(startServerButton);

		serverSubPanel.add(Box.createRigidArea(new Dimension(Integer.MAX_VALUE, 20)));

		stopServerButton.setActionCommand("STOP_SERVER");
		stopServerButton.addActionListener(serverManager);
		stopServerButton.setEnabled(false);
		stopServerButton.setAlignmentX(CENTER_ALIGNMENT);
		serverSubPanel.add(stopServerButton);

		serverSubPanel.add(Box.createVerticalGlue());

		///////////////////////////////////////
		JPanel tableButtonPanel = new JPanel();
		BoxLayout tableButtonPanelLayout = new BoxLayout(tableButtonPanel, BoxLayout.PAGE_AXIS);
		tableButtonPanel.setLayout(tableButtonPanelLayout);
		tableButtonPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		tableButtonPanel.add(Box.createVerticalGlue());
		tableButtonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		
		tableLabel.setAlignmentX(CENTER_ALIGNMENT);
		tableButtonPanel.add(tableLabel);

		tableButtonPanel.add(Box.createRigidArea(new Dimension(0, 60)));

		addTaskButton.setActionCommand("add");
		addTaskButton.addActionListener(taskTable);
		addTaskButton.setAlignmentX(CENTER_ALIGNMENT);
		addTaskButton.setMaximumSize(new Dimension(120, 30));
		tableButtonPanel.add(addTaskButton);

		tableButtonPanel.add(Box.createRigidArea(new Dimension(Integer.MAX_VALUE, 20)));

		removeTaskButton.setActionCommand("remove");
		removeTaskButton.addActionListener(taskTable);
		removeTaskButton.setAlignmentX(CENTER_ALIGNMENT);
		removeTaskButton.setMaximumSize(new Dimension(120, 30));
		tableButtonPanel.add(removeTaskButton);

		tableButtonPanel.add(Box.createVerticalGlue());

		///////////////////////////////////////
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new GridLayout(2, 1, 0, 2));
		sidePanel.setMinimumSize(new Dimension(230, 0));
		sidePanel.setPreferredSize(new Dimension(250, 0));

		sidePanel.add(serverSubPanel);
		sidePanel.add(tableButtonPanel);

		///////////////////////////////////////
		JScrollPane tableScrollPane = new JScrollPane(taskTable);
		tableScrollPane.setMinimumSize(new Dimension(400, 0));
		tableScrollPane.setPreferredSize(new Dimension(500, 0));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, sidePanel);
		splitPane.setDividerLocation(800);
		add(splitPane, BorderLayout.CENTER);
	}

	public TaskTable getTaskTable() {
		return taskTable;
	}

	public JTextField getPortTextField() {
		return portTextField;
	}

	public JLabel getServerStateLabelInfo() {
		return serverStateLabelInfo;
	}

	public JButton getStartServerButton() {
		return startServerButton;
	}

	public JButton getStopServerButton() {
		return stopServerButton;
	}
}
