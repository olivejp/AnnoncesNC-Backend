package com.oliweb.db.dao;

import org.jetbrains.annotations.Contract;

public enum enumAdminUtilisateur {
    OUI("O"),

    NON("N");

    private String valeur;

    enumAdminUtilisateur(String valeur) {
        this.valeur = valeur;
    }

    @Contract(pure = true)
    public String valeur() {
        return valeur;
    }
}
