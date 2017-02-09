import com.oliweb.DB.DAO.AnnonceDAO;
import com.oliweb.DB.DTO.AnnonceDTO;
import com.oliweb.DB.DTO.CategorieDTO;
import com.oliweb.DB.DTO.PhotoDTO;
import com.oliweb.DB.DTO.UtilisateurDTO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class RestServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    Connection connection;
    private ArrayList<PhotoDTO> listPhoto;
    private PhotoDTO photoDTO;
    private CategorieDTO categorieDTO;
    private UtilisateurDTO utilisateurDTO;
    private AnnonceDTO annonceDTO;

    private void initializeData() {
        listPhoto = new ArrayList<>();
        photoDTO = new PhotoDTO(1, "/superTof.jpg", 1);
        listPhoto.add(photoDTO);
        categorieDTO = new CategorieDTO(1, "Immobilier", "#515525", 5);
        utilisateurDTO = new UtilisateurDTO(1, "orlanth23@hotmail.com", 790723);
        annonceDTO = new AnnonceDTO(1, categorieDTO, utilisateurDTO, 123456, "description", "titre", true, (long) 1, listPhoto);
    }

    @Test
    public void testAdd() {
        Connection connection = Mockito.mock(Connection.class);
        Statement statement = Mockito.mock(Statement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        try {
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);
            when(statement.executeUpdate(anyString())).thenReturn(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initializeData();
        AnnonceDAO annonceDAO = new AnnonceDAO(connection);
        assertTrue(annonceDAO.insert(annonceDTO));
    }
}
