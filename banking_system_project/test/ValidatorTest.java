package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidatorTest {

	private static final String CHECKING_ID = "12345678";
	private static final String SAVINGS_ID = "23456789";
	private static final String CD_ID = "34567890";
	private static final double APR = 1.0;
	CreateCommandValidator createCommandValidator;
	DepositCommandValidator depositCommandValidator;
	WithdrawCommandValidator withdrawCommandValidator;
	TransferCommandValidator transferCommandValidator;
	PassTimeCommandValidator passTimeCommandValidator;
	Bank bank;
	private CheckingAccount checking;
	private SavingsAccount savings;
	private CDAccount cd;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		createCommandValidator = new CreateCommandValidator(bank);
		depositCommandValidator = new DepositCommandValidator(bank);
		withdrawCommandValidator = new WithdrawCommandValidator(bank);
		transferCommandValidator = new TransferCommandValidator(bank);
		passTimeCommandValidator = new PassTimeCommandValidator(bank);
		checking = new CheckingAccount(CHECKING_ID, APR);
		savings = new SavingsAccount(SAVINGS_ID, APR);
		cd = new CDAccount(CD_ID, APR, 1000.0);
	}

	// CREATE TESTS

	@Test
	void valid_command_with_lowercase_c() {
		assertTrue(createCommandValidator.isValid("create checking 12345678 0.2"));
	}

	@Test
	void valid_command_with_uppercase_c() {
		assertTrue(createCommandValidator.isValid("Create checking 87654321 0.2"));
	}

	@Test
	void valid_command_with_mixed_letters() {
		assertTrue(createCommandValidator.isValid("CrEaTe checking 12345678 0.6"));
	}

	@Test
	void valid_command_with_savings() {
		assertTrue(createCommandValidator.isValid("Create savings 12345678 0.2"));
	}

	@Test
	void valid_command_with_cdaccount_starting_amount() {
		assertTrue(createCommandValidator.isValid("Create cd 12345678 0.2 1000"));
	}

	@Test
	void invalid_command_with_invalid_account_name() {
		assertFalse(createCommandValidator.isValid("Create potato 12345678 0.2"));
	}

	@Test
	void invalid_command_with_invalid_create_name() {
		assertFalse(createCommandValidator.isValid("tomato potato 12345678 0.2"));
	}

	@Test
	void invalid_command_with_misspell_create() {
		assertFalse(createCommandValidator.isValid("crate savings 12345678 0.2"));
	}

	@Test
	void invalid_command_with_missing_create() {
		assertFalse(createCommandValidator.isValid("savings 12345678 0.5"));
	}

	@Test
	void invalid_command_with_invalid_APR() {
		assertFalse(createCommandValidator.isValid("Create savings 12345678 100.0"));
	}

	@Test
	void invalid_command_with_invalid_ID() {
		assertFalse(createCommandValidator.isValid("Create savings 123456 0.3"));
	}

	@Test
	void invalid_command_with_missing_APR() {
		assertFalse(createCommandValidator.isValid("create savings 12345678"));
	}

	@Test
	void invalid_command_with_too_many() {
		assertFalse(createCommandValidator.isValid("create savings 12345678 0.2 1000"));
	}

	@Test
	void invalid_command_with_missing_spaces() {
		assertFalse(createCommandValidator.isValid("create checking12345678 0.2"));
	}

	@Test
	void invalid_command_with_deposit_of_cd_under_1000() {
		assertFalse(createCommandValidator.isValid("create cd 12345678 0.6 250"));
	}

	@Test
	void invalid_command_with_deposit_of_cd_over_10000() {
		assertFalse(createCommandValidator.isValid("create cd 12345678 0.6 20000"));
	}

	@Test
	void invalid_command_with_missing_ID() {
		assertFalse(createCommandValidator.isValid("create checking 0.2"));
	}

	@Test
	void invalid_command_with_negative_APR() {
		assertFalse(createCommandValidator.isValid("create checking 12345678 -0.1"));
	}

	@Test
	void invalid_command_with_over_10_APR() {
		assertFalse(createCommandValidator.isValid("create savings 12345678 20.0"));
	}

	@Test
	void invalid_command_with_invalid_mixed_ID() {
		assertFalse(createCommandValidator.isValid("create checking ygdyaudh 0.2"));
	}

	@Test
	void valid_create_command_with_case_sensitive() {
		assertTrue(createCommandValidator.isValid("create checking 12345678 0.01"));
	}

	@Test
	void invalid_create_command_cd_missing_initial_balance() {
		assertFalse(createCommandValidator.isValid("create cd 12345678 1.0"));
	}

	@Test
	void invalid_create_command_cd_with_too_many_words() {
		assertFalse(createCommandValidator.isValid("create cd 12345678 1.0 1000.0 potato"));
	}

	@Test
	void test_invalid_apr_below_zero() {
		assertFalse(createCommandValidator.isValid("create savings 12345678 -1.0"));
	}

	@Test
	void test_invalid_apr_above_ten() {
		assertFalse(createCommandValidator.isValid("create savings 1234567810.1"));
	}

	@Test
	void test_valid_apr_at_zero() {
		assertTrue(createCommandValidator.isValid("create savings 12345678 0.0"));
	}

	@Test
	void test_invalid_cd_balance_above_maximum() {
		assertFalse(createCommandValidator.isValid("create cd 12345678 1.0 10001"));
	}

	@Test
	void test_invalid_account_type() {
		assertFalse(createCommandValidator.isValid("create unknown 12345678 1.0"));
	}

	// DEPOSIT TESTS

	@Test
	void valid_deposit_command_with_capital_D() {
		bank.addAccount(checking);
		assertTrue(depositCommandValidator.isValid("Deposit 12345678 100.0"));
	}

	@Test
	void valid_deposit_command_with_lowercase_D() {
		bank.addAccount(checking);
		assertTrue(depositCommandValidator.isValid("deposit 12345678 100.0"));
	}

	@Test
	void invalid_deposit_command_no_deposit() {
		assertFalse(depositCommandValidator.isValid("12345678 1000.0"));
	}

	@Test
	void test_invalid_deposit_account_not_found() {
		assertFalse(depositCommandValidator.isValid("deposit 87654321 100"));
	}

	@Test
	void test_invalid_deposit_negative_amount() {
		bank.addAccount(savings);
		assertFalse(depositCommandValidator.isValid("deposit 23456789 -50"));
	}

	@Test
	void test_valid_deposit_savings_within_limit() {
		bank.addAccount(savings);
		assertTrue(depositCommandValidator.isValid("deposit 23456789 2000"));
	}

	@Test
	void test_invalid_deposit_savings_exceeds_limit() {
		bank.addAccount(savings);
		assertFalse(depositCommandValidator.isValid("deposit 23456789 3000"));
	}

	@Test
	void test_valid_deposit_checking_within_limit() {
		bank.addAccount(checking);
		assertTrue(depositCommandValidator.isValid("deposit 12345678 500"));
	}

	@Test
	void test_invalid_deposit_checking_exceeds_limit() {
		bank.addAccount(checking);
		assertFalse(depositCommandValidator.isValid("deposit 12345678 1500"));
	}

	@Test
	void test_invalid_deposit_cd_account() {
		bank.addAccount(cd);
		assertFalse(depositCommandValidator.isValid("deposit 34567890 1000"));
	}

	@Test
	void test_invalid_deposit_zero_amount() {
		bank.addAccount(savings);
		assertFalse(depositCommandValidator.isValid("deposit 12345678 0"));
	}

	@Test
	void test_valid_deposit_savings_at_limit() {
		bank.addAccount(savings);
		assertTrue(depositCommandValidator.isValid("deposit 23456789 2500"));
	}

	@Test
	void test_valid_deposit_checking_at_limit() {
		bank.addAccount(checking);
		assertTrue(depositCommandValidator.isValid("deposit 12345678 1000"));
	}

	@Test
	void invalid_deposit_command_no_ID() {
		assertFalse(depositCommandValidator.isValid("deposit 1000.0"));
	}

	@Test
	void invalid_deposit_command_invalid_ID() {
		assertFalse(depositCommandValidator.isValid("deposit asdfghj 1000.0"));
	}

	@Test
	void invalid_deposit_command_no_amount() {
		assertFalse(depositCommandValidator.isValid("deposit 12345678"));
	}

	@Test
	void invalid_deposit_command_no_nothing() {
		assertFalse(depositCommandValidator.isValid("deposit"));
	}

	@Test
	void invalid_deposit_command_negative_amount() {
		assertFalse(depositCommandValidator.isValid("deposit 12345678 -1000.0"));
	}

	@Test
	void invalid_deposit_command_many_spaces() {
		assertFalse(depositCommandValidator.isValid("deposit 12345678 1000.0 cd"));
	}

	@Test
	void test_valid_deposit_zero() {
		bank.addAccount(checking);
		assertTrue(depositCommandValidator.isValid("deposit 12345678 0.0"));
	}

	@Test
	void invalid_account_number_length_for_deposit() {
		assertFalse(depositCommandValidator.isValid("deposit 1234567 0.0"));
	}

	@Test
	void invalid_account_number_length_for_deposit2() {
		assertFalse(depositCommandValidator.isValid("deposit 123456789 0.0"));
	}

	// WITHDRAW TESTS

	@Test
	void valid_withdraw_command() {
		bank.addAccount(checking);
		assertTrue(withdrawCommandValidator.isValid("withdraw 12345678 100.0"));
	}

	@Test
	void invalid_withdraw_command_negative_amount() {
		assertFalse(withdrawCommandValidator.isValid("withdraw 12345678 -50.0"));
	}

	@Test
	void invalid_withdraw_command_invalid_account_number() {
		assertFalse(withdrawCommandValidator.isValid("withdraw 8765432 100.0"));
	}

	@Test
	void invalid_withdraw_command_invalid_format() {
		assertFalse(withdrawCommandValidator.isValid("withdraw 12345678"));
	}

	@Test
	void invalid_withdraw_command_empty_command() {
		assertFalse(withdrawCommandValidator.isValid("withdraw"));
	}

	@Test
	void invalid_withdraw_command_extra_arguments() {
		assertFalse(withdrawCommandValidator.isValid("withdraw 12345678 100.0 extra"));
	}

	@Test
	void invalid_withdraw_command_missing_id() {
		assertFalse(withdrawCommandValidator.isValid("withdraw 100.0"));
	}

	@Test
	void valid_withdraw_zero() {
		bank.addAccount(checking);
		assertTrue(withdrawCommandValidator.isValid("withdraw 12345678 0.0"));
	}

	@Test
	void valid_withdraw_command_mixed() {
		bank.addAccount(checking);
		assertTrue(withdrawCommandValidator.isValid("wiThDRAW 12345678 100.0"));
	}

	@Test
	void valid_withdraw_command_all_uppercase() {
		bank.addAccount(checking);
		assertTrue(withdrawCommandValidator.isValid("WITHDRAW 12345678 100.0"));
	}

	@Test
	void invalid_withdraw_command_swapped_id_amount() {
		assertFalse(withdrawCommandValidator.isValid("withdraw 50 12345678"));
	}

	@Test
	void valid_withdraw_from_savings_command_all_uppercase() {
		bank.addAccount(savings);
		assertTrue(withdrawCommandValidator.isValid("WITHDRAW 23456789 0.0"));
	}

	@Test
	void valid_withdraw_command_from_savings() {
		bank.addAccount(savings);
		assertTrue(withdrawCommandValidator.isValid("withdraw 23456789 100.0"));
	}

	@Test
	void invalid_withdraw_command_from_savings_negative() {
		bank.addAccount(savings);
		assertFalse(withdrawCommandValidator.isValid("WITHDRAW 23456789 -50.0"));
	}

	@Test
	void test_invalid_withdraw_negative_amount() {
		bank.addAccount(savings);
		assertFalse(withdrawCommandValidator.isValid("withdraw 23456789 -50"));
	}

	@Test
	void test_valid_withdraw_savings_within_limit() {
		bank.addAccount(savings);
		savings.deposit(2000.0);
		assertTrue(withdrawCommandValidator.isValid("withdraw 23456789 1000"));
	}

	@Test
	void test_invalid_withdraw_savings_exceeds_limit() {
		bank.addAccount(savings);
		savings.deposit(3000.0);
		assertFalse(withdrawCommandValidator.isValid("withdraw 23456789 1500"));
	}

	@Test
	void test_valid_withdraw_checking_within_limit() {
		bank.addAccount(checking);
		checking.deposit(1000.0);
		assertTrue(withdrawCommandValidator.isValid("withdraw 12345678 400"));
	}

	@Test
	void test_invalid_withdraw_checking_exceeds_limit() {
		bank.addAccount(checking);
		checking.deposit(500.0);
		assertFalse(withdrawCommandValidator.isValid("withdraw 12345678 500"));
	}

	@Test
	void test_invalid_withdraw_cd_account_before_maturity() {
		bank.addAccount(cd);
		bank.getAccountMonths().put("12345678", 6);
		assertFalse(withdrawCommandValidator.isValid("withdraw 12345678 1000"));
	}

	@Test
	void test_valid_withdraw_cd_account_after_maturity() {
		bank.addAccount(cd);
		bank.getAccountMonths().put("34567890", 12);
		assertTrue(withdrawCommandValidator.isValid("withdraw 34567890 1000"));
	}

	@Test
	void test_invalid_withdraw_zero_amount() {
		bank.addAccount(savings);
		assertFalse(withdrawCommandValidator.isValid("withdraw 12345678 0"));
	}

	@Test
	void test_invalid_withdraw_insufficient_balance() {
		bank.addAccount(savings);
		savings.deposit(500.0);
		assertFalse(withdrawCommandValidator.isValid("withdraw 12345678 1000"));
	}

	@Test
	void test_valid_withdraw_exact_balance() {
		bank.addAccount(savings);
		savings.deposit(500.0);
		assertTrue(withdrawCommandValidator.isValid("withdraw 23456789 500"));
	}

	// TRANSFER TESTS

	@Test
	void invalid_transfer_command_negative_amount() {
		assertFalse(transferCommandValidator.isValid("transfer 12345678 87654321 -500.0"));
	}

	@Test
	void invalid_transfer_command_invalid_id() {
		assertFalse(transferCommandValidator.isValid("transfer 12345 87654321 500.0"));
	}

	@Test
	void invalid_transfer_command_short_ids() {
		assertFalse(transferCommandValidator.isValid("transfer 12345 8765 500.0"));
	}

	@Test
	void invalid_transfer_command_short_toID() {
		assertFalse(transferCommandValidator.isValid("transfer 12345678 8765 500.0"));
	}

	@Test
	void valid_transfer_command_zero() {
		bank.addAccount(checking);
		bank.addAccount(savings);
		assertTrue(transferCommandValidator.isValid("transfer 12345678 23456789 0.0"));
	}

	@Test
	void valid_transfer_command_uppercase() {
		bank.addAccount(checking);
		bank.addAccount(savings);
		checking.deposit(750.0);
		assertTrue(transferCommandValidator.isValid("TRANSFER 12345678 23456789 500.0"));
	}

	@Test
	void valid_transfer_command_mixed() {
		bank.addAccount(checking);
		bank.addAccount(savings);
		checking.deposit(750.0);
		assertTrue(transferCommandValidator.isValid("TRANSfer 12345678 23456789 500.0"));
	}

	@Test
	void test_invalid_transfer_incorrect_segments() {
		assertFalse(transferCommandValidator.isValid("transfer 12345678 87654321"));
	}

	@Test
	void test_invalid_transfer_account_not_found() {
		bank.addAccount(savings);
		assertFalse(transferCommandValidator.isValid("transfer 23456789 87654321 100"));
	}

	@Test
	void test_invalid_transfer_negative_amount() {
		bank.addAccount(savings);
		bank.addAccount(checking);
		assertFalse(transferCommandValidator.isValid("transfer 12345678 87654321 -50"));
	}

	@Test
	void test_invalid_transfer_cd_account_involved() {
		bank.addAccount(cd);
		bank.addAccount(checking);
		assertFalse(transferCommandValidator.isValid("transfer 12345678 34567890 100"));
		assertFalse(transferCommandValidator.isValid("transfer 34567890 12345678 100"));
	}

	@Test
	void test_valid_transfer_savings_to_checking_within_limit() {
		bank.addAccount(savings);
		bank.addAccount(checking);
		savings.deposit(2000.0);
		assertTrue(transferCommandValidator.isValid("transfer 23456789 12345678 1000"));
	}

	@Test
	void test_invalid_transfer_savings_to_checking_exceeds_limit() {
		bank.addAccount(savings);
		bank.addAccount(checking);
		savings.deposit(3000.0);
		assertFalse(transferCommandValidator.isValid("transfer 23456789 12345678 2500"));
	}

	@Test
	void test_valid_transfer_checking_to_savings_within_limit() {
		bank.addAccount(checking);
		bank.addAccount(savings);
		checking.deposit(500.0);
		assertTrue(transferCommandValidator.isValid("transfer 12345678 23456789 500"));
	}

	@Test
	void test_invalid_transfer_zero_amount() {
		bank.addAccount(savings);
		bank.addAccount(checking);
		assertFalse(transferCommandValidator.isValid("transfer 12345678 87654321 0"));
	}

	@Test
	void test_valid_transfer_savings_to_checking_at_limit() {
		bank.addAccount(savings);
		bank.addAccount(checking);
		savings.deposit(2500.0);
		assertTrue(transferCommandValidator.isValid("transfer 23456789 12345678 1000"));
	}

	@Test
	void test_valid_transfer_checking_to_savings_at_limit() {
		bank.addAccount(checking);
		bank.addAccount(savings);
		checking.deposit(1000.0);
		checking.deposit(1000.0);
		checking.deposit(1000.0);
		assertTrue(transferCommandValidator.isValid("transfer 12345678 23456789 2500"));
	}

	// PASS TESTS

	@Test
	void valid_pass_time_command() {
		assertTrue(passTimeCommandValidator.isValid("pass 1"));
	}

	@Test
	void valid_pass_time_max_command() {
		assertTrue(passTimeCommandValidator.isValid("pass 60"));
	}

	@Test
	void invalid_pass_time_zero_months() {
		assertFalse(passTimeCommandValidator.isValid("pass 0"));
	}

	@Test
	void invalid_pass_time_exceeding_months() {
		assertFalse(passTimeCommandValidator.isValid("pass 61"));
	}

	@Test
	void invalid_pass_time_non_numeric() {
		assertFalse(passTimeCommandValidator.isValid("pass abc"));
	}

	@Test
	void invalid_pass_time_missing_months() {
		assertFalse(passTimeCommandValidator.isValid("pass"));
	}

	@Test
	void invalid_pass_time_extra_arguments() {
		assertFalse(passTimeCommandValidator.isValid("pass 5 extra"));
	}

	@Test
	void invalid_pass_time_negative() {
		assertFalse(passTimeCommandValidator.isValid("pass -1"));
	}

	@Test
	void invalid_pass_time_extra_arguments_negative_and_extra() {
		assertFalse(passTimeCommandValidator.isValid("pass -1 extra"));
	}

}