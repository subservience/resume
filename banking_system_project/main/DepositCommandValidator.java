package banking;

public class DepositCommandValidator extends CommandValidator {

	public DepositCommandValidator(Bank bank) {
		super(bank);
	}

	public boolean isValid(String[] segments) {
		if (segments.length != 3) {
			return false;
		}

		String accountNumber = segments[1];
		String depositAmount = segments[2];

		if (!accountNumber.matches("\\d{8}")) {
			return false;
		}

		if (!bank.hasAccount(accountNumber)) {
			return false;
		}

		double amount = Double.parseDouble(depositAmount);
		Account account = bank.getAccount(accountNumber);

		if (account instanceof CDAccount) {
			return false;
		} else if (amount < 0) {
			return false;
		} else if (account instanceof SavingsAccount && (amount > 2500)) {
			return false;
		} else if (account instanceof CheckingAccount && (amount > 1000)) {
			return false;
		}
		return true;
	}
}
