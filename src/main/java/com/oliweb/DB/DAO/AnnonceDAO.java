package com.oliweb.DB.DAO;

import com.oliweb.DB.DTO.AnnonceDTO;
import com.oliweb.DB.utility.ConstantsDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class AnnonceDAO {

    private Integer idannonce;
    private Integer utilisateur_idutilisateur;
    private Integer categorie_idcategorie;
    private String titreAnnonce;
    private String descriptionAnnonce;
    private Timestamp datePublicationAnnonce;
    private Integer prixAnnonce;

    public static final String TABLE_NAME = "annonce";
    public static final String COL_ID_ANNONCE = "idannonce";
    public static final String COL_ID_UTILISATEUR = "utilisateur_idutilisateur";
    public static final String COL_ID_CATEGORY = "categorie_idcategorie";
    public static final String COL_TITRE_ANNONCE = "titreAnnonce";
    public static final String COL_DESCRIPTION_ANNONCE = "descriptionAnnonce";
    public static final String COL_DATE_PUBLICATION = "datePublicationAnnonce";
    public static final String COL_PRIX_ANNONCE = "prixAnnonce";
    public static final String COL_STATUT_ANNONCE = "statutAnnonce";


    @Deprecated
    public static ArrayList<AnnonceDTO> searchByKeyword(String keyword) throws Exception {
        Connection dbConn = MyConnection.getInstance();
        ArrayList<AnnonceDTO> myList = new ArrayList<AnnonceDTO>();
        Statement stmt = dbConn.createStatement();

        // Le String keyword peut �tre compos� de plusieurs mots. On va le d�couper (split) pour faire une recherche sur tous ces mots.
        String[] keys = keyword.split(" ");
        for (int i = 0; i < keys.length; i++) {
            String query = "SELECT " + COL_ID_ANNONCE + ", "
                    + COL_TITRE_ANNONCE + ", "
                    + COL_DESCRIPTION_ANNONCE + ", "
                    + COL_DATE_PUBLICATION + ", "
                    + COL_PRIX_ANNONCE + ", "
                    + COL_ID_CATEGORY + ", "
                    + COL_ID_UTILISATEUR + ""
                    + " FROM " + TABLE_NAME
                    + " WHERE (" + COL_DESCRIPTION_ANNONCE + " LIKE '%" + keys[i] + "%'"
                    + " OR " + COL_TITRE_ANNONCE + " LIKE '%" + keys[i] + "%') "
                    + " AND " + COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'"
                    + " ORDER BY " + COL_DATE_PUBLICATION + " DESC";
            ResultSet rs = stmt.executeQuery(query);

//			logger.info("searchByKeyword : Query=" + query);

            while (rs.next()) {
                // Ajout de l'annonce dans la liste
                myList.add(transfertAnnonce(rs));
            }
        }

        stmt.close();

        // On retourne la liste d'annonce que l'on vient de cr�er.
        return myList;
    }

    @Deprecated
    public static ArrayList<AnnonceDAO> listByIdCategory(Integer idcategorie) throws Exception {
        Connection dbConn = MyConnection.getInstance();
        ArrayList<AnnonceDAO> myList = new ArrayList<AnnonceDAO>();

        // On va récupérer toutes les annonces pour la catégorie demandée
        Statement stmt = dbConn.createStatement();
        String query = "SELECT " + COL_ID_ANNONCE + ", " + COL_TITRE_ANNONCE + ", " + COL_DESCRIPTION_ANNONCE + ", " + COL_DATE_PUBLICATION + ", " + COL_PRIX_ANNONCE + ", " + COL_ID_CATEGORY + ", " + COL_ID_UTILISATEUR + ""
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_ID_CATEGORY + " = " + String.valueOf(idcategorie)
                + " AND " + COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'"
                + " ORDER BY " + COL_DATE_PUBLICATION + " DESC";

//		logger.info("listByIdCategory : Query=" + query);

        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            // Création d'une nouvelle annonce
            AnnonceDAO annonceDAO = new AnnonceDAO();

            // Renseignement des champs de l'annonce
            annonceDAO.setIdannonce(rs.getInt(COL_ID_ANNONCE));
            annonceDAO.setTitreAnnonce(rs.getString(COL_TITRE_ANNONCE));
            annonceDAO.setDescriptionAnnonce(rs.getString(COL_DESCRIPTION_ANNONCE));
            annonceDAO.setDatePublicationAnnonce(rs.getTimestamp(COL_DATE_PUBLICATION));
            annonceDAO.setPrixAnnonce(rs.getInt(COL_PRIX_ANNONCE));
            annonceDAO.setCategorie_idcategorie(rs.getInt(COL_ID_CATEGORY));
            annonceDAO.setUtilisateur_idutilisateur(rs.getInt(COL_ID_UTILISATEUR));

            // Ajout de l'annonce dans la liste
            myList.add(annonceDAO);
        }

        stmt.close();

        // On retourne la liste d'annonce que l'on vient de créer.
        return myList;
    }

    public Integer getIdannonce() {
        return idannonce;
    }

    public void setIdannonce(Integer idannonce) {
        this.idannonce = idannonce;
    }

    public Integer getUtilisateur_idutilisateur() {
        return utilisateur_idutilisateur;
    }

    public void setUtilisateur_idutilisateur(Integer utilisateur_idutilisateur) {
        this.utilisateur_idutilisateur = utilisateur_idutilisateur;
    }

    public Integer getCategorie_idcategorie() {
        return categorie_idcategorie;
    }

    public void setCategorie_idcategorie(Integer categorie_idcategorie) {
        this.categorie_idcategorie = categorie_idcategorie;
    }

    public String getTitreAnnonce() {
        return titreAnnonce;
    }

    public void setTitreAnnonce(String titreAnnonce) {
        this.titreAnnonce = titreAnnonce;
    }

    public String getDescriptionAnnonce() {
        return descriptionAnnonce;
    }

    public void setDescriptionAnnonce(String descriptionAnnonce) {
        this.descriptionAnnonce = descriptionAnnonce;
    }

    public Timestamp getDatePublicationAnnonce() {
        return datePublicationAnnonce;
    }

    public void setDatePublicationAnnonce(Timestamp datePublicationAnnonce) {
        this.datePublicationAnnonce = datePublicationAnnonce;
    }

    public Integer getPrixAnnonce() {
        return prixAnnonce;
    }

    public void setPrixAnnonce(Integer prixAnnonce) {
        this.prixAnnonce = prixAnnonce;
    }

    public static AnnonceDTO getById(Integer idAnnonce) {
        Connection dbConn = MyConnection.getInstance();
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
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return retour;
    }

    public static ArrayList<AnnonceDTO> searchByKeywordWithPage(String keyword, Integer page) throws Exception {
        Connection dbConn = MyConnection.getInstance();
        ArrayList<AnnonceDTO> myList = new ArrayList<>();
        Statement stmt = dbConn.createStatement();

        // Le String keyword peut être composé de plusieurs mots. On va le découper (split) pour faire une recherche sur tous ces mots.
        String[] keys = keyword.split(" ");
        for (int i = 0; i < keys.length; i++) {
            String query = "SELECT " + COL_ID_ANNONCE + ", "
                    + COL_TITRE_ANNONCE + ", "
                    + COL_DESCRIPTION_ANNONCE + ", "
                    + COL_DATE_PUBLICATION + ", "
                    + COL_PRIX_ANNONCE + ", "
                    + COL_ID_CATEGORY + ", "
                    + COL_ID_UTILISATEUR + ""
                    + " FROM " + TABLE_NAME
                    + " WHERE (" + COL_DESCRIPTION_ANNONCE + " LIKE '%" + keys[i] + "%'"
                    + " OR " + COL_TITRE_ANNONCE + " LIKE '%" + keys[i] + "%') "
                    + " AND " + COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'"
                    + " ORDER BY " + COL_DATE_PUBLICATION + " DESC"
                    + " LIMIT " + String.valueOf((page - 1) * ConstantsDB.PAGINATION) + "," + String.valueOf(ConstantsDB.PAGINATION - 1);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                // Ajout de l'annonce dans la liste
                myList.add(transfertAnnonce(rs));
            }
        }

        stmt.close();

        // On retourne la liste d'annonce que l'on vient de créer.
        return myList;
    }

    public static ArrayList<AnnonceDTO> getByIdCategory(Integer idCategory) {
        Connection dbConn = MyConnection.getInstance();
        ArrayList<AnnonceDTO> myList = new ArrayList<>();

        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT * "
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_CATEGORY + " = " + String.valueOf(idCategory)
                    + " AND " + COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'"
                    + " ORDER BY " + COL_DATE_PUBLICATION + " DESC";
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                // Ajout de l'annonce dans la liste
                myList.add(transfertAnnonce(rs));
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // On retourne la liste d'annonce que l'on vient de créer.
        return myList;
    }

    public static ArrayList<AnnonceDTO> getByIdUser(Integer idUser, Integer page) {
        Connection dbConn = MyConnection.getInstance();
        ArrayList<AnnonceDTO> myList = new ArrayList<>();

        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT * "
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUser)
                    + " ORDER BY " + COL_DATE_PUBLICATION + " DESC"
                    + " LIMIT " + String.valueOf((page - 1) * ConstantsDB.PAGINATION) + "," + String.valueOf(ConstantsDB.PAGINATION - 1);
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                myList.add(transfertAnnonce(rs));
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myList;
    }

    public static ArrayList<AnnonceDTO> getByIdCategoryWithPage(Integer idCategory, Integer page) {
        Connection dbConn = MyConnection.getInstance();
        ArrayList<AnnonceDTO> myList = new ArrayList<>();

        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT * "
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_CATEGORY + " = " + String.valueOf(idCategory)
                    + " AND " + COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'"
                    + " ORDER BY " + COL_DATE_PUBLICATION + " DESC"
                    + " LIMIT " + String.valueOf((page - 1) * ConstantsDB.PAGINATION) + "," + String.valueOf(ConstantsDB.PAGINATION - 1);
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                myList.add(transfertAnnonce(rs));
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myList;
    }

    public static ArrayList<AnnonceDTO> getListAnnonce(Integer page) {
        Connection dbConn = MyConnection.getInstance();
        ArrayList<AnnonceDTO> myList = new ArrayList<>();

        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT * "
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_STATUT_ANNONCE + " = '" + enumStatutAnnonce.VALID.valeur() + "'"
                    + " ORDER BY " + COL_DATE_PUBLICATION + " DESC"
                    + " LIMIT " + String.valueOf((page - 1) * ConstantsDB.PAGINATION) + "," + String.valueOf(ConstantsDB.PAGINATION - 1);
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                myList.add(transfertAnnonce(rs));
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return myList;
    }

    public static AnnonceDTO transfertAnnonce(ResultSet rs) {
        // Création d'une nouvelle annonce
        AnnonceDTO annonce = new AnnonceDTO();

        // Renseignement des champs de l'annonce
        try {
            annonce.setIdANO(rs.getInt(COL_ID_ANNONCE));
            annonce.setTitreANO(rs.getString(COL_TITRE_ANNONCE));
            annonce.setDescriptionANO(rs.getString(COL_DESCRIPTION_ANNONCE));

            // On formate la date Timestamp en String
            String dateAsText = new SimpleDateFormat("yyyyMMddHHmm").format(rs.getTimestamp(COL_DATE_PUBLICATION));

            annonce.setDatePublished(Long.valueOf(dateAsText));
            annonce.setPriceANO(rs.getInt(COL_PRIX_ANNONCE));
            annonce.setCategorieANO(CategorieDAO.getById(rs.getInt(COL_ID_CATEGORY)));
            annonce.setOwnerANO(UtilisateurDAO.getById(rs.getInt(COL_ID_UTILISATEUR)));
            annonce.setPhotos(PhotoDAO.getByIdAnnonce(annonce.getIdANO()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return annonce;
    }

    public static int getMaxId(Integer idCategorie, Integer idUtilisateur) {
        Connection dbConn = MyConnection.getInstance();
        String query;
        int retour = -1;

        try {
            Statement stmt = dbConn.createStatement();

            query = "Select MAX(" + COL_ID_ANNONCE + ") as MAXID from " + TABLE_NAME + " WHERE "
                    + COL_ID_CATEGORY + " = " + idCategorie + " AND "
                    + COL_ID_UTILISATEUR + " = " + idUtilisateur;
            ResultSet rs;

            rs = stmt.executeQuery(query);
            if (rs.next()) {
                retour = rs.getInt("MAXID");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour;
    }

    public static boolean update(AnnonceDTO annonce) {
        Connection dbConn = MyConnection.getInstance();
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
            System.out.println(query);
            records = stmt.executeUpdate(query);

            // When record is successfully inserted
            if (records != 0) {
                insertStatus = true;
                TransactionDAO.insert(dbConn, query); // On insére la transaction
            } else {
                insertStatus = false;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insertStatus;
    }

    public static boolean insert(AnnonceDTO annonce) {
        Connection dbConn = MyConnection.getInstance();
        boolean insertStatus = false;
        int records;
        String query;
        try {
            Statement stmt = dbConn.createStatement();
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

            records = stmt.executeUpdate(query);

            // When record is successfully inserted
            if (records != 0) {
                insertStatus = true;
                // On met à jour l'ID de l'annonce qu'on vient de créer.
                annonce.setIdANO(getMaxId(annonce.getCategorieANO().getIdCAT(), annonce.getOwnerANO().getIdUTI()));

                TransactionDAO.insert(dbConn, query); // On insère la transaction
            } else {
                insertStatus = false;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insertStatus;
    }

    public static boolean exist(Integer idAnnonce) {
        Connection dbConn = MyConnection.getInstance();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existStatus;
    }

    public static boolean deleteByIdUser(Integer idUser) {
        Connection dbConn = MyConnection.getInstance();
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
                PhotoDAO.deleteByIdAnnonce(rs.getInt(COL_ID_ANNONCE));
            }

            // On finit par supprimer l'annonce elle m�me
            delete = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUser);

//		logger.info("deleteByIdUser : Query=" + delete);

            records = stmt.executeUpdate(delete);

            if (records != 0) {
                deleteStatus = true;
                TransactionDAO.insert(dbConn, delete); // On ins�re la transaction
            } else {
                deleteStatus = false;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deleteStatus;
    }

    public static boolean devalideByIdUser(Integer idUser) {
        Connection dbConn = MyConnection.getInstance();
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

                    if (records != 0) {
                        updateStatus = true;
                        TransactionDAO.insert(dbConn, update); // On ins�re la transaction
                    } else {
                        updateStatus = false;
                    }
                } else {
                    updateStatus = true;
                }
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateStatus;
    }

    public static boolean delete(Integer idAnnonce) {
        Connection dbConn = MyConnection.getInstance();
        boolean deleteStatus = false;
        int records;
        String query;

        try {
            Statement stmt = dbConn.createStatement();

            // Suppression de toutes les photos attachées
            PhotoDAO.deleteByIdAnnonce(idAnnonce);

            query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID_ANNONCE + " = " + String.valueOf(idAnnonce);

            records = stmt.executeUpdate(query);

            //When record is successfully deleted
            if (records != 0) {
                deleteStatus = true;
                TransactionDAO.insert(dbConn, query); // On insère la transaction
            } else {
                deleteStatus = false;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deleteStatus;
    }

    public static Integer numberAnnonceByIdUser(Integer idUser) {
        Connection dbConn = MyConnection.getInstance();
        Integer retour = 0;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                retour = rs.getInt(1);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour;
    }

    public static ArrayList<AnnonceDTO> getMultiParam(Integer idCat, String keyword, Integer minPrice, Integer maxPrice, boolean photo, Integer page) {
        Connection dbConn = MyConnection.getInstance();
        ArrayList<AnnonceDTO> myList = new ArrayList<>();

        ArrayList<String> conditions = new ArrayList<>();

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
            if (idCat != ConstantsDB.id_all_categories) {
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
                conditions.add(COL_ID_ANNONCE + " IN (SELECT " + PhotoDAO.COL_ID_ANNONCE + " FROM " + PhotoDAO.TABLE_NAME + ")");
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
            String limit = " LIMIT " + String.valueOf((page - 1) * ConstantsDB.PAGINATION) + "," + String.valueOf(ConstantsDB.PAGINATION - 1);
            String query = select + where + conditions_concat + order + limit;

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                myList.add(transfertAnnonce(rs));
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myList;
    }

    public static Integer getNbAnnonce() {
        Connection dbConn = MyConnection.getInstance();
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return nb_annonce;
    }
}
