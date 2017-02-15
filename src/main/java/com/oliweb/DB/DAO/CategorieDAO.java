package com.oliweb.DB.DAO;

import com.oliweb.DB.Contract.AnnonceContract;
import com.oliweb.DB.DTO.CategorieDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.oliweb.DB.Contract.CategorieContract.*;


public class CategorieDAO {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Connection dbConn;

    public CategorieDAO(Connection dbConn) {
        this.dbConn = dbConn;
    }

    public ArrayList<CategorieDTO> getCompleteList() {
        ArrayList<CategorieDTO> myList = new ArrayList<>();

        try {
            if (!dbConn.isClosed()) {
                Statement stmt = dbConn.createStatement();

                ResultSet rs, rs2;
                String query;

                query = "SELECT " + COL_ID_CATEGORIE + ", " + COL_NOM_CATEGORIE + ", " + COL_COULEUR_CATEGORIE + " FROM " + TABLE_NAME;
                rs = stmt.executeQuery(query);
                while (rs.next()) {
                    CategorieDTO cat = new CategorieDTO();
                    cat.setIdCAT(rs.getInt(COL_ID_CATEGORIE));
                    cat.setNameCAT(rs.getString(COL_NOM_CATEGORIE));
                    cat.setCouleurCAT(rs.getString(COL_COULEUR_CATEGORIE));
                    myList.add(cat);
                }
                rs.close();

                // On lance une deuxi�me requ�te pour r�cup�rer le nombre d 'annonce par cat�gorie
                for (CategorieDTO categorie : myList) {
                    query = "SELECT count(" + AnnonceContract.COL_ID_ANNONCE + ") FROM " + AnnonceContract.TABLE_NAME + " WHERE " + AnnonceContract.COL_ID_CATEGORY + " = " + String.valueOf(categorie.getIdCAT());
                    rs2 = stmt.executeQuery(query);
                    if (rs2.next()) {
                        categorie.setNbAnnonceCAT(rs2.getInt(1));
                    }
                    rs2.close();
                }
                stmt.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getCompleteList", e);
        }
        return myList;
    }

    public CategorieDTO getById(Integer idCategorie) {

        CategorieDTO categorie = new CategorieDTO();

        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT " + COL_ID_CATEGORIE + ", "
                    + COL_NOM_CATEGORIE + ", "
                    + COL_COULEUR_CATEGORIE
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_CATEGORIE + " = " + String.valueOf(idCategorie);

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                categorie.setIdCAT(rs.getInt(COL_ID_CATEGORIE));
                categorie.setNameCAT(rs.getString(COL_NOM_CATEGORIE));
                categorie.setCouleurCAT(rs.getString(COL_COULEUR_CATEGORIE));
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getById", e);
        }
        return categorie;
    }

    public boolean existById(Integer id) {
        boolean exist = false;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_ID_CATEGORIE + " =" + String.valueOf(id);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    exist = true;
                }
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "existById", e);
        }
        return exist;
    }

    @SuppressWarnings("unused")
    public CategorieDTO getByName(String nameCategorie) {

        CategorieDTO categorie = new CategorieDTO();
        String query = "SELECT " + COL_ID_CATEGORIE + ", " + COL_NOM_CATEGORIE + " "
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_NOM_CATEGORIE + " = ?";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setString(1, nameCategorie);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                categorie.setIdCAT(results.getInt(COL_ID_CATEGORIE));
                categorie.setNameCAT(results.getString(COL_NOM_CATEGORIE));
            }
            stmt.close();
            results.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "existById", e);
        }

        return categorie;
    }
}
