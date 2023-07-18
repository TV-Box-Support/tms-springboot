package com.vnptt.tms.dto;

public class ApplicationDTO extends AbstractCreateDateDTO {
    private String packagename;
    private String name;
    private Long version;
    private boolean issystem;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isIssystem() {
        return issystem;
    }

    public void setIssystem(boolean issystem) {
        this.issystem = issystem;
    }
}
