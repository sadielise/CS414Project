package a4.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Controller {
	private Model model;
	private View view;

	public void setModel(Model m){
		model = m;
	}
	public void setView(View v){
		view = v;
	}

	public JButton getRollButton(){
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(model.isStarted){
					model.roll();
				}
			}

		});

		return button;
	}

	public JButton getDevelopButton(){
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(model.isStarted){
					DevelopDialog.createAndShowDevelopDialog(model, false);
				}
			}
		});

		return button;
	}

	public JButton getNewGameButton(){
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{

				NewGameDialog.createAndDisplayNewGameDialog(model);
			}
		});

		return button;
	}

	public JButton getTradeButton(){
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(model.isStarted){
					TradeDialog.createAndShowTradeDialog(model);
				}
			}
		});

		return button;
	}

	public JButton getEndTurnButton(){
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(model.isStarted){
					if(!model.hasRolled){
						model.roll();
					}
					model.endTurn();
				}
			}
		});

		return button;
	}

	public void createLandedOnUnownedDialog(String property, int cost){
		int choice = JOptionPane.showConfirmDialog(view, "Would you like to purchase " + property+ " for $"+cost+"?", "Purchase dialog", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION){
			model.purchaseProperty(model.getPlayer(), property);
		}
		else{
			createAuctionDialog(property);
		}
	}
	
	public void createAuctionDialog(String property){
		
	}
	
	public void createUnableToPayDialog(String player, int rentDue) {
		JOptionPane.showMessageDialog(view, "You were unable to pay, and must undevelop!");
		DevelopDialog.createAndShowDevelopDialog(model, true);	
	}
	public void createPaidRentDialog(String playerName, int rentAmount) {
		JOptionPane.showMessageDialog(view, "You paid $" + rentAmount +" to " + playerName + " for landing on owned property");

	}
	public void createSentToJailDialog(String playerName) {
		JOptionPane.showMessageDialog(view, "You were sent to jail!");
	}
	public void createPropertyCannotBeDevelopedDialog(String propertyName) {
		JOptionPane.showMessageDialog(view, propertyName +" cannot be developed!");
	}
}
