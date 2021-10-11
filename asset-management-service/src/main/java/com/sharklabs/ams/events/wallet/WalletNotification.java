package com.sharklabs.ams.events.wallet;

import java.util.Date;
/* Written By Kumail Khan*/
public class WalletNotification {
    private String username;

    private String tenantUUID;

    private String title;

    private String message;

    private boolean readStatus;

    private String type;

    private Date notificationTime;

    private String assetUUID;

    private int notificationsetting;
    private Double thresholdValue;

    public String getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(String currencyValue) {
        this.currencyValue = currencyValue;
    }

    private String currencyValue;
    public Double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }

    public int getNotificationsetting() {
        return notificationsetting;
    }

    public void setNotificationsetting(int notificationsetting) {
        this.notificationsetting = notificationsetting;
    }
}
