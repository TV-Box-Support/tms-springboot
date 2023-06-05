package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rolesmanagement")
public class RoleManagementEntity extends BaseEntity{

    @Column(name = "name", nullable = false, unique = true)
    private String name;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addListDevice(ListDeviceEntity listDeviceEntity) {
        this.deviceEntities.add(listDeviceEntity);
        listDeviceEntity.getListDeviceRoleManagement().add(this);
    }

    public void removeListDevice(long listDeviceId) {
        ListDeviceEntity listDevice = this.deviceEntities.stream().filter(listDeviceEntity -> listDeviceEntity.getId() == listDeviceId).findFirst().orElse(null);
        if (listDevice != null) {
            this.deviceEntities.remove(listDevice);
            listDevice.getListDeviceDetail().remove(this);
        }
    }

}
