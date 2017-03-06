import com.oliweb.db.dao.AnnonceDAO;
import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dto.Annonce;
import com.oliweb.db.dto.Categorie;
import com.oliweb.db.dto.Utilisateur;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertTrue;

public class RestAnnonceDAOTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    Categorie categorie;
    @Mock
    Utilisateur utilisateur;

    public static Annonce insertAnnonce(String titre, String description, int prix, Utilisateur utilisateur, Integer idCategorieDTO) {
        Annonce annonce = new Annonce();
        annonce.setTitreANO(titre);
        annonce.setDescriptionANO(description);
        annonce.setPriceANO(prix);
        annonce.setIdCategorieANO(idCategorieDTO);
        annonce.setUtilisateurANO(utilisateur);
        return annonce;
    }

    @Test
    public void testInsertDeleteAnnonce() {
        AnnonceDAO annonceDAO = new AnnonceDAO(MyConnection.getInstance());
        Annonce Annonce = insertAnnonce("titre", "description", 123, utilisateur, categorie.getIdCAT());
        assertTrue(annonceDAO.save(Annonce));
        assertTrue(annonceDAO.delete(Annonce.getIdANO()));
    }


}
