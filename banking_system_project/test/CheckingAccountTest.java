package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CheckingAccountTest {

	@Test
	public void testCheckingAccountInitialization() {
		CheckingAccount checkingAccount = new CheckingAccount("87654321", 1.5);

		assertEquals("87654321", checkingAccount.getId());
		assertEquals(1.5, checkingAccount.getApr(), 0.01);
		assertEquals(0.0, checkingAccount.getBalance(), 0.01);
	}
}
