package banking;

public class CommandProcessor {
	Bank bank;
	CommandValidator commandValidator;

	public CommandProcessor(Bank bank) {
		this.bank = bank;
		this.commandValidator = new CommandValidator(bank);
	}

	public void process(String command) {
		String[] segments = command.split(" ");

		if (commandValidator.isValid(command)) {
			if (segments[0].equalsIgnoreCase("create")) {
				makeAccount(segments);
			} else if (segments[0].equalsIgnoreCase("deposit")) {
				makeDeposit(segments);
			} else if (segments[0].equalsIgnoreCase("withdraw")) {
				makeWithdraw(segments);
			} else if (segments[0].equalsIgnoreCase("transfer")) {
				makeTransfer(segments);
			} else if (segments[0].equalsIgnoreCase("pass")) {
				passTime(segments);
			}
		}
	}

	private void makeAccount(String[] segments) {
		String accountType = segments[1];
		String id = segments[2];
		double apr = Double.parseDouble(segments[3]);

		if (accountType.equalsIgnoreCase("checking")) {
			bank.addAccount(new CheckingAccount(id, apr));
		} else if (accountType.equalsIgnoreCase("savings")) {
			bank.addAccount(new SavingsAccount(id, apr));
		} else if (accountType.equalsIgnoreCase("cd")) {
			double initialBalance = Double.parseDouble(segments[4]);
			bank.addAccount(new CDAccount(id, apr, initialBalance));
		}
	}

	private void makeDeposit(String[] segments) {
		String id = segments[1];
		double amount = Double.parseDouble(segments[2]);
		String command = String.join(" ", segments);
		bank.recordTransaction(command);
		bank.deposit(id, amount);
	}

	private void makeWithdraw(String[] segments) {
		String id = segments[1];
		double amount = Double.parseDouble(segments[2]);
		String command = String.join(" ", segments);
		bank.recordTransaction(command);
		bank.withdraw(id, amount);
	}

	private void makeTransfer(String[] segments) {
		String fromId = segments[1];
		String toId = segments[2];
		double requestedAmount = Double.parseDouble(segments[3]);

		double withdrawnAmount = bank.withdraw(fromId, requestedAmount);
		String command = String.join(" ", segments);
		bank.recordTransaction(command);
		bank.deposit(toId, withdrawnAmount);
	}

	private void passTime(String[] segments) {
		int months = Integer.parseInt(segments[1]);
		bank.passTime(months);
	}
}
