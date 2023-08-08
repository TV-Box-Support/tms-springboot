package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "application")
public class ApplicationEntity extends BaseCreateDateEntity {
    @Column(name = "packagename", nullable = false)
    private String packagename;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "version", nullable = false)
    private Long version;
    @Column(name = "isSystem", nullable = false)
    private boolean issystem;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "applicationEntityDetail")
    private List<DeviceApplicationEntity> deviceApplicationEntities = new ArrayList<>();

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<DeviceApplicationEntity> getDeviceApplicationEntities() {
        return deviceApplicationEntities;
    }

    public void setDeviceApplicationEntities(List<DeviceApplicationEntity> deviceApplicationEntities) {
        this.deviceApplicationEntities = deviceApplicationEntities;
    }

    public List<DeviceApplicationEntity> getDeviceApplications() {
        return deviceApplicationEntities;
    }

    public void setDeviceApplications(List<DeviceApplicationEntity> deviceApplicationEntities) {
        this.deviceApplicationEntities = deviceApplicationEntities;
    }

    public boolean isIssystem() {
        return issystem;
    }

    public void setIssystem(boolean issystem) {
        this.issystem = issystem;
    }
}
