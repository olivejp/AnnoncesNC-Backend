package com.oliweb.utility;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Crypto {
	public static String desEncryptIt(String value) {
		try {
			DESKeySpec keySpec = new DESKeySpec(Constants.cryptoPass.getBytes("UTF8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			byte[] clearText = value.getBytes("UTF8");
			// Cipher is not thread safe
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
			return encrypedValue;

		} catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String desDecryptIt(String value) {
		try {
			DESKeySpec keySpec = new DESKeySpec(Constants.cryptoPass.getBytes("UTF8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
			// cipher is not thread safe
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

			String decrypedValue = new String(decrypedValueBytes);
			return decrypedValue;

		} catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return value;
	}
}
