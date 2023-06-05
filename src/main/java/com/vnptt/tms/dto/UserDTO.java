package com.vnptt.tms.dto;


import java.util.List;

public class UserDTO extends AbstractDTO  {

    private String name;
    private String username;
    private String password;
    private String company;
    private String email;
    private Long contact;
    private boolean active;
    private List<String> ruleName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getContact() {
        return contact;
    }

    public void setContact(Long contact) {
        this.contact = contact;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getRuleName() {
        return ruleName;
    }

    public void setRuleName(List<String> ruleName) {
        this.ruleName = ruleName;
    }

}
