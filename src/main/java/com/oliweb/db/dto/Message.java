package com.oliweb.db.dto;

public class Message {
    private Integer idMessage;
    private Utilisateur sender;
    private Utilisateur receiver;
    private String message;
    private Long dateMessage;

    public Message(Integer idMessage, Utilisateur sender, Utilisateur receiver, String message, Long dateMessage) {
        super();
		this.idMessage = idMessage;
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
		this.dateMessage = dateMessage;
	}

    public Message() {
        // TODO Auto-generated constructor stub
	}

	public Integer getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(Integer idMessage) {
		this.idMessage = idMessage;
	}

    public Utilisateur getSender() {
        return sender;
	}

    public void setIdSender(Utilisateur sender) {
        this.sender = sender;
	}

    public Utilisateur getReceiver() {
        return receiver;
	}

    public void setIdReceiver(Utilisateur receiver) {
        this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getDateMessage() {
		return dateMessage;
	}

	public void setDateMessage(Long dateMessage) {
		this.dateMessage = dateMessage;
	}


}
