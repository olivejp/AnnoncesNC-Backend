package com.oliweb.db.dto;

public class UtilisateurDTO {
    private Integer idUTI;
    private String emailUTI;
    private Integer telephoneUTI;
    private String passwordUTI;
    private String dateCreationUTI;
    private String dateLastConnexionUTI;
    private String adminUTI;
    private String statutUTI;

    public UtilisateurDTO() {
    }
    
    public Integer getIdUTI() {
        return idUTI;
    }

    public void setIdUTI(Integer idUTI) {
        this.idUTI = idUTI;
    }

    public String getEmailUTI() {
        return emailUTI;
    }

    public void setEmailUTI(String emailUTI) {
        this.emailUTI = emailUTI;
    }

    public Integer getTelephoneUTI() {
        return telephoneUTI;
    }

    public void setTelephoneUTI(Integer telephoneUTI) {
        this.telephoneUTI = telephoneUTI;
    }

    public String getPasswordUTI() {
        return passwordUTI;
    }

    public void setPasswordUTI(String passwordUTI) {
        this.passwordUTI = passwordUTI;
    }

    public String getDateCreationUTI() {
        return dateCreationUTI;
    }

    public void setDateCreationUTI(String dateCreationUTI) {
        this.dateCreationUTI = dateCreationUTI;
    }

    public String getDateLastConnexionUTI() {
        return dateLastConnexionUTI;
    }

    public void setDateLastConnexionUTI(String dateLastConnexionUTI) {
        this.dateLastConnexionUTI = dateLastConnexionUTI;
    }

    public String getAdminUTI() {
        return adminUTI;
    }

    public void setAdminUTI(String adminUTI) {
        this.adminUTI = adminUTI;
    }

    public String getStatutUTI() {
        return statutUTI;
    }

    public void setStatutUTI(String statutUTI) {
        this.statutUTI = statutUTI;
    }
}
