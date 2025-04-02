package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {

	private static final String ID = "12345678";
	private static final double APR = 1.0;
	private CheckingAccount checking;
	private SavingsAccount savings;
	private CDAccount cd;

	@BeforeEach
	public void setUp() {
		checking = new CheckingAccount(ID, APR);
		savings = new SavingsAccount(ID, APR);
		cd = new CDAccount(ID, APR, 0.0);
	}

	@Test
	void checking_and_savings_account_initial_at_zero() {
		assertEquals(0.0, checking.getBalance(), 0.01);
		assertEquals(0.0, savings.getBalance(), 0.01);
	}

	@Test
	void cd_account_initial_equals_amount() {
		cd.deposit(100.0);
		assertEquals(100.0, cd.getBalance(), 0.01);
	}

	@Test
	void account_apr() {
		assertEquals(1.0, checking.getApr(), 0.01);
		assertEquals(1.0, savings.getApr(), 0.01);
		assertEquals(1.0, cd.getApr(), 0.01);
	}

	@Test
	void deposit_into_checking_account() {
		checking.deposit(100.0);
		savings.deposit(100.0);
		cd.deposit(100.0);

		assertEquals(100.0, checking.getBalance(), 0.01);
		assertEquals(100.0, savings.getBalance(), 0.01);
		assertEquals(100.0, cd.getBalance(), 0.01);
	}

	@Test
	void withdraw_from_accounts() {
		checking.deposit(100.0);
		savings.deposit(100.0);
		cd.deposit(100.0);
		checking.withdraw(50.0);
		savings.withdraw(50.0);
		cd.withdraw(50.0);

		assertEquals(50.0, checking.getBalance(), 0.01);
		assertEquals(50.0, savings.getBalance(), 0.01);
		assertEquals(50.0, cd.getBalance(), 0.01);
	}

	@Test
	void withdraw_from_accounts_equal_zero() {
		checking.deposit(100.0);
		savings.deposit(100.0);
		cd.deposit(100.0);
		checking.withdraw(150.0);
		savings.withdraw(150.0);
		cd.withdraw(150.0);

		assertEquals(0.0, checking.getBalance(), 0.01);
		assertEquals(0.0, savings.getBalance(), 0.01);
		assertEquals(0.0, cd.getBalance(), 0.01);
	}

	@Test
	void withdraw_from_accounts_equal_exactly_zero() {
		checking.deposit(100.0);
		savings.deposit(100.0);
		cd.deposit(100.0);
		checking.withdraw(100.0);
		savings.withdraw(100.0);
		cd.withdraw(100.0);

		assertEquals(0.0, checking.getBalance(), 0.01);
		assertEquals(0.0, savings.getBalance(), 0.01);
		assertEquals(0.0, cd.getBalance(), 0.01);
	}

	@Test
	void two_deposits_into_accounts() {
		checking.deposit(100.0);
		savings.deposit(100.0);
		cd.deposit(100.0);
		checking.deposit(100.0);
		savings.deposit(100.0);
		cd.deposit(100.0);

		assertEquals(200.0, checking.getBalance(), 0.01);
		assertEquals(200.0, savings.getBalance(), 0.01);
		assertEquals(200.0, cd.getBalance(), 0.01);
	}

	@Test
	public void two_withdraws_from_accounts() {
		checking.deposit(100.0);
		savings.deposit(100.0);
		cd.deposit(100.0);
		checking.withdraw(25.0);
		savings.withdraw(25.0);
		cd.withdraw(25.0);
		checking.withdraw(25.0);
		savings.withdraw(25.0);
		cd.withdraw(25.0);

		assertEquals(50.0, checking.getBalance(), 0.01);
		assertEquals(50.0, savings.getBalance(), 0.01);
		assertEquals(50.0, cd.getBalance(), 0.01);
	}

	@Test
	void test_deposit_positive_amount() {
		savings.deposit(100.0);
		double deposited = 100.0;
		assertEquals(100.0, deposited, 0.01);
		assertEquals(100.0, savings.getBalance(), 0.01);
	}

	@Test
	void test_deposit_zero() {
		savings.deposit(0.0);
		double deposited = savings.deposit(0.0);
		assertEquals(0.0, deposited, 0.01);
		assertEquals(0.0, savings.getBalance(), 0.01);
	}

	@Test
	void test_withdraw_less_than_balance() {
		savings.deposit(200.0);
		double withdrawn = savings.withdraw(100.0);
		assertEquals(100.0, withdrawn, 0.01);
		assertEquals(100.0, savings.getBalance(), 0.01);
	}

	@Test
	void test_withdraw_exact_balance() {
		savings.deposit(200.0);
		double withdrawn = savings.withdraw(200.0);
		assertEquals(200.0, withdrawn, 0.01);
		assertEquals(0.0, savings.getBalance(), 0.01);
	}

	@Test
	void test_withdraw_more_than_balance() {
		savings.deposit(200.0);
		double withdrawn = savings.withdraw(300.0);
		assertEquals(200.0, withdrawn, 0.01);
		assertEquals(0.0, savings.getBalance(), 0.01);
	}

	@Test
	void test_apply_apr() {
		savings.deposit(1000.0);
		double interest = savings.applyAPR();
		assertEquals(0.83, interest, 0.01);
	}

	@Test
	void test_deposit_small_amount() {
		savings.deposit(0.01);
		double deposited = savings.deposit(0.01);
		assertEquals(0.01, deposited, 0.01);
		assertEquals(0.01, savings.getBalance(), 0.01);
	}

	@Test
	void test_withdraw_small_amount() {
		savings.deposit(0.01);
		double withdrawn = savings.withdraw(0.01);
		assertEquals(0.01, withdrawn, 0.01);
		assertEquals(0.0, savings.getBalance(), 0.01);
	}

}
