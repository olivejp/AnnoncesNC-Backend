package com.oliweb.DB.DAO;

import com.oliweb.DB.DTO.PhotoDTO;
import com.oliweb.DB.utility.Properties;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.oliweb.DB.Contract.PhotoContract.*;

public class PhotoDAO {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Connection dbConn;

    public PhotoDAO(Connection dbConn) {
        this.dbConn = dbConn;
    }

    private PhotoDTO transfertPhoto(ResultSet rs) throws SQLException {
        PhotoDTO photo = new PhotoDTO();

        // Renseignement des champs de l'annonce
        photo.setIdPhoto(rs.getInt(COL_ID_PHOTO));
        photo.setIdAnnoncePhoto(rs.getInt(COL_ID_ANNONCE));
        photo.setNamePhoto(rs.getString(COL_NOM_PHOTO));
        return photo;
    }

    private boolean launchQuery(String query, Integer idAnnonce, Integer idPhoto, String namePhoto) {
        boolean existStatus = false;
        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, idPhoto);
            stmt.setInt(2, idAnnonce);
            stmt.setString(3, namePhoto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    existStatus = true;
                }
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "existByAnnonceAndName", e);
        }
        return existStatus;
    }

    public boolean existByAnnonceAndName(Integer idAnnonce, Integer idPhoto, String namePhoto) {

        String query = "SELECT COUNT(*) FROM " + TABLE_NAME
                + " WHERE " + COL_ID_PHOTO + "= ?"
                + " AND " + COL_ID_ANNONCE + "= ? "
                + " AND " + COL_NOM_PHOTO + "= ?";

        return launchQuery(query, idAnnonce, idPhoto, namePhoto);
    }


    public boolean existWithDifferentName(Integer idAnnonce, Integer idPhoto, String namePhoto) {
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE "
                + COL_ID_PHOTO + "= ? AND "
                + COL_ID_ANNONCE + "= ? AND "
                + COL_NOM_PHOTO + "<> ?";

        return launchQuery(query, idAnnonce, idPhoto, namePhoto);
    }

    public boolean exist(Integer idAnnonce, Integer idPhoto) {
        Connection dbConn = MyConnection.getInstance();
        boolean existStatus = false;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_PHOTO + " =" + String.valueOf(idPhoto)
                    + " AND " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    existStatus = true;
                }
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "exist", e);
        }
        return existStatus;
    }

    public boolean update(PhotoDTO photo) {
        boolean updateStatus = false;
        int records;
        String query;
        try {
            Statement stmt = dbConn.createStatement();
            query = "UPDATE " + TABLE_NAME + " SET "
                    + COL_NOM_PHOTO + " = '" + photo.getNamePhoto() + "'"
                    + " WHERE " + COL_ID_PHOTO + " = " + String.valueOf(photo.getIdPhoto())
                    + " AND " + COL_ID_ANNONCE + " = " + String.valueOf(photo.getIdAnnoncePhoto());
            records = stmt.executeUpdate(query);

            updateStatus = records != 0;
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "update", e);
        }
        return updateStatus;
    }

    public PhotoDTO getById(Integer idPhoto) {
        PhotoDTO retour = null;

        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT " + COL_ID_PHOTO + ", "
                    + COL_NOM_PHOTO + ", "
                    + COL_ID_ANNONCE
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_PHOTO + " = " + String.valueOf(idPhoto);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                retour = transfertPhoto(rs);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getById", e);
        }
        return retour;
    }

    public PhotoDTO getById(Integer idAnnonce, Integer idPhoto) {
        PhotoDTO retour = null;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT " + COL_ID_PHOTO + ", "
                    + COL_NOM_PHOTO + ", "
                    + COL_ID_ANNONCE
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_PHOTO + " = " + String.valueOf(idPhoto)
                    + " AND " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                retour = transfertPhoto(rs);
            }

            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getById", e);
        }
        return retour;
    }

    public int getMaxId() {
        String query;
        int retour = -1;

        ResultSet rs;
        try {
            Statement stmt = dbConn.createStatement();
            query = "SELECT MAX(" + COL_ID_PHOTO + ") AS MAXID FROM " + TABLE_NAME;
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                retour = rs.getInt("MAXID");
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getMaxId", e);
        }

        return retour;
    }

    private int getMaxIdByAnnonce(Integer idAnnonce) {
        Connection dbConn = MyConnection.getInstance();
        String query;
        int retour = -1;

        try {
            Statement stmt = dbConn.createStatement();
            query = "Select MAX(" + COL_ID_PHOTO + ") as MAXID from " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + idAnnonce;
            ResultSet rs;
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                retour = rs.getInt("MAXID");
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getMaxIdByAnnonce", e);
        }

        return retour;
    }

    public int insert(PhotoDTO photo) {
        int retour = 0;
        int records;
        Statement stmt;
        Integer idAnnonce = photo.getIdAnnoncePhoto();

        try {
            stmt = dbConn.createStatement();
            String query = "INSERT INTO " + TABLE_NAME + " ("
                    + COL_NOM_PHOTO + ", "
                    + COL_ID_ANNONCE
                    + ") VALUES ('"
                    + photo.getNamePhoto() + "', "
                    + String.valueOf(idAnnonce) + ")";
            records = stmt.executeUpdate(query);
            stmt.close();

            //When record is successfully inserted
            if (records > 0) {
                retour = getMaxIdByAnnonce(idAnnonce);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "insert", e);
        }
        return retour;
    }

    private boolean deleteRealFile(String realFilePath) {
        // On va supprimer la photo bitmap dans le r�pertoire d'upload.
        String basename = FilenameUtils.getName(realFilePath);
        String diretory_to_delete = Properties.getProperty(Properties.DIRECTORY_IMAGE) + basename;
        File file = new File(diretory_to_delete);
        return file.delete();
    }

    public boolean delete(Integer idAnnonce, Integer idPhoto) {
        boolean deleteStatus = false;
        int records;
        String delete, query, where, nom_photo = null;

        try {
            Statement stmt = dbConn.createStatement();
            where = " WHERE " + COL_ID_PHOTO + " = " + String.valueOf(idPhoto) + " AND " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);
            query = "SELECT " + COL_NOM_PHOTO + " FROM " + TABLE_NAME + where;
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                nom_photo = rs.getString(COL_NOM_PHOTO);
            }
            delete = "DELETE FROM " + TABLE_NAME + where;
            records = stmt.executeUpdate(delete);

            //When record is successfully inserted
            if (records != 0) {
                deleteStatus = true;
                deleteRealFile(nom_photo); // On tente de supprimer réellement la photo
            } else {
                deleteStatus = false;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "delete", e);
        }
        return deleteStatus;
    }

    public boolean deleteByIdAnnonce(Integer idAnnonce) {
        boolean deleteStatus = false;
        String query1, query2;

        // On lance la requ�te pour supprimer les donn�es. Mais la m�thode doit quand m�me nous renvoyer true meme si elle n'a rien supprim�.
        // Donc on va lire le nombre de photos pour cette annonce et s'il n'en reste aucune alors on renvoie true, sinon false;
        try {
            Statement stmt = dbConn.createStatement();

            query1 = "SELECT " + COL_ID_PHOTO + ", " + COL_NOM_PHOTO + " FROM " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);
            ResultSet rs = stmt.executeQuery(query1);
            while (rs.next()) {
                int idPhoto = rs.getInt(1);
                String nomPhoto = rs.getString(2);
                if (delete(idAnnonce, idPhoto)) { // Si on a r�ussi � supprimer la photo dans la BD
                    deleteRealFile(nomPhoto); // On va supprimer la vraie photo
                }
            }
            rs.close();

            // Pour v�rifier qu'on a tout supprimer on va faire un select count, si r�sultat = 0 alors on a tout supprimer
            query2 = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);
            rs = stmt.executeQuery(query2);

            //When record is successfully inserted
            if (rs.next()) {
                deleteStatus = rs.getInt(1) == 0;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "deleteByIdAnnonce", e);
        }
        return deleteStatus;
    }

    ArrayList<PhotoDTO> getByIdAnnonce(Integer idAnnonce) {
        ArrayList<PhotoDTO> photosDTO = new ArrayList<>();
        String query;

        try {
            Statement stmt = dbConn.createStatement();
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                photosDTO.add(new PhotoDTO(rs.getInt(COL_ID_PHOTO), rs.getString(COL_NOM_PHOTO), rs.getInt(COL_ID_ANNONCE)));
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getByIdAnnonce", e);
        }
        return photosDTO;
    }

}