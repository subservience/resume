package banking;

public class TransferCommandValidator extends CommandValidator {

	public TransferCommandValidator(Bank bank) {
		super(bank);
	}

	public boolean isValid(String[] segments) {
		if (segments.length != 4) {
			return false;
		}

		String fromId = segments[1];
		String toId = segments[2];
		String transferAmount = segments[3];

		if (!fromId.matches("\\d{8}") || !toId.matches("\\d{8}")) {
			return false;
		}

		if (!bank.hasAccount(fromId)) {
			return false;
		} else if (!bank.hasAccount(toId)) {
			return false;
		}

		Account fromAccount = bank.getAccount(fromId);
		Account toAccount = bank.getAccount(toId);

		double amount = Double.parseDouble(transferAmount);
		if (amount < 0) {
			return false;
		}

		if (fromAccount instanceof CDAccount || toAccount instanceof CDAccount) {
			return false;
		} else if (fromAccount instanceof SavingsAccount && toAccount instanceof CheckingAccount && (amount > 1000)) {
			return false;
		} else if (toAccount instanceof SavingsAccount && fromAccount instanceof CheckingAccount && (amount > 2500)) {
			return false;
		}
		return true;
	}
}