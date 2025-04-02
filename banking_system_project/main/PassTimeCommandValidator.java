package banking;

public class PassTimeCommandValidator extends CommandValidator {
	public PassTimeCommandValidator(Bank bank) {
		super(bank);
	}

	public boolean isValid(String[] segments) {
		if (segments.length != 2) {
			return false;
		}

		String monthsString = segments[1];
		if (!monthsString.matches("\\d+")) {
			return false;
		}

		int months = Integer.parseInt(monthsString);
		if (months < 1 || months > 60) {
			return false;
		}

		return true;
	}
}
