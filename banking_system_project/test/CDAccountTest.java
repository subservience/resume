package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CDAccountTest {

	@Test
	public void testCDAccountInitialization() {
		CDAccount cdAccount = new CDAccount("12345678", 2.5, 1000.0);

		assertEquals("12345678", cdAccount.getId());
		assertEquals(2.5, cdAccount.getApr(), 0.01);
		assertEquals(1000.0, cdAccount.getBalance(), 0.01);
	}
}