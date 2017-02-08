package com.oliweb.DB.DAO;

public enum enumStatutAnnonce {
	UNREGISTRED("R"),
	VALID("V"),
	SOLD("S");
	private String valeur;
	enumStatutAnnonce(String valeur){
		this.valeur = valeur;
	}
	public String valeur(){
		return valeur;
	}
	
}
