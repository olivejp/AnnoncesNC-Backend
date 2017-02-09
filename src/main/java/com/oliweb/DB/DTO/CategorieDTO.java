package com.oliweb.DB.DTO;

public class CategorieDTO {
	protected Integer idCAT;
	protected String nameCAT;
	protected String couleurCAT;
	protected int nbAnnonceCAT;
    
    public CategorieDTO(Integer p_idCat, String p_nameCat, String p_couleurCat, int p_nbAnnonceCAT) {
        idCAT = p_idCat;
        nameCAT = p_nameCat;
        couleurCAT = p_couleurCat;
        nbAnnonceCAT = p_nbAnnonceCAT;
    }
    
    public CategorieDTO() {
        idCAT = null;
        nameCAT = null;
    }
    
    public Integer getIdCAT() {
        return idCAT;
    }

    public void setIdCAT(Integer idCAT) {
        this.idCAT = idCAT;
    }

    public String getNameCAT() {
        return nameCAT;
    }

    public void setNameCAT(String nameCAT) {
        this.nameCAT = nameCAT;
    }

	public String getCouleurCAT() {
		return couleurCAT;
	}

	public void setCouleurCAT(String couleurCAT) {
		this.couleurCAT = couleurCAT;
	}

	public int getNbAnnonceCAT() {
		return nbAnnonceCAT;
	}

	public void setNbAnnonceCAT(int nbAnnonceCAT) {
		this.nbAnnonceCAT = nbAnnonceCAT;
	}
}
