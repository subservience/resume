package banking;

public class CreateCommandValidator extends CommandValidator {

	private final String[] validTypes = { "savings", "checking", "cd" };

	public CreateCommandValidator(Bank bank) {
		super(bank);
	}

	public boolean isValid(String[] segments) {
		if (segments.length < 4 || segments.length > 5) {
			return false;
		}

		String accountType = segments[1];
		if (!isValidAccountType(accountType)) {
			return false;
		}

		String accountNumber = segments[2];
		if (!accountNumber.matches("\\d{8}")) {
			return false;
		}

		if (bank.hasAccount(accountNumber)) {
			return false;
		}

		String aprString = segments[3];
		if (!isValidAPR(aprString)) {
			return false;
		}

		if (!accountType.equalsIgnoreCase("cd") && segments.length > 4) {
			return false;
		}

		if (accountType.equalsIgnoreCase("cd")) {
			if (segments.length != 5) {
				return false;
			}
			return isValidBalance(segments[4]);
		}

		return true;
	}

	private boolean isValidAccountType(String type) {
		for (String validType : validTypes) {
			if (validType.equalsIgnoreCase(type)) {
				return true;
			}
		}
		return false;
	}

	private boolean isValidAPR(String apr) {
		double aprValue = Double.parseDouble(apr);
		return aprValue >= 0 && aprValue <= 10;
	}

	private boolean isValidBalance(String balance) {
		double balanceValue = Double.parseDouble(balance);
		return balanceValue >= 1000 && balanceValue <= 10000;
	}
}
