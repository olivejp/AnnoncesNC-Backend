package com.oliweb.DB.DAO;

import com.oliweb.DB.DTO.PhotoDTO;
import com.oliweb.DB.utility.ConstantsDB;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.oliweb.DB.Contract.PhotoContract.*;

public class PhotoDAO {
    private Integer idPhoto;
    private String nomPhoto;
    private String idAnnonce;

    private Connection dbConn;

    public PhotoDAO(Connection dbConn) {
        this.dbConn = dbConn;
    }

    public Integer getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(Integer idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getNomPhoto() {
        return nomPhoto;
    }

    public void setNomPhoto(String nomPhoto) {
        this.nomPhoto = nomPhoto;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    private PhotoDTO transfertPhoto(ResultSet rs) throws Exception {
        PhotoDTO photo = new PhotoDTO();

        // Renseignement des champs de l'annonce
        photo.setIdPhoto(rs.getInt(COL_ID_PHOTO));
        photo.setIdAnnoncePhoto(rs.getInt(COL_ID_ANNONCE));
        photo.setNamePhoto(rs.getString(COL_NOM_PHOTO));
        return photo;
    }

    public boolean existByAnnonceAndName(Integer idAnnonce, Integer idPhoto, String namePhoto) {
        boolean existStatus = false;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE "
                    + COL_ID_PHOTO + "=" + String.valueOf(idPhoto) + " AND "
                    + COL_ID_ANNONCE + "=" + String.valueOf(idAnnonce) + " AND "
                    + COL_NOM_PHOTO + "='" + namePhoto + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    existStatus = true;
                }
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existStatus;
    }


    public boolean existWithDifferentName(Integer idAnnonce, Integer idPhoto, String namePhoto) {
        boolean existStatus = false;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE "
                    + COL_ID_PHOTO + "=" + String.valueOf(idPhoto) + " AND "
                    + COL_ID_ANNONCE + "=" + String.valueOf(idAnnonce) + " AND "
                    + COL_NOM_PHOTO + "<>'" + namePhoto + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    existStatus = true;
                }
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existStatus;
    }

    public boolean exist(Integer idAnnonce, Integer idPhoto) throws Exception {
        Connection dbConn = MyConnection.getInstance();
        boolean existStatus = false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateStatus;
    }

    public PhotoDTO getById(Integer idPhoto) throws Exception {
        Statement stmt = dbConn.createStatement();
        PhotoDTO retour = null;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour;
    }

    public int getMaxId() throws Exception {
        String query;
        int retour = -1;

        Statement stmt = dbConn.createStatement();

        query = "SELECT MAX(" + COL_ID_PHOTO + ") AS MAXID FROM " + TABLE_NAME;
        ResultSet rs;
        rs = stmt.executeQuery(query);
        if (rs.next()) {
            retour = rs.getInt("MAXID");
        }
        stmt.close();
        return retour;
    }

    private int getMaxIdByAnnonce(Integer idAnnonce) throws Exception {
        Connection dbConn = MyConnection.getInstance();
        String query;
        int retour = -1;

        Statement stmt = dbConn.createStatement();

        query = "Select MAX(" + COL_ID_PHOTO + ") as MAXID from " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + idAnnonce;
        ResultSet rs;
        rs = stmt.executeQuery(query);
        if (rs.next()) {
            retour = rs.getInt("MAXID");
        }
        stmt.close();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour;
    }

    private boolean deleteRealFile(String realFilePath) {
        // On va supprimer la photo bitmap dans le r�pertoire d'upload.
        String basename = FilenameUtils.getName(realFilePath);
        String diretory_to_delete = ConstantsDB.directory_image + basename;
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
        } catch (Exception e) {
            e.printStackTrace();
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

            // Pour v�rifier qu'on a tout supprimer on va faire un select count, si r�sultat = 0 alors on a tout supprimer
            query2 = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);
            rs = stmt.executeQuery(query2);

            //When record is successfully inserted
            if (rs.next()) {
                deleteStatus = rs.getInt(1) == 0;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deleteStatus;
    }

    ArrayList<PhotoDTO> getByIdAnnonce(Integer idAnnonce) throws Exception {
        ArrayList<PhotoDTO> photosDTO = new ArrayList<>();
        String query;

        Statement stmt = dbConn.createStatement();
        query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            photosDTO.add(new PhotoDTO(rs.getInt(COL_ID_PHOTO), rs.getString(COL_NOM_PHOTO), rs.getInt(COL_ID_ANNONCE)));
        }
        stmt.close();
        return photosDTO;
    }

}
