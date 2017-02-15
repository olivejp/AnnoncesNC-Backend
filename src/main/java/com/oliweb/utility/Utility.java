package com.oliweb.utility;

import com.google.gson.Gson;
import com.oliweb.DB.DTO.CategorieDTO;
import com.oliweb.DB.DTO.UtilisateurDTO;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utility {

	public static final String TAG    	  = "tag";
	public static final String STATUS 	  = "status";
	public static final String ERROR_MSG  = "error_msg";
	public static final String ID_ANNONCE = "idAnnonce";
	public static final String ID_UTILISATEUR = "idUtilisateur";
	public static final String NOM_UTILISATEUR = "nomUtilisateur";
	public static final String PRENOM_UTILISATEUR = "prenomUtilisateur";
	public static final String EMAIL_UTILISATEUR = "emailUtilisateur";
	public static final String TELEPHONE_UTILISATEUR = "telephoneUtilisateur";
	public static final String TITRE_ANNONCE = "titreAnnonce";
	public static final String DESCRIPTION_ANNONCE = "descriptionAnnonce";
	public static final String PRIX_ANNONCE = "prixAnnonce";
	public static final String DATE_PUBLI_ANNONCE = "datePubliAnnonce";
	public static final String CATEGORIE_ANNONCE = "categorieAnnonce";
	public static final String LISTE_ANNONCE = "listAnnonce";
	public static final String LISTE_CATEGORIE = "listCategorie";
	public static final String IMAGE_ANNONCE = "imageAnnonce";
	public static final String UTILISATEUR_NOM = "nomUtilisateur";
	public static final String UTILISATEUR_TEL = "telephoneUtilisateur";
	public static final String UTILISATEUR_EMAIL = "emailUtilisateur";
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static String sansAccent(String s) {
		final String accents = "?????????????????????????????????????????????????"; // A compl?ter...
		final String letters = "AAAAAAaaaaaaEEEEeeeeIIIIOOOOOUUUUYiiiiooooouuuuyy"; // A compl?ter...

		StringBuffer buffer = null;
		for(int i=s.length()-1 ; i>=0; i--) {
			int index = accents.indexOf(s.charAt(i));
			if (index>=0) {
				if (buffer==null) {
					buffer = new StringBuffer(s);
				}
				buffer.setCharAt(i, letters.charAt(index));
			}
		}
		return buffer==null ? s : buffer.toString();
	}

	public static boolean isNotNull(String txt) {
        return txt != null && txt.trim().length() >= 0;
    }


    private static JSONObject constructSimpleJSON(String tag, boolean status) {
        JSONObject obj = new JSONObject();
		try {
			obj.put(TAG, tag);
			obj.put(STATUS, status);
		} catch (JSONException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
		return obj;
	}


	public static String constructListCategoryJSON(String tag, boolean status, ArrayList<CategorieDTO> myList){
		JSONObject obj = constructSimpleJSON(tag, status);
		try {
			Gson gson = new Gson();
			String response = gson.toJson(myList);
			obj.put(LISTE_CATEGORIE, response);
		} catch (JSONException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
		return obj.toString();
	}

	public static String constructUtilisateurJSON(String tag, boolean status, UtilisateurDTO utilisateur) {
		JSONObject obj = constructSimpleJSON(tag, status);
		try {
			obj.put(ID_UTILISATEUR, utilisateur.getIdUTI());
			obj.put(EMAIL_UTILISATEUR, utilisateur.getEmailUTI());
			obj.put(TELEPHONE_UTILISATEUR, utilisateur.getTelephoneUTI());
		} catch (JSONException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
		return obj.toString();
	}

	public static String constructInsertJSON(String tag, boolean status, int newId) {
		JSONObject obj = constructSimpleJSON(tag, status);
		try {
			obj.put(ID_ANNONCE, newId);
		} catch (JSONException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
		return obj.toString();
	}

	public static String constructJSON(String tag, boolean status, String err_msg) {
		JSONObject obj = constructSimpleJSON(tag, status);
		try {
			obj.put(ERROR_MSG, err_msg);
		} catch (JSONException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
		return obj.toString(); 
	}	
}
