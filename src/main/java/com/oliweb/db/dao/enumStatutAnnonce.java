package com.oliweb.db.dao;

public enum enumStatutAnnonce {
    UNREGISTRED("R"),
    VALID("V"),
    SOLD("S");

    private String valeur;

    enumStatutAnnonce(String valeur) {
        this.valeur = valeur;
    }

    public String valeur() {
        return valeur;
    }
}
