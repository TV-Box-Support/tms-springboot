package com.vnptt.tms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Device")
public class DeviceEntity extends BaseEntity{
    @Column(name = "product")
    private String product;
    @Column(name = "model", nullable = false)
    private String model;
    @Column(name = "serialNumber", nullable = false, unique = true)
    private String sn;
    @Column(name = "mac", nullable = false, unique = true)
    private String mac;
    @Column(name = "fiwareVersion")
    private Integer firmwareVer;
    @Column(name = "HDMI")
    private Integer hdmi;
    @Column(name = "IP")
    private Long ip;
    @Column(name = "dateOfManufacture", nullable = false)
    private Date date;
    @Column(name = "location")
    private String location;
    @Column(name = "desciption", length = 2000)
    private String desciption;

    @ManyToMany(mappedBy = "deviceEntitiesApplication")
    private List<ApplicationEntity> applicationEntities = new ArrayList<>();

    @OneToMany(mappedBy = "deviceEntityDetail")
    private List<DevicePolicyDetailEntity> devicePolicyDetailEntities = new ArrayList<>();

    @OneToMany(mappedBy = "deviceEntityHistory")
    private List<HistoryPerformanceEntity> historyPerformanceEntities = new ArrayList<>();

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getFirmwareVer() {
        return firmwareVer;
    }

    public void setFirmwareVer(Integer firmwareVer) {
        this.firmwareVer = firmwareVer;
    }

    public Integer getHdmi() {
        return hdmi;
    }

    public void setHdmi(Integer hdmi) {
        this.hdmi = hdmi;
    }

    public Long getIp() {
        return ip;
    }

    public void setIp(Long ip) {
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public List<ApplicationEntity> getApplicationEntities() {
        return applicationEntities;
    }

    public void setApplicationEntities(List<ApplicationEntity> applicationEntities) {
        this.applicationEntities = applicationEntities;
    }

    public List<DevicePolicyDetailEntity> getDevicePolicyDetailEntities() {
        return devicePolicyDetailEntities;
    }

    public void setDevicePolicyDetailEntities(List<DevicePolicyDetailEntity> devicePolicyDetailEntities) {
        this.devicePolicyDetailEntities = devicePolicyDetailEntities;
    }

    public List<HistoryPerformanceEntity> getHistoryPerformanceEntities() {
        return historyPerformanceEntities;
    }

    public void setHistoryPerformanceEntities(List<HistoryPerformanceEntity> historyPerformanceEntities) {
        this.historyPerformanceEntities = historyPerformanceEntities;
    }
}
