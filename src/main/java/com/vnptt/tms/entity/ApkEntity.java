package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "apk")
public class ApkEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "packagename", nullable = false)
    private String packagename;
    @Column(name = "apkfileUrl", nullable = false)
    private String apkfileUrl;
    @Column(name = "version", nullable = false)
    private String version;
    @Column(name = "md5", nullable = false)
    private String md5;
    @Column(name = "packagesize", nullable = false)
    private Long packagesize;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "apkEntitiesPolicy")
    private List<PolicyEntity> policyEntities = new ArrayList<>();

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

    public List<PolicyEntity> getPolicyEntities() {
        return policyEntities;
    }

    public void setPolicyEntities(List<PolicyEntity> policyEntities) {
        this.policyEntities = policyEntities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
