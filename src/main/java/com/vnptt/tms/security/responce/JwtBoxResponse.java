package com.vnptt.tms.security.responce;

public class JwtBoxResponse {

    private Long id;
    private String token;
    private String type;
    private String serialnumber;
    private String mac;
    private String roles;

    public JwtBoxResponse(String accessToken, Long id, String serialnumber, String mac, String roles, String type) {
        this.token = accessToken;
        this.id = id;
        this.serialnumber = serialnumber;
        this.mac = mac;
        this.roles = roles;
        this.type = type;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
