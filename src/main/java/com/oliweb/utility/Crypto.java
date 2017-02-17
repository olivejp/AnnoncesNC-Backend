package com.oliweb.utility;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Crypto {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Crypto() {
    }

	public static String desEncryptIt(String value) {
		try {
            DESKeySpec keySpec = new DESKeySpec(Proprietes.getProperty(Proprietes.CRYPTO_PASS).getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			byte[] clearText = value.getBytes("UTF8");
			// Cipher is not thread safe
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			return Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);

		} catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return value;
	}

	public static String desDecryptIt(String value) {
		try {
            DESKeySpec keySpec = new DESKeySpec(Proprietes.getProperty(Proprietes.CRYPTO_PASS).getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
			// cipher is not thread safe
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypedValueBytes = cipher.doFinal(encrypedPwdBytes);

			return new String(decrypedValueBytes);

		} catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return value;
	}
}
