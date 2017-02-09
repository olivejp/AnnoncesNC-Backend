package com.oliweb.DB.DTO;

public class MessageDTO {
    private Integer idMessage;
    private UtilisateurDTO sender;
    private UtilisateurDTO receiver;
    private String message;
    private Long dateMessage;

    public MessageDTO(Integer idMessage, UtilisateurDTO sender, UtilisateurDTO receiver, String message, Long dateMessage) {
        super();
		this.idMessage = idMessage;
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
		this.dateMessage = dateMessage;
	}

	public MessageDTO() {
		// TODO Auto-generated constructor stub
	}

	public Integer getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(Integer idMessage) {
		this.idMessage = idMessage;
	}

	public UtilisateurDTO getSender() {
		return sender;
	}

	public void setIdSender(UtilisateurDTO sender) {
		this.sender = sender;
	}

	public UtilisateurDTO getReceiver() {
		return receiver;
	}

	public void setIdReceiver(UtilisateurDTO receiver) {
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
