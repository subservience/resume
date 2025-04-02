package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandProcessorTest {

	private Bank bank;
	private CommandProcessor processor;
	private CommandValidator validator;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		processor = new CommandProcessor(bank);
		validator = new CommandValidator(bank);
	}

	@Test
	void test_create_checking_account() {
		processor.process("create checking 12345678 1.0");

		Account account = bank.getAccount("12345678");
		assertEquals("12345678", account.getId());
		assertEquals(1.0, account.getApr(), 0.001);
		assertEquals(0.0, account.getBalance(), 0.001);
	}

	@Test
	void test_create_savings_account() {
		processor.process("create savings 12345678 1.0");

		Account account = bank.getAccount("12345678");
		assertEquals("12345678", account.getId());
		assertEquals(1.0, account.getApr(), 0.01);
		assertEquals(0.0, account.getBalance(), 0.01);
	}

	@Test
	void test_create_cd_account() {
		processor.process("create cd 12345678 1.0 1000.0");

		Account account = bank.getAccount("12345678");
		assertEquals("12345678", account.getId());
		assertEquals(1.0, account.getApr(), 0.01);
		assertEquals(1000.0, account.getBalance(), 0.01);
	}

	@Test
	void test_deposit_to_account() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 200");

		Account account = bank.getAccount("12345678");
		assertEquals(200.0, account.getBalance(), 0.01);
	}

	@Test
	void test_deposit_to_account_twice() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 200");
		Account account = bank.getAccount("12345678");
		processor.process("deposit 12345678 100");
		assertEquals(300.0, account.getBalance(), 0.01);
	}

	@Test
	void test_invalid_deposit_zero() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 0");
		Account account = bank.getAccount("12345678");
		assertEquals(0.0, account.getBalance(), 0.01);
	}

	@Test
	void test_invalid_account_number_length() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 1234567 100");
		processor.process("deposit 123456789 0");
		Account account = bank.getAccount("12345678");
		assertEquals(0.0, account.getBalance(), 0.01);
	}

	@Test
	void test_create_account_and_deposit_multiple_times() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 100");
		processor.process("deposit 12345678 200");
		processor.process("deposit 12345678 150");

		Account account = bank.getAccount("12345678");
		assertEquals(450.0, account.getBalance(), 0.01);
	}

	@Test
	void test_create_multiple_accounts_deposit() {
		processor.process("create checking 11112222 1.0");
		processor.process("create savings 33334444 2.5");

		processor.process("deposit 11112222 300");
		processor.process("deposit 33334444 400");

		Account checkingAccount = bank.getAccount("11112222");
		Account savingsAccount = bank.getAccount("33334444");

		assertEquals(300.0, checkingAccount.getBalance(), 0.01);
		assertEquals(400.0, savingsAccount.getBalance(), 0.01);
	}

	@Test
	void test_withdraw_from_account() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 500.0");
		processor.process("withdraw 12345678 200.0");

		Account account = bank.getAccount("12345678");
		assertEquals(300.0, account.getBalance(), 0.01);
	}

	@Test
	void test_withdraw_insufficient_balance() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 100.0");
		processor.process("withdraw 12345678 200.0");

		Account account = bank.getAccount("12345678");
		assertEquals(0.0, account.getBalance(), 0.01);
	}

	@Test
	void test_withdraw_invalid_account() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 500.0");
		processor.process("withdraw 87654321 100.0");

		Account account = bank.getAccount("12345678");
		assertEquals(500.0, account.getBalance(), 0.01);
	}

	@Test
	void test_withdraw_negative_amount() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 500.0");
		processor.process("withdraw 12345678 -50.0");

		Account account = bank.getAccount("12345678");
		assertEquals(500.0, account.getBalance(), 0.01);
	}

	@Test
	void test_withdraw_zero_amount() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 500.0");
		processor.process("withdraw 12345678 0.0");

		Account account = bank.getAccount("12345678");
		assertEquals(500.0, account.getBalance(), 0.01);
	}

	@Test
	void test_transfer_between_accounts() {
		processor.process("create checking 12345678 1.0");
		processor.process("create savings 87654321 1.0");
		processor.process("deposit 12345678 500.0");

		processor.process("transfer 12345678 87654321 300.0");

		Account fromAccount = bank.getAccount("12345678");
		Account toAccount = bank.getAccount("87654321");

		assertEquals(200.0, fromAccount.getBalance(), 0.01);
		assertEquals(300.0, toAccount.getBalance(), 0.01);
	}

	@Test
	void test_transfer_exceeding_balance() {
		processor.process("create checking 12345678 1.0");
		processor.process("create savings 87654321 1.0");
		processor.process("deposit 12345678 200.0");

		processor.process("transfer 12345678 87654321 500.0");

		Account fromAccount = bank.getAccount("12345678");
		Account toAccount = bank.getAccount("87654321");

		assertEquals(0.0, fromAccount.getBalance(), 0.01);
		assertEquals(200.0, toAccount.getBalance(), 0.01);
	}

	@Test
	void test_transfer_exact_balance() {
		processor.process("create checking 12345678 1.0");
		processor.process("create savings 87654321 1.0");
		processor.process("deposit 12345678 200.0");

		processor.process("transfer 12345678 87654321 200.0");

		Account fromAccount = bank.getAccount("12345678");
		Account toAccount = bank.getAccount("87654321");

		assertEquals(0.0, fromAccount.getBalance(), 0.01);
		assertEquals(200.0, toAccount.getBalance(), 0.01);
	}

	@Test
	void test_deposit_savings_within_limit() {
		processor.process("create savings 12345678 1.0");
		processor.process("deposit 12345678 2000.0");

		Account account = bank.getAccount("12345678");
		assertEquals(2000.0, account.getBalance(), 0.01);
	}

	@Test
	void test_deposit_savings_exceeds_limit() {
		processor.process("create savings 12345678 1.0");
		processor.process("deposit 12345678 3000.0");

		Account account = bank.getAccount("12345678");
		assertEquals(0.0, account.getBalance(), 0.01);
	}

	@Test
	void test_deposit_checking_within_limit() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 500.0");

		Account account = bank.getAccount("12345678");
		assertEquals(500.0, account.getBalance(), 0.01);
	}

	@Test
	void test_deposit_checking_exceeds_limit() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 1500.0");

		Account account = bank.getAccount("12345678");
		assertEquals(0.0, account.getBalance(), 0.01);
	}

	@Test
	void test_deposit_cd_account() {
		processor.process("create cd 12345678 1.0 2000.0");
		processor.process("deposit 12345678 1000.0");

		Account account = bank.getAccount("12345678");
		assertEquals(2000.0, account.getBalance(), 0.01);
	}

	@Test
	void test_deposit_negative_amount() {
		processor.process("create savings 12345678 1.0");
		processor.process("deposit 12345678 -50.0");

		Account account = bank.getAccount("12345678");
		assertEquals(0.0, account.getBalance(), 0.01);
	}

	@Test
	void test_deposit_checking_at_limit() {
		processor.process("create checking 12345678 1.0");
		processor.process("deposit 12345678 1000.0");

		Account account = bank.getAccount("12345678");
		assertEquals(1000.0, account.getBalance(), 0.01);
	}

	@Test
	void test_deposit_savings_at_limit() {
		processor.process("create savings 12345678 1.0");
		processor.process("deposit 12345678 2500.0");

		Account account = bank.getAccount("12345678");
		assertEquals(2500.0, account.getBalance(), 0.01);
	}

	@Test
	void test_deposit_zero_amount() {
		processor.process("create savings 12345678 1.0");
		processor.process("deposit 12345678 0.0");

		Account account = bank.getAccount("12345678");
		assertEquals(0.0, account.getBalance(), 0.01);
	}

	@Test
	void test_pass_time_applies_apr_for_savings_account() {
		processor.process("create savings 12345678 3.0");
		processor.process("deposit 12345678 1000.0");

		processor.process("pass 1");

		Account account = bank.getAccount("12345678");
		double expectedBalance = bank.updateBalance(account);
		assertEquals(expectedBalance, account.getBalance(), 0.01);
	}

	@Test
	void test_pass_time_applies_apr_for_savings_account_multiple() {
		processor.process("create savings 12345678 3.0");
		processor.process("deposit 12345678 1000.0");
		processor.process("create checking 23456789 3.0");
		processor.process("deposit 12345678 1000.0");

		processor.process("pass 1");

		Account account = bank.getAccount("12345678");
		double expectedBalance = bank.updateBalance(account);
		assertEquals(expectedBalance, account.getBalance(), 0.01);
	}

	@Test
	void test_pass_time_removes_account_with_zero_balance() {
		processor.process("create checking 12345678 1.0");
		processor.process("pass 1");

		assertFalse(bank.hasAccount("12345678"));
	}

	@Test
	void test_pass_time_removes_account_with_zero_balance_multiple() {
		processor.process("create checking 12345678 1.0");
		processor.process("create savings 23456789 1.0");
		processor.process("pass 1");

		assertFalse(bank.hasAccount("12345678"));
		assertFalse(bank.hasAccount("23456789"));
	}

	@Test
	void test_pass_time_deducts_minimum_balance_fee() {
		processor.process("create savings 12345678 2.0");
		processor.process("deposit 12345678 50.0");

		processor.process("pass 1");

		Account account = bank.getAccount("12345678");
		assertEquals(25.041666666666668, account.getBalance());
	}

	@Test
	void test_pass_time_applies_apr_for_cd_account() {
		processor.process("create cd 12345678 2.0 2000.0");
		processor.process("pass 1");

		Account account = bank.getAccount("12345678");
		double expectedBalance = bank.updateBalance(account);
		assertEquals(expectedBalance, account.getBalance(), 0.01);
	}

	@Test
	void test_pass_time_applies_apr_for_cd_account_multiple() {
		processor.process("create cd 12345678 2.0 2000.0");
		processor.process("pass 3");

		Account account = bank.getAccount("12345678");
		double expectedBalance = bank.updateBalance(account);
		assertEquals(expectedBalance, account.getBalance(), 0.01);
	}

	@Test
	void test_pass_time_applies_apr_for_cd_account_multiple_multiple() {
		processor.process("create cd 12345678 2.0 2000.0");
		processor.process("pass 5");

		Account account = bank.getAccount("12345678");
		double expectedBalance = bank.updateBalance(account);
		assertEquals(expectedBalance, account.getBalance(), 0.01);
	}

	@Test
	void test_pass_time_multiple_account_types() {
		processor.process("create cd 12345678 3.0 1000.0");
		processor.process("create savings 87654321 4.0");
		processor.process("deposit 87654321 500.0");
		processor.process("pass 2");

		Account cdAccount = bank.getAccount("12345678");

		Account savingsAccount = bank.getAccount("87654321");
		double expectedCDBalance = bank.updateBalance(cdAccount);
		double expectedSavingsBalance = bank.updateBalance(savingsAccount);

		assertEquals(expectedCDBalance, cdAccount.getBalance(), 0.01);
		assertEquals(expectedSavingsBalance, savingsAccount.getBalance(), 0.01);
	}

	@Test
	void test_transfer_negative_amount() {
		processor.process("create savings 12345678 1.0");
		processor.process("create checking 87654321 1.0");
		processor.process("transfer 12345678 87654321 -50");

		assertFalse(validator.isValid("transfer 12345678 87654321 -50"));
	}

}