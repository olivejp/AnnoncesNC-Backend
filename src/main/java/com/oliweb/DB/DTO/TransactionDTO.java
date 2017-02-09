package com.oliweb.DB.DTO;

import java.sql.Timestamp;

public class TransactionDTO {
	protected Integer idTRANSACTION;
	protected String transaction;
	protected Timestamp dateCreation;
	protected Timestamp dateRecuperation;
	
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
	
}
