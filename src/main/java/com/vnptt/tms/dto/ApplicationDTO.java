package com.vnptt.tms.dto;

public class ApplicationDTO extends AbstractDTO{
    private String packagename;
    private int version;

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
