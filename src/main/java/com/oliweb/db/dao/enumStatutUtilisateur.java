package com.oliweb.db.dao;

import org.jetbrains.annotations.Contract;

public enum enumStatutUtilisateur {
	UNREGISTRED("R"),
	VALID("V");
	private String valeur;
	enumStatutUtilisateur(String valeur){
		this.valeur = valeur;
	}

    @Contract(pure = true)
    public String valeur() {
        return valeur;
	}
	
}
