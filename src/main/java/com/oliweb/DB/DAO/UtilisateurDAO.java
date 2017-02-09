package com.oliweb.DB.DAO;

import com.oliweb.DB.DTO.UtilisateurDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.oliweb.DB.Contract.UtilisateurContract.*;

public class UtilisateurDAO {

    private Connection dbConn;

    private Integer idutilisateur;
    private String nomUtilisateur;
    private String prenomUtilisateur;
    private Integer telephoneUtilisateur;
    private String emailUtilisateur;
    private Timestamp dateCreationUtilisateur;
    private String adminUtilisateur;

    public UtilisateurDAO(Connection connection, Integer idutilisateur, String nomUtilisateur, String prenomUtilisateur,
                          Integer telephoneUtilisateur, String emailUtilisateur, Timestamp dateCreationUtilisateur, String adminUtilisateur) {
        super();
        dbConn = connection;
        this.idutilisateur = idutilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.prenomUtilisateur = prenomUtilisateur;
        this.telephoneUtilisateur = telephoneUtilisateur;
        this.emailUtilisateur = emailUtilisateur;
        this.dateCreationUtilisateur = dateCreationUtilisateur;
        this.adminUtilisateur = adminUtilisateur;
    }

    public UtilisateurDAO(Connection connection) {
        super();
        dbConn = connection;
    }

    public Integer getIdutilisateur() {
        return idutilisateur;
    }

    public void setIdutilisateur(Integer idutilisateur) {
        this.idutilisateur = idutilisateur;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getPrenomUtilisateur() {
        return prenomUtilisateur;
    }

    public void setPrenomUtilisateur(String prenomUtilisateur) {
        this.prenomUtilisateur = prenomUtilisateur;
    }

    public Integer getTelephoneUtilisateur() {
        return telephoneUtilisateur;
    }

    public void setTelephoneUtilisateur(Integer telephoneUtilisateur) {
        this.telephoneUtilisateur = telephoneUtilisateur;
    }

    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }

    public void setEmailUtilisateur(String emailUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;
    }

    public Timestamp getDateCreationUtilisateur() {
        return dateCreationUtilisateur;
    }

    public void setDateCreationUtilisateur(Timestamp dateCreationUtilisateur) {
        this.dateCreationUtilisateur = dateCreationUtilisateur;
    }

    public String getAdminUtilisateur() {
        return adminUtilisateur;
    }

    public void setAdminUtilisateur(String adminUtilisateur) {
        this.adminUtilisateur = adminUtilisateur;
    }

    public boolean checkLogin(String email, String pwd) {
        boolean isUserAvailable = false;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL_UTILISATEUR + " = '" + email
                    + "' AND " + COL_PASSWORD_UTILISATEUR + "='" + pwd + "' AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                isUserAvailable = true;
            }
            stmt.close();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return isUserAvailable;
    }

    public boolean checkAdmin(String email, String pwd) throws Exception {
        boolean isUserAvailable = false;
        Statement stmt = dbConn.createStatement();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL_UTILISATEUR + " = '" + email
                + "' AND " + COL_PASSWORD_UTILISATEUR + "=" + "'" + pwd + "' AND " + COL_ADMIN_UTILISATEUR + "='O'"
                + " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            isUserAvailable = true;
        }
        stmt.close();

        return isUserAvailable;
    }

    public boolean checkAdmin(Integer idUser) {
        boolean isAdmin = false;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUser)
                    + " AND " + COL_ADMIN_UTILISATEUR + "='O'"
                    + " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                isAdmin = true;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAdmin;
    }

    public UtilisateurDTO getById(Integer idUtilisateur) {
        UtilisateurDTO utilisateur = new UtilisateurDTO();
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT " + COL_ID_UTILISATEUR + ", "
                    + COL_TELEPHONE_UTILISATEUR + ", "
                    + COL_EMAIL_UTILISATEUR
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUtilisateur)
                    + " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                utilisateur.setIdUTI(rs.getInt(COL_ID_UTILISATEUR));
                utilisateur.setEmailUTI(rs.getString(COL_EMAIL_UTILISATEUR));
                utilisateur.setTelephoneUTI(rs.getInt(COL_TELEPHONE_UTILISATEUR));
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

    public void updateDateLastConnexion(Integer idUser) {
        try {
            Statement stmt = dbConn.createStatement();
            String dateAsString = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String update = "UPDATE " + TABLE_NAME + " SET " + COL_DATE_LAST_CONNEXION + " = " + dateAsString + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
            stmt.executeUpdate(update);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean update(UtilisateurDTO user) {
        int retour = 0;
        try {
            Statement stmt = dbConn.createStatement();
            String update = "UPDATE " + TABLE_NAME
                    + " SET " + COL_EMAIL_UTILISATEUR + " = '" + user.getEmailUTI()
                    + "' , " + COL_TELEPHONE_UTILISATEUR + " = " + user.getTelephoneUTI()
                    + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(user.getIdUTI());
            System.out.println(update);
            retour = stmt.executeUpdate(update);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour != 0;
    }

    public boolean existById(Integer id) {

        boolean exist = false;

        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(id)
                    + " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    exist = true;
                }
            }
            stmt.close();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return exist;
    }

    public boolean existByEmail(String email) {
        boolean exist = false;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_EMAIL_UTILISATEUR + " = '" + email + "' "
                    + " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";
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

    public UtilisateurDTO getByEmail(String email) {
        UtilisateurDTO utilisateur = new UtilisateurDTO();
        Statement stmt;
        String query;
        ResultSet results;

        try {
            stmt = dbConn.createStatement();
            query = "SELECT " + COL_ID_UTILISATEUR + ", "
                    + COL_EMAIL_UTILISATEUR + ", "
                    + COL_TELEPHONE_UTILISATEUR + " "
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_EMAIL_UTILISATEUR + "='" + email + "'"
                    + " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";

            results = stmt.executeQuery(query);
            while (results.next()) {
                utilisateur.setIdUTI(results.getInt(COL_ID_UTILISATEUR));
                utilisateur.setEmailUTI(results.getString(COL_EMAIL_UTILISATEUR));
                utilisateur.setTelephoneUTI(results.getInt(COL_TELEPHONE_UTILISATEUR));
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

    public int insert(String email, String pwd, Integer telephone) {
        int insertStatus = -1;
        if (!existByEmail(email)) {
            try {
                Statement stmt = dbConn.createStatement();
                String query = "INSERT INTO " + TABLE_NAME
                        + "(" + COL_EMAIL_UTILISATEUR + ", "
                        + COL_TELEPHONE_UTILISATEUR + ", "
                        + COL_PASSWORD_UTILISATEUR + ")"
                        + " VALUES ('"
                        + email + "', "
                        + String.valueOf(telephone) + ", '"
                        + pwd + "')";
                int records = stmt.executeUpdate(query);
                stmt.close();

                //When record is successfully inserted
                if (records != 0) {
                    insertStatus = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // Il existe déjà un enregistrement avec cette adresse mail
            insertStatus = -2;
        }
        return insertStatus;
    }

    public String getPasswordByEmail(String email) throws Exception {

        Statement stmt;
        String query;
        ResultSet results;
        String password = null;

        stmt = dbConn.createStatement();
        query = "SELECT " + COL_PASSWORD_UTILISATEUR
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_EMAIL_UTILISATEUR + "='" + email + "'";

        results = stmt.executeQuery(query);
        if (results.next()) {
            password = results.getString(COL_PASSWORD_UTILISATEUR);
        }
        stmt.close();

        return password;
    }

    public String getPasswordByIdUser(Integer idUser) {

        Statement stmt;
        String query;
        ResultSet results;
        String password = null;

        try {
            stmt = dbConn.createStatement();
            query = "SELECT " + COL_PASSWORD_UTILISATEUR
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_UTILISATEUR + "=" + idUser;

            results = stmt.executeQuery(query);
            if (results.next()) {
                password = results.getString(COL_PASSWORD_UTILISATEUR);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    public boolean changePassword(Integer idUser, String newPassword) {

        Statement stmt;
        String update;
        int retour;
        boolean updateStatus = false;

        try {
            stmt = dbConn.createStatement();
            update = "UPDATE " + TABLE_NAME + " SET " + COL_PASSWORD_UTILISATEUR + " = '" + newPassword + "' WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);

            retour = stmt.executeUpdate(update);

            updateStatus = retour != 0;

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateStatus;
    }

    public boolean unregisterUser(Integer idUser) {

        Statement stmt;
        String update;
        int retour;
        boolean updateStatus = false;

        // On supprime d'abord les annonces de l'utilisateur
        AnnonceDAO annonceDAO = new AnnonceDAO(dbConn);
        if (annonceDAO.devalideByIdUser(idUser)) {
            try {
                stmt = dbConn.createStatement();
                update = "UPDATE " + TABLE_NAME + " SET " + COL_STATUT_UTILISATEUR + " = '" + enumStatutUtilisateur.UNREGISTRED.valeur() + "' WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
                retour = stmt.executeUpdate(update);
                updateStatus = retour != 0;
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return updateStatus;
    }

    public Integer getNbUser() {
        Statement stmt;
        ResultSet results;
        String query;
        Integer nb_user = 0;

        try {
            stmt = dbConn.createStatement();
            query = "SELECT COUNT(*) FROM " + TABLE_NAME
                    + " WHERE " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";

            results = stmt.executeQuery(query);
            if (results.next()) {
                nb_user = results.getInt(1);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nb_user;
    }

    public boolean deleteById(Integer idUser) throws Exception {
        Statement stmt = dbConn.createStatement();
        int retour = 0;
        boolean deleteStatus = false;

        // On supprime d'abord les annonces de l'utilisateur
        AnnonceDAO annonceDAO = new AnnonceDAO(dbConn);
        if (annonceDAO.deleteByIdUser(idUser)) {
            String delete = "DELETE FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
            retour = stmt.executeUpdate(delete);

            deleteStatus = retour != 0;
            stmt.close();
        }
        return deleteStatus;
    }
}
