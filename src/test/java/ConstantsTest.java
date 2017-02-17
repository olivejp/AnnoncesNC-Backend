import com.oliweb.utility.Proprietes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstantsTest {

    @Test
    public void testConstantsDB() {
        String dbName = Proprietes.getProperty(Proprietes.DB_NAME);
        assertEquals(dbName, "annoncesrest");
    }
}
