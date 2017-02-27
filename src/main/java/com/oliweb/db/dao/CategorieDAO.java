package com.oliweb.db.dao;

import com.oliweb.db.dto.CategorieDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.oliweb.db.contract.CategorieContract.*;

public class CategorieDAO extends AbstractDAO<CategorieDTO> {

    public CategorieDAO(Connection dbConn) {
        super(dbConn);
    }

    public List<CategorieDTO> getCompleteList() {
        List<CategorieDTO> myList = new ArrayList<>();
        AnnonceDAO annonceDAO = new AnnonceDAO(dbConn);
        String query = "SELECT " + COL_ID_CATEGORIE + ", " + COL_NOM_CATEGORIE + ", " + COL_COULEUR_CATEGORIE + " FROM " + TABLE_NAME;

        try {
            Statement stmt = dbConn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                CategorieDTO cat = new CategorieDTO();
                cat.setIdCAT(rs.getInt(COL_ID_CATEGORIE));
                cat.setNameCAT(rs.getString(COL_NOM_CATEGORIE));
                cat.setCouleurCAT(rs.getString(COL_COULEUR_CATEGORIE));
                cat.setNbAnnonceCAT(annonceDAO.getNbAnnonceByCategorie(cat.getIdCAT()));
                myList.add(cat);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getCompleteList", e);
        }
        return myList;
    }

    @Override
    public CategorieDTO get(int idCategorie) {
        CategorieDTO categorie = null;
        String query = "SELECT " + COL_ID_CATEGORIE + ", "
                + COL_NOM_CATEGORIE + ", "
                + COL_COULEUR_CATEGORIE
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_ID_CATEGORIE + " = ?";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, idCategorie);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                categorie = new CategorieDTO();
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

    @Override
    public boolean save(CategorieDTO item) {
        boolean insertStatus = false;

        String query = "INSERT INTO " + TABLE_NAME + " (" + COL_NOM_CATEGORIE + ", "
                + COL_COULEUR_CATEGORIE + ")"
                + " VALUES( ?, ?)";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, item.getNameCAT());
            stmt.setString(2, item.getCouleurCAT());

            if (stmt.executeUpdate() != 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                item.setIdCAT(rs.getInt(1));  // On met à jour l'ID de l'annonce qu'on vient de créer.
                insertStatus = true;
                rs.close();
            }
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "insert", e);
        }
        return insertStatus;
    }

    @Override
    public boolean update(CategorieDTO item) {
        return false;
    }

    @Override
    public boolean delete(int itemId) {
        boolean deleteStatus = false;
        int records;
        String query;
        try {
            Statement stmt = dbConn.createStatement();

            query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID_CATEGORIE + " = " + String.valueOf(itemId);

            records = stmt.executeUpdate(query);

            //When record is successfully deleted
            deleteStatus = records != 0;
            stmt.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "delete", e);
        }
        return deleteStatus;
    }

    public boolean deleteAll() {
        boolean deleteStatus = false;
        int records;
        String query;
        try {
            Statement stmt = dbConn.createStatement();
            query = "DELETE FROM " + TABLE_NAME;
            records = stmt.executeUpdate(query);
            deleteStatus = records != 0;
            stmt.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "deleteAll", e);
        }
        return deleteStatus;
    }
}
