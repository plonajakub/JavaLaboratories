package lab05.gui.component_sets;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SupplierPanel extends JPanel {

	private static final long serialVersionUID = -3719187563851230571L;

	private static final String NAME_CONST = "Name: ";
	private static final String STATE_CONST = "State: ";

	private final JLabel nameLabel;
	private final JLabel stateLabel;

	public SupplierPanel() {
		this.nameLabel = new JLabel(NAME_CONST + "-", SwingConstants.CENTER);
		this.stateLabel = new JLabel(STATE_CONST + "-", SwingConstants.CENTER);

		this.setLayout(new GridLayout(2, 1));
		this.setBackground(Color.decode("#78c625"));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		prepareComponenets();
	}

	private void prepareComponenets() {
//		nameLabel.setMaximumSize(labelSize);
//		nameLabel.setPreferredSize(labelSize);
//		nameLabel.setMinimumSize(labelSize);
		this.add(nameLabel);

//		stateLabel.setMaximumSize(labelSize);
//		stateLabel.setPreferredSize(labelSize);
//		stateLabel.setMinimumSize(labelSize);
		this.add(stateLabel);

	}

	public void setNameLabelText(String name) {
		nameLabel.setText(NAME_CONST + name);
	}

	public void setStateLabelText(String stateDiscription) {
		stateLabel.setText(STATE_CONST + stateDiscription);
	}
}
