package com.vnptt.tms.dto;

public class CommandDTO extends AbstractDTO{

    private String name;
    private String command;

    private Long commandNotificationId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }

    public Long getCommandNotificationId() {
        return commandNotificationId;
    }

    public void setCommandNotificationId(Long commandNotificationId) {
        this.commandNotificationId = commandNotificationId;
    }
}
