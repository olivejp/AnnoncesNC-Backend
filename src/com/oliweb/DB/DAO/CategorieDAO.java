package com.oliweb.DB.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.oliweb.DB.DTO.CategorieDTO;


public class CategorieDAO {

    private Integer idCategorie;
    private String nomCategorie;
    private String couleurCategorie;
    private Integer versionCategorie;

    public static final String TABLE_NAME = "categorie";
    public static final String COL_ID_CATEGORIE = "idcategorie";
    public static final String COL_NOM_CATEGORIE = "nomCategorie";
    public static final String COL_COULEUR_CATEGORIE = "couleurCategorie";


    public Integer getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(Integer idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getCouleurCategorie() {
        return couleurCategorie;
    }

    public void setCouleurCategorie(String couleurCategorie) {
        this.couleurCategorie = couleurCategorie;
    }

    public Integer getVersionCategorie() {
        return versionCategorie;
    }

    public void setVersionCategorie(Integer versionCategorie) {
        this.versionCategorie = versionCategorie;
    }

    public static ArrayList<CategorieDTO> getCompleteList() {
        ArrayList<CategorieDTO> myList = new ArrayList<>();

        Connection dbConn = MyConnection.getInstance();
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
                query = null;
                for (CategorieDTO categorie : myList) {
                    query = "SELECT count(" + AnnonceDAO.COL_ID_ANNONCE + ") FROM " + AnnonceDAO.TABLE_NAME + " WHERE " + AnnonceDAO.COL_ID_CATEGORY + " = " + String.valueOf(categorie.getIdCAT());
                    rs2 = stmt.executeQuery(query);
                    if (rs2.next()) {
                        categorie.setNbAnnonceCAT(rs2.getInt(1));
                    }
                    rs2.close();
                }
                stmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myList;
    }

    public static CategorieDTO getById(Integer idCategorie) {
        Connection dbConn = MyConnection.getInstance();
        CategorieDTO categorie = new CategorieDTO();

        try {
            Statement stmt = (Statement) dbConn.createStatement();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categorie;
    }

    public static boolean existById(Integer id) {
        Connection dbConn = MyConnection.getInstance();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exist;
    }

    // @SuppressWarnings("unused")
    public static CategorieDTO getByName(String nameCategorie) throws Exception {
        Connection dbConn = MyConnection.getInstance();
        CategorieDTO categorie = new CategorieDTO();

        Statement stmt = (Statement) dbConn.createStatement();
        String query = "SELECT " + COL_ID_CATEGORIE + ", " + COL_NOM_CATEGORIE + " "
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_NOM_CATEGORIE + " ='" + nameCategorie + "'";
        //System.out.println(query);
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            categorie.setIdCAT(results.getInt(COL_ID_CATEGORIE));
            categorie.setNameCAT(results.getString(COL_NOM_CATEGORIE));
        }
        //System.out.println(records);
        stmt.close();
        return categorie;
    }
}
