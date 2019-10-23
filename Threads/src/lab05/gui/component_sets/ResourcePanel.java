package lab05.gui.component_sets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// resource panel
public class ResourcePanel extends JPanel {

	private static final long serialVersionUID = 23333082617008817L;
	
	private static final String NAME_CONST = "Name: ";
	private static final String LEVEL_CONST = "Level: ";
	private static final String STATE_CONST = "State: ";
	private static final String USER_CONST = "User: ";
	
	private final JLabel nameLabel;
	private final JLabel levelLabel;
	private final JLabel stateLabel;
	private final JLabel currentUserLabel;
	
	public ResourcePanel() {
		this.nameLabel = new JLabel(NAME_CONST + "-", SwingConstants.CENTER);
		this.levelLabel = new JLabel(LEVEL_CONST + "0", SwingConstants.CENTER);
		this.stateLabel = new JLabel(STATE_CONST + "idle", SwingConstants.CENTER);
		this.currentUserLabel = new JLabel(USER_CONST + "-", SwingConstants.CENTER);
		
		this.setLayout(new GridLayout(4, 1));
		this.setBackground(Color.decode("#f46542"));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		prepareComponenets();
	}

	private void prepareComponenets() {
//		nameLabel.setMaximumSize(labelSize);
//		nameLabel.setPreferredSize(labelSize);
//		nameLabel.setMinimumSize(labelSize);
		this.add(nameLabel);
		
//		levelLabel.setMaximumSize(labelSize);
//		levelLabel.setPreferredSize(labelSize);
//		levelLabel.setMinimumSize(labelSize);
		this.add(levelLabel);
		
//		currentUserLabel.setMaximumSize(labelSize);
//		currentUserLabel.setPreferredSize(labelSize);
//		currentUserLabel.setMinimumSize(labelSize);
		this.add(currentUserLabel);	
		
//		stateLabel.setMaximumSize(labelSize);
//		stateLabel.setPreferredSize(labelSize);
//		stateLabel.setMinimumSize(labelSize);
		this.add(stateLabel);
	}

	public void setNameLabelText(String name) {
		nameLabel.setText(NAME_CONST + name);
	}
	
	public void setLevelLabelText(String level) {
		levelLabel.setText(LEVEL_CONST + level);
	}
	
	public void setStateLabelText(String stateDescription) {
		stateLabel.setText(STATE_CONST + stateDescription);
	}

	public void setCurrentUser(String user) {
		currentUserLabel.setText(USER_CONST + user);
	}
	
}
