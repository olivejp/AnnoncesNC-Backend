package com.oliweb.db.dto;

public class Categorie {
    private Integer idCAT;
    private String nameCAT;
    private String couleurCAT;
    private int nbAnnonceCAT;

    public Categorie(Integer p_idCat, String p_nameCat, String p_couleurCat, int p_nbAnnonceCAT) {
        idCAT = p_idCat;
        nameCAT = p_nameCat;
        couleurCAT = p_couleurCat;
        nbAnnonceCAT = p_nbAnnonceCAT;
    }

    public Categorie() {
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
