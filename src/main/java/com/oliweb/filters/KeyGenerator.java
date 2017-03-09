package com.oliweb.filters;

import com.oliweb.utility.Crypto;
import com.oliweb.utility.Proprietes;

public class KeyGenerator {
    public static String getKey() {
        return Crypto.desEncryptIt(Proprietes.getProperty(Proprietes.CRYPTO_PASS));
    }
}
