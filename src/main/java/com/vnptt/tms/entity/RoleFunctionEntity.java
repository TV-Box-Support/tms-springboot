package com.vnptt.tms.entity;

import com.vnptt.tms.config.ERoleFunction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rolefunction")
public class RoleFunctionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private ERoleFunction name;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "ruleEntities")
    private List<UserEntity> userEntities = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public ERoleFunction getName() {
        return name;
    }

    public void setName(ERoleFunction name) {
        this.name = name;
    }

    public List<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(List<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }
}
