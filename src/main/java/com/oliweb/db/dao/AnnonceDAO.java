package com.oliweb.db.dao;

import com.oliweb.db.contract.AnnonceContract;
import com.oliweb.db.contract.PhotoContract;
import com.oliweb.db.dto.AnnonceDTO;
import com.oliweb.utility.Proprietes;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static com.oliweb.db.contract.AnnonceContract.*;


public class AnnonceDAO extends AbstractDAO<AnnonceDTO> {

    public AnnonceDAO(Connection dbConn) {
        super(dbConn);
    }

    public List<AnnonceDTO> getByIdCategory(Integer idCategory) {
        List<AnnonceDTO> myList = new ArrayList<>();

        String query = "SELECT * "
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_ID_CATEGORY + " = ?"
                + " AND " + COL_STATUT_ANNONCE + " = ?"
                + " ORDER BY " + COL_DATE_PUBLICATION + " DESC";

        try {
            PreparedStatement pstmt = dbConn.prepareStatement(query);
            pstmt.setInt(1, idCategory);
            pstmt.setString(2, enumStatutAnnonce.VALID.valeur());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                myList.add(transfertAnnonce(rs));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getByIdCategory", e);
        }
        return myList;
    }

    public List<AnnonceDTO> getByIdUser(Integer idUser, Integer page) {
        List<AnnonceDTO> myList = new ArrayList<>();
        Integer pagination = Integer.valueOf(Proprietes.getProperty(Proprietes.PAGINATION));

        String query = "SELECT * "
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_ID_UTILISATEUR + " = ?"
                + " ORDER BY " + COL_DATE_PUBLICATION + " DESC"
                + " LIMIT ? , ?";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, idUser);
            stmt.setInt(2, (page - 1) * pagination);
            stmt.setInt(3, pagination - 1);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                myList.add(transfertAnnonce(rs));
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getByIdUser", e);
        }
        return myList;
    }

    public List<AnnonceDTO> getByIdCategoryWithPage(Integer idCategory, Integer page) {
        List<AnnonceDTO> myList = new ArrayList<>();
        Integer pagination = Integer.valueOf(Proprietes.getProperty(Proprietes.PAGINATION));

        String query = "SELECT * "
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_ID_CATEGORY + " = ? "
                + " AND " + COL_STATUT_ANNONCE + " = ? "
                + " ORDER BY " + COL_DATE_PUBLICATION + " DESC"
                + " LIMIT ?,?";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, idCategory);
            stmt.setString(2, enumStatutAnnonce.VALID.valeur());
            stmt.setInt(3, (page - 1) * pagination);
            stmt.setInt(4, pagination - 1);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                myList.add(transfertAnnonce(rs));
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getByIdCategoryWithPage", e);
        }
        return myList;
    }

    public List<AnnonceDTO> getListAnnonce(Integer page) {
        List<AnnonceDTO> myList = new ArrayList<>();
        Integer pagination = Integer.valueOf(Proprietes.getProperty(Proprietes.PAGINATION));

        String query = "SELECT * "
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_STATUT_ANNONCE + " = ? "
                + " ORDER BY " + COL_DATE_PUBLICATION + " DESC"
                + " LIMIT ?, ?";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setString(1, enumStatutAnnonce.VALID.valeur());
            stmt.setInt(2, (page - 1) * pagination);
            stmt.setInt(3, pagination - 1);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                myList.add(transfertAnnonce(rs));
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getListAnnonce", e);
        }
        return myList;
    }

    public Integer getNbAnnonceByCategorie(Integer idCategorie) {
        String query = "SELECT count(" + AnnonceContract.COL_ID_ANNONCE +
                ") FROM " + AnnonceContract.TABLE_NAME +
                " WHERE " + AnnonceContract.COL_ID_CATEGORY + " = " + String.valueOf(idCategorie) +
                " AND " + AnnonceContract.COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'";
        Integer retour = 0;
        try {
            Statement stmt = dbConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                retour = rs.getInt(1);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return retour;
    }

    private AnnonceDTO transfertAnnonce(ResultSet rs) {
        // Création d'une nouvelle annonce
        AnnonceDTO annonce = new AnnonceDTO();

        // Renseignement des champs de l'annonce
        try {
            annonce.setIdANO(rs.getInt(COL_ID_ANNONCE));
            annonce.setTitreANO(rs.getString(COL_TITRE_ANNONCE));
            annonce.setDescriptionANO(rs.getString(COL_DESCRIPTION_ANNONCE));

            // On formate la date Timestamp en String
            String dateAsText = new SimpleDateFormat("yyyyMMddHHmm").format(rs.getTimestamp(COL_DATE_PUBLICATION));

            CategorieDAO categorieDAO = new CategorieDAO(dbConn);
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(dbConn);
            PhotoDAO photoDAO = new PhotoDAO(dbConn);

            annonce.setDatePublished(Long.valueOf(dateAsText));
            annonce.setPriceANO(rs.getInt(COL_PRIX_ANNONCE));
            annonce.setCategorieANO(categorieDAO.get(rs.getInt(COL_ID_CATEGORY)));
            annonce.setUtilisateurANO(utilisateurDAO.get(rs.getInt(COL_ID_UTILISATEUR)));
            annonce.setPhotos(photoDAO.getByIdAnnonce(annonce.getIdANO()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "transfertAnnonce", e);
        }
        return annonce;
    }

    boolean deleteByIdUser(Integer idUser) {
        boolean deleteStatus = false;
        int records;
        String delete;
        String select;
        try {
            Statement stmt = dbConn.createStatement();

            // On va supprimer toutes les photos attachées aux annonces de cet utilisateur
            select = "SELECT " + COL_ID_ANNONCE + " FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUser);
            ResultSet rs = stmt.executeQuery(select);
            while (rs.next()) {
                PhotoDAO photoDAO = new PhotoDAO(dbConn);
                photoDAO.deleteByIdAnnonce(rs.getInt(COL_ID_ANNONCE));
            }
            // On finit par supprimer l'annonce elle m�me
            delete = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUser);
            records = stmt.executeUpdate(delete);
            deleteStatus = records != 0;
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "deleteByIdUser", e);
        }
        return deleteStatus;
    }

    boolean devalideByIdUser(Integer idUser) {

        String NB_LIGNES = "NB_LIGNES";
        boolean updateStatus = false;
        int records, nb_lignes;
        String update, query;

        try {
            Statement stmt = dbConn.createStatement();
            query = "SELECT COUNT(*) AS " + NB_LIGNES + " FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + "=" + String.valueOf(idUser);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                nb_lignes = rs.getInt(NB_LIGNES);
                if (nb_lignes > 0) {
                    // On dévalide toutes les annonces de cet utilisateur
                    update = "UPDATE " + TABLE_NAME + " SET " + COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.UNREGISTRED.valeur() + "' WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUser);
                    records = stmt.executeUpdate(update);

                    updateStatus = records != 0;
                } else {
                    updateStatus = true;
                }
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "devalideByIdUser", e);
        }
        return updateStatus;
    }

    public Integer numberAnnonceByIdUser(Integer idUser) {
        Integer retour = 0;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                retour = rs.getInt(1);
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "numberAnnonceByIdUser", e);
        }
        return retour;
    }

    public void addKeywordCondition(String keywords, List<String> conditions) {
        if (!keywords.isEmpty()) {
            String keyword_concat = "(";
            String[] keys = keywords.split(" ");
            for (int i = 0; i < keys.length; i++) {
                keyword_concat = keyword_concat.concat(COL_DESCRIPTION_ANNONCE).concat(" LIKE '%").concat(keys[i]).concat("%' OR ");
                keyword_concat = keyword_concat.concat(COL_TITRE_ANNONCE).concat(" LIKE '%").concat(keys[i]).concat("%'");
                keyword_concat = (i < keys.length - 1) ? keyword_concat.concat(" OR ") : keyword_concat;
            }
            keyword_concat = keyword_concat + ")";
            conditions.add(keyword_concat);
        }
    }

    public List<AnnonceDTO> getMultiParam(Integer idCat, String keyword, Integer minPrice, Integer maxPrice, boolean photo, Integer page) {
        List<AnnonceDTO> myList = new ArrayList<>();
        List<String> conditions = new ArrayList<>();
        Integer pagination = Integer.valueOf(Proprietes.getProperty(Proprietes.PAGINATION));
        Integer id_all_categorie = Integer.valueOf(Proprietes.getProperty(Proprietes.ID_ALL_CATEGORIE));

        // On va récupérer toutes les annonces pour la catégorie demandée
        String select = "SELECT " + COL_ID_ANNONCE + ", " +
                COL_TITRE_ANNONCE + ", " +
                COL_DESCRIPTION_ANNONCE + ", " +
                COL_DATE_PUBLICATION + ", " +
                COL_PRIX_ANNONCE + ", " +
                COL_ID_CATEGORY + ", " +
                COL_ID_UTILISATEUR +
                " FROM " + TABLE_NAME;

        String where = " WHERE ";

        // On ne prend que les annonces valides
        conditions.add(COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'");

        // Si on a renseigné une catégorie, on va rechercher sur cette catégorie
        if (idCat != null) {
            if (!Objects.equals(idCat, id_all_categorie)) {
                conditions.add(COL_ID_CATEGORY + " = " + String.valueOf(idCat));
            }
        }

        // Si on a renseigné des mots clef, on va rechercher sur ces mots clef
        if (keyword != null) {
            addKeywordCondition(keyword, conditions);
        }

        // Si on a renseigné une fourchette de prix
        if (minPrice != null && maxPrice != null) {
            if (minPrice != 0 && maxPrice != 0) {
                conditions.add(COL_PRIX_ANNONCE + " BETWEEN " + String.valueOf(minPrice) + " AND " + String.valueOf(maxPrice));
            }
        }

        // Si on en veut uniquement les annonces avec photo
        if (photo) {
            conditions.add(COL_ID_ANNONCE + " IN (SELECT " + PhotoContract.COL_ID_ANNONCE + " FROM " + PhotoContract.TABLE_NAME + ")");
        }

        // Concaténation de toutes les conditions
        int i = 0;
        String cond;
        String conditions_concat = "";
        while (i < conditions.size()) {
            cond = conditions.get(i);
            conditions_concat = (i < conditions.size() - 1) ? conditions_concat + cond + " AND " : conditions_concat + cond;
            i++;
        }

        String order = " ORDER BY " + COL_DATE_PUBLICATION + " DESC";
        String limit = " LIMIT " + String.valueOf((page - 1) * pagination) + "," + String.valueOf(pagination - 1);
        String query = select + where + conditions_concat + order + limit;

        try {
            Statement stmt = dbConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                myList.add(transfertAnnonce(rs));
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getMultiParam", e);
        }
        return myList;
    }

    public Integer getNbAnnonce() {
        Statement stmt;
        ResultSet results;
        String query;
        Integer nb_annonce = 0;

        try {
            stmt = dbConn.createStatement();
            query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'";

            results = stmt.executeQuery(query);
            if (results.next()) {
                nb_annonce = results.getInt(1);
            }
            stmt.close();
            results.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getCount", e);
        }
        return nb_annonce;
    }

    @Override
    public boolean update(AnnonceDTO annonce) {
        boolean insertStatus = false;
        int records;
        String query = "UPDATE " + TABLE_NAME + " SET "
                + COL_TITRE_ANNONCE + " = ?, "
                + COL_DESCRIPTION_ANNONCE + " = ?, "
                + COL_PRIX_ANNONCE + " = ?, "
                + COL_ID_CATEGORY + " = ?, "
                + COL_DATE_PUBLICATION + " = ? "
                + " WHERE " + COL_ID_ANNONCE + " = ?";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setString(1, annonce.getTitreANO());
            stmt.setString(2, annonce.getDescriptionANO());
            stmt.setInt(3, annonce.getPriceANO());
            stmt.setInt(4, annonce.getCategorieANO().getIdCAT());
            stmt.setString(5, "CURRENT_TIME()");
            stmt.setInt(6, annonce.getIdANO());
            records = stmt.executeUpdate();

            // When record is successfully inserted
            insertStatus = records != 0;
            stmt.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "update", e);
        }
        return insertStatus;
    }

    @Override
    public boolean save(AnnonceDTO annonce) {
        boolean insertStatus = false;
        int newId;

        String query = "INSERT INTO " + TABLE_NAME + " (" + COL_TITRE_ANNONCE + ", "
                + COL_DESCRIPTION_ANNONCE + ", "
                + COL_PRIX_ANNONCE + ", "
                + COL_ID_UTILISATEUR + ", "
                + COL_ID_CATEGORY + ", "
                + COL_DATE_PUBLICATION + ", "
                + COL_STATUT_ANNONCE + ")"
                + " VALUES( ?, ?, ?, ?, ?, CURRENT_TIME(), ?)";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, annonce.getTitreANO());
            stmt.setString(2, annonce.getDescriptionANO());
            stmt.setInt(3, annonce.getPriceANO());
            stmt.setInt(4, annonce.getUtilisateurANO().getIdUTI());
            stmt.setInt(5, annonce.getCategorieANO().getIdCAT());
            stmt.setString(6, enumStatutAnnonce.VALID.valeur());

            if (stmt.executeUpdate() != 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                newId = rs.getInt(1);
                insertStatus = true;
                // On met à jour l'ID de l'annonce qu'on vient de créer.
                annonce.setIdANO(newId);
                rs.close();
            } else {
                insertStatus = false;
            }
            stmt.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "insert", e);
        }
        return insertStatus;
    }

    @Override
    public boolean delete(int itemId) {
        boolean deleteStatus = false;
        int records;
        String query;
        try {
            Statement stmt = dbConn.createStatement();

            // Suppression de toutes les photos attachées
            PhotoDAO photoDAO = new PhotoDAO(dbConn);
            photoDAO.deleteByIdAnnonce(itemId);

            query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(itemId);

            records = stmt.executeUpdate(query);

            //When record is successfully deleted
            deleteStatus = records != 0;
            stmt.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "delete", e);
        }
        return deleteStatus;
    }

    @Override
    public AnnonceDTO get(int idAnnonce) {
        AnnonceDTO annonceDTO = null;
        String query = "SELECT " + COL_ID_ANNONCE + ", "
                + COL_TITRE_ANNONCE + ", "
                + COL_DESCRIPTION_ANNONCE + ", "
                + COL_DATE_PUBLICATION + ", "
                + COL_PRIX_ANNONCE + ", "
                + COL_ID_CATEGORY + ", "
                + COL_ID_UTILISATEUR
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_ID_ANNONCE + " = ?";
        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, idAnnonce);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                annonceDTO = transfertAnnonce(rs);
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getById", e);
        }
        return annonceDTO;
    }
}
