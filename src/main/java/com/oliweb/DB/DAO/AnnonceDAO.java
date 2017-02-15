package com.oliweb.DB.DAO;

import com.oliweb.DB.Contract.PhotoContract;
import com.oliweb.DB.DTO.AnnonceDTO;
import com.oliweb.DB.utility.Properties;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.oliweb.DB.Contract.AnnonceContract.*;


public class AnnonceDAO {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Connection dbConn;

    public AnnonceDAO(Connection dbConn) {
        this.dbConn = dbConn;
    }

    @Deprecated
    public int getMaxId(Integer idCategorie, Integer idUtilisateur) {
        int retour = -1;
        String query = "SELECT MAX(" + COL_ID_ANNONCE + ") AS MAXID FROM " + TABLE_NAME + " WHERE "
                + COL_ID_CATEGORY + " = ? AND "
                + COL_ID_UTILISATEUR + " = ?";
        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, idCategorie);
            stmt.setInt(2, idUtilisateur);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                retour = rs.getInt("MAXID");
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getMaxId", e);
        }
        return retour;
    }

    @Deprecated
    public ArrayList<AnnonceDTO> searchByKeywordWithPage(String keyword, Integer page) {
        ArrayList<AnnonceDTO> myList = new ArrayList<>();

        Integer pagination = Integer.valueOf(Properties.getProperty(Properties.PAGINATION));

        String query = "SELECT " + COL_ID_ANNONCE + ", "
                + COL_TITRE_ANNONCE + ", "
                + COL_DESCRIPTION_ANNONCE + ", "
                + COL_DATE_PUBLICATION + ", "
                + COL_PRIX_ANNONCE + ", "
                + COL_ID_CATEGORY + ", "
                + COL_ID_UTILISATEUR + ""
                + " FROM " + TABLE_NAME
                + " WHERE (" + COL_DESCRIPTION_ANNONCE + " LIKE ?"
                + " OR " + COL_TITRE_ANNONCE + " LIKE ?"
                + " AND " + COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'"
                + " ORDER BY " + COL_DATE_PUBLICATION + " DESC"
                + " LIMIT " + String.valueOf((page - 1) * pagination) + "," + String.valueOf(pagination - 1);

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);

            // Le String keyword peut être composé de plusieurs mots. On va le découper (split) pour faire une recherche sur tous ces mots.
            String[] keys = keyword.split(" ");
            for (String key : keys) {
                String likeKey = "%" + key + "%";
                stmt.setString(1, likeKey);
                stmt.setString(2, likeKey);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    // Ajout de l'annonce dans la liste
                    myList.add(transfertAnnonce(rs));
                }
                rs.close();
            }

            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "searchByKeywordWithPage", e);
        }

        // On retourne la liste d'annonce que l'on vient de créer.
        return myList;
    }

    public ArrayList<AnnonceDTO> getByIdCategory(Integer idCategory) {
        ArrayList<AnnonceDTO> myList = new ArrayList<>();

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


    public AnnonceDTO getById(Integer idAnnonce) {

        AnnonceDTO retour = null;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT " + COL_ID_ANNONCE + ", "
                    + COL_TITRE_ANNONCE + ", "
                    + COL_DESCRIPTION_ANNONCE + ", "
                    + COL_DATE_PUBLICATION + ", "
                    + COL_PRIX_ANNONCE + ", "
                    + COL_ID_CATEGORY + ", "
                    + COL_ID_UTILISATEUR
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                retour = transfertAnnonce(rs);
            }

            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getById", e);
        }
        return retour;
    }

    public ArrayList<AnnonceDTO> getByIdUser(Integer idUser, Integer page) {
        ArrayList<AnnonceDTO> myList = new ArrayList<>();
        Integer pagination = Integer.valueOf(Properties.getProperty(Properties.PAGINATION));
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT * "
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUser)
                    + " ORDER BY " + COL_DATE_PUBLICATION + " DESC"
                    + " LIMIT " + String.valueOf((page - 1) * pagination) + "," + String.valueOf(pagination - 1);
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);

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

    public ArrayList<AnnonceDTO> getByIdCategoryWithPage(Integer idCategory, Integer page) {
        ArrayList<AnnonceDTO> myList = new ArrayList<>();
        Integer pagination = Integer.valueOf(Properties.getProperty(Properties.PAGINATION));
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT * "
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_CATEGORY + " = " + String.valueOf(idCategory)
                    + " AND " + COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'"
                    + " ORDER BY " + COL_DATE_PUBLICATION + " DESC"
                    + " LIMIT " + String.valueOf((page - 1) * pagination) + "," + String.valueOf(pagination - 1);
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
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

    public ArrayList<AnnonceDTO> getListAnnonce(Integer page) {
        ArrayList<AnnonceDTO> myList = new ArrayList<>();
        Integer pagination = Integer.valueOf(Properties.getProperty(Properties.PAGINATION));
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT * "
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'"
                    + " ORDER BY " + COL_DATE_PUBLICATION + " DESC"
                    + " LIMIT " + String.valueOf((page - 1) * pagination) + "," + String.valueOf(pagination - 1);
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);

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
            annonce.setCategorieANO(categorieDAO.getById(rs.getInt(COL_ID_CATEGORY)));
            annonce.setOwnerANO(utilisateurDAO.getById(rs.getInt(COL_ID_UTILISATEUR)));
            annonce.setPhotos(photoDAO.getByIdAnnonce(annonce.getIdANO()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "transfertAnnonce", e);
        }
        return annonce;
    }


    public boolean update(AnnonceDTO annonce) {
        boolean insertStatus = false;
        int records;
        String query;
        try {
            Statement stmt = dbConn.createStatement();
            query = "UPDATE " + TABLE_NAME + " SET "
                    + COL_TITRE_ANNONCE + " = '" + annonce.getTitreANO() + "', "
                    + COL_DESCRIPTION_ANNONCE + " = '" + annonce.getDescriptionANO() + "', "
                    + COL_PRIX_ANNONCE + " = " + String.valueOf(annonce.getPriceANO()) + ", "
                    + COL_ID_CATEGORY + " = " + String.valueOf(annonce.getCategorieANO().getIdCAT()) + ", "
                    + COL_DATE_PUBLICATION + " = CURRENT_TIME()"
                    + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(annonce.getIdANO());
            records = stmt.executeUpdate(query);

            // When record is successfully inserted
            insertStatus = records != 0;
            stmt.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "update", e);
        }
        return insertStatus;
    }

    public boolean insert(AnnonceDTO annonce) {
        boolean insertStatus = false;
        int newId;
        String query;
        try {
            query = "INSERT INTO " + TABLE_NAME + " (" + COL_TITRE_ANNONCE + ", "
                    + COL_DESCRIPTION_ANNONCE + ", "
                    + COL_PRIX_ANNONCE + ", "
                    + COL_ID_UTILISATEUR + ", "
                    + COL_ID_CATEGORY + ", "
                    + COL_DATE_PUBLICATION + ", "
                    + COL_STATUT_ANNONCE + ")"
                    + " values('" +
                    annonce.getTitreANO() + "','" +
                    annonce.getDescriptionANO() + "'," +
                    annonce.getPriceANO() + "," +
                    annonce.getOwnerANO().getIdUTI() + "," +
                    annonce.getCategorieANO().getIdCAT() + "," +
                    "CURRENT_TIME(), '" +
                    enumStatutAnnonce.VALID.valeur() + "')";

            PreparedStatement stmt = dbConn.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);

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

    public boolean exist(Integer idAnnonce) {
        boolean existStatus = false;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " =" + String.valueOf(idAnnonce);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    existStatus = true;
                }
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "exist", e);
        }
        return existStatus;
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

    public boolean delete(Integer idAnnonce) {
        boolean deleteStatus = false;
        int records;
        String query;
        try {
            Statement stmt = dbConn.createStatement();

            // Suppression de toutes les photos attachées
            PhotoDAO photoDAO = new PhotoDAO(dbConn);
            photoDAO.deleteByIdAnnonce(idAnnonce);

            query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);

            records = stmt.executeUpdate(query);

            //When record is successfully deleted
            deleteStatus = records != 0;
            stmt.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "delete", e);
        }
        return deleteStatus;
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

    public ArrayList<AnnonceDTO> getMultiParam(Integer idCat, String keyword, Integer minPrice, Integer maxPrice, boolean photo, Integer page) {
        ArrayList<AnnonceDTO> myList = new ArrayList<>();
        ArrayList<String> conditions = new ArrayList<>();
        Integer pagination = Integer.valueOf(Properties.getProperty(Properties.PAGINATION));
        Integer id_all_categorie = Integer.valueOf(Properties.getProperty(Properties.ID_ALL_CATEGORIE));
        // On va récupérer toutes les annonces pour la catégorie demandée
        try {
            Statement stmt = dbConn.createStatement();
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
            if (!Objects.equals(idCat, id_all_categorie)) {
                conditions.add(COL_ID_CATEGORY + " = " + String.valueOf(idCat));
            }

            // Si on a renseigné des mots clef, on va rechercher sur ces mots clef
            if (!keyword.isEmpty()) {
                String keyword_concat = "(";
                String[] keys = keyword.split(" ");
                for (int i = 0; i < keys.length; i++) {
                    if (i < keys.length - 1) {
                        keyword_concat = keyword_concat + COL_DESCRIPTION_ANNONCE + " LIKE '%" + keys[i] + "%' OR " + COL_TITRE_ANNONCE + " LIKE '%" + keys[i] + "%' OR ";
                    } else {
                        keyword_concat = keyword_concat + COL_DESCRIPTION_ANNONCE + " LIKE '%" + keys[i] + "%' OR " + COL_TITRE_ANNONCE + " LIKE '%" + keys[i] + "%' ";
                    }
                }
                keyword_concat = keyword_concat + ")";
                conditions.add(keyword_concat);
            }

            // Si on a renseigné une fourchette de prix
            if (minPrice != 0 && maxPrice != 0) {
                conditions.add(COL_PRIX_ANNONCE + " BETWEEN " + String.valueOf(minPrice) + " AND " + String.valueOf(maxPrice));
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
                if (i < conditions.size() - 1) {
                    conditions_concat = conditions_concat + cond + " AND ";
                } else {
                    conditions_concat = conditions_concat + cond;
                }
                i++;
            }

            String order = " ORDER BY " + COL_DATE_PUBLICATION + " DESC";
            String limit = " LIMIT " + String.valueOf((page - 1) * pagination) + "," + String.valueOf(pagination - 1);
            String query = select + where + conditions_concat + order + limit;

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
            LOGGER.log(Level.SEVERE, "getNbAnnonce", e);
        }
        return nb_annonce;
    }
}
