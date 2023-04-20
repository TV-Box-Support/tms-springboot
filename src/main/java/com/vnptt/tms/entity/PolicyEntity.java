package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "policy")
public class PolicyEntity extends BaseEntity {
    @Column(name = "policyname", unique = true, nullable = false)
    private String policyname;

    @Column(name = "status", nullable = false)
    private int Status;

    @ManyToOne
    @JoinColumn(name = "commandId")
    private CommandEntity commandEntity;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "apkPolicy",
            joinColumns = @JoinColumn(name = "policy_id"),
            inverseJoinColumns = @JoinColumn(name = "apk_id"))
    private List<ApkEntity> apkEntitiesPolicy = new ArrayList<>();

    @OneToMany(mappedBy = "policyEntityDetail",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.REMOVE
            })
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

    public void addApk(ApkEntity apk) {
        this.apkEntitiesPolicy.add(apk);
        apk.getPolicyEntities().add(this);
    }

    public void removeApk(long apkId) {
        ApkEntity apkEntity = this.apkEntitiesPolicy.stream().filter(app -> app.getId() == apkId).findFirst().orElse(null);
        if (apkEntity != null) {
            this.apkEntitiesPolicy.remove(apkEntity);
            apkEntity.getPolicyEntities().remove(this);
        }
    }
}
