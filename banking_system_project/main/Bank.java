package banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
	private HashMap<String, Account> accounts;
	private HashMap<String, Integer> accountMonths;
	private HashMap<String, List<String>> accountTransactions;
	private boolean withdrewThisMonth;
	private double total_accounts = 0;

	public Bank() {
		accounts = new HashMap<>();
		accountMonths = new HashMap<>();
		accountTransactions = new HashMap<>();
		this.withdrewThisMonth = false;
	}

	public int getAccountAmount() {
		return (int) total_accounts;
	}

	public Map<String, Account> getAccounts() {
		return accounts;
	}

	public Map<String, List<String>> getAccountTransactions() {
		return accountTransactions;
	}

	public void addAccount(Account account) {
		accounts.put(account.getId(), account);
		total_accounts += 1;
	}

	public void recordTransaction(String command) {
		String[] segments = command.split(" ");

		if (command.toLowerCase().startsWith("transfer")) {
			String toId = segments[1];
			String fromId = segments[2];
			accountTransactions.putIfAbsent(toId, new ArrayList<>());
			accountTransactions.putIfAbsent(fromId, new ArrayList<>());
			accountTransactions.get(toId).add(command);
			accountTransactions.get(fromId).add(command);
		}

		String accountId = segments[1];
		accountTransactions.putIfAbsent(accountId, new ArrayList<>());
		accountTransactions.get(accountId).add(command);
	}

	public boolean hasAccount(String id) {
		return accounts.containsKey(id);
	}

	public Account getAccount(String id) {
		return accounts.get(id);
	}

	public void deposit(String id, double amount) {
		Account account = accounts.get(id);
		account.deposit(amount);
	}

	public double withdraw(String id, double amount) {
		Account account = accounts.get(id);
		return account.withdraw(amount);
	}

	public void incrementAccountMonths(String accountId) {
		int currentMonths = accountMonths.getOrDefault(accountId, 0);
		accountMonths.put(accountId, currentMonths + 1);
		withdrewThisMonth = false;
	}

	public void passTime(int months) {
		for (int m = 0; m < months; m++) {
			Map<String, Account> allAccounts = new HashMap<>(accounts);
			for (Account account : allAccounts.values()) {
				if (!closeAccountIfBalanceZero(account)) {
					balanceDecay(account);
					updateBalance(account);
					incrementAccountMonths(account.getId());
				}
			}
		}
	}

	public boolean closeAccountIfBalanceZero(Account account) {
		if (account.getBalance() == 0) {
			accounts.remove(account.getId());
			accountMonths.remove(account.getId());
			return true;
		}
		return false;
	}

	public void balanceDecay(Account account) {
		if (account.getBalance() < 100) {
			account.withdraw(25);
		}
	}

	public double updateBalance(Account account) {
		if (account instanceof CDAccount) {
			for (int k = 0; k < 4; k++) {
				account.deposit(account.applyAPR());
			}
		} else {
			account.deposit(account.applyAPR());
		}

		return account.getBalance();
	}

	public HashMap<String, Integer> getAccountMonths() {
		return accountMonths;
	}
}