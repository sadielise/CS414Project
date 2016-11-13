package a4.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import a4.gui.IModel;

public class MonopolyGame implements IMonopolyGame {
	private List<Player> players;
	private Board board;
	private List<Die> dice;
	private Bank bank;
	private List<Property> properties;
	private int houseCount;
	private int hotelCount;
	private Player currentPlayer;
	private IModel model;
	private int initialBankBalance = 20580;
	public Timer gameTime;
	private int numHouses = 32;
	private int numHotels = 5;
	private int initialPlayerBalance = 1500;
	private int numDiceSides = 6;
	private int minNumPlayers = 2;
	private int maxNumPlayers = 4;


	public List<Player> getPlayerList() {
		return players;
	}
	
	public Bank getBank() {
		return bank;
	}

	public Board getBoard() {
		return board;
	}

	public void setModel(IModel model) {
		this.model = model;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public int getHouseCount() {
		return houseCount;
	}

	@Override
	public String getCurrentPlayer() {
		return currentPlayer.toString();
	}

	public Player getCurrentPlayerReference() {
		return currentPlayer;
	}
	
	//return the player object associated with playerName
	public Player findPlayer(String playerName) {
		for (Player curr : players) {
			if (curr.toString().equals(playerName)) {
				return curr;
			}
		}
		return null;
	}

	//return the Property onject associated with propertyName
	public Property findProperty(String propertyName) {
		for (Property curr : properties) {
			if (curr.toString().equals(propertyName)) {
				return curr;
			}
		}
		return null;
	}

	//get the list of players as strings
	@Override
	public List<String> getPlayers() {
		List<String> playerNames = new ArrayList<String>();
		for (Player curr : players) {
			if (curr != null) {
				playerNames.add(curr.toString());
			}
		}
		return playerNames;
	}

	//get the Player bankroll associated with the string "player"
	@Override
	public int getBankroll(String player) {
		for (Player curr : players) {
			if (player.equals(curr.toString())) {
				return curr.getBalance();
			}
		}
		return -1;
	}

	//get the Player location associated with the string "player"
	@Override
	public int getLocation(String player) {
		Player temp = findPlayer(player);
		if (temp == null) {
			return -1;
		}
		return temp.getLocation();
	}

	//return the list of properties that a player owns as strings
	@Override
	public List<String> getProperties(String player) {
		List<String> propertyList = new ArrayList<String>();
		for (Property curr : properties) {
			if (curr.getOwner() != null) {
				if (curr.getOwner().toString().equals(player)) {
					propertyList.add(curr.toString());
				}
			}
		}
		return propertyList;
	}

	//return the property located at "location" as a string
	@Override
	public String getProperty(int location) {
		BoardSpace space = board.getSpaces().get(location);
		if (space.getType() == BoardSpaceType.PROPERTY) {
			PropertySpace temp = (PropertySpace) space;
			return temp.getProperty().toString();
		}
		return null;
	}

	//return the player's order number
	@Override
	public int getPlayerNumber(String player) {
		Player playerToFind = findPlayer(player);
		if (playerToFind == null) {
			return -1;
		}
		return players.indexOf(playerToFind);
	}

	//returns the number of houses for the property at "location"
	@Override
	public int getNumberHouses(int location) {
		int numHouses = 0;
		BoardSpace space = board.getSpaces().get(location);
		if (space.getType() == BoardSpaceType.PROPERTY) {
			Property p = ((PropertySpace) space).getProperty();
			if (p.getType() == PropertyType.STREET) {
				Street s = (Street) p;
				numHouses += s.getHouseCount();
				if (s.getHotelCount() > 0) {
					numHouses += 5;
				}
			}
		}
		return numHouses;
	}

	//create all of instances of the objects needed to play a monopoly game
	public boolean setupGame(List<String> names, int time) {
		if (names == null || names.size() < minNumPlayers || names.size() > maxNumPlayers) {
			return false;
		}

		players = new ArrayList<Player>();
		properties = new ArrayList<Property>();
		board = new Board();
		dice = new ArrayList<Die>();
		dice.add(new Die(numDiceSides));
		dice.add(new Die(numDiceSides));
		bank = new Bank(initialBankBalance);
		bank.setBalance(initialBankBalance);
		houseCount = numHouses;
		hotelCount = numHotels;

		for (String name : names) {
			Player newPlayer = new Player(name, 0, 0);
			bank.transferMoney(newPlayer, initialPlayerBalance);
			players.add(new Player(name, initialPlayerBalance, 0));
		}

		for (BoardSpace space : board.getSpaces()) {
			if (space.getType() == BoardSpaceType.PROPERTY) {
				properties.add(((PropertySpace) space).getProperty());
			}
		}

		determinePlayOrder();
		currentPlayer = players.get(0);
		startTimer(time);
		return true;
	}
	
	//clear the variables from an old game, setup a new game, nd start the timer
	@Override
	public void newGame(List<String> playerNames, int timeInMinutes) {
		players = null;
		board = null;
		dice = null;
		bank = null;
		properties = null;
		currentPlayer = null;
		BoardSpace.restartCounter();
		boolean success = setupGame(playerNames, timeInMinutes);
		if (success) {
			model.newGameCreated();
		} else {
			model.newGameFailedToCreate();
		}
	}
	
	//create and start the timer for the game
	public void startTimer(int timeInMinutes) {
		gameTime = new Timer();
		long timeInMilliseconds = timeInMinutes * 60000;
		gameTime.schedule(new TimerTask() {
			public void run() {
				endGame();
			}
		}, timeInMilliseconds);
	}

	//end a players Turn and start the turn of the next player
	@Override
	public void endTurn() {
		int currentPlayerNumber = players.indexOf(currentPlayer);
		int nextPlayerNumber = (currentPlayerNumber + 1) % players.size();
		currentPlayer = players.get(nextPlayerNumber);
		if (currentPlayer.getInJail()) {
			model.startJailTurn(currentPlayer.toString());
		} else {
			model.startNormalTurn(currentPlayer.toString());
		}
	}

	// returns the player that wins the game
	public void endGame() {
		Player winner = players.get(0);
		HashMap<Player, Integer> liquidatedFunds = new HashMap<Player, Integer>();
		for (Player p : players) {
			liquidatedFunds.put(p, p.getBalance());
		}
		for (Property p : properties) {
			if (p.getOwner() != null) {
				int housesValue = 0;
				int hotelValue = 0;
				if (p.getType() == PropertyType.STREET) {
					Street s = (Street) p;
					housesValue = s.getHouseCount() * s.getNeighborhood().getHouseValue();
					hotelValue = s.getHotelCount() * s.getNeighborhood().getHouseValue();
				}
				int propertyValue = p.getValue();
				int oldValue = liquidatedFunds.get(p.getOwner());
				liquidatedFunds.put(p.getOwner(), oldValue + housesValue + hotelValue + propertyValue);
			}
		}
		for (Player p : players) {
			if (liquidatedFunds.get(p) > liquidatedFunds.get(winner))
				winner = p;
		}
		model.endGame(winner.toString());
	}

	// returns true if the player is added
	// returns false if player has already been added
	public boolean addPlayer(Player player) {
		if (players.contains(player)) {
			return false;
		} else {
			players.add(player);
			return true;
		}
	}

	//remove a player from the game
	//remove player as owner from properties
	public boolean removePlayer(Player player) {
		if (players.contains(player)) {
			for (Property property : properties) {
				if (property.getOwner() != null) {
					if (property.getOwner().equals(player)) {
						property.setOwner(null);
					}
				}
			}
			players.remove(player);
			if (players.size() == 1) {
				endGame();
				return true;
			}
			return true;
		} else if (player == null) {
			return false;
		} else {
			return false;
		}
	}

	@Override
	public void roll() {
		roll(0);
	}

	//roll the die and keep track of the number of doubles rolled
	//send the player to jail if 3 sets of doubles rolled
	public void roll(int pastNumberOfDoubles) {
		int value1 = dice.get(0).roll();
		int value2 = dice.get(1).roll();
//		int value1 = 0;
//		int value2 = 1;
		boolean doubles = (value1 == value2);
		model.rolled(value1 + value2, doubles);
		if (doubles && pastNumberOfDoubles == 2) {
			board.getSpaces().get(currentPlayer.getLocation()).removePlayer(currentPlayer);
			JailSpace jail = (JailSpace) board.getSpaces().get(board.getJailLocation());
			jail.putPlayerInJail(currentPlayer);
			model.playerSentToJail(currentPlayer.toString());
		} else {
			board.getSpaces().get(currentPlayer.getLocation()).removePlayer(currentPlayer);
			if (currentPlayer.move(value1 + value2, board.getSpaces().size())) {
				bank.transferMoney(currentPlayer, 200);
			}
			board.getSpaces().get(currentPlayer.getLocation()).addPlayer(currentPlayer);
			playerMoved();
			if (doubles) {
				pastNumberOfDoubles++;
				roll(pastNumberOfDoubles);
			}
		}
	}

	//perform the action associated with the boardSpace that the player moved to
	public void playerMoved() {
		BoardSpace spaceOfPlayer = board.getSpaces().get(currentPlayer.getLocation());
		spaceOfPlayer.landedOnAction(model, currentPlayer, bank, dice);
		if (spaceOfPlayer.getType() == BoardSpaceType.GOTOJAIL) {
			board.getSpaces().get(currentPlayer.getLocation()).removePlayer(currentPlayer);
			JailSpace jail = (JailSpace) board.getSpaces().get(board.getJailLocation());
			jail.putPlayerInJail(currentPlayer);
		}
	}

	//randomize the list of players to determine the order of play
	public void determinePlayOrder() {
		Collections.shuffle(players);
	}

	/*
	 * returns true if player has enough money to buy property returns false if
	 * player cannot purchase property PreCondition: the current Player is on a
	 * property space public boolean purchaseProperty(){ int location =
	 * currentPlayer.getLocation(); BoardSpace space =
	 * board.getSpaces().get(location); Property property =
	 * ((PropertySpace)space).getProperty(); return
	 * purchaseProperty(currentPlayer, property, property.getValue());
	 */
	@Override
	public void purchaseProperty(String player, String property) {
		Player buyingPlayer = findPlayer(player);
		Property propertyToBuy = findProperty(property);
		if (buyingPlayer == null || propertyToBuy == null) {
			model.couldNotPurchaseProperty(player, property);
		} else {
			if (buyingPlayer.purchaseProperty(bank, propertyToBuy, propertyToBuy.getValue())) {
				model.purchasedProperty(player, property);
			} else {
				model.couldNotPurchaseProperty(player, property);
			}
		}
	}

	//attempts to unmortgage the property
	//sends result of unmortgage to model
	private int unmortgageProperty(Property propertyToUnmortgage) {
		int unmortgageCost = (int) (propertyToUnmortgage.getValue() * 1.1);
		if (!propertyToUnmortgage.getIsMortgaged()) {
			return -1;
		} else {
			if (!propertyToUnmortgage.getOwner().transferMoney(bank, unmortgageCost)) {
				return -1;
			}
			propertyToUnmortgage.setIsMortgaged(false);
			return unmortgageCost;
		}
	}

	//trade the properties associated with the strings "currProperty" and "otherProperty"
	@Override
	public void trade(String currProperty, String otherProperty) {
		Property property1 = findProperty(currProperty);
		Property property2 = findProperty(otherProperty);
		if (property1 == null || property2 == null) {
			model.tradeFailed(currProperty, otherProperty);
		} else {
			property1.tradeProperty(property2);
			model.tradeSucceeded(currProperty, otherProperty);
		}
	}
	
	//purchase a property after an auction
	//sends result of purchase to model
	@Override
	public void purchaseAuctionedProperty(List<Integer> offers) {
		Property propertyToAuction = null;
		BoardSpace space = board.getSpaces().get(currentPlayer.getLocation());
		if (space.getType() == BoardSpaceType.PROPERTY) {
			propertyToAuction = ((PropertySpace) space).getProperty();
			int[] bids = new int[offers.size()];
			for (int i = 0; i < offers.size(); i++) {
				bids[i] = offers.get(i).intValue();
			}
			if (bid(bids, propertyToAuction)) {
				model.purchasedProperty(propertyToAuction.getOwner().toString(), propertyToAuction.toString());
			} else {
				model.auctionFailed(propertyToAuction.toString());
			}
		} else {
			model.auctionFailed(propertyToAuction.toString());
		}
	}

	//find the highest bid
	//sell property to player who bid the highest
	public boolean bid(int[] bids, Property property) {
		int highestBid = 0;
		int winningPlayer = 0;
		for (int i = 0; i < bids.length; i++) {
			if (bids[i] > highestBid) {
				highestBid = bids[i];
				winningPlayer = i;
			} else if (bids[i] == highestBid) {
				int rnd = (int) (Math.random() * 2) + 1;
				if (rnd == 2) {
					winningPlayer = i;
				}
			}
		}
		return players.get(winningPlayer).purchaseProperty(bank, property, highestBid);
	}

	//purchase a house for a street if it is legal
	public int buyHouse(Street street) {
		if (houseCount != 0 && street.getIsMortgaged() == false) {
			boolean houseBought = street.getNeighborhood().addHouse(street);
			if (houseBought) {
				if (street.getHotelCount() == 1) {
					hotelCount--;
					houseCount += 4;
				}
				street.getOwner().transferMoney(bank, street.getNeighborhood().getHouseValue());
				return street.getHouseCount();
			}
		}
		return -1;
	}



	//develop a property (unmortgage, buy house) if legal
	//sends result of development to model
	@Override
	public void developProperty(String property) {
		Property currentProperty = findProperty(property);
		if (currentProperty == null) {
			System.err.println("Error: null property : " + property);
		} else if (currentProperty.getOwner() == null) {
			model.propertyCannotBeDeveloped(property);
		} else if (currentProperty.getIsMortgaged()) {
			int value = unmortgageProperty(currentProperty);
			if (value != -1) {
				model.propertyWasUnmortgagedFor(property, value);
			} else {
				model.propertyCannotBeDeveloped(property);
			}
		} else if (currentProperty.getType() == PropertyType.STREET) {
			Street currentStreet = (Street) currentProperty;
			int success = buyHouse(currentStreet);
			if (success == -1) {
				model.propertyCannotBeDeveloped(property);
			} else {
				model.propertyWasDeveloped(currentStreet.toString(), currentStreet.getHouseCount());
			}
		} else {
			model.propertyCannotBeDeveloped(property);
		}
	}

	//undevelop a property (mortgage, sell houses) if legal, when a player needs more money
	//sends result of undevelop to model
	@Override
	public void undevelop(String property, String playerOwed, int amountOwed) {
		Property currentProperty = findProperty(property);
		if (currentProperty == null) {
			model.couldNotUndevelopProperty(property);
		}
		else {
			int propertyHotelCount = -1;
			if(currentProperty.getType() == PropertyType.STREET){
				propertyHotelCount = ((Street)currentProperty).getHotelCount();
			}
			int undevelopingValue = currentProperty.undevelop(bank);
			if(undevelopingValue != -1){
				if(currentProperty.getType() == PropertyType.STREET){
					if(propertyHotelCount != ((Street)currentProperty).getHotelCount()){
						hotelCount++;
						houseCount -= 4;
					}
				}
				model.propertyWasUnDevelopedFor(property, undevelopingValue);
			}
			else{
				model.couldNotUndevelopProperty(property);
			}
			if (currentPlayer.getBalance() < amountOwed) {
				amountOwed -= currentPlayer.getBalance();
				currentPlayer.transferMoney(bank, currentPlayer.getBalance());
				model.unableToPay(playerOwed, amountOwed);
			} else {
				currentPlayer.transferMoney(bank, amountOwed);
			}
		}
	}

	//get the list of properties that "player" can develop
	public List<String> getDevelopableProperties(String player) {
		List<String> propertyList = new ArrayList<String>();
		for (Property curr : properties) {
			if (curr.getOwner() != null) {
				if (curr.getOwner().toString().equals(player)) {
					if (curr.getIsMortgaged()) {
						propertyList.add(curr.toString());
					} else if (curr.getType() == PropertyType.STREET && ((Street) curr).getHotelCount() < 1) {
						if (((Street) curr).getNeighborhood().hasOwner()) {
							if (((Street) curr).getNeighborhood().getOwner().toString().equals(player)) {
								propertyList.add(curr.toString());
							}
						}
					}
				}
			}
		}
		return propertyList;
	}

	//get the list of properties that "player" can undevelop
	public List<String> getUndevelopableProperties(String player) {
		List<String> propertyList = new ArrayList<String>();
		for (Property curr : properties) {
			if (curr.getOwner() != null) {
				if (curr.getOwner().toString().equals(player)) {
					if (!curr.getIsMortgaged()) {
						propertyList.add(curr.toString());
					}
				}
			}
		}
		return propertyList;
	}

	//player attempts to toll doubles to get out of jail
	//player pays the jail fine if they do not have any more chances to roll doubles
	public boolean rollToGetOutOfJail(Player player) {
		JailSpace jail = (JailSpace) board.getSpaces().get(10);
		if (player.getInJail() == false) {
			return false;
		} else if (jail.getAttempts(player) > 2) {
			return false;
		} else {
			int value1 = dice.get(0).roll();
			int value2 = dice.get(1).roll();
			boolean doubles = (value1 == value2);
			model.rolled(value1 + value2, doubles);
			if (doubles) {
				model.succeededInLeavingJail();
				board.getSpaces().get(player.getLocation()).removePlayer(player);
				currentPlayer.move(value1 + value2, board.getSpaces().size());
				board.getSpaces().get(player.getLocation()).addPlayer(player);
				jail.removePlayer(player);
				jail.getOutOfJail(player);
				playerMoved();
				return true;
			} else {
				jail.incrementAttempts(player);
				if (jail.getAttempts(player) > 2) {
					if (payFineToLeaveJail(player)) {
						model.succeededInLeavingJail();
						board.getSpaces().get(player.getLocation()).removePlayer(player);
						currentPlayer.move(value1 + value2, board.getSpaces().size());
						board.getSpaces().get(player.getLocation()).addPlayer(player);
						playerMoved();
						return true;
					}
				}
				return false;
			}
		}
	}

	//player payer the jail fine to get out of jail
	@Override
	public void payJailFine(String player, boolean isPayingFine) {
		Player playerPayingFine = findPlayer(player);
		if (isPayingFine == true) {
			if (payFineToLeaveJail(playerPayingFine)) {
				model.succeededInLeavingJail();
				roll();
			} else {
				model.failedToLeaveJail();
			}
		} else {
			if (rollToGetOutOfJail(playerPayingFine) == false) {
				model.failedToLeaveJail();
			}
		}
	}

	//jail fine is transfered from player to the bank
	//sends result of payment to model
	public boolean payFineToLeaveJail(Player player) {
		JailSpace jail = (JailSpace) board.getSpaces().get(10);
		if (player.getInJail() == false) {
			return false;
		} else if (player.transferMoney(bank, 50) == false) {
			model.unableToPayFine(50);
			return false;
		} else {
			jail.getOutOfJail(player);
			model.paidRentTo("Jail", 50);
			return true;
		}
	}
}
