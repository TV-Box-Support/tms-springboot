package com.vnptt.tms.dto;


public class PolicyDTO  extends AbstractDTO<PolicyDTO>{

    private String commandName;
    private String policyname;
    private int Status;

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

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
}
