import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oliweb.db.dao.CategorieDAO;
import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dto.Categorie;
import com.oliweb.db.dto.Utilisateur;
import com.oliweb.dto.ReturnWS;
import com.oliweb.restservice.CategorieRestService;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestCategorieTest {

    private static final String NOM_CATEGORIE = "TEST";
    private static final String COULEUR_CATEGORIE = "#555555";
    private static Gson gson = new Gson();
    private static CategorieDAO categorieDAO = new CategorieDAO(MyConnection.getInstance());
    @Mock
    Utilisateur utilisateur;

    public static Categorie generateCategorie() {
        Categorie categorie = new Categorie();
        categorie.setNameCAT(NOM_CATEGORIE);
        categorie.setCouleurCAT(COULEUR_CATEGORIE);
        return categorie;
    }

    public static Categorie saveCategorie(Categorie categorie) {
        assertTrue(categorieDAO.save(categorie));
        return categorie;
    }

    public static void deleteAllCategorie() {
        saveCategorie(generateCategorie());
        assertTrue(categorieDAO.deleteAll());
        assertEquals(0, categorieDAO.getCompleteList().size());
    }

    @Test
    public void testInsertDeleteCategorie() {
        deleteAllCategorie();
        Categorie categorie = saveCategorie(generateCategorie());
        assertTrue(categorieDAO.delete(categorie.getIdCAT()));
    }

    @Test
    public void testRestListCategorie() {
        deleteAllCategorie();
        saveCategorie(generateCategorie());

        CategorieRestService categorieRestService = new CategorieRestService();
        String returnWs = categorieRestService.getListCategorie();

        Type returnWsType = new TypeToken<ReturnWS>() {
        }.getType();
        ReturnWS rs = gson.fromJson(returnWs, returnWsType);

        Type listType = new TypeToken<ArrayList<Categorie>>() {
        }.getType();
        ArrayList<Categorie> myList = gson.fromJson(rs.getMsg(), listType);
        assertEquals(myList.get(0).getNameCAT(), NOM_CATEGORIE);
    }
}
