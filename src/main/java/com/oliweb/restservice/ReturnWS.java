package com.oliweb.restservice;

public class ReturnWS {

    private String tag;
    private boolean status;
    private String msg;
    private Integer idServer;
    private Integer idLocal;

    public ReturnWS(String tag, boolean status, String msg, Integer idServer, Integer idLocal) {
        this.tag = tag;
        this.status = status;
        this.msg = msg;
        this.idServer = idServer;
        this.idLocal = idLocal;
    }

    public ReturnWS(String tag, boolean status, String msg, Integer idServer) {
        this.tag = tag;
        this.status = status;
        this.msg = msg;
        this.idServer = idServer;
        this.idLocal = null;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isValid() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getIdServer() {
        return idServer;
    }

    public void setIdServer(Integer id) {
        this.idServer = id;
    }

    public Integer getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Integer id) {
        this.idLocal = id;
    }

}
