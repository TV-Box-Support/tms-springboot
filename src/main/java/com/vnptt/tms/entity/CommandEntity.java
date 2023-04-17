package com.vnptt.tms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "command")
public class CommandEntity extends BaseEntity {

    @Column(name = "command", nullable = false, unique = true)
    private String command;

    @OneToMany(mappedBy = "commandEntity")
    private List<PolicyEntity> policyEntities = new ArrayList<>();

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
}
