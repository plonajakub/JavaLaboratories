package lab05.gui.component_sets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MixerInfoPanel extends JPanel {

	private static final long serialVersionUID = -1458913417420514942L;
	
	private static final String NAME_CONST = "Mixer State";
	private static final String ACTIVE_QUERY_CONST = "Active queries: ";
	private static final String DELAYD_QUEY_CONST = "Delayed queries: ";

	private final JLabel nameLabel;
	private final JLabel activeQueriesLabel;
	private final JLabel delayedQueriesLabel;

	public MixerInfoPanel() {
		this.nameLabel = new JLabel(NAME_CONST, SwingConstants.CENTER);
		this.activeQueriesLabel = new JLabel(ACTIVE_QUERY_CONST + "0", SwingConstants.CENTER);
		this.delayedQueriesLabel = new JLabel(DELAYD_QUEY_CONST + "0", SwingConstants.CENTER);
		
		this.setLayout(new GridLayout(3, 1));
		this.setBackground(Color.decode("#00FFAA"));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		prepareComponenets();
	}

	private void prepareComponenets() {
//		nameLabel.setMaximumSize(labelSize);
//		nameLabel.setPreferredSize(labelSize);
//		nameLabel.setMinimumSize(labelSize);
		this.add(nameLabel);

//		activeQueriesLabel.setMaximumSize(labelSize);
//		activeQueriesLabel.setPreferredSize(labelSize);
//		activeQueriesLabel.setMinimumSize(labelSize);
		this.add(activeQueriesLabel);
		
//		delayedQueriesLabel.setMaximumSize(labelSize);
//		delayedQueriesLabel.setPreferredSize(labelSize);
//		delayedQueriesLabel.setMinimumSize(labelSize);
		this.add(delayedQueriesLabel);
	}

	public void setNameLabelText(String name) {
		nameLabel.setText(name);
	}

	public void setActiveQueriesText(String activeQueries) {
		activeQueriesLabel.setText(ACTIVE_QUERY_CONST + activeQueries);
	}
	
	public void setDelayedQueriesText(String delayedQueries) {
		delayedQueriesLabel.setText(DELAYD_QUEY_CONST + delayedQueries);
	}

}
