package com.oliweb.db.dto;

import java.util.ArrayList;
import java.util.List;

public class AnnonceDTO {
    private Integer idANO;               // Identifiant
    private CategorieDTO categorieANO;   // Une annonce appartient a une catégorie
    private UtilisateurDTO utilisateurANO;     // Une annonce est rédigée par une personne
    private Integer priceANO;            // L'annonce a un prix
    private String descriptionANO;       // Description de l'annonce
    private String titreANO;             // Titre de l'annonce
    private boolean publishedANO;        // True si l'annonce est publiée sinon False
    private Long datePublished;          // La date de la parution
    private List<PhotoDTO> photos;       // Les photos de l'annonce

    public AnnonceDTO() {
        this.idANO = 0;
        this.categorieANO = null;
        this.utilisateurANO = null;
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

    public UtilisateurDTO getUtilisateurANO() {
        return utilisateurANO;
    }

    public void setUtilisateurANO(UtilisateurDTO utilisateurANO) {
        this.utilisateurANO = utilisateurANO;
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

    public List<PhotoDTO> getPhotos() {
        return photos;
	}

    public void setPhotos(List<PhotoDTO> photos) {
        this.photos = photos;
	}

	
    
}