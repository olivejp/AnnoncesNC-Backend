package com.oliweb.DB.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oliweb.DB.DTO.UtilisateurDTO;

public class UtilisateurDAO {
    private Integer idutilisateur;
    private String nomUtilisateur;
    private String prenomUtilisateur;
    private Integer telephoneUtilisateur;
    private String emailUtilisateur;
    private Timestamp dateCreationUtilisateur;
    private String adminUtilisateur;

    public static final String TABLE_NAME = "utilisateur";
    public static final String COL_ID_UTILISATEUR = "idutilisateur";
    public static final String COL_TELEPHONE_UTILISATEUR = "telephoneUtilisateur";
    public static final String COL_EMAIL_UTILISATEUR = "emailUtilisateur";
    public static final String COL_DATE_CREATION_UTILISATEUR = "dateCreationUtilisateur";
    public static final String COL_PASSWORD_UTILISATEUR = "passwordUtilisateur";
    public static final String COL_DATE_LAST_CONNEXION = "dateLastConnexion";
    public static final String COL_ADMIN_UTILISATEUR = "adminUtilisateur";
    public static final String COL_STATUT_UTILISATEUR = "statutUtilisateur";
    public static final String COL_SALT_UTILISATEUR = "saltUtilisateur";

    public UtilisateurDAO(Integer idutilisateur, String nomUtilisateur, String prenomUtilisateur,
                          Integer telephoneUtilisateur, String emailUtilisateur, Timestamp dateCreationUtilisateur, String adminUtilisateur) {
        super();
        this.idutilisateur = idutilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.prenomUtilisateur = prenomUtilisateur;
        this.telephoneUtilisateur = telephoneUtilisateur;
        this.emailUtilisateur = emailUtilisateur;
        this.dateCreationUtilisateur = dateCreationUtilisateur;
        this.adminUtilisateur = adminUtilisateur;
    }

    public UtilisateurDAO() {
        super();
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

    public static boolean checkLogin(String email, String pwd) {
        boolean isUserAvailable = false;
        Connection dbConn = MyConnection.getInstance();

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

    public static boolean checkAdmin(String email, String pwd) throws Exception {
        boolean isUserAvailable = false;
        Connection dbConn = MyConnection.getInstance();

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

    public static boolean checkAdmin(Integer idUser) {
        boolean isAdmin = false;
        Connection dbConn = MyConnection.getInstance();

        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUser)
                    + " AND " + COL_ADMIN_UTILISATEUR + "='O'"
                    + " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";
            ;
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

    public static UtilisateurDTO getById(Integer idUtilisateur) {
        Connection dbConn = MyConnection.getInstance();
        UtilisateurDTO utilisateur = new UtilisateurDTO();

        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT " + COL_ID_UTILISATEUR + ", "
                    + COL_TELEPHONE_UTILISATEUR + ", "
                    + COL_EMAIL_UTILISATEUR
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUtilisateur)
                    + " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";
            ;

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

    public static void updateDateLastConnexion(Integer idUser) {
        Connection dbConn = MyConnection.getInstance();
        try {
            Statement stmt = dbConn.createStatement();

            String dateAsString = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String update = "UPDATE " + TABLE_NAME + " SET " + COL_DATE_LAST_CONNEXION + " = " + dateAsString + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
            int records = stmt.executeUpdate(update);
            stmt.close();

            //When record is successfully inserted
            if (records != 0) {
                TransactionDAO.insert(dbConn, update);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean update(UtilisateurDTO user) {
        Connection dbConn = MyConnection.getInstance();
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

            // When record is successfully inserted
            if (retour != 0) {
                TransactionDAO.insert(dbConn, update);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour != 0;
    }

    public static boolean existById(Integer id) {
        Connection dbConn = MyConnection.getInstance();
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

    public static boolean existByEmail(String email) {
        Connection dbConn = MyConnection.getInstance();
        boolean exist = false;

        try {
            Statement stmt = dbConn.createStatement();
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_EMAIL_UTILISATEUR + " = '" + email + "' "
                    + " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";
            ;
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

    public static UtilisateurDTO getByEmail(String email) {
        Connection dbConn = MyConnection.getInstance();
        UtilisateurDTO utilisateur = new UtilisateurDTO();
        Statement stmt;
        String query;
        ResultSet results;

        try {
            stmt = (Statement) dbConn.createStatement();
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

    public static int insert(String email, String pwd, Integer telephone) {
        int insertStatus = -1;
        if (!existByEmail(email)) {

            Connection dbConn = MyConnection.getInstance();

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
                    TransactionDAO.insert(dbConn, query);
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

    public static String getPasswordByEmail(String email) throws Exception {
        Connection dbConn = MyConnection.getInstance();
        Statement stmt;
        String query;
        ResultSet results;
        String password = null;

        stmt = (Statement) dbConn.createStatement();
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

    public static String getPasswordByIdUser(Integer idUser) {
        Connection dbConn = MyConnection.getInstance();
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

    public static boolean changePassword(Integer idUser, String newPassword) {
        Connection dbConn = MyConnection.getInstance();
        Statement stmt;
        String update;
        int retour;
        boolean updateStatus = false;

        try {
            stmt = dbConn.createStatement();
            update = "UPDATE " + TABLE_NAME + " SET " + COL_PASSWORD_UTILISATEUR + " = '" + newPassword + "' WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);

            retour = stmt.executeUpdate(update);

            if (retour != 0) {
                updateStatus = true;
                TransactionDAO.insert(dbConn, update); // On ins�re la transaction
            } else {
                updateStatus = false;
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateStatus;
    }

    public static boolean unregisterUser(Integer idUser) {
        Connection dbConn = MyConnection.getInstance();
        Statement stmt;
        String update;
        int retour = 0;
        boolean updateStatus = false;

        // On supprime d'abord les annonces de l'utilisateur
        if (AnnonceDAO.devalideByIdUser(idUser)) {
            try {
                stmt = dbConn.createStatement();
                update = "UPDATE " + TABLE_NAME + " SET " + COL_STATUT_UTILISATEUR + " = '" + enumStatutUtilisateur.UNREGISTRED.valeur() + "' WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
                retour = stmt.executeUpdate(update);
                System.out.println("Retour = " + String.valueOf(retour) + update);
                if (retour != 0) {
                    updateStatus = true;
                    TransactionDAO.insert(dbConn, update); // On ins�re la transaction
                } else {
                    updateStatus = false;
                }
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return updateStatus;
    }

    public static Integer getNbUser() {
        Connection dbConn = MyConnection.getInstance();
        Statement stmt;
        ResultSet results;
        String query;
        Integer nb_user = 0;

        try {
            stmt = dbConn.createStatement();
            query = "SELECT COUNT(*) FROM " + TABLE_NAME
                    + " WHERE " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";
            ;

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

    public static boolean deleteById(Integer idUser) throws Exception {
        Connection dbConn = MyConnection.getInstance();
        Statement stmt = dbConn.createStatement();
        int retour = 0;
        boolean deleteStatus = false;

        // On supprime d'abord les annonces de l'utilisateur
        if (AnnonceDAO.deleteByIdUser(idUser)) {
            String delete = "DELETE FROM " + TABLE_NAME
                    + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
            retour = stmt.executeUpdate(delete);

            if (retour != 0) {
                deleteStatus = true;
                TransactionDAO.insert(dbConn, delete); // On ins�re la transaction
            } else {
                deleteStatus = false;
            }

            stmt.close();
        }

        return deleteStatus;
    }
}
