import com.oliweb.utility.JwtGenerator;
import com.oliweb.utility.Proprietes;
import io.jsonwebtoken.Jwts;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JwtTestSuit {

    @Test
    public void testJwtGenerator() {
        String compactJws = JwtGenerator.generateJwt("Joe", "audience", "01");
        assertTrue(Jwts.parser().setSigningKey(Proprietes.getProperty(Proprietes.CRYPTO_PASS)).parseClaimsJws(compactJws).getBody().getSubject().equals("Joe"));
        assertTrue(Jwts.parser().setSigningKey(Proprietes.getProperty(Proprietes.CRYPTO_PASS)).parseClaimsJws(compactJws).getBody().getAudience().equals("audience"));
        assertTrue(Jwts.parser().setSigningKey(Proprietes.getProperty(Proprietes.CRYPTO_PASS)).parseClaimsJws(compactJws).getBody().getId().equals("01"));
    }
}
