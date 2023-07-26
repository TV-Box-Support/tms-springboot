package com.vnptt.tms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alertdialog")
public class AlertDialogEntity extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @OneToMany(mappedBy = "alertDialogEntity")
    private List<CommandEntity> commandEntitiesNotification = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CommandEntity> getCommandEntitiesNotification() {
        return commandEntitiesNotification;
    }

    public void setCommandEntitiesNotification(List<CommandEntity> commandEntitiesNotification) {
        this.commandEntitiesNotification = commandEntitiesNotification;
    }
}
