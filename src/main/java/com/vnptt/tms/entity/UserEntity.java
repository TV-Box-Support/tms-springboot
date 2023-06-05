package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User")
public class UserEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "company")
    private String company;
    @Column(name = "email")
    private String email;
    @Column(name = "contact")
    private Long contact;
    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private Boolean active;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "rolefunction_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rule_id"))
    private List<RoleFunctionEntity> ruleEntities = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "roleManId", nullable = false)
    private RoleManagementEntity roleManagementEntityUser;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<RoleFunctionEntity> getRuleEntities() {
        return ruleEntities;
    }

    public void setRuleEntities(List<RoleFunctionEntity> ruleEntities) {
        this.ruleEntities = ruleEntities;
    }

    public RoleManagementEntity getRoleManagementEntityUser() {
        return roleManagementEntityUser;
    }

    public void setRoleManagementEntityUser(RoleManagementEntity roleManagementEntityUser) {
        this.roleManagementEntityUser = roleManagementEntityUser;
    }
}
