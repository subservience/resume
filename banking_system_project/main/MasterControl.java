package banking;

import java.util.List;

public class MasterControl {
	private CommandValidator commandValidator;
	private CommandProcessor commandProcessor;
	private CommandStorage commandStorage;

	public MasterControl(CommandValidator commandValidator, CommandProcessor commandProcessor,
			CommandStorage commandStorage) {
		this.commandValidator = commandValidator;
		this.commandProcessor = commandProcessor;
		this.commandStorage = commandStorage;
	}

	public List<String> start(List<String> input) {
		for (String command : input) {
			if (commandValidator.isValid(command)) {
				commandProcessor.process(command);
			} else {
				commandStorage.addInvalidCommand(command);
			}
		}
		return commandStorage.generateOutput();
	}

}
