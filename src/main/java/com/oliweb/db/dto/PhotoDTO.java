package com.oliweb.db.dto;

public class PhotoDTO {
	private Integer idPhoto;
	private String namePhoto;
	private Integer idAnnoncePhoto;

	public PhotoDTO(Integer idPhoto, String namePhoto, Integer idAnnoncePhoto) {
		this.idPhoto = idPhoto;
		this.namePhoto = namePhoto;
		this.idAnnoncePhoto = idAnnoncePhoto;
	}
	public PhotoDTO() {
		super();
		this.idPhoto = null;
		this.namePhoto = null;
		this.idAnnoncePhoto = null;
	}
	public Integer getIdPhoto() {
		return idPhoto;
	}
	public void setIdPhoto(Integer idPhoto) {
		this.idPhoto = idPhoto;
	}
	public String getNamePhoto() {
		return namePhoto;
	}
	public void setNamePhoto(String namePhoto) {
		this.namePhoto = namePhoto;
	}
	public Integer getIdAnnoncePhoto() {
		return idAnnoncePhoto;
	}
	public void setIdAnnoncePhoto(Integer idAnnoncePhoto) {
		this.idAnnoncePhoto = idAnnoncePhoto;
	}
}
