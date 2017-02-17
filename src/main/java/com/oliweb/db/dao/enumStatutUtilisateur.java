package com.oliweb.db.dao;

public enum enumStatutUtilisateur {
	UNREGISTRED("R"),
	VALID("V");
	private String valeur;
	enumStatutUtilisateur(String valeur){
		this.valeur = valeur;
	}
	public String valeur(){
		return valeur;
	}
	
}
