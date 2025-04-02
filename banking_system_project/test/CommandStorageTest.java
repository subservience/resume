package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandStorageTest {
	private CommandStorage commandStorage;

	private Bank bank;

	@BeforeEach
	public void setUp() {
		commandStorage = new CommandStorage(bank);
	}

	@Test
	void test_add_command() {
		commandStorage.addInvalidCommand("ceat savings 12345678 1.0");
		List<String> commands = commandStorage.getInvalidCommands();
		assertEquals(1, commands.size());
		assertEquals("ceat savings 12345678 1.0", commands.get(0));
	}

	@Test
	void test_get_commands() {
		commandStorage.addInvalidCommand("creat checking 12345678 1.0");
		commandStorage.addInvalidCommand("crea savings 87654321 1.0");

		List<String> commands = commandStorage.getInvalidCommands();
		assertEquals(2, commands.size());
		assertEquals("creat checking 12345678 1.0", commands.get(0));
		assertEquals("crea savings 87654321 1.0", commands.get(1));
	}
}