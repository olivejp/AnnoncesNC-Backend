import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dao.UtilisateurDAO;
import com.oliweb.db.dao.enumStatutUtilisateur;
import com.oliweb.db.dto.Utilisateur;
import com.oliweb.restservice.UtilisateurRestService;
import org.junit.Test;

import static org.junit.Assert.*;

public class RestUtilisateurTest {

    private static final String EMAIL_UTILISATEUR = "TEST";
    private static final int TEL_UTILISATEUR = 790723;

    private static UtilisateurDAO utilisateurDAO = new UtilisateurDAO(MyConnection.getInstance());
    private static UtilisateurRestService utilisateurRestService = new UtilisateurRestService();

    public static Utilisateur generateUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmailUTI(EMAIL_UTILISATEUR);
        utilisateur.setTelephoneUTI(TEL_UTILISATEUR);
        utilisateur.setStatutUTI(enumStatutUtilisateur.VALID.valeur());
        return utilisateur;
    }

    public static void deleteAllUtilisateur() {
        // On créé au moins un utilisateur pour pouvoir tester la suppression
        assertNotNull(saveUtilisateur(generateUtilisateur()));
        assertTrue(utilisateurDAO.deleteAll());
    }

    public static Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        assertTrue(utilisateurDAO.save(utilisateur));
        return utilisateur;
    }

    @Test
    public void testInsertDeleteUtilisateur() {
        UtilityTest.initDb();
        Utilisateur utilisateur = saveUtilisateur(generateUtilisateur());
        int id = utilisateur.getIdUTI();
        utilisateurRestService.unregister(id);
        assertEquals(utilisateurDAO.get(id).getStatutUTI(), enumStatutUtilisateur.UNREGISTRED.valeur());
    }
}
