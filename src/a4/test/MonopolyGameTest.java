package a4.test;

import static org.junit.Assert.*;

import java.awt.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import a4.domain.Bank;
import a4.domain.BoardSpace;
import a4.domain.GoToJailSpace;
import a4.domain.IncomeTaxSpace;
import a4.domain.JailSpace;
import a4.domain.LuxuryTaxSpace;
import a4.domain.MonopolyGame;
import a4.domain.Player;
import a4.domain.Property;
import a4.domain.PropertySpace;
import a4.gui.Model;

public class MonopolyGameTest {
	private MonopolyGame testGame;
	@Before
	public void doBeforeTests(){
		testGame = new MonopolyGame();
		ArrayList<String> names = new ArrayList<String>();
		names.add("Chancey");
		names.add("David");
		testGame.newGame(names, 30);
		Model model = new Model();
		testGame.setModel(model);
	}
	
	@Test
	public void testSetupGame(){
		ArrayList<String> names = new ArrayList<String>();
		names.add("Chancey");
		names.add("David");
		assertTrue(testGame.setupGame(names, 30));
		assertTrue(testGame.getCurrentPlayerReference().equals(testGame.getPlayerList().get(0)));
	}
	
	@Test
	public void testSetupGameNullListOfNames(){
		assertFalse(testGame.setupGame(null, 30));
	}
	
	@Test
	public void testSetupGameTooFewPlayers(){
		ArrayList<String> names = new ArrayList<String>();
		names.add("Chancey");
		assertFalse(testGame.setupGame(names, 30));
	}
	
	@Test
	public void testSetupGameTooManyPlayers(){
		ArrayList<String> names = new ArrayList<String>();
		names.add("Chancey");
		names.add("David");
		names.add("Jeff");
		names.add("Gabby");
		names.add("Saddie");
		assertFalse(testGame.setupGame(names, 30));
		
	}
	
	@Test
	public void testRoll() {
		int oldLocation = testGame.getCurrentPlayerReference().getLocation();
		testGame.roll();
		assertNotEquals(oldLocation, testGame.getCurrentPlayerReference().getLocation());
	}
	
	@Test 
	public void testPlayerMovedToUnownedProperty(){
		testGame.getCurrentPlayerReference().setLocation(1);
		testGame.playerMoved();
		assertTrue(testGame.getBoard().getSpaces().get(1) instanceof PropertySpace);
		assertTrue(true);
		assertNull(((PropertySpace)testGame.getBoard().getSpaces().get(1)).getProperty().getOwner());
	}
	
	@Test
	public void testPlayerMovedToOwnedPropertyAndCanPayRent(){
		Player tempPlayer = testGame.getPlayerList().get(testGame.getPlayerList().size()-1);
		assertNotNull(tempPlayer);
		Property tempProperty = ((PropertySpace)testGame.getBoard().getSpaces().get(1)).getProperty();
		tempProperty.setOwner(tempPlayer);
		testGame.getCurrentPlayerReference().setLocation(1);
		int currentPlayerBalance = testGame.getCurrentPlayerReference().getBalance();
		int ownerBalance = tempPlayer.getBalance();
		int rent = tempProperty.getRent();
		testGame.playerMoved();
		assertEquals(currentPlayerBalance - rent, testGame.getCurrentPlayerReference().getBalance());
		assertEquals(ownerBalance + rent, tempPlayer.getBalance());
	}
	
	@Test
	public void testPlayerMovedToOwnedPropertyAndCannotPayRent(){
		Player tempPlayer = testGame.getPlayerList().get(testGame.getPlayerList().size()-1);
		assertNotNull(tempPlayer);
		Property tempProperty = ((PropertySpace)testGame.getBoard().getSpaces().get(1)).getProperty();
		tempProperty.setOwner(tempPlayer);
		testGame.getCurrentPlayerReference().setLocation(1);
		testGame.getCurrentPlayerReference().setBalance(0);
		int ownerBalance = tempPlayer.getBalance();
		testGame.playerMoved();
		assertEquals(0, testGame.getCurrentPlayerReference().getBalance());
		assertEquals(ownerBalance, tempPlayer.getBalance());
	}
	
	@Test
	public void testPlayerMovedToGoToJail(){
		Player currentPlayer = testGame.getCurrentPlayerReference();
		int goToJailLoc = 0;
		JailSpace tempJail = null;
		for(BoardSpace space: testGame.getBoard().getSpaces()){
			if(space instanceof GoToJailSpace){
				goToJailLoc = space.getLocation();
			}
			if(space instanceof JailSpace){
				tempJail = (JailSpace)space;			}
		}
		assertNotEquals(0, goToJailLoc);
		assertNotNull(tempJail);
		currentPlayer.setLocation(goToJailLoc);
		testGame.playerMoved();
		assertTrue(currentPlayer.getInJail());
		assertEquals(currentPlayer.getLocation(), tempJail.getLocation());	
	}
	
	@Test
	public void testPlayerMovedToLuxuryTaxAndCanPay(){
		LuxuryTaxSpace luxuryTax = null;
		for(BoardSpace space: testGame.getBoard().getSpaces()){
			if(space instanceof LuxuryTaxSpace){
				luxuryTax = (LuxuryTaxSpace)space;
			}
		}
		assertNotNull(luxuryTax);
		Player currentPlayer = testGame.getCurrentPlayerReference();
		currentPlayer.setLocation(luxuryTax.getLocation());
		int playerBalance = currentPlayer.getBalance();
		int bankBalance = testGame.getBank().getBalance();
		testGame.playerMoved();
		assertEquals(playerBalance - 200, currentPlayer.getBalance());
		assertEquals(bankBalance + 200, testGame.getBank().getBalance());
	}
	
	@Test
	public void testPlayerMovedToLuxuryTaxAndCannotPay(){
		LuxuryTaxSpace luxuryTax = null;
		for(BoardSpace space: testGame.getBoard().getSpaces()){
			if(space instanceof LuxuryTaxSpace){
				luxuryTax = (LuxuryTaxSpace)space;
			}
		}
		assertNotNull(luxuryTax);
		Player currentPlayer = testGame.getCurrentPlayerReference();
		currentPlayer.setLocation(luxuryTax.getLocation());
		currentPlayer.setBalance(0);
		int bankBalance = testGame.getBank().getBalance();
		testGame.playerMoved();
		assertEquals(0, currentPlayer.getBalance());
		assertEquals(bankBalance, testGame.getBank().getBalance());
	}
	
	@Test
	public void testPlayerMovedToIncomeTaxAndCanPay(){
		IncomeTaxSpace incomeTax = null;
		for(BoardSpace space: testGame.getBoard().getSpaces()){
			if(space instanceof IncomeTaxSpace){
				incomeTax = (IncomeTaxSpace)space;
			}
		}
		assertNotNull(incomeTax);
		Player currentPlayer = testGame.getCurrentPlayerReference();
		currentPlayer.setLocation(incomeTax.getLocation());
		int playerBalance = currentPlayer.getBalance();
		int bankBalance = testGame.getBank().getBalance();
		testGame.playerMoved();
		assertEquals(playerBalance - 100, currentPlayer.getBalance());
		assertEquals(bankBalance + 100, testGame.getBank().getBalance());
	}
	
	@Test
	public void testPlayerMovedToIncomeTaxAndCannotPay(){
		IncomeTaxSpace incomeTax = null;
		for(BoardSpace space: testGame.getBoard().getSpaces()){
			if(space instanceof IncomeTaxSpace){
				incomeTax = (IncomeTaxSpace)space;
			}
		}
		assertNotNull(incomeTax);
		Player currentPlayer = testGame.getCurrentPlayerReference();
		currentPlayer.setLocation(incomeTax.getLocation());
		currentPlayer.setBalance(0);
		int bankBalance = testGame.getBank().getBalance();
		testGame.playerMoved();
		assertEquals(0, currentPlayer.getBalance());
		assertEquals(bankBalance, testGame.getBank().getBalance());
	}
	
	@Test
	public void testPlayerMovedToOpenSpace(){
		
	}
	
	@Test 
	public void testPlayerMovedToJailSpace(){
		
	}
	@Test
	public void testTransferMoneyPlayerToPlayer(){
		Player testPlayer1 = testGame.getPlayerList().get(0);
		Player testPlayer2 = testGame.getPlayerList().get(1);
		int balance1 = testPlayer1.getBalance();
		int balance2 = testPlayer2.getBalance();
		boolean success = testGame.transferMoney(testPlayer1, testPlayer2, balance1);
		assertTrue(success);
		assertEquals(0, testPlayer1.getBalance());
		assertEquals(balance2 + balance1, testPlayer2.getBalance());
	}
	
	@Test
	public void testTransferMoneyPlayerToPlayerOverdraft(){
		Player testPlayer1 = testGame.getPlayerList().get(0);
		Player testPlayer2 = testGame.getPlayerList().get(1);
		int balance1 = testPlayer1.getBalance();
		int balance2 = testPlayer2.getBalance();
		boolean success = testGame.transferMoney(testPlayer1, testPlayer2, balance1 +100);
		assertFalse(success);
		assertEquals(balance1, testPlayer1.getBalance());
		assertEquals(balance2 , testPlayer2.getBalance());
	}
	
	@Test
	public void testTransferMoneyPlayerToBank(){
		Player testPlayer = testGame.getCurrentPlayerReference();
		Bank testBank = testGame.getBank();
		int playerBalance = testPlayer.getBalance();
		int bankBalance = testBank.getBalance();
		boolean success = testGame.transferMoney(testPlayer, testBank, playerBalance);
		assertTrue(success);
		assertEquals(0, testPlayer.getBalance());
		assertEquals(playerBalance + bankBalance, testBank.getBalance());	
	}
	
	@Test
	public void testTransferMoneyPlayerToBankOverdraft(){
		Player testPlayer = testGame.getCurrentPlayerReference();
		Bank testBank = testGame.getBank();
		int playerBalance = testPlayer.getBalance();
		int bankBalance = testBank.getBalance();
		boolean success = testGame.transferMoney(testPlayer, testBank, playerBalance + 100);
		assertFalse(success);
		assertEquals(playerBalance, testPlayer.getBalance());
		assertEquals(bankBalance, testBank.getBalance());	
	}
	
	@Test
	public void testTranferMoneyBankToPlayer(){
		Player testPlayer = testGame.getCurrentPlayerReference();
		Bank testBank = testGame.getBank();
		int playerBalance = testPlayer.getBalance();
		int bankBalance = testBank.getBalance();
		boolean success = testGame.transferMoney(testBank, testPlayer, bankBalance);
		assertTrue(success);
		assertEquals(playerBalance + bankBalance, testPlayer.getBalance());
		assertEquals(0, testBank.getBalance());
	}
	
	@Test
	public void testTransferMoneyBankToPlayerOverdraft(){
		Player testPlayer = testGame.getCurrentPlayerReference();
		Bank testBank = testGame.getBank();
		int playerBalance = testPlayer.getBalance();
		int bankBalance = testBank.getBalance();
		boolean success = testGame.transferMoney(testBank, testPlayer, bankBalance + 100);
		assertFalse(success);
		assertEquals(playerBalance, testPlayer.getBalance());
		assertEquals(bankBalance, testBank.getBalance());
	}
	
	@Test 
	public void testGetHouseCount(){
		testGame.setHouseCount(10);
		assertEquals(10, testGame.getHouseCount());
	}
	
	@Test
	public void testSetHouseCount(){
		int oldCount = testGame.getHouseCount();
		int newCount = oldCount +2;
		testGame.setHouseCount(newCount);
		assertEquals(testGame.getHouseCount(), newCount);
	}
	
	@Test
	public void testGetCurrentPlayer(){
		String testString = testGame.getCurrentPlayerReference().toString();
		assertEquals(testString, testGame.getCurrentPlayer());
	}

	@Test
	public void testAddPlayer_NewPlayer(){
		Player player = new Player("Gabby", 123456, 0);
		assertTrue(testGame.addPlayer(player));
		//TODO: check number of players
	}
	
	@Test
	public void testAddPlayer_AddExistingPlayer(){
		Player player = new Player("Gabby", 123456, 0);
		assertTrue(testGame.addPlayer(player));
		assertFalse(testGame.addPlayer(player));
		//TODO: check number of players
	}
	
	@Test
	public void testPurchaseProperty_Success(){
		Player player = new Player("Gabby", 200, 0);
		int propertyValue = 100;
		Property property = new Property("Super cool property", propertyValue);
		assertTrue(testGame.purchaseProperty(player, property, propertyValue));
		assertTrue(100 == player.getBalance());
		assertTrue(player == property.getOwner());
		//TODO: check bank balance
		//TODO: check that player moved?
	}
	
	@Test
	public void testPurchaseProperty_PlayerDoesntHaveEnough(){
		Player player = new Player("Gabby", 200, 0);
		int propertyValue = 300;
		Property property = new Property("Super cool property", propertyValue);
		assertFalse(testGame.purchaseProperty(player, property, propertyValue));
		assertTrue(200 == player.getBalance());
		assertTrue(null == property.getOwner());
		//TODO: chack bank balance
		//TODO: check that player moved
	}
	
	//TODO: test bid!
	
	@Test
	public void testGetPlayers(){
		ArrayList<String> testList = (ArrayList<String>)testGame.getPlayers();
		int count = 0;
		for(Player curr: testGame.getPlayerList()){
			assertEquals(curr.toString(), testList.get(count));
			count++;
		}
		
	}
	
	@Test
	public void testDeterminePlayOrder(){
		ArrayList<Player> testList = new ArrayList<Player>();
		ArrayList<Player> shuffledTestList = (ArrayList<Player>)testGame.getPlayerList();
		for(Player curr: shuffledTestList){
			testList.add(curr);
		}
		int changes = 0;
		int counter = 0;
		while(changes == 0 && counter < 10){
			testGame.determinePlayOrder();
			for(int i=0; i<testList.size(); i++){
//				System.out.println(testList.get(i) + "\t" + shuffledTestList.get(i));
				if(!testList.get(i).equals(shuffledTestList.get(i))){
					changes++;
				}
			}
			counter++;
		}
		assertNotEquals(0, changes);
	}
	
	@Test
	public void testFindPlayer(){
		String testName = testGame.getCurrentPlayer();
		Player testPlayer = testGame.findPlayer(testName);
		assertEquals(testName, testPlayer.getName());
	}
	
	@Test
	public void testFindPlayerNotInGame(){
		String testName = "JIMBALKSJ";
		Player testPlayer = testGame.findPlayer(testName);
		assertNull(testPlayer);
	}
	
	@Test
	public void testRemovePlayer(){
		Player testPlayer = testGame.getCurrentPlayerReference();
		assertTrue(testGame.getPlayerList().contains(testPlayer));
		boolean success = testGame.removePlayer(testPlayer);
		assertTrue(success);
		assertFalse(testGame.getPlayerList().contains(testPlayer));
	}
	
	@Test
	public void testRemovePlayerNull(){
		Player testPlayer = null;
		assertFalse(testGame.getPlayerList().contains(testPlayer));
		boolean success = testGame.removePlayer(testPlayer);
		assertFalse(success);
	}
	
	@Test
	public void testRemovePlayerNotInGame(){
	Player testPlayer = new Player("asldkfj", 100, 0);
	assertFalse(testGame.getPlayerList().contains(testPlayer));
	boolean success = testGame.removePlayer(testPlayer);
	assertFalse(success);
	}

	@Test
	public void testBuyHouse(){ //finish when neighborhood updated
		Player testPlayer = testGame.getCurrentPlayerReference();
		testGame.transferMoney(testGame.getBank(), testPlayer, 10000);
		testPlayer.setLocation(1);
		BoardSpace space = testGame.getBoard().getSpaces().get(testPlayer.getLocation()); 
		Property property = ((PropertySpace)space).getProperty();
		assertTrue(testGame.purchaseProperty());
		testPlayer.setLocation(3);
		assertTrue(testGame.purchaseProperty());
//		assertEquals(1, testGame.buyHouse(property));		
	}
	
	@Test
	public void testGoToJail(){
		testGame.goToJail();
		JailSpace tempJail = null;
		for(BoardSpace space: testGame.getBoard().getSpaces()){
			if(space instanceof JailSpace){
				tempJail = (JailSpace)space;
			}
		}
		assertNotNull(tempJail);
		assertEquals(tempJail.getLocation(), testGame.getCurrentPlayerReference().getLocation());
		assertTrue(tempJail.isInPrison(testGame.getCurrentPlayerReference()));
	}
}
