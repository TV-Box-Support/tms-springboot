package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "deviceApplication")
public class DeviceApplicationEntity extends BaseTimeEntity{

    @Column(name = "isAlive", nullable = false, columnDefinition = "boolean default true")
    private Boolean isalive;
    @ManyToOne
    @JoinColumn(name = "deviceId", nullable = false)
    private DeviceEntity deviceAppEntityDetail;
    @ManyToOne
    @JoinColumn(name = "applicationId", nullable = false)
    private ApplicationEntity applicationEntityDetail;
    @OneToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "historyDeviceApplicationEntity")
    private List<HistoryApplicationEntity> historyApplicationEntitiesDetail = new ArrayList<>();
    public Boolean getIsalive() {
        return isalive;
    }

    public void setIsalive(Boolean isalive) {
        this.isalive = isalive;
    }

    public DeviceEntity getDeviceAppEntityDetail() {
        return deviceAppEntityDetail;
    }

    public void setDeviceAppEntityDetail(DeviceEntity deviceAppEntityDetail) {
        this.deviceAppEntityDetail = deviceAppEntityDetail;
    }

    public ApplicationEntity getApplicationEntityDetail() {
        return applicationEntityDetail;
    }

    public void setApplicationEntityDetail(ApplicationEntity applicationEntityDetail) {
        this.applicationEntityDetail = applicationEntityDetail;
    }

    public List<HistoryApplicationEntity> getHistoryApplicationEntitiesDetail() {
        return historyApplicationEntitiesDetail;
    }

    public void setHistoryApplicationEntitiesDetail(List<HistoryApplicationEntity> historyApplicationEntitiesDetail) {
        this.historyApplicationEntitiesDetail = historyApplicationEntitiesDetail;
    }
}
