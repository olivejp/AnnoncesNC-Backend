import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oliweb.db.dao.AnnonceDAO;
import com.oliweb.db.dao.CategorieDAO;
import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dao.UtilisateurDAO;
import com.oliweb.db.dto.Annonce;
import com.oliweb.db.dto.Categorie;
import com.oliweb.db.dto.Utilisateur;
import com.oliweb.dto.ReturnWS;
import com.oliweb.restservice.AnnonceRestService;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RestAnnonceServiceTest {

    private static Gson gson = new Gson();

    @Mock
    Categorie categorie;

    @Mock
    Utilisateur utilisateur;

    // Test pour demontrer qu'on ne peut pas poster d'annonce si l'utilisateur n'existe pas.
    @Test
    public void testFailInsertAnnonce() {
        CategorieDAO categorieDAO = new CategorieDAO(MyConnection.getInstance());
        categorieDAO.delete(1);

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(MyConnection.getInstance());
        utilisateurDAO.delete(1);

        AnnonceRestService AnnonceRestService = new AnnonceRestService();
        String returnWs = AnnonceRestService.post(1, 1, "Titre", "Description", 123, 0);

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
        Categorie categorie = RestCategorieTest.saveCategorie(RestCategorieTest.generateCategorie());

        // Creation dun nouvel utilisateur
        Utilisateur utilisateur = RestUtilisateurTest.saveUtilisateur(RestUtilisateurTest.generateUtilisateur());

        // Call the WS to post annonce
        AnnonceRestService annonceRestService = new AnnonceRestService();
        String returnWs = annonceRestService.post(categorie.getIdCAT(), utilisateur.getIdUTI(), "Titre", "Description", 123, 0);

        // Récupération du type retourné
        Type returnWsType = new TypeToken<ReturnWS>() {
        }.getType();
        ReturnWS rs = gson.fromJson(returnWs, returnWsType);

        // Le résultat doit être vrai
        assertTrue(rs.isValid());

        Type annonceType = new TypeToken<Annonce>() {
        }.getType();

        // Recupération de l'annonce
        Annonce annonce = gson.fromJson(rs.getMsg(), annonceType);
        assertEquals(annonce.getTitreANO(), "Titre");
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

        Type listAnnonceType = new TypeToken<ArrayList<Annonce>>() {
        }.getType();

        // Recupération de l'annonce
        ArrayList<Annonce> mListAnnonce = gson.fromJson(rs.getMsg(), listAnnonceType);
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
