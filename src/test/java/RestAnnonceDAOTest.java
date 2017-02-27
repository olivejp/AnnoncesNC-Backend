import com.google.gson.Gson;
import com.oliweb.db.dao.AnnonceDAO;
import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dto.AnnonceDTO;
import com.oliweb.db.dto.CategorieDTO;
import com.oliweb.db.dto.UtilisateurDTO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertTrue;

public class RestAnnonceDAOTest {

    private static Gson gson = new Gson();
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    CategorieDTO categorieDTO;
    @Mock
    UtilisateurDTO utilisateurDTO;

    public static AnnonceDTO insertAnnonce(String titre, String description, int prix, UtilisateurDTO utilisateurDTO, CategorieDTO categorieDTO) {
        AnnonceDTO annonceDTO = new AnnonceDTO();
        annonceDTO.setTitreANO(titre);
        annonceDTO.setDescriptionANO(description);
        annonceDTO.setPriceANO(prix);
        annonceDTO.setCategorieANO(categorieDTO);
        annonceDTO.setUtilisateurANO(utilisateurDTO);
        return annonceDTO;
    }

    @Test
    public void testInsertDeleteAnnonce() {
        AnnonceDAO AnnonceDAO = new AnnonceDAO(MyConnection.getInstance());
        AnnonceDTO AnnonceDTO = insertAnnonce("titre", "description", 123, utilisateurDTO, categorieDTO);
        assertTrue(AnnonceDAO.save(AnnonceDTO));
        assertTrue(AnnonceDAO.delete(AnnonceDTO.getIdANO()));
    }


}
