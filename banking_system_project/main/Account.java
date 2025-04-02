package banking;

public abstract class Account {
	private final String id;
	private final double apr;
	double balance;

	public Account(String id, double apr) {
		this.id = id;
		this.apr = apr;
		this.balance = 0.0;
	}

	public String getId() {
		return id;
	}

	public double getBalance() {
		return balance;
	}

	public double getApr() {
		return apr;
	}

	public double deposit(double amount) {
		balance += amount;
		return amount;
	}

	public double withdraw(double amount) {
		double withdrawnAmount;
		if (amount > balance) {
			withdrawnAmount = balance;
			balance -= withdrawnAmount;
		} else {
			withdrawnAmount = amount;
			balance -= amount;
		}
		return withdrawnAmount;
	}

	public double applyAPR() {
		return balance * (apr / 100 / 12);
	}
}