package com.vnptt.tms.dto;

public class ApkDTO extends AbstractDTO{
    private String packagename;
    private String apkfileUrl;
    private String version;
    private String md5;
    private Long packagesize;

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getApkfileUrl() {
        return apkfileUrl;
    }

    public void setApkfileUrl(String apkfileUrl) {
        this.apkfileUrl = apkfileUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Long getPackagesize() {
        return packagesize;
    }

    public void setPackagesize(Long packagesize) {
        this.packagesize = packagesize;
    }
}
