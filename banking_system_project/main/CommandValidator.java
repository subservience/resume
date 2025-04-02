package banking;

public class CommandValidator {

	Bank bank;
	CreateCommandValidator createCommandValidator;
	DepositCommandValidator depositCommandValidator;
	WithdrawCommandValidator withdrawCommandValidator;
	TransferCommandValidator transferCommandValidator;
	PassTimeCommandValidator passTimeCommandValidator;

	public CommandValidator(Bank bank) {
		this.bank = bank;
	}

	public boolean isValid(String command) {
		this.createCommandValidator = new CreateCommandValidator(bank);
		this.depositCommandValidator = new DepositCommandValidator(bank);
		this.withdrawCommandValidator = new WithdrawCommandValidator(bank);
		this.transferCommandValidator = new TransferCommandValidator(bank);
		this.passTimeCommandValidator = new PassTimeCommandValidator(bank);

		String[] segments = command.split(" ");

		if (segments[0].equalsIgnoreCase("create")) {
			return createCommandValidator.isValid(segments);
		} else if (segments[0].equalsIgnoreCase("deposit")) {
			return depositCommandValidator.isValid(segments);
		} else if (segments[0].equalsIgnoreCase("withdraw")) {
			return withdrawCommandValidator.isValid(segments);
		} else if (segments[0].equalsIgnoreCase("transfer")) {
			return transferCommandValidator.isValid(segments);
		} else if (segments[0].equalsIgnoreCase("pass")) {
			return passTimeCommandValidator.isValid(segments);
		}
		return false;
	}
}
