package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Device")
public class DeviceEntity extends BaseEntity {
    @Column(name = "product")
    private String product;
    @Column(name = "model")
    private String model;
    @Column(name = "serialNumber", nullable = false, unique = true)
    private String sn;
    @Column(name = "mac", unique = true)
    private String mac;
    @Column(name = "fiwareVersion")
    private String firmwareVer;
    @Column(name = "HDMI")
    private Integer hdmi;
    @Column(name = "IP")
    private String ip;
    @Column(name = "network")
    private String network;
    @Column(name = "rom")
    private Long rom;
//    @Column(name = "dateOfManufacture")
//    private Date date;
    @Column(name = "location")
    private String location;
    @Column(name = "description", length = 2000)
    private String description;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "deviceAppEntityDetail")
    private List<DeviceApplicationEntity> deviceApplicationEntities;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "deviceEntityDetail")
    private List<DevicePolicyDetailEntity> devicePolicyDetailEntities = new ArrayList<>();

    @OneToMany(mappedBy = "deviceEntityHistory")
    private List<HistoryPerformanceEntity> historyPerformanceEntities = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "listDeviceDetail")
    private List<ListDeviceEntity> listDeviceDetail = new ArrayList<>();


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

    public String getFirmwareVer() {
        return firmwareVer;
    }

    public void setFirmwareVer(String firmwareVer) {
        this.firmwareVer = firmwareVer;
    }

    public Integer getHdmi() {
        return hdmi;
    }

    public void setHdmi(Integer hdmi) {
        this.hdmi = hdmi;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }

    public Long getRom() {
        return rom;
    }

    public void setRom(Long rom) {
        this.rom = rom;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DeviceApplicationEntity> getDeviceApplicationEntities() {
        return deviceApplicationEntities;
    }

    public void setDeviceApplicationEntities(List<DeviceApplicationEntity> deviceApplicationEntities) {
        this.deviceApplicationEntities = deviceApplicationEntities;
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

    public List<ListDeviceEntity> getListDeviceDetail() {
        return listDeviceDetail;
    }

    public void setListDeviceDetail(List<ListDeviceEntity> listDeviceDetail) {
        this.listDeviceDetail = listDeviceDetail;
    }
}
