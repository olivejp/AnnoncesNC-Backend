import com.oliweb.DB.utility.Properties;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstantsTest {

    @Test
    public void testConstantsDB() {
        String dbName = Properties.getProperty(Properties.DB_NAME);
        assertEquals(dbName, "annoncesrest");
    }
}
