package banking;

public class CDAccount extends Account {
	public CDAccount(String id, double apr, double initialBalance) {
		super(id, apr);
		this.balance = initialBalance;
	}
}