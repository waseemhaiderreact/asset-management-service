package com.sharklabs.ams.wallet;
import com.amazonaws.services.elasticmapreduce.model.ScalingTrigger;

/* Wriiten By Kumail Khan*/
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity (name = "t_wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Boolean allowedNegative;
    private String walletUUID;
    private String walletType;
    private String productType;
    private String capacityUnit;
    private Double capacity;
    private Date dateTime;
    private String orgUUID;
    private String assetUUID;
    private String userUUID;
    private Double thresholdValue;
    private String thresholdType;
    private Double balance;
    private String walletName;

    public String getWalletName() {
        return walletName;
    }
    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }
    public Boolean getAllowedNegative() {
        return allowedNegative;
    }
    public void setAllowedNegative(Boolean allowedNegative) {
        this.allowedNegative = allowedNegative;
    }
    public Double getThresholdValue() {
        return thresholdValue;
    }
    public void setThresholdValue(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }
    public String getThresholdType() {
        return thresholdType;
    }
    public void setThresholdType(String thresholdType) {
        this.thresholdType = thresholdType;
    }
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public String getCapacityUnit() {
        return capacityUnit;
    }
    public void setCapacityUnit(String capacityUnit) {
        this.capacityUnit = capacityUnit;
    }
    public Double getCapacity() {
        return capacity;
    }
    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getWalletUUID() {
        return walletUUID;
    }
    public void setWalletUUID(String walletUUID) {
        this.walletUUID = walletUUID;
    }
    public String getWalletType() {
        return walletType;
    }
    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public Date getDateTime() {
        return dateTime;
    }
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    public String getOrgUUID() {
        return orgUUID;
    }
    public void setOrgUUID(String orgUUID) {
        this.orgUUID = orgUUID;
    }
    public String getAssetUUID() {
        return assetUUID;
    }
    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }
    public String getUserUUID() {
        return userUUID;
    }
    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }
}
