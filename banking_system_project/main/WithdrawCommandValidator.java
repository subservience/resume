package banking;

public class WithdrawCommandValidator extends CommandValidator {

	public WithdrawCommandValidator(Bank bank) {
		super(bank);
	}

	public boolean isValid(String[] segments) {
		if (segments.length != 3) {
			return false;
		}

		String accountNumber = segments[1];
		String withdrawAmount = segments[2];

		if (!accountNumber.matches("\\d{8}")) {
			return false;
		}

		if (!bank.hasAccount(accountNumber)) {
			return false;
		}

		double amount = Double.parseDouble(withdrawAmount);
		if (amount < 0) {
			return false;
		}

		Account account = bank.getAccount(accountNumber);
		if (account instanceof SavingsAccount && amount > 1000) {
			return false;
		} else if (account instanceof CheckingAccount && amount > 400) {
			return false;
		} else if ((account instanceof CDAccount && bank.getAccountMonths().get(accountNumber) < 12)) {
			return false;
		}
		return true;
	}
}
