import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oliweb.db.dao.CategorieDAO;
import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dto.CategorieDTO;
import com.oliweb.db.dto.UtilisateurDTO;
import com.oliweb.restservice.CategorieRestService;
import com.oliweb.restservice.ReturnWS;
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
    UtilisateurDTO utilisateurDTO;

    public static CategorieDTO generateCategorie() {
        CategorieDTO categorieDTO = new CategorieDTO();
        categorieDTO.setNameCAT(NOM_CATEGORIE);
        categorieDTO.setCouleurCAT(COULEUR_CATEGORIE);
        return categorieDTO;
    }

    public static CategorieDTO saveCategorie(CategorieDTO categorieDTO) {
        assertTrue(categorieDAO.save(categorieDTO));
        return categorieDTO;
    }

    public static void deleteAllCategorie() {
        saveCategorie(generateCategorie());
        assertTrue(categorieDAO.deleteAll());
        assertEquals(0, categorieDAO.getCompleteList().size());
    }

    @Test
    public void testInsertDeleteCategorie() {
        deleteAllCategorie();
        CategorieDTO categorieDTO = saveCategorie(generateCategorie());
        assertTrue(categorieDAO.delete(categorieDTO.getIdCAT()));
    }

    @Test
    public void testRestListCategorie() {
        deleteAllCategorie();
        saveCategorie(generateCategorie());

        CategorieRestService categorieRestService = new CategorieRestService();
        String returnWs = categorieRestService.listCategorie();

        Type returnWsType = new TypeToken<ReturnWS>() {
        }.getType();
        ReturnWS rs = gson.fromJson(returnWs, returnWsType);

        Type listType = new TypeToken<ArrayList<CategorieDTO>>() {
        }.getType();
        ArrayList<CategorieDTO> myList = gson.fromJson(rs.getMsg(), listType);
        assertEquals(myList.get(0).getNameCAT(), NOM_CATEGORIE);
    }
}
