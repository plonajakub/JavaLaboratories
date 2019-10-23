package lab06_pop.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import lab06_pop.net.client.ClientManager;

public class ClientPanel extends JPanel {

	private final JTextArea logTextArea;
	private final JLabel clientPanelLabel;
	private final JLabel clientStateLabel, clientStateInfoLabel;
	private final JLabel hostAdressLabel;
	private final JTextField hostAdressTextField;
	private final JLabel hostPortLabel;
	private final JTextField hostPortTextField;
	private final JButton startClientButton;
	private final JButton stopClientButton;

	private final ClientManager clientManager;

	ClientPanel() {
		logTextArea = new JTextArea();
		clientPanelLabel = new JLabel("--- Client panel ---");
		clientStateLabel = new JLabel("Client status:");
		clientStateInfoLabel = new JLabel("stopped");
		hostAdressLabel = new JLabel("Host IP:");
		hostAdressTextField = new JTextField("localhost");
		hostPortLabel = new JLabel("Port:");
		hostPortTextField = new JTextField("8080");
		startClientButton = new JButton("Start client");
		stopClientButton = new JButton("Stop client");

		clientManager = new ClientManager(this);

		BorderLayout mainLayout = new BorderLayout();
		setLayout(mainLayout);

		prepareComponents();
	}

	private void prepareComponents() {
		
		//////////////////////////////////////////////
		FlowLayout statusPanelLayout = new FlowLayout(FlowLayout.CENTER);
		JPanel statusPanel = new JPanel(statusPanelLayout);
		statusPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		statusPanel.add(Box.createHorizontalGlue());

		statusPanel.add(clientStateLabel);
		
		statusPanel.add(Box.createHorizontalGlue());

		clientStateInfoLabel.setBorder(BorderFactory.createLineBorder(Color.red));
		clientStateInfoLabel.setForeground(Color.red);
		clientStateInfoLabel.setHorizontalAlignment(JLabel.CENTER);
		clientStateInfoLabel.setPreferredSize(new Dimension(80, 20));
		statusPanel.add(clientStateInfoLabel);

		//////////////////////////////////////////////
		FlowLayout hostAdressPanelLayout = new FlowLayout(FlowLayout.CENTER, 10, 5);
		JPanel hostAdressPanel = new JPanel(hostAdressPanelLayout);
		hostAdressPanel.setLayout(hostAdressPanelLayout);

		hostAdressPanel.add(hostAdressLabel);

		hostAdressTextField.setMinimumSize(new Dimension(80, 20));
		hostAdressTextField.setPreferredSize(new Dimension(100, 20));
		hostAdressTextField.setMaximumSize(new Dimension(140, 20));
		hostAdressPanel.add(hostAdressTextField);

		JPanel surroundingHostAdressPanel = new JPanel();
		BoxLayout surroundingHostAdressPanelLayout = new BoxLayout(surroundingHostAdressPanel, BoxLayout.LINE_AXIS);
		surroundingHostAdressPanel.setLayout(surroundingHostAdressPanelLayout);

		surroundingHostAdressPanel.add(Box.createHorizontalGlue());

		hostAdressPanel.setAlignmentX(CENTER_ALIGNMENT);
		hostAdressPanel.setMinimumSize(new Dimension(180, 30));
		hostAdressPanel.setPreferredSize(new Dimension(180, 30));
		hostAdressPanel.setMaximumSize(new Dimension(200, 30));
		surroundingHostAdressPanel.add(hostAdressPanel);

		surroundingHostAdressPanel.add(Box.createHorizontalGlue());
		//////////////////////////////////////////////
		FlowLayout hostPortPanelLayout = new FlowLayout(FlowLayout.CENTER, 10, 5);
		JPanel hostPortPanel = new JPanel(hostPortPanelLayout);

		hostPortPanel.add(hostPortLabel);

		hostPortTextField.setMinimumSize(new Dimension(40, 20));
		hostPortTextField.setPreferredSize(new Dimension(50, 20));
		hostPortTextField.setMaximumSize(new Dimension(60, 20));
		hostPortPanel.add(hostPortTextField);
		
		JPanel surroundingHostPortPanel = new JPanel();
		BoxLayout surroundingHostPortPanelLayout = new BoxLayout(surroundingHostPortPanel, BoxLayout.LINE_AXIS);
		surroundingHostPortPanel.setLayout(surroundingHostPortPanelLayout);
		
		surroundingHostPortPanel.add(Box.createHorizontalGlue());

		hostPortPanel.setAlignmentX(CENTER_ALIGNMENT);
		hostPortPanel.setMinimumSize(new Dimension(180, 30));
		hostPortPanel.setPreferredSize(new Dimension(180, 30));
		hostPortPanel.setMaximumSize(new Dimension(200, 30));
		surroundingHostPortPanel.add(hostPortPanel);

		surroundingHostPortPanel.add(Box.createHorizontalGlue());
		
		//////////////////////////////////////////////
		JPanel clientSubPanel = new JPanel();
		BoxLayout clientSubPanelLayout = new BoxLayout(clientSubPanel, BoxLayout.PAGE_AXIS);
		clientSubPanel.setLayout(clientSubPanelLayout);
		clientSubPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		clientSubPanel.setMinimumSize(new Dimension(230, 0));
		clientSubPanel.setPreferredSize(new Dimension(250, 0));
		clientSubPanel.setMaximumSize(new Dimension(300, 0));

		clientSubPanel.add(Box.createVerticalGlue());
		clientSubPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		clientPanelLabel.setAlignmentX(CENTER_ALIGNMENT);
		clientSubPanel.add(clientPanelLabel);

		clientSubPanel.add(Box.createRigidArea(new Dimension(0, 50)));

		statusPanel.setAlignmentX(CENTER_ALIGNMENT);
		clientSubPanel.add(statusPanel);

		clientSubPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		clientSubPanel.add(Box.createVerticalGlue());

		surroundingHostAdressPanel.setAlignmentX(CENTER_ALIGNMENT);
		clientSubPanel.add(surroundingHostAdressPanel);

		surroundingHostPortPanel.setAlignmentX(CENTER_ALIGNMENT);
		clientSubPanel.add(surroundingHostPortPanel);

		clientSubPanel.add(Box.createRigidArea(new Dimension(0, 30)));

		startClientButton.setActionCommand("START_CLIENT");
		startClientButton.addActionListener(clientManager);
		startClientButton.setAlignmentX(CENTER_ALIGNMENT);
		startClientButton.setMaximumSize(new Dimension(100, 30));
		clientSubPanel.add(startClientButton);

		clientSubPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		stopClientButton.setActionCommand("STOP_CLIENT");
		stopClientButton.addActionListener(clientManager);
		stopClientButton.setEnabled(false);
		stopClientButton.setAlignmentX(CENTER_ALIGNMENT);
		stopClientButton.setMaximumSize(new Dimension(100, 30));
		clientSubPanel.add(stopClientButton);

		clientSubPanel.add(Box.createVerticalGlue());

		//////////////////////////////////////////////
		logTextArea.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(logTextArea);
		logScrollPane.setMinimumSize(new Dimension(400, 0));
		logScrollPane.setPreferredSize(new Dimension(500, 0));

		clientSubPanel.setMinimumSize(new Dimension(100, 0));
		clientSubPanel.setPreferredSize(new Dimension(120, 0));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, logScrollPane, clientSubPanel);
		splitPane.setDividerLocation(800);
		add(splitPane, BorderLayout.CENTER);

	}

	public JTextArea getLogTextArea() {
		return logTextArea;
	}

	public JLabel getClientStateInfoLabel() {
		return clientStateInfoLabel;
	}

	public JTextField getHostAdressTextField() {
		return hostAdressTextField;
	}

	public JTextField getHostPortTextField() {
		return hostPortTextField;
	}

	public JButton getStartClientButton() {
		return startClientButton;
	}

	public JButton getStopClientButton() {
		return stopClientButton;
	}
}
