import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oliweb.db.dao.AnnonceDAO;
import com.oliweb.db.dao.CategorieDAO;
import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dao.UtilisateurDAO;
import com.oliweb.db.dto.AnnonceDTO;
import com.oliweb.db.dto.CategorieDTO;
import com.oliweb.db.dto.UtilisateurDTO;
import com.oliweb.restservice.AnnonceRestService;
import com.oliweb.restservice.ReturnWS;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RestAnnonceServiceTest {

    private static Gson gson = new Gson();

    @Mock
    CategorieDTO categorieDTO;

    @Mock
    UtilisateurDTO utilisateurDTO;

    // Test pour demontrer qu'on ne peut pas poster d'annonce si l'utilisateur n'existe pas.
    @Test
    public void testFailInsertAnnonce() {
        CategorieDAO categorieDAO = new CategorieDAO(MyConnection.getInstance());
        categorieDAO.delete(1);

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(MyConnection.getInstance());
        utilisateurDAO.delete(1);

        AnnonceRestService AnnonceRestService = new AnnonceRestService();
        String returnWs = AnnonceRestService.post(1, 1, "Titre", "Description", 123);

        Type returnWsType = new TypeToken<ReturnWS>() {
        }.getType();
        ReturnWS rs = gson.fromJson(returnWs, returnWsType);

        assertFalse(rs.isValid());
    }

    // Test de connexion d'un utilisateur
    @Test
    public void testWinInsertAnnonce() {
        // Initialisation de la DB
        UtilityTest.initDb();

        // Creation dune nouvelle categorie
        CategorieDTO categorie = RestCategorieTest.saveCategorie(RestCategorieTest.generateCategorie());

        // Creation dun nouvel utilisateur
        UtilisateurDTO utilisateur = RestUtilisateurTest.saveUtilisateur(RestUtilisateurTest.generateUtilisateur());

        // Call the WS to post annonce
        AnnonceRestService annonceRestService = new AnnonceRestService();
        String returnWs = annonceRestService.post(categorie.getIdCAT(), utilisateur.getIdUTI(), "Titre", "Description", 123);

        // Récupération du type retourné
        Type returnWsType = new TypeToken<ReturnWS>() {
        }.getType();
        ReturnWS rs = gson.fromJson(returnWs, returnWsType);

        // Le résultat doit être vrai
        assertTrue(rs.isValid());

        Type annonceType = new TypeToken<AnnonceDTO>() {
        }.getType();

        // Recupération de l'annonce
        AnnonceDTO annonceDTO = gson.fromJson(rs.getMsg(), annonceType);
        assertEquals(annonceDTO.getTitreANO(), "Titre");
    }

    // Test de recherche d'annonce
    @Test
    public void testSearchAnnonce() {
        testWinInsertAnnonce();

        AnnonceRestService annonceRestService = new AnnonceRestService();
        String returnWs = annonceRestService.getByMultiparam(0, 0, 0, "e", false, 1);

        Type returnWsType = new TypeToken<ReturnWS>() {
        }.getType();
        ReturnWS rs = gson.fromJson(returnWs, returnWsType);

        // Le résultat doit être vrai
        assertTrue(rs.isValid());

        Type listAnnonceType = new TypeToken<ArrayList<AnnonceDTO>>() {
        }.getType();

        // Recupération de l'annonce
        ArrayList<AnnonceDTO> mListAnnonce = gson.fromJson(rs.getMsg(), listAnnonceType);
        assertTrue(mListAnnonce.size() > 0);
    }

    @Test
    public void testMultiparam() {
        List<String> listConditions = new ArrayList<>();
        String keywords = "str1 str2";
        AnnonceDAO annonceDAO = new AnnonceDAO(MyConnection.getInstance());
        annonceDAO.addKeywordCondition(keywords, listConditions);
    }
}
