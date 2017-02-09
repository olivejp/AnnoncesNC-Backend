package com.oliweb.restservice;


import com.google.gson.Gson;
import com.oliweb.DB.DAO.AnnonceDAO;
import com.oliweb.DB.DAO.PhotoDAO;
import com.oliweb.DB.DTO.PhotoDTO;
import com.oliweb.DB.utility.ConstantsDB;
import com.oliweb.DB.utility.ReturnClass;
import com.oliweb.utility.Constants;
import org.apache.commons.io.FilenameUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/photos")
public class Photo {

    private static Gson gson = new Gson();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String dopostphoto(@QueryParam("idAnnonce") Integer idAnnonce,
                              @QueryParam("idPhoto") Integer idPhoto,
                              @QueryParam("nomPhoto") String nomPhoto){
        PhotoDTO photoDTO;
        ReturnClass rs = new ReturnClass("dopostphoto", false, null, null);

        // Récupération du filename qu'on vient de recevoir
        String newName = FilenameUtils.getName(nomPhoto);

        // Vérification que l'annonce existe bien
        if (AnnonceDAO.exist(idAnnonce)) {

            // Vérification si la photo existe déjà
            photoDTO = PhotoDAO.getById(idAnnonce, idPhoto);
            if (photoDTO != null) {
                // La photo existe déjà, on va vérifier si elle a changé de nom ou pas
                String presentName = FilenameUtils.getName(photoDTO.getNamePhoto());
                if (!presentName.equals(newName)) {
                    // La photo existe mais ne porte pas le même nom qu'auparavant
                    // On va mettre à jour notre photo
                    photoDTO.setNamePhoto(ConstantsDB.upload_directory + newName);
                    if (PhotoDAO.update(photoDTO)) {
                        rs.setStatus(true);
                        rs.setId(photoDTO.getIdPhoto());
                        rs.setMsg(nomPhoto);
                    } else {
                        rs.setMsg("La photo n'a pas pu être mise à jour dans la BD");
                    }
                } else {
                    // La photo n'a pas changée, elle existe déjà
                    rs.setStatus(true);
                    rs.setMsg(Constants.PHOTO_ALREADY_EXIST);
                }
            } else {
                // La photo n'existe pas, il faut la créer
                photoDTO = new PhotoDTO();
                photoDTO.setNamePhoto(ConstantsDB.upload_directory + newName);
                photoDTO.setIdAnnoncePhoto(idAnnonce);
                Integer newId = PhotoDAO.insert(photoDTO);
                if (newId != 0) {
                    photoDTO.setIdPhoto(newId);
                    rs.setStatus(true);
                    rs.setId(newId);
                    rs.setMsg(nomPhoto);
                } else {
                    rs.setMsg("La photo n'a pas pu être créée dans la BD");
                }
            }
        } else {
            rs.setMsg("L'annonce n°:" + String.valueOf(idAnnonce) + " n'existe pas");
        }
        return gson.toJson(rs);
    }

    @GET
    @Path("/photoExist")
    @Produces(MediaType.APPLICATION_JSON)
    public String photoExist(@QueryParam("idAnnonce") Integer idAnnonce, @QueryParam("idPhoto") Integer idPhoto, @QueryParam("namePhoto") String namePhoto) {
        ReturnClass rs = new ReturnClass("photoexist", false, null, null);
        boolean exist;

        exist = PhotoDAO.existByAnnonceAndName(idAnnonce, idPhoto, namePhoto);
        if (exist) {
            rs.setStatus(exist);
            rs.setMsg(Constants.same_name);
        } else {
            exist = PhotoDAO.existWithDifferentName(idAnnonce, idPhoto, namePhoto);
            rs.setStatus(exist);
            rs.setMsg(Constants.different_name);
        }
        return gson.toJson(rs);
    }
}
