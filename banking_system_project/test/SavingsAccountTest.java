package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SavingsAccountTest {

	@Test
	public void testSavingsAccountInitialization() {
		SavingsAccount savingsAccount = new SavingsAccount("12345678", 2.5);

		assertEquals("12345678", savingsAccount.getId());
		assertEquals(2.5, savingsAccount.getApr(), 0.01);
		assertEquals(0.0, savingsAccount.getBalance(), 0.01);
	}
}