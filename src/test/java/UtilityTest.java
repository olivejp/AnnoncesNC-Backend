import com.oliweb.db.dao.CategorieDAO;
import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dao.UtilisateurDAO;
import com.oliweb.utility.Proprietes;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UtilityTest {

    public static void initDb() {
        Connection connection = MyConnection.getInstance();
        CategorieDAO categorieDAO = new CategorieDAO(connection);
        categorieDAO.deleteAll();

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
        utilisateurDAO.deleteAll();
    }

    @Test
    public void testConstantsDB() {
        String dbName = Proprietes.getProperty(Proprietes.DB_NAME);
        assertEquals(dbName, "annoncesrest");
    }

    @Test
    public void testConnection() {
        assertNotNull(MyConnection.getInstance());
    }
}
