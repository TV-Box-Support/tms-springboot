package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "listdevice")
public class ListDeviceEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "description", length = 2000)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "listdeviceDetail",
            joinColumns = @JoinColumn(name = "list_device_id"),
            inverseJoinColumns = @JoinColumn(name = "device_id"))
    private List<DeviceEntity> listDeviceDetail;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "ListdeviceRolemanagement",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "role_management_id"))
    private List<RoleManagementEntity> listDeviceRoleManagement;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<DeviceEntity> getListDeviceDetail() {
        return listDeviceDetail;
    }

    public void setListDeviceDetail(List<DeviceEntity> listDeviceDetail) {
        this.listDeviceDetail = listDeviceDetail;
    }

    public List<RoleManagementEntity> getListDeviceRoleManagement() {
        return listDeviceRoleManagement;
    }

    public void setListDeviceRoleManagement(List<RoleManagementEntity> listDeviceRoleManagement) {
        this.listDeviceRoleManagement = listDeviceRoleManagement;
    }

    public void addDevice(DeviceEntity deviceEntity) {
        this.listDeviceDetail.add(deviceEntity);
        deviceEntity.getListDeviceDetail().add(this);
    }

    public void removeDevice(long deviceId) {
        DeviceEntity deviceEntity = this.listDeviceDetail.stream().filter(app -> app.getId() == deviceId).findFirst().orElse(null);
        if (deviceEntity != null) {
            this.listDeviceDetail.remove(deviceEntity);
            deviceEntity.getListDeviceDetail().remove(this);
        }
    }
}
