package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "command")
public class CommandEntity extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "command", nullable = false)
    private String command;

    @OneToMany(mappedBy = "commandEntity")
    private List<PolicyEntity> policyEntities = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "alertDialogId")
    private AlertDialogEntity alertDialogEntity;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<PolicyEntity> getPolicyEntities() {
        return policyEntities;
    }

    public void setPolicyEntities(List<PolicyEntity> policyEntities) {
        this.policyEntities = policyEntities;
    }

    public AlertDialogEntity getAlertDialogEntity() {
        return alertDialogEntity;
    }

    public void setAlertDialogEntity(AlertDialogEntity alertDialogEntity) {
        this.alertDialogEntity = alertDialogEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
