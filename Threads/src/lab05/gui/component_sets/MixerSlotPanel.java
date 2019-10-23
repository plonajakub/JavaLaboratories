package lab05.gui.component_sets;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MixerSlotPanel extends JPanel {

	private static final long serialVersionUID = 2546000905359172420L;

	private static final String NAME_CONST = "Slot No. ";
	private static final String USER_CONST = "User: ";
	private static final String STATE_CONST = "State: ";

	private final JLabel slotIndexLabel;
	private final JLabel userLabel;
	private final JLabel stateLabel;

	public MixerSlotPanel(int index) {
		this.slotIndexLabel = new JLabel(NAME_CONST + Integer.toString(index), SwingConstants.CENTER);
		this.userLabel = new JLabel(USER_CONST + "-", SwingConstants.CENTER);
		this.stateLabel = new JLabel(STATE_CONST + "slot free", SwingConstants.CENTER);

		this.setLayout(new GridLayout(3, 1));
		this.setBackground(Color.decode("#24c6c6"));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		prepareComponenets();
	}

	private void prepareComponenets() {
//		slotIndexLabel.setMaximumSize(labelSize);
//		slotIndexLabel.setPreferredSize(labelSize);
//		slotIndexLabel.setMinimumSize(labelSize);
		this.add(slotIndexLabel);

//		userLabel.setMaximumSize(labelSize);
//		userLabel.setPreferredSize(labelSize);
//		userLabel.setMinimumSize(labelSize);
		this.add(userLabel);

//		stateLabel.setMaximumSize(labelSize);
//		stateLabel.setPreferredSize(labelSize);
//		stateLabel.setMinimumSize(labelSize);
		this.add(stateLabel);
	}

	public void setCurrentSlotUser(String user) {
		userLabel.setText(USER_CONST + user);
	}

	public void setStateLabelText(String stateDiscription) {
		stateLabel.setText(STATE_CONST + stateDiscription);
	}
}
