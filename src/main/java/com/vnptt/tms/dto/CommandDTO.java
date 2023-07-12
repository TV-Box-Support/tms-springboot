package com.vnptt.tms.dto;

public class CommandDTO extends AbstractDTO{
    private String command;

    private Long commandNotificationId;

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
