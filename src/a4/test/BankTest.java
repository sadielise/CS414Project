package a4.test;

import static org.junit.Assert.*;

import org.junit.Test;
import a4.domain.*;

public class BankTest {

	@Test
	public void testGetBalance_Success() {
		int initialBalance = 500000;
		Bank bank = new Bank(initialBalance);
		int actual = bank.getBalance();
		assertTrue(actual == initialBalance);
	}

	@Test
	public void testGetBalance_ZeroBalance() {
		int initialBalance = 0;
		Bank bank = new Bank(initialBalance);
		int actual = bank.getBalance();
		assertTrue(actual == initialBalance);
	}

	@Test
	public void testSetBalance_Success() {
		int initialBalance = 500000;
		Bank bank = new Bank(initialBalance);
		int newBalance = 300000;
		bank.setBalance(newBalance);
		int actual = bank.getBalance();
		assertTrue(actual == newBalance);
	}

	@Test
	public void testSetBalance_SetToZero() {
		int initialBalance = 500000;
		Bank bank = new Bank(initialBalance);
		int newBalance = 0;
		bank.setBalance(newBalance);
		int actual = bank.getBalance();
		assertTrue(actual == newBalance);
	}

	@Test
	public void testRemoveBalance_Success() {
		int initialBalance = 500000;
		Bank bank = new Bank(initialBalance);
		int toRemove = 2500;
		int actual = bank.removeBalance(toRemove);
		assertTrue(actual == toRemove);
		assertTrue(bank.getBalance() == (initialBalance - toRemove));
	}

	@Test
	public void testRemoveBalance_RemoveZero() {
		int initialBalance = 500000;
		Bank bank = new Bank(initialBalance);
		int actual = bank.removeBalance(0);
		assertTrue(0 == actual);
		assertTrue(bank.getBalance() == initialBalance);
	}

	@Test
	public void testRemoveBalance_RemoveTooMuch() {
		int initialBalance = 500000;
		Bank bank = new Bank(initialBalance);
		int actual = bank.removeBalance(600000);
		assertTrue(actual == initialBalance);
		assertTrue(0 == bank.getBalance());
	}

	@Test
	public void testRemoveBalance_RemoveTooMuchCorner() {
		int initialBalance = 500000;
		Bank bank = new Bank(initialBalance);
		int actual = bank.removeBalance(initialBalance + 1);
		assertTrue(actual == initialBalance);
		assertTrue(0 == bank.getBalance());
	}

	@Test
	public void testRemoveBalance_SuccessCorner() {
		int initialBalance = 500000;
		Bank bank = new Bank(initialBalance);
		int actual = bank.removeBalance(initialBalance - 1);
		assertTrue(actual == (initialBalance - 1));
		assertTrue(1 == bank.getBalance());
	}

	@Test
	public void testRemoveBalance_BankEmpty() {
		int initialBalance = 0;
		Bank bank = new Bank(initialBalance);
		int actual = bank.removeBalance(500000);
		assertTrue(0 == actual);
		assertTrue(0 == bank.getBalance());
	}

	@Test
	public void testAddBalance_Success() {
		int initialBalance = 500000;
		Bank bank = new Bank(initialBalance);
		int actual = bank.addBalance(700);
		int expected = initialBalance + 700;
		assertTrue(actual == expected);
	}

	@Test
	public void testAddBalance_AddZero() {
		int initialBalance = 500000;
		Bank bank = new Bank(initialBalance);
		int actual = bank.addBalance(0);
		assertTrue(actual == initialBalance);
	}

	@Test
	public void testAddBalance_AddOne() {
		int initialBalance = 500000;
		Bank bank = new Bank(initialBalance);
		int actual = bank.addBalance(1);
		int expected = initialBalance + 1;
		assertTrue(actual == expected);
	}

}