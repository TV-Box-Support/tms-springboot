package com.vnptt.tms.entity;

import javax.persistence.*;

@Entity
@Table (name ="User")
public class UserEntity extends BaseEntity{
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "username", nullable = false,  unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "company")
    private String company;
    @Column(name = "mail")
    private String mail;
    @Column(name = "contact")
    private Long contact;
    @ManyToOne
    @JoinColumn(name = "ruleId", nullable = false)
    private RuleEntity ruleEntity;

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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Long getContact() {
        return contact;
    }

    public void setContact(Long contact) {
        this.contact = contact;
    }

    public RuleEntity getRuleEntity() {
        return ruleEntity;
    }

    public void setRuleEntity(RuleEntity ruleEntity) {
        this.ruleEntity = ruleEntity;
    }
}
