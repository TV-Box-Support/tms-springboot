package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rule")
public class RuleEntity extends BaseEntity{

    @Column(name = "name", nullable = false,  unique = true)
    private String name;

    @OneToMany(mappedBy = "ruleEntity")
    private List<UserEntity> userEntities =new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(List<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }
}
