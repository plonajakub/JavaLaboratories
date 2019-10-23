package lab07_pop.terminal;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class TerminalMainPanel extends JPanel {

	private final JLabel lastTicketLabel, lastTicketInfoLabel;
	private final JComboBox<String> categoryComboBox;
	private final JButton getTicketButton;

	private final Terminal terminal;

	TerminalMainPanel() {
		lastTicketLabel = new JLabel("Last issued ticket:", SwingConstants.CENTER);
		lastTicketInfoLabel = new JLabel("-", SwingConstants.CENTER);
		categoryComboBox = new JComboBox<>();
		getTicketButton = new JButton("Get ticket");

		terminal = new Terminal(this);

		BoxLayout mainLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(mainLayout);

		prepareComponents();
	}

	private void prepareComponents() {

		////////////////////////////////////////////////////
		JPanel lastTicketPanel = new JPanel();
		BoxLayout lastTicketPanelLayout = new BoxLayout(lastTicketPanel, BoxLayout.LINE_AXIS);
		lastTicketPanel.setLayout(lastTicketPanelLayout);

		lastTicketLabel.setAlignmentX(LEFT_ALIGNMENT);
		lastTicketPanel.add(lastTicketLabel);

		lastTicketPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		lastTicketInfoLabel.setAlignmentX(LEFT_ALIGNMENT);
		lastTicketInfoLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.red),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		lastTicketPanel.add(lastTicketInfoLabel);

		////////////////////////////////////////////////////
		add(Box.createVerticalGlue());

		lastTicketPanel.setAlignmentX(CENTER_ALIGNMENT);
		add(lastTicketPanel);

		add(Box.createVerticalGlue());

		categoryComboBox.setAlignmentX(CENTER_ALIGNMENT);
		categoryComboBox.setMaximumSize(new Dimension(150, 50));
		add(categoryComboBox);

		add(Box.createRigidArea(new Dimension(0, 15)));

		getTicketButton.setAlignmentX(CENTER_ALIGNMENT);
		add(getTicketButton);

		add(Box.createVerticalGlue());

		////////////////////////////////////////////////////
	}

	JLabel getLastTicketInfoLabel() {
		return lastTicketInfoLabel;
	}

	JComboBox<String> getCategoryComboBox() {
		return categoryComboBox;
	}

	JButton getGetTicketButton() {
		return getTicketButton;
	}
}
