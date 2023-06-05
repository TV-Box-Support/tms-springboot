package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rolesmanagement")
public class RoleManagementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "description", length = 2000)
    private String description;

    @OneToMany(mappedBy = "roleManagementEntityUser")
    private List<UserEntity> userEntitiesMana = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "listDeviceRoleManagement")
    private List<ListDeviceEntity> deviceEntities = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UserEntity> getUserEntitiesMana() {
        return userEntitiesMana;
    }

    public void setUserEntitiesMana(List<UserEntity> userEntitiesMana) {
        this.userEntitiesMana = userEntitiesMana;
    }

    public List<ListDeviceEntity> getDeviceEntities() {
        return deviceEntities;
    }

    public void setDeviceEntities(List<ListDeviceEntity> deviceEntities) {
        this.deviceEntities = deviceEntities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
