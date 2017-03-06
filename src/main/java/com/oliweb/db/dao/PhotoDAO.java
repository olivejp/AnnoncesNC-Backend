package com.oliweb.db.dao;

import com.oliweb.db.dto.Photo;
import com.oliweb.utility.Proprietes;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.oliweb.db.contract.PhotoContract.*;

public class PhotoDAO extends AbstractDAO<Photo> {

    public PhotoDAO(Connection dbConn) {
        super(dbConn);
    }

    private Photo transfertPhoto(ResultSet rs) throws SQLException {
        Photo photo = new Photo();
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
            if (rs.next() && rs.getInt(1) >= 1) {
                existStatus = true;
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


    private boolean deleteRealFile(String realFilePath) {
        // On va supprimer la photo bitmap dans le r�pertoire d'upload.
        String basename = FilenameUtils.getName(realFilePath);
        String diretory_to_delete = Proprietes.getProperty(Proprietes.DIRECTORY_IMAGE) + basename;
        File file = new File(diretory_to_delete);
        return file.delete();
    }


    public boolean deleteByIdAnnonce(Integer idAnnonce) {
        boolean deleteStatus = true;
        String query;

        try {
            Statement stmt = dbConn.createStatement();

            query = "SELECT " + COL_ID_PHOTO + " FROM " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                deleteStatus = deleteStatus && delete(rs.getInt(1));
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "deleteByIdAnnonce", e);
        }
        return deleteStatus;
    }

    List<Photo> getByIdAnnonce(Integer idAnnonce) {
        ArrayList<Photo> photosDTO = new ArrayList<>();
        String query;

        try {
            Statement stmt = dbConn.createStatement();
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                photosDTO.add(new Photo(rs.getInt(COL_ID_PHOTO), rs.getString(COL_NOM_PHOTO), rs.getInt(COL_ID_ANNONCE)));
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getByIdAnnonce", e);
        }
        return photosDTO;
    }

    @Override
    public boolean save(Photo photo) {
        boolean insertStatus = false;
        PreparedStatement stmt;
        Integer idAnnonce = photo.getIdAnnoncePhoto();

        String query = "INSERT INTO " + TABLE_NAME + " ("
                + COL_NOM_PHOTO + ", "
                + COL_ID_ANNONCE
                + ") VALUES ('"
                + photo.getNamePhoto() + "', "
                + String.valueOf(idAnnonce) + ")";

        try {
            stmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            if (stmt.executeUpdate() != 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    insertStatus = true;
                    photo.setIdPhoto(rs.getInt(1));
                }
                rs.close();
            } else {
                insertStatus = false;
            }
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "insert", e);
        }
        return insertStatus;
    }

    @Override
    public boolean delete(int idPhoto) {
        boolean deleteStatus = false;
        String query, nom_photo = "";

        Photo photo = get(idPhoto);
        if (photo != null) {
            nom_photo = photo.getNamePhoto();
        }

        query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID_PHOTO + " = ? ";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, idPhoto);
            if (stmt.executeUpdate() != 0) {
                deleteStatus = true;
                deleteRealFile(nom_photo); // On tente de supprimer réellement la photo
            } else {
                deleteStatus = false;
            }
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "delete", e);
        }
        return deleteStatus;
    }

    @Override
    public Photo get(int idPhoto) {
        Photo photo = null;

        String query = "SELECT " + COL_ID_PHOTO + ", "
                + COL_NOM_PHOTO + ", "
                + COL_ID_ANNONCE
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_ID_PHOTO + " = ?";
        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, idPhoto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                photo = transfertPhoto(rs);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getById", e);
        }
        return photo;
    }

    @Override
    public boolean update(Photo photo) {
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
}
