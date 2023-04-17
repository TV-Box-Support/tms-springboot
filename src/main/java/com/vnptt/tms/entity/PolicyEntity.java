package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="policy")
public class PolicyEntity extends BaseEntity{
    @Column(name = "policyname", unique = true, nullable = false)
    private String policyname;

    @Column(name = "status", nullable = false)
    private int Status;

    @ManyToOne
    @JoinColumn(name = "commandId", nullable = false)
    private CommandEntity commandEntity;

    @ManyToMany
    @JoinTable(name = "apkPolicy",
            joinColumns = @JoinColumn(name = "policy_id"),
            inverseJoinColumns = @JoinColumn(name = "apk_id"))
    private List<ApkEntity> apkEntitiesPolicy = new ArrayList<>();

    @OneToMany(mappedBy = "policyEntityDetail")
    private List<DevicePolicyDetailEntity> devicePolicyDetailEntities = new ArrayList<>();

    public String getPolicyname() {
        return policyname;
    }

    public void setPolicyname(String policyname) {
        this.policyname = policyname;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public CommandEntity getCommandEntity() {
        return commandEntity;
    }

    public void setCommandEntity(CommandEntity commandEntity) {
        this.commandEntity = commandEntity;
    }

    public List<ApkEntity> getApkEntitiesPolicy() {
        return apkEntitiesPolicy;
    }

    public void setApkEntitiesPolicy(List<ApkEntity> apkEntitiesPolicy) {
        this.apkEntitiesPolicy = apkEntitiesPolicy;
    }

    public List<DevicePolicyDetailEntity> getDevicePolicyDetailEntities() {
        return devicePolicyDetailEntities;
    }

    public void setDevicePolicyDetailEntities(List<DevicePolicyDetailEntity> devicePolicyDetailEntities) {
        this.devicePolicyDetailEntities = devicePolicyDetailEntities;
    }
}
