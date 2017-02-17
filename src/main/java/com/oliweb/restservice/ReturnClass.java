package com.oliweb.restservice;

class ReturnClass {

	private String tag;
	private boolean status;
	private String msg;
	private Integer id;

    ReturnClass(String tag, boolean status, String msg, Integer id) {
        this.tag = tag;
		this.status = status;
		this.msg = msg;
		this.id = id;
	}

    void setStatus(boolean status) {
        this.status = status;
	}

    void setMsg(String msg) {
        this.msg = msg;
	}

    void setId(Integer id) {
        this.id = id;
	}
	
}