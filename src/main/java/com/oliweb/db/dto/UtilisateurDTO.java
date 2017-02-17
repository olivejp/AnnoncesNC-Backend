package com.oliweb.db.dto;

public class UtilisateurDTO {
    private Integer idUTI;
    private String emailUTI;
    private Integer telephoneUTI;
    
    public UtilisateurDTO(Integer idUTI, String emailUTI, Integer telephoneUTI) {
        this.idUTI = idUTI;
        this.emailUTI = emailUTI;
        this.telephoneUTI = telephoneUTI;
    }
    
    public UtilisateurDTO() {
        this.idUTI = null;
        this.emailUTI = null;
        this.telephoneUTI = null;
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
}
