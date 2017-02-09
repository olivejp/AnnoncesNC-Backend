package com.oliweb.utility;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class SessionIdentifierGenerator {
	private static SecureRandom random = new SecureRandom();

	public static String nextSessionId() {
		return new BigInteger(50, random).toString(20);
	}
}

