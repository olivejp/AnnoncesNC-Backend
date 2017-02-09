package com.oliweb.DB.DTO;

import java.util.ArrayList;

public class AnnonceDTO {
    protected Integer idANO;               // Identifiant
    protected CategorieDTO categorieANO;   // Une annonce appartient a une catégorie
    protected UtilisateurDTO ownerANO;     // Une annonce est rédigée par une personne
    protected Integer priceANO;            // L'annonce a un prix
    protected String descriptionANO;       // Description de l'annonce
    protected String titreANO;             // Titre de l'annonce
    protected boolean publishedANO;        // True si l'annonce est publiée sinon False
    protected Long datePublished;          // La date de la parution
    protected ArrayList<PhotoDTO> photos;  // Les photos de l'annonce
    
    /* Constructeur */
    public AnnonceDTO(Integer idANO, CategorieDTO categorieANO, UtilisateurDTO ownerANO, Integer priceANO, String descriptionANO, String titreANO, boolean publishedANO, Long datePublished, ArrayList<PhotoDTO> listPhoto) {
        this.idANO = idANO;
        this.categorieANO = categorieANO;
        this.ownerANO = ownerANO;
        this.priceANO = priceANO;
        this.descriptionANO = descriptionANO;
        this.titreANO = titreANO;
        this.publishedANO = publishedANO;
        this.datePublished = datePublished;
        this.photos = listPhoto;
    }

    public AnnonceDTO() {
        this.idANO = 0;
        this.categorieANO = null;
        this.ownerANO = null;
        this.priceANO = 0;
        this.descriptionANO = "";
        this.titreANO = "";
        this.publishedANO = false;
        this.datePublished = (long) 0;
        this.photos = new ArrayList<>();
    }
    
    public Integer getIdANO() {
        return idANO;
    }

    public void setIdANO(Integer idANO) {
        this.idANO = idANO;
    }

    public CategorieDTO getCategorieANO() {
        return categorieANO;
    }

    public void setCategorieANO(CategorieDTO categorieANO) {
        this.categorieANO = categorieANO;
    }

    public UtilisateurDTO getOwnerANO() {
        return ownerANO;
    }

    public void setOwnerANO(UtilisateurDTO ownerANO) {
        this.ownerANO = ownerANO;
    }

    public Integer getPriceANO() {
        return priceANO;
    }

    public void setPriceANO(Integer priceANO) {
        this.priceANO = priceANO;
    }

    public String getDescriptionANO() {
        return descriptionANO;
    }

    public void setDescriptionANO(String descriptionANO) {
        this.descriptionANO = descriptionANO;
    }

    public String getTitreANO() {
        return titreANO;
    }

    public void setTitreANO(String titreANO) {
        this.titreANO = titreANO;
    }

    public boolean isPublishedANO() {
        return publishedANO;
    }

    public void setPublishedANO(boolean publishedANO) {
        this.publishedANO = publishedANO;
    }

    public Long getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Long datePublished) {
        this.datePublished = datePublished;
    }

	public ArrayList<PhotoDTO> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<PhotoDTO> photos) {
		this.photos = photos;
	}

	
    
}