package banking;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankTest {

	private static final String CHECKING_ID = "12345678";
	private static final String SAVINGS_ID = "23456789";
	private static final String CD_ID = "34567890";
	private static final double APR = 1.0;
	private Bank bank;
	private CheckingAccount checking;
	private SavingsAccount savings;
	private CDAccount cd;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		checking = new CheckingAccount(CHECKING_ID, APR);
		savings = new SavingsAccount(SAVINGS_ID, APR);
		cd = new CDAccount(CD_ID, APR, 0.0);
	}

	@Test
	public void initial_bank_account_count() {
		assertEquals(0, bank.getAccountAmount());
	}

	@Test
	public void adding_account_to_bank() {
		bank.addAccount(checking);
		assertEquals(1, bank.getAccountAmount());
	}

	@Test
	public void adding_multiple_accounts_to_bank() {
		bank.addAccount(checking);
		bank.addAccount(savings);
		assertEquals(2, bank.getAccountAmount());
	}

	@Test
	public void retrieve_account_from_bank() {
		bank.addAccount(cd);
		assertEquals(cd, bank.getAccount(CD_ID));
	}

	@Test
	public void deposit_by_id_from_bank() {
		bank.addAccount(checking);
		bank.deposit(CHECKING_ID, 300.0);
		assertEquals(300.0, bank.getAccount(CHECKING_ID).getBalance(), 0.01);
	}

	@Test
	public void withdraw_by_id_from_bank() {
		bank.addAccount(checking);
		bank.deposit(CHECKING_ID, 300.0);
		bank.withdraw(CHECKING_ID, 150.0);
		assertEquals(150.0, bank.getAccount(CHECKING_ID).getBalance(), 0.01);
	}

	@Test
	public void deposit_twice_using_id_from_bank() {
		bank.addAccount(checking);
		bank.deposit(CHECKING_ID, 300.0);
		bank.deposit(CHECKING_ID, 300.0);
		assertEquals(600.0, bank.getAccount(CHECKING_ID).getBalance(), 0.01);
	}

	@Test
	public void withdraw_twice_using_id_from_bank() {
		bank.addAccount(checking);
		bank.deposit(CHECKING_ID, 300.0);
		bank.withdraw(CHECKING_ID, 150.0);
		bank.withdraw(CHECKING_ID, 150.0);
		assertEquals(0.0, bank.getAccount(CHECKING_ID).getBalance(), 0.01);
	}

	@Test
	public void deposit_multiple_times_using_id_from_bank() {
		bank.addAccount(checking);
		bank.deposit(CHECKING_ID, 300.0);
		bank.deposit(CHECKING_ID, 300.0);
		bank.deposit(CHECKING_ID, 300.0);
		assertEquals(900.0, bank.getAccount(CHECKING_ID).getBalance(), 0.01);
	}

	@Test
	public void withdraw_multiple_times_using_id_from_bank() {
		bank.addAccount(checking);
		bank.deposit(CHECKING_ID, 300.0);
		bank.withdraw(CHECKING_ID, 100.0);
		bank.withdraw(CHECKING_ID, 100.0);
		bank.withdraw(CHECKING_ID, 100.0);
		assertEquals(0.0, bank.getAccount(CHECKING_ID).getBalance(), 0.01);
	}

	@Test
	void test_record_transaction() {
		bank.recordTransaction("deposit 12345678 100");
		bank.recordTransaction("withdraw 12345678 50");

		List<String> transactions = bank.getAccountTransactions().get("12345678");
		assertEquals(2, transactions.size());
		assertTrue(transactions.contains("deposit 12345678 100"));
		assertTrue(transactions.contains("withdraw 12345678 50"));
	}

	@Test
	void test_has_account() {
		bank.addAccount(savings);
		assertTrue(bank.hasAccount("23456789"));
		assertFalse(bank.hasAccount("87654321"));
	}

	@Test
	void test_get_account() {
		bank.addAccount(savings);
		assertNotNull(bank.getAccount("23456789"));
		assertNull(bank.getAccount("87654321"));
	}

	@Test
	void test_deposit() {
		bank.addAccount(savings);
		bank.deposit("23456789", 200.0);
		assertEquals(200.0, savings.getBalance(), 0.01);
	}

	@Test
	void test_withdraw() {
		bank.addAccount(savings);
		savings.deposit(500.0);
		double withdrawn = bank.withdraw("23456789", 300.0);
		assertEquals(300.0, withdrawn, 0.01);
		assertEquals(200.0, savings.getBalance(), 0.01);
	}

	@Test
	void test_increment_account_months() {
		bank.addAccount(savings);
		bank.incrementAccountMonths("23456789");
		assertEquals(1, bank.getAccountMonths().get("23456789"));
		bank.incrementAccountMonths("23456789");
		assertEquals(2, bank.getAccountMonths().get("23456789"));
	}

	@Test
	void test_pass_time() {
		bank.addAccount(cd);
		cd.deposit(2000.0);
		bank.passTime(12);

		assertEquals(12, bank.getAccountMonths().get("34567890"));
		assertEquals(2081.5868742433304, cd.getBalance(), 0.01);
	}

	@Test
	void test_close_account_if_balance_zero() {
		bank.addAccount(savings);
		savings.deposit(200.0);
		bank.withdraw("23456789", 200.0);

		boolean closed = bank.closeAccountIfBalanceZero(savings);
		assertTrue(closed);
		assertFalse(bank.hasAccount("23456789"));
	}

	@Test
	void test_balance_decay() {
		bank.addAccount(savings);
		savings.deposit(90.0);
		bank.balanceDecay(savings);
		assertEquals(65.0, savings.getBalance(), 0.01);
	}

	@Test
	void test_update_balance() {
		bank.addAccount(savings);
		savings.deposit(1000.0);
		bank.updateBalance(savings);

		assertEquals(1000.8333333333334, savings.getBalance(), 0.01);
	}

}