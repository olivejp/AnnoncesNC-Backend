package com.oliweb.db.dao;

import com.oliweb.db.contract.CategorieContract;
import com.oliweb.db.dto.CategorieDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

    public boolean existById(Integer id) {
        boolean exist = false;
        String query = "SELECT COUNT(*) FROM " + CategorieContract.TABLE_NAME
                + " WHERE " + CategorieContract.COL_ID_CATEGORIE + " = ?";
        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) >= 1) {
                exist = true;
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "existById", e);
        }
        return exist;
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
            ResultSet rs = stmt.executeQuery(query);
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
        return false;
    }

    @Override
    public boolean update(CategorieDTO item) {
        return false;
    }

    @Override
    public boolean delete(int itemId) {
        return false;
    }
}
