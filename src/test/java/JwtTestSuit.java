import com.oliweb.filters.KeyGenerator;
import com.oliweb.utility.JwtGenerator;
import io.jsonwebtoken.Jwts;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JwtTestSuit {

    @Test
    public void testJwtGenerator() {
        String key = KeyGenerator.getKey();

        String compactJws = JwtGenerator.generateJwt("Joe", "audience", "01");
        assertTrue(Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getSubject().equals("Joe"));
        assertTrue(Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getAudience().equals("audience"));
        assertTrue(Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getId().equals("01"));
    }
}
