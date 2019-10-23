package lab07_pop.office;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class OfficeMainPanel extends JPanel {

	private final JLabel currentTicketLabel, currentTicketInfoLabel;
	private final JButton officeButton;

	private final Office office;

	OfficeMainPanel() {
		currentTicketLabel = new JLabel("Current ticket:", SwingConstants.CENTER);
		currentTicketInfoLabel = new JLabel("-", SwingConstants.CENTER);
		officeButton = new JButton();

		office = new Office(this);

		BoxLayout mainLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(mainLayout);

		prepareComponents();
	}

	private void prepareComponents() {

		////////////////////////////////////////////////////
		JPanel currentTicketPanel = new JPanel();
		BoxLayout currentTicketPanelLayout = new BoxLayout(currentTicketPanel, BoxLayout.LINE_AXIS);
		currentTicketPanel.setLayout(currentTicketPanelLayout);

		currentTicketLabel.setAlignmentX(LEFT_ALIGNMENT);
		currentTicketPanel.add(currentTicketLabel);

		currentTicketPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		currentTicketInfoLabel.setAlignmentX(LEFT_ALIGNMENT);
		currentTicketInfoLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.red),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		currentTicketPanel.add(currentTicketInfoLabel);

		////////////////////////////////////////////////////
		add(Box.createVerticalGlue());

		currentTicketPanel.setAlignmentX(CENTER_ALIGNMENT);
		add(currentTicketPanel);

		add(Box.createVerticalGlue());

		officeButton.setAlignmentX(CENTER_ALIGNMENT);
		add(officeButton);

		add(Box.createVerticalGlue());

		////////////////////////////////////////////////////
	}

	JLabel getCurrentTicketInfoLabel() {
		return currentTicketInfoLabel;
	}

	JButton getOfficeButton() {
		return officeButton;
	}
}
