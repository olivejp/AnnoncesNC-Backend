import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dao.UtilisateurDAO;
import com.oliweb.db.dao.enumStatutUtilisateur;
import com.oliweb.db.dto.UtilisateurDTO;
import com.oliweb.restservice.UtilisateurRestService;
import org.junit.Test;

import static org.junit.Assert.*;

public class RestUtilisateurTest {

    private static final String EMAIL_UTILISATEUR = "TEST";
    private static final int TEL_UTILISATEUR = 790723;

    private static UtilisateurDAO utilisateurDAO = new UtilisateurDAO(MyConnection.getInstance());
    private static UtilisateurRestService utilisateurRestService = new UtilisateurRestService();

    public static UtilisateurDTO generateUtilisateur() {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setEmailUTI(EMAIL_UTILISATEUR);
        utilisateurDTO.setTelephoneUTI(TEL_UTILISATEUR);
        utilisateurDTO.setStatutUTI(enumStatutUtilisateur.VALID.valeur());
        return utilisateurDTO;
    }

    public static void deleteAllUtilisateur() {
        // On créé au moins un utilisateur pour pouvoir tester la suppression
        assertNotNull(saveUtilisateur(generateUtilisateur()));
        assertTrue(utilisateurDAO.deleteAll());
    }

    public static UtilisateurDTO saveUtilisateur(UtilisateurDTO utilisateurDTO) {
        assertTrue(utilisateurDAO.save(utilisateurDTO));
        return utilisateurDTO;
    }

    @Test
    public void testInsertDeleteUtilisateur() {
        UtilityTest.initDb();
        UtilisateurDTO utilisateurDTO = saveUtilisateur(generateUtilisateur());
        int id = utilisateurDTO.getIdUTI();
        utilisateurRestService.unregister(id);
        assertEquals(utilisateurDAO.get(id).getStatutUTI(), enumStatutUtilisateur.UNREGISTRED.valeur());
    }
}
