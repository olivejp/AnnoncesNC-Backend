package com.oliweb.DB.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.oliweb.DB.DTO.TransactionDTO;

public class TransactionDAO {
	private Integer idTRANSACTION;
	private String transaction;
	private Timestamp dateCreation;
	private Timestamp dateRecuperation;

	public static final String TABLE_NAME = "TRANSACTION";
	public static final String COL_ID_TRANSACTION = "idTRANSACTION";
	public static final String COL_TRANSACTION = "transaction";
	public static final String COL_DATE_CREATION = "dateCreation";
	public static final String COL_DATE_RECUPERATION = "dateRecuperation";
	
	public Integer getIdTRANSACTION() {
		return idTRANSACTION;
	}
	public void setIdTRANSACTION(Integer idTRANSACTION) {
		this.idTRANSACTION = idTRANSACTION;
	}
	public String getTransaction() {
		return transaction;
	}
	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}
	public Timestamp getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(Timestamp dateCreation) {
		this.dateCreation = dateCreation;
	}
	public Timestamp getDateRecuperation() {
		return dateRecuperation;
	}
	public void setDateRecuperation(Timestamp dateRecuperation) {
		this.dateRecuperation = dateRecuperation;
	}
	
	
	/**
	 * M�thode de transfert d'un ResultSet vers une Annonce
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static TransactionDTO transfertTransaction(ResultSet rs) throws Exception{
		// Cr�ation d'une nouvelle annonce
		TransactionDTO transaction = new TransactionDTO();

		// Renseignement des champs de l'annonce
		transaction.setIdTRANSACTION(rs.getInt(COL_ID_TRANSACTION));
		transaction.setTransaction(rs.getString(COL_TRANSACTION));
		transaction.setDateCreation(rs.getTimestamp(COL_DATE_CREATION));
		transaction.setDateRecuperation(rs.getTimestamp(COL_DATE_RECUPERATION));
		return transaction;
	}
	
	
	/**
	 * 
	 * @param transaction
	 * @return
	 * @throws Exception
	 */
	public static boolean insert(Connection dbConn, String transaction) throws Exception {
		boolean insertStatus = false;
		int records;
		String query;
		Statement stmt = (Statement) dbConn.createStatement();
		query = "INSERT INTO " + TABLE_NAME + " (" + COL_TRANSACTION + ", "
				+ COL_DATE_CREATION + ", "
				+ COL_DATE_RECUPERATION + ")"
				+ " values('"+
				transaction.replace("'", "''")+ "',"+
				"CURRENT_TIME(), 0)";
		
		records = stmt.executeUpdate(query);

		// When record is successfully inserted
		if (records != 0) {
			insertStatus = true;
		}else{
			insertStatus = false;
		}
		stmt.close();
		return insertStatus;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static ArrayList<TransactionDTO> getList(Connection dbConn) throws Exception{
		ArrayList<TransactionDTO> myArray = new ArrayList<>();
		
		Statement stmt = (Statement) dbConn.createStatement();
		String query = "SELECT * "
				+ " FROM " + TABLE_NAME
				+ " WHERE " + COL_DATE_RECUPERATION + " = 0 "
				+ " ORDER BY " + COL_DATE_CREATION;
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()){
			myArray.add(transfertTransaction(rs));
		}
		
		
		// On met � jour les transactions qu'on a envoy� pour ne pas les renvoyer une seconde fois.
		String update = "UPDATE " + TABLE_NAME + " SET " + COL_DATE_RECUPERATION + "= NOW() " 
				+ " WHERE " + COL_DATE_RECUPERATION + " = 0 ";
		
		stmt.executeUpdate(update);
		
		stmt.close();
		
		return myArray;
	}
}
