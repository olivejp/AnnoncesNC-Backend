package com.oliweb.restservice;

import com.google.gson.Gson;
import com.oliweb.DB.DAO.MyConnection;
import com.oliweb.DB.DAO.UtilisateurDAO;
import com.oliweb.DB.utility.ReturnClass;
import com.oliweb.S_SMS.SendSms;
import com.oliweb.utility.Constants;
import com.oliweb.utility.Utility;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.Statement;


@Path("/services")
public class Service {

    private static Gson gson = new Gson();

    @POST
    @Path("/checkconnection")
    @Produces(MediaType.APPLICATION_JSON)
    public String CheckConnection(){
        ReturnClass rs = new ReturnClass("checkconnection", true, null, null);
        return gson.toJson(rs);
    }

    @POST
    @Path("/sendsms")
    @Produces(MediaType.APPLICATION_JSON)
    public String SendSms(@QueryParam("to") String to, @QueryParam("from") String from, @QueryParam("body") String body) {
        String tag = "sendsms";
        boolean retour = false;
        String message;
        ReturnClass rs = new ReturnClass(tag, false, null, null);
        message = SendSms.send(from, to, body, retour);
        rs.setMsg(message);
        rs.setStatus(retour);
        return gson.toJson(rs);
    }

    @GET
    @Path("/dosql")
    @Produces(MediaType.APPLICATION_JSON)
    public String doSql(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("query") String query){
        String retour = null;
        String tag = "doSql";
        try {
            if (UtilisateurDAO.checkAdmin(email, password)){
                Connection dbConn = MyConnection.getInstance();
                Statement stmt = dbConn.createStatement();
                stmt.executeUpdate(query);
                stmt.close();
                retour = Utility.constructJSON(tag, true, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retour = Utility.constructJSON(tag, false, Constants.SERVER_EXCEPTION);
        }
        return retour;
    }
}
