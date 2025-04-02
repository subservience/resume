package banking;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CommandStorage {
	private final List<String> commands;
	private final List<String> accountData;
	private final List<String> output;
	private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
	private final Bank bank;

	public CommandStorage(Bank bank) {
		this.commands = new ArrayList<>();
		this.accountData = new ArrayList<>();
		this.output = new ArrayList<>();
		this.bank = bank;
		decimalFormat.setRoundingMode(RoundingMode.FLOOR);
	}

	public void addInvalidCommand(String command) {
		commands.add(command);
	}

	public List<String> getInvalidCommands() {
		return new ArrayList<>(commands);
	}

	public List<String> generateOutput() {
		output.addAll(getAccountStatuses());
		output.addAll(getInvalidCommands());
		return new ArrayList<>(output);
	}

	public List<String> getAccountStatuses() {
		for (String accountId : bank.getAccounts().keySet()) {

			Account account = bank.getAccounts().get(accountId);
			String accountType = "";
			if (account instanceof SavingsAccount) {
				accountType = "Savings";
			} else if (account instanceof CheckingAccount) {
				accountType = "Checking";
			} else if (account instanceof CDAccount) {
				accountType = "Cd";
			}
			String formattedAccountData = accountType + " " + account.getId() + " "
					+ decimalFormat.format(account.getBalance()) + " " + decimalFormat.format(account.getApr());
			accountData.add(formattedAccountData);

			if (bank.getAccountTransactions().containsKey(account.getId())) {
				accountData.addAll(bank.getAccountTransactions().get(account.getId()));
			}
		}
		return new ArrayList<>(accountData);
	}
}
