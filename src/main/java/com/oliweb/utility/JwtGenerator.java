package com.oliweb.utility;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JwtGenerator {

    public static String generateJwt(String subject, String audience, String idUtilisateur) {

        // get base64 encoded version of the key
        String key = Crypto.desEncryptIt(Proprietes.getProperty(Proprietes.CRYPTO_PASS));

        JwtBuilder jwtBuilder = Jwts.builder()
            .setIssuer(Proprietes.getProperty(Proprietes.JWT_ISSUER))
            .setSubject(subject)
            .setAudience(audience)
            .setId(idUtilisateur)
            .signWith(SignatureAlgorithm.HS256, key);

        return jwtBuilder.compact();
    }
}
